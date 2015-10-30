package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.SimParcel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

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
            EventBus.getDefault().post(new AttendanceErrorEvent("Loading From Network now"));
        }else
        EventBus.getDefault().post(new AttendanceProccesedEvent("Loaded Offline", parcel));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new AttendanceErrorEvent("Error Fetching!"));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}
