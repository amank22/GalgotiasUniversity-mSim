package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
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
 * Created by aman on 19-02-2015.
 */
public class TimeTableJob extends Job {
    private DbSimHelper dbhelper;


    public TimeTableJob() {
        super(new Params(AppConstants.PRIORITY2).groupBy(AppConstants.GroupTimeTable).requireNetwork());
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new TimeTableStartEvent("Fetching From Network"));
    }

    @Override
    public void onRun() throws Throwable {
        if (!Connection_detect.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        String s = GetTTData();
        dbhelper.deleteTimeTable();
        parseDoc(s);
        EventBus.getDefault().post(new TimeTableSuccessEvent("Stored", null));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        if (cancelReason == CancelReason.REACHED_RETRY_LIMIT)
            EventBus.getDefault().post(new LocalErrorEvent(AppConstants.ERROR_CONTENT_FETCH));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        EventBus.getDefault().post(new LocalErrorEvent("Retrying for " + runCount + " time"));
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK)) {
            EventBus.getDefault().post(new LocalErrorEvent(throwable.getMessage()));
            return RetryConstraint.CANCEL;
        } else if (throwable.getMessage().equals(AppConstants.ERROR_SESSION_EXPIRED)) {
            EventBus.getDefault().post(new SessionExpiredEvent());
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }


    private String GetTTData() throws Exception {
        IonMethods.get(AppConstants.TimeTableString);
        return IonMethods.post(AppConstants.TimeTableString,
                getnvp());
    }

    private ContentValues getnvp() {
        ContentValues nameValuePair = new ContentValues();
        nameValuePair.put("__EVENTTARGET", "");
        nameValuePair.put("__EVENTARGUMENT", "");
        nameValuePair.put("__VIEWSTATE",
                AppConstants.viewstate);
        nameValuePair.put("ctl00$ctl00$txtCaseCSS",
                "textDefault");
        nameValuePair.put("__EVENTVALIDATION",
                AppConstants.eventvalidate);
        nameValuePair.put(
                "ctl00$ctl00$MCPH1$SCPH$hdnStudentId", "1241");
        nameValuePair.put("ctl00$ctl00$MCPH1$SCPH$Button1", "Weekly");

        return nameValuePair;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }


    private Elements parseDoc(String s) throws Exception {
        Document doc = Jsoup.parse(s);
        Elements table = doc.select("table > tbody > tr").not(".top-heading");
        if (table.isEmpty()) {
            throw new Exception(AppConstants.ERROR_NO_CONTENT);
        }
        String currentDay = null;
        for (Element row : table) {
            if (row.select("td").first().hasAttr("rowspan")) {
                currentDay = row.select("td").first().text();
            }
            Elements values = row.select("td span");
            String group = values.get(3).text();
            String subject = values.get(1).text();
            String faculty = values.get(2).text();
            String timeSlot = values.get(4).text();
            String hall = values.get(5).text();
            dbhelper.addnewtimetable(subject, currentDay, group, faculty, timeSlot, hall);
        }
        return table;
    }


}
