package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
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
 * Created by Anmol Agarwal on 8/24/2017.
 */

public class SeatingPlanJob extends Job {
    public SeatingPlanJob() {
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
        String infoData = IonMethods.getString(AppConstants.SeatingPlanString);
        String parsedInfo = parseSeatingPlan(infoData);
        FileUtil.createFile(getApplicationContext(), AppConstants.FILE_NAME_SEATING, parsedInfo);
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_SEATING,false, parsedInfo, false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_SEATING,false, AppConstants.ERROR_CONTENT_FETCH, true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_SEATING,false, throwable.getMessage(), true));
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

    private String parseSeatingPlan(String infoData) {
        Document doc = Jsoup.parse(infoData);
        Elements rows = doc.select("table tr");
        StringBuilder sb = new StringBuilder();
        for (Element row : rows) {
            if (sb.length() != 0)
                sb.append("==");
            if (row.hasClass("top-heading")) {
                sb.append(row.text()).append(":").append(AppConstants.SEATING_HEADING);
            } else {
                sb.append(row.text());
            }
        }
        return sb.toString();
    }
}
