package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SimParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class AttFindParcel extends Job {

    private String typevalue;
    private String fromDate = "", toDate = "";
    private List<SimParcel> parcel;
    private DbSimHelper dbhelper;

    public AttFindParcel(String value, String fromDate, String toDate) {
        super(new Params(AppConstants.PRIORITY4));
        typevalue = value;
        dbhelper = DbSimHelper.getInstance();
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new AttendanceFetchingEvent(false));
    }

    @Override
    public void onRun() throws Throwable {
        switch (typevalue) {
            case AppConstants.ATT_TODAY:
                parcel = dbhelper.getTodaysAttd();
                break;
            case "Monthly+Attendance"://ignoring these and not even deleting
                parcel = dbhelper.getMonthlyAttd();
                break;
            case AppConstants.ATT_SUBJECT:
                parcel = dbhelper.getSubjAttd(fromDate, toDate);
                break;
            case "Semester+Attendance":
                parcel = dbhelper.getSemAttd();
                break;
        }
        if (parcel.isEmpty()) {
            EventBus.getDefault().post(new AttendanceErrorEvent(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND));
        } else
            EventBus.getDefault().post(new AttendanceProccesedEvent("Loaded Offline", parcel));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new AttendanceErrorEvent("Error Fetching!"));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

}