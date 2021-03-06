package com.aman.teenscribblers.galgotiasuniversitymsim.service;

/**
 * Created by aman on 23-10-2015 in Galgotias University(mSim).
 */

import android.content.ContentValues;
import android.content.Context;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

public class RegistrationIntentService {

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     */
    public static boolean sendRegistrationToServer(Context context) throws Exception {
        String token = FirebaseInstanceId.getInstance().getToken();
        ContentValues cv = new ContentValues();
        String admno = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_ADMNO_KEY, PrefUtils.DEFAULT_ADMNO).trim();
        String name = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_NAME_KEY, PrefUtils.DEFAULT_NAME);
        String email = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_EMAIL_KEY, PrefUtils.DEFAULT_EMAIL);
        String mobile = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_MOBILE_KEY, PrefUtils.DEFAULT_PHONE);
        if (!admno.equals(PrefUtils.DEFAULT_ADMNO)) {
            cv.put("adm_no", admno);
        } else {
            return false;
        }
        if (!name.equals(PrefUtils.DEFAULT_NAME))
            cv.put("name", name);
        if (!email.equals(PrefUtils.DEFAULT_EMAIL))
            cv.put("email", email);
        if (!mobile.equals(PrefUtils.DEFAULT_PHONE))
            cv.put("mobile", mobile);
        cv.put("gcm_id", token);
        return IonMethods.postBasicstoServer(cv) && IonMethods.postProfiletoServer(cv);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     */
    public static void subscribeTopics(Context context, String newTopic) {
        String admno = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_ADMNO_KEY, PrefUtils.DEFAULT_ADMNO).trim();
        if (newTopic == null && !PrefUtils.getFromPrefs(context, PrefUtils.ARE_TOKEN_SUBSCRIBED, false)) {
            for (String topic : AppConstants.TOPICS) {
                if (topic.equals("Placements") && !admno.startsWith("12")) {
                    continue;
                }
                FirebaseMessaging.getInstance().subscribeToTopic(topic);
                PrefUtils.saveToPrefs(context, PrefUtils.ARE_TOKEN_SUBSCRIBED, true);
            }
        } else {
            FirebaseMessaging.getInstance().subscribeToTopic(newTopic);
        }
    }

}
