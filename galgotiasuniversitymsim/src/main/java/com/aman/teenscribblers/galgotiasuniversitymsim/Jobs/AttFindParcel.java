package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.SimParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class AttFindParcel extends Job {

    String typevalue;
    private List<SimParcel> parcel;
    DbSimHelper dbhelper;

    public AttFindParcel(String value) {
        super(new Params(AppConstants.PRIORITY4));
        typevalue = value;
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new AttendanceFetchingEvent(false));
    }

    @Override
    public void onRun() throws Throwable {
        switch (typevalue) {
            case "Today+Attendance":
                parcel = dbhelper.getTodaysAttd();
                break;
            case "Monthly+Attendance":
                parcel = dbhelper.getMonthlyAttd();
                break;
            case "Subject+Wise+Attendance":
                parcel = dbhelper.getSubjAttd();
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