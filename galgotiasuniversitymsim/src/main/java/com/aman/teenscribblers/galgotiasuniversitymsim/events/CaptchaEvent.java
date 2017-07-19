package com.aman.teenscribblers.galgotiasuniversitymsim.events;

import android.graphics.Bitmap;

import java.util.Map;

/**
 * Created by amankapoor on 03/07/17.
 */

public class CaptchaEvent {

    private Map<String,String> params;
    private Bitmap bitmap;
    private String errorMsg;

    public CaptchaEvent(Map<String, String> params, Bitmap bitmap, String errorMsg) {
        this.params = params;
        this.bitmap = bitmap;
        this.errorMsg = errorMsg;
    }

    public CaptchaEvent(Map<String, String> params, Bitmap bitmap) {
        this.params = params;
        this.bitmap = bitmap;
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
