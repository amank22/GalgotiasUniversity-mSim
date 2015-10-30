package com.aman.teenscribblers.galgotiasuniversitymsim.Service;

/**
 * Created by aman on 23-10-2015 in Galgotias University(mSim).
 */

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(this);
            // R.string.gcm_defaultSenderId (the Sender ID) is typically derived from google-services.json.
            // See https://developers.google.com/cloud-messaging/android/start for details on this file.
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]
            Log.i(TAG, "GCM Registration Token: " + token);
            boolean sentToken = sharedPreferences
                    .getBoolean(AppConstants.SENT_TOKEN_TO_SERVER, false);
            boolean result = sentToken || sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, result).apply();
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(AppConstants.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(AppConstants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private boolean sendRegistrationToServer(String token) throws Exception {
        ContentValues cv = new ContentValues();
        String admno = PrefUtils.getFromPrefs(RegistrationIntentService.this, PrefUtils.PREFS_USER_ADMNO_KEY, PrefUtils.DEFAULT_ADMNO);
        String name = PrefUtils.getFromPrefs(RegistrationIntentService.this, PrefUtils.PREFS_USER_NAME_KEY, PrefUtils.DEFAULT_NAME);
        String email = PrefUtils.getFromPrefs(RegistrationIntentService.this, PrefUtils.PREFS_USER_EMAIL_KEY, PrefUtils.DEFAULT_EMAIL);
        if (!admno.equals(PrefUtils.DEFAULT_ADMNO))
            cv.put("adm_no", admno);
        if (!name.equals(PrefUtils.DEFAULT_NAME))
            cv.put("name", name);
        if (!email.equals(PrefUtils.DEFAULT_EMAIL))
            cv.put("email", email);
        cv.put("gcm_id", token);
        return IonMethods.postBasicstoServer(cv);
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        String admno = PrefUtils.getFromPrefs(RegistrationIntentService.this, PrefUtils.PREFS_USER_ADMNO_KEY, PrefUtils.DEFAULT_ADMNO);
        for (String topic : AppConstants.TOPICS) {
            if (topic.equals("Placements") && !admno.startsWith("12")) {
                continue;
            }
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
