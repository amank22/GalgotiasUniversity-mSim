package com.aman.teenscribblers.galgotiasuniversitymsim.service;

/**
 * Created by aman on 23-10-2015 in Galgotias University(mSim).
 */

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyInstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        try {
            RegistrationIntentService.sendRegistrationToServer(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
