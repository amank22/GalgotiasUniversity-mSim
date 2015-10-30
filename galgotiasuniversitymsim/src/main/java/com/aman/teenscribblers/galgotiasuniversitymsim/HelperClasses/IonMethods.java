package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders.Any.B;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;

public class IonMethods {

    private static final String TAG = "ION METHODS";

    public static boolean simlogin(String username, String pass)
            throws Exception {

        boolean status = get(AppConstants.LoginString);
        if (status) {
            B base = Ion.with(GUApp.getInstance()).load(AppConstants.LoginString);
            base.setBodyParameter("__LASTFOCUS", "")
                    .setBodyParameter("__EVENTTARGET", "")
                    .setBodyParameter("__EVENTARGUMENT", "")
                    .setBodyParameter(
                            "__VIEWSTATE",
                            AppConstants.viewstate)
                    .setBodyParameter(
                            "__EVENTVALIDATION",
                            AppConstants.eventvalidate)
                    .setBodyParameter("txtUserId", username)
                    .setBodyParameter("txtPass", pass)
                    .setBodyParameter("btnLogin_", "");
            Response<String> result;
            result = base.followRedirect(true)
//                        .setLogging("GU", Log.DEBUG)
                    .asString().withResponse().get();
            if (result.getHeaders().code() != 200) {
                status = false;
            } else if (result.getHeaders().code() == 200) {
                status = true;
                setvsev(result.getResult());
            }
        }
        return status;
    }

    public static boolean get(String url)
            throws Exception {
        Boolean status = false;
        B base = Ion.with(GUApp.getInstance()).load(url);
        Response<String> result;
        result = base.followRedirect(true).asString().withResponse().get();
        if ((result.getHeaders().code() == 302)) {
            status = false;
        } else if (result.getHeaders().code() == 200) {
            status = true;
            setvsev(result.getResult());
        }
        return status;
    }

    public static String getString(String url) throws Exception {
        String status = null;
        B base = Ion.with(GUApp.getInstance()).load(url);
        Response<String> result;
        result = base.followRedirect(true)
//                    .setLogging("GET", Log.DEBUG)
                .asString().withResponse().get();
        System.out.println(result.getHeaders().code());
        if (result.getHeaders().code() == 302) {
            status = "error";
        } else if (result.getHeaders().code() == 200) {
            status = result.getResult();
            setvsev(result.getResult());

        }
        return status;
    }

    public static String post(String url,
                              ContentValues nvp) throws Exception {

        String status = "error";

        B base = Ion.with(GUApp.getInstance().getApplicationContext()).load(url);
        for (Map.Entry<String, Object> entry : nvp.valueSet()) {
            base.setBodyParameter(entry.getKey(), entry.getValue().toString());
        }
        Response<String> result;
        result = base.followRedirect(true)
                .setLogging("POST", Log.DEBUG)
                .asString().withResponse().get();
//        System.out.println(result);
        Uri timeout = Uri
                .parse("http://182.71.87.38/iSIM/msg.htm");
        if (result.getRequest().getUri().getPath().contains(timeout.getPath())) {
            return "expired";
        }
        if (result.getHeaders().code() == 302) {
            status = "error";
        } else if (result.getHeaders().code() == 200) {
            status = result.getResult();
        }
        return status;
    }

    static void setvsev(String s) {
        Document document = Jsoup.parse(s);
        AppConstants.viewstate = document.select("#__VIEWSTATE").attr(
                "value");
        AppConstants.eventvalidate = document
                .select("#__EVENTVALIDATION").attr("value");
        Log.d("ViewState", AppConstants.viewstate);
        Log.d("EventStates", AppConstants.eventvalidate);

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
            Log.d(TAG,result.getResult().toString());
            status = true;
        }
        return status;
    }
}
