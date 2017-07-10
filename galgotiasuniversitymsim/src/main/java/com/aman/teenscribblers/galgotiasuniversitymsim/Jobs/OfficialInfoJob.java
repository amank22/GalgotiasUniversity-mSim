package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
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
public class OfficialInfoJob extends Job {

    public OfficialInfoJob() {
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
        String infoData = IonMethods.getString(AppConstants.OfficialInfoString);
        String parsedInfo = parsePersonalInfo(infoData);
        FileUtil.createFile(getApplicationContext(), AppConstants.FILE_NAME_OFFICIAL, parsedInfo);
        EventBus.getDefault().post(new InfoEvent(false, parsedInfo, false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new InfoEvent(false, AppConstants.ERROR_CONTENT_FETCH, true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new InfoEvent(false, throwable.getMessage(), true));
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

    //TODO:Add a method for image link extraction
    private String parsePersonalInfo(String infoData) {
        Document doc = Jsoup.parse(infoData);
        Elements rows = doc.select("table:not(table:only-child) tr");
        StringBuilder sb = new StringBuilder();
        for (Element row : rows) {
            if (sb.length() != 0)
                sb.append("==");
            if (row.hasClass("top-heading")) {
                sb.append(row.text()).append(":").append(AppConstants.OFFICIAL_HEADING);
            } else {
                sb.append(row.text());
            }
        }
        Element image = doc.select(".collegelogo1 img").first();
        if (image != null) {
            String src = AppConstants.BaseUrl + image.attr("src");
            PrefUtils.saveToPrefs(getApplicationContext(), PrefUtils.PREFS_USER_IMAGE, src);
        }
        return sb.toString();
    }
}
