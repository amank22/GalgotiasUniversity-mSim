package com.aman.teenscribblers.galgotiasuniversitymsim.helper;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.CaptchaEvent;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders.Any.B;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IonMethods {

    private static final String TAG = "ION METHODS";

    public static synchronized CaptchaEvent simCaptcha() throws Exception {
        String page = getString(AppConstants.LoginString);
        Map<String, String> params = getLoginParams(page);
        return new CaptchaEvent(params, getCaptcha(AppConstants.captchaurl));
    }

    public static synchronized boolean simLogin(String username, String pass, String captcha, Map<String, String> params)
            throws Exception {

        B base = Ion.with(GUApp.getInstance()).load(AppConstants.LoginString);

        for (Map.Entry<String, String> entry : parseBodyParameters(username, pass, captcha, params).entrySet()) {
            base.setBodyParameter(entry.getKey(), entry.getValue());
        }
        Response<String> result;
        result = base.followRedirect(true)
                .setLogging("GU", Log.DEBUG)
                .asString().withResponse().get();
        if (result.getRequest().getUri().toString().equalsIgnoreCase(AppConstants.HomeString)) {
            setvsev(result.getResult());
        } else if (result.getHeaders().code() == 200) {
            throw new Exception("Sorry,Something's just didn't match up.Let's try again");
        } else {
            throw new Exception("Sorry,There is some problem in logging in.Let's try again");
        }
        return true;
    }

    private static Map<String, String> parseBodyParameters(String username, String pass, String captcha, Map<String, String> params) {
        Map<String, String> temp = new HashMap<>(params.size());
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            String[] entryArr = entry.getKey().split("~");
            String name = entryArr[0];
            String title = "";
            if (entryArr.length == 2)
                title = entryArr[1];
            if (title.equals("User ID")) {
                temp.put(name, username);
            } else if (title.equals("Password")) {
                temp.put(name, pass);
            } else if (name.startsWith("txt")) {
                temp.put(name, captcha);
            } else {
                temp.put(name, entry.getValue());
            }
        }
        return temp;
    }

    public static void get(String url)
            throws Exception {
        B base = Ion.with(GUApp.getInstance()).load(url);
        Response<String> result;
        result = base.followRedirect(true).asString().withResponse().get();
        //TODO: check if session is expired and check for remaining code also like 500,404 etc
        final String path = result.getRequest().getUri().toString();
        if (result.getHeaders().code() == 200 && !path.contains(AppConstants.ERROR_BASE_URL)) {
            setvsev(result.getResult());
        } else {
            //Considering all remaining cases for re-authorisation
            throw new Exception(AppConstants.ERROR_SESSION_EXPIRED);
        }
    }

    public static String getString(String url) throws Exception {
        B base = Ion.with(GUApp.getInstance()).load(url);
        Response<String> result;
        result = base.followRedirect(true)
//                    .setLogging("GET", Log.DEBUG)
                .asString().withResponse().get();
        //TODO: check if session is expired and check for remaining code also like 500,404 etc
        final String path = result.getRequest().getUri().toString();
        if (result.getHeaders().code() == 200 && !path.contains(AppConstants.ERROR_BASE_URL)) {
            setvsev(result.getResult());
            return result.getResult();
        } else {
            //Considering all remaining cases for re-authorisation
            throw new Exception(AppConstants.ERROR_SESSION_EXPIRED);
        }
    }

    private static Bitmap getCaptcha(String url)
            throws Exception {
        B base = Ion.with(GUApp.getInstance()).load(url);
        Bitmap captcha = base.noCache().asBitmap().get();
        if (captcha == null) {
            throw new Exception("Unable to download Captcha");
        }
        return captcha;
    }

    public static String post(String url, ContentValues nvp) throws Exception {

        B base = Ion.with(GUApp.getInstance().getApplicationContext()).load(url);
        for (Map.Entry<String, Object> entry : nvp.valueSet()) {
            base.setBodyParameter(entry.getKey(), entry.getValue().toString());
        }
        Response<String> result;
        result = base.followRedirect(true)
//                .setLogging("POST", Log.DEBUG)
                .asString().withResponse().get();

        final String path = result.getRequest().getUri().toString();
        if (path.contains(AppConstants.ERROR_BASE_URL)) {
            throw new Exception(AppConstants.ERROR_SESSION_EXPIRED);
        }
        if (result.getHeaders().code() == 302) {
            throw new Exception(AppConstants.ERROR_CONTENT_FETCH);
        } else if (result.getHeaders().code() == 200) {
            return result.getResult();
        } else {
            throw new Exception(AppConstants.ERROR_SESSION_EXPIRED);
        }
    }

    public static void setvsev(String s) {
        Document document = Jsoup.parse(s);
        AppConstants.viewstate = document.select("#__VIEWSTATE").attr(
                "value");
        AppConstants.eventvalidate = document
                .select("#__EVENTVALIDATION").attr("value");
        AppConstants.viewStateGenerator = document
                .select("#__VIEWSTATEGENERATOR").attr("value");
        Log.d("ViewState", AppConstants.viewstate);
        Log.d("EventStates", AppConstants.eventvalidate);
        Log.d("VIEWSTATEGENERATOR", AppConstants.viewStateGenerator);
    }

    private static Map<String, String> getLoginParams(String s) {
        Document document = Jsoup.parse(s);
        Elements inputs = document.select("input");
        Map<String, String> loginParams = new HashMap<>();
        for (Element input : inputs) {
            String name = input.attr("name");
            String value = input.attr("value");
            String title = input.attr("title");
            String checked = input.attr("checked");
            String type = input.attr("type");
            if (type.equals("radio") && checked.equals("checked")) {
                loginParams.put(name + "~" + title, value);
            } else if (!type.equals("radio")) {
                loginParams.put(name + "~" + title, value);
            }
        }
        loginParams.put("__LASTFOCUS", "");
        loginParams.put("__EVENTTARGET", "");
        loginParams.put("__EVENTARGUMENT", "");
        Log.d(TAG, "getLoginParams: " + loginParams.toString());
        return loginParams;
    }

    public static boolean postBasicstoServer(ContentValues nvp) throws Exception {

        boolean status = false;

        B base = Ion.with(GUApp.getInstance().getApplicationContext())
                .load("https://galgotiasuniversity.herokuapp.com/v1/student/register");
        for (Map.Entry<String, Object> entry : nvp.valueSet()) {
            base.setBodyParameter(entry.getKey(), entry.getValue().toString());
        }
        Response<JsonObject> result;
        result = base
                .setLogging("POST", Log.DEBUG)
                .asJsonObject().withResponse().get();
        System.out.println(result);
        if (result.getHeaders().code() != 200) {
            status = false;
        } else if (result.getHeaders().code() == 200) {
            Log.d(TAG, result.getResult().toString());
            status = true;
        }
        return status;
    }

    public static boolean postProfiletoServer(ContentValues nvp) throws Exception {

        boolean status = false;

        B base = Ion.with(GUApp.getInstance().getApplicationContext())
                .load("https://galgotiasuniversity.herokuapp.com/v1/student/update");
        for (Map.Entry<String, Object> entry : nvp.valueSet()) {
            if (entry.getValue() != null) {
                base.setBodyParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        Response<JsonObject> result;
        result = base
                .setLogging("POST", Log.DEBUG)
                .asJsonObject().withResponse().get();
        System.out.println(result);
        if (result.getHeaders().code() != 200) {
            status = false;
        } else if (result.getHeaders().code() == 200) {
            Log.d(TAG, result.getResult().toString());
            status = true;
        }
        return status;
    }
}
