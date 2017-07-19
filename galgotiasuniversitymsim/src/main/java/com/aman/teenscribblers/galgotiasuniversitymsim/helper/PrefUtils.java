package com.aman.teenscribblers.galgotiasuniversitymsim.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class PrefUtils {
    public static final String PREFS_LOGIN_USERNAME_KEY = "__USERNAME__";
    public static final String PREFS_LOGIN_PASSWORD_KEY = "__PASSWORD__";
    public static final String PREFS_USER_ADMNO_KEY = "Admission No";
    public static final String PREFS_USER_NAME_KEY = "Name";
    public static final String PREFS_USER_EMAIL_KEY = "Email ID";
    public static final String PREFS_USER_GENDER_KEY = "Gender";
    public static final String PREFS_USER_IMAGE = "__IMAGE__";
    public static final String DEFAULT_ADMNO = "";
    public static final String DEFAULT_NAME = "";
    public static final String DEFAULT_EMAIL = "";


    public static final String ARE_TOKEN_SUBSCRIBED = "is_token_subscribed";

    /**
     * Called to save supplied value in shared preferences against given key.
     *
     * @param context Context of caller activity
     * @param key     Key of value to save against
     * @param value   Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();// TODO: 12/07/17 Encrypt the shared preference before adding to file
        editor.putString(key, value);
        editor.apply();
    }

    public static void saveToPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();// TODO: 12/07/17 Encrypt the shared preference before adding to file
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     *
     * @param context      Context of caller activity
     * @param key          Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static boolean getFromPrefs(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    public static void deleteuser(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}