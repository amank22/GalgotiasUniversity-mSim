package com.aman.teenscribblers.galgotiasuniversitymsim.Pojo;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by aman on 7/6/16.
 */
public class LoginBean {

    Map<String, String> params;
    Bitmap bitmap;
    String errorMsg;

    public LoginBean(Map<String, String> params, Bitmap bitmap, String errorMsg) {
        this.params = params;
        this.bitmap = bitmap;
        this.errorMsg = errorMsg;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
