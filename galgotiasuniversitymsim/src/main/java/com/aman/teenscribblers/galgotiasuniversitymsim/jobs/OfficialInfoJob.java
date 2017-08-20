package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.ConnectionDetector;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.google.firebase.iid.FirebaseInstanceId;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 10/6/16.
 */
public class OfficialInfoJob extends Job {

    public OfficialInfoJob() {
        super(new Params(AppConstants.PRIORITY4).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        String infoData = IonMethods.getString(AppConstants.OfficialInfoString);
        String parsedInfo = parsePersonalInfo(infoData);
        FileUtil.createFile(getApplicationContext(), AppConstants.FILE_NAME_OFFICIAL, parsedInfo);
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_OFFICIAL, false, parsedInfo, false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_OFFICIAL, false, AppConstants.ERROR_CONTENT_FETCH, true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_OFFICIAL, false, throwable.getMessage(), true));
            return RetryConstraint.CANCEL;
        } else if (throwable.getMessage().equals(AppConstants.ERROR_SESSION_EXPIRED)) {
            EventBus.getDefault().post(new SessionExpiredEvent());
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

    private String parsePersonalInfo(String infoData) {
        Document doc = Jsoup.parse(infoData);
        Elements rows = doc.select("table tr");
        StringBuilder sb = new StringBuilder();
        ContentValues serverCv = new ContentValues();
        for (Element row : rows) {
            if (sb.length() != 0)
                sb.append("==");
            if (row.hasClass("top-heading")) {
                sb.append(row.text()).append(":").append(AppConstants.OFFICIAL_HEADING);
            } else {
                sb.append(row.text());
                String[] keyPair = row.text().split(":");
                if (keyPair.length > 1)
                    serverCv.put(keyPair[0].trim(), keyPair[1].trim());
            }
        }
        sendProfileDataToServer(serverCv);
        return sb.toString();
    }

    private void sendProfileDataToServer(ContentValues serverCv) {
        String admNo = serverCv.getAsString(PrefUtils.PREFS_USER_ADMNO_KEY_OFFICIAL).trim();
        serverCv.remove(PrefUtils.PREFS_USER_ADMNO_KEY_OFFICIAL);
        serverCv.put("adm_no", admNo);
        serverCv.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
        try {
            IonMethods.postProfiletoServer(serverCv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
