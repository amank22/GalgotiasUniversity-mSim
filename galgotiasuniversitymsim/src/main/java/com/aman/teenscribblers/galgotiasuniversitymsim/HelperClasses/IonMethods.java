package com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses;

import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;
import com.koushikdutta.ion.builder.Builders.Any.B;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public class IonMethods {

    public static boolean simlogin(String username, String pass)
            throws Exception {

        boolean status = false;
        status = get(AppConstants.LoginString);
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
            Response<String> result = base.followRedirect(true)
                    .setLogging("GU", Log.DEBUG)
                    .asString().withResponse().get();
            if (result.getException()!=null)
               throw result.getException();
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
        Response<String> result = base.followRedirect(true).asString().withResponse().get();
        if ((result.getException() != null)
                || (result.getHeaders().code() == 302)) {
            status = false;
        } else if (result.getHeaders().code() == 200) {
            status = true;
            setvsev(result.getResult());
        }
        return status;
    }

    public static String getString(String url) {
        String status = null;
        B base = Ion.with(GUApp.getInstance()).load(url);
        Response<String> result;
        try {
            result = base.followRedirect(true)
                    .setLogging("GET", Log.DEBUG)
                    .asString().withResponse().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return "error";
        } catch (ExecutionException e) {
            e.printStackTrace();
            return "error";
        }
        System.out.println(result.getHeaders().code());
        if (result.getException() != null
                || result.getHeaders().code() == 302) {
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

        Response<String> result = base.followRedirect(true)
                .setLogging("POST", Log.DEBUG)
                .asString().withResponse().get();
        System.out.println(result);
        Uri timeout = Uri
                .parse("http://182.71.87.38/iSIM/msg.htm");
        if (result.getRequest().getUri().getPath().contains(timeout.getPath())) {
            return "expired";
        }
        if (result.getException() != null
                || result.getHeaders().code() == 302) {
            status = "error";
        } else if (result.getHeaders().code() == 200) {
            status = result.getResult();
            // setvsev(result.getResult());
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
}
