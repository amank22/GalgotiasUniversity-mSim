package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.ConnectionDetector;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SeatingPlanParcel;
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
public class SeatingPlanJob extends Job {
    private DbSimHelper dbhelper;


    public SeatingPlanJob() {
        super(new Params(AppConstants.PRIORITY2).groupBy(AppConstants.GroupSeatingPlan).requireNetwork());
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        if (!ConnectionDetector.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        String s = GetSeatingPlanData();
        dbhelper.deleteSeatingPlan();
        parseDoc(s);
        EventBus.getDefault().post(new SuccessEvent<SeatingPlanParcel>("Stored", null));
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


    private String GetSeatingPlanData() throws Exception {
        return IonMethods.getString(AppConstants.seatingPlanurl);
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
        for (Element row : table) {
            Elements values = row.select("td span");
            String examDate = values.get(1).text();
            String examTime = values.get(2).text();
            String subjectCode = values.get(8).text();
            String roomNumber = values.get(9).text();
            String seatNumber = values.get(10).text();
            dbhelper.addNewSeatingPlan(examDate, examTime, subjectCode, roomNumber, seatNumber);
        }
        return table;
    }


}
