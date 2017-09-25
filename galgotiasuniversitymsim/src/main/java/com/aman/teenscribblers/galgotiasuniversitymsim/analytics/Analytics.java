package com.aman.teenscribblers.galgotiasuniversitymsim.analytics;

import android.content.Context;
import android.os.Bundle;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by amankapoor on 19/07/17.
 */

public class Analytics {

    public static void selectContent(Context context, FirebaseAnalytics mFirebaseAnalytics, String id, String name, String type) {
        String admNo = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "no_adm_no");
        String mobile = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_MOBILE_KEY, "no_phone");
        String email = PrefUtils.getFromPrefs(context, PrefUtils.PREFS_USER_EMAIL_KEY, "no_email");
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, type);
        bundle.putString("user", admNo);
        bundle.putString("mobile", mobile);
        bundle.putString("email", email);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName(name)
                .putContentType(type)
                .putCustomAttribute("user", admNo)
                .putCustomAttribute("mobile", mobile)
                .putCustomAttribute("email", email)
                .putContentId(id));
    }
}
