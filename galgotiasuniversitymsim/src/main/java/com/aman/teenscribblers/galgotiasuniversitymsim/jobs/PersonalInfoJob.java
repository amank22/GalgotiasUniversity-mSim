package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.birbit.android.jobqueue.CancelReason;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 10/6/16.
 */
public class PersonalInfoJob extends Job {

    public PersonalInfoJob() {
        super(new Params(AppConstants.PRIORITY4).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (!Connection_detect.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        String infoData = IonMethods.getString(AppConstants.PersonalInfoString);
        String parsedInfo = parsePersonalInfo(infoData);
        FileUtil.createFile(getApplicationContext(), AppConstants.FILE_NAME_PERSONAL, parsedInfo);
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_PERSONAL, false, parsedInfo, false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_PERSONAL, false, AppConstants.ERROR_CONTENT_FETCH, true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_PERSONAL, false, throwable.getMessage(), true));
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
        Elements rows = doc.select("table:not(table:only-child) tr");
        ContentValues serverCv = new ContentValues();
        StringBuilder sb = new StringBuilder();
        for (Element row : rows) {
            if (sb.length() != 0)
                sb.append("==");
            if (row.hasClass("top-heading")) {
                sb.append(row.text()).append(":").append(AppConstants.PERSONAL_HEADING);
            } else {
                sb.append(row.text());
                String[] keyPair = row.text().split(":");
                if (keyPair.length > 1)
                    serverCv.put(keyPair[0].trim(), keyPair[1].trim());
            }
        }
        sendProfileDataToServer(serverCv);
        Element image = doc.select(".collegelogo1 img").first();
        if (image != null) {
            String src = AppConstants.BaseUrl + image.attr("src");
            PrefUtils.saveToPrefs(getApplicationContext(), PrefUtils.PREFS_USER_IMAGE, src);
        }
        return sb.toString();
    }

    private void sendProfileDataToServer(ContentValues serverCv) {
        String admNo = serverCv.getAsString(PrefUtils.PREFS_USER_ADMNO_KEY);
        serverCv.remove(PrefUtils.PREFS_USER_ADMNO_KEY);
        serverCv.put("adm_no", admNo);
        try {
            IonMethods.postProfiletoServer(serverCv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
