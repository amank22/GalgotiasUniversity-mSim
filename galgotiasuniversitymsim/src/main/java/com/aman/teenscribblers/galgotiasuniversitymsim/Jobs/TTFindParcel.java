package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.TimeTableParcel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class TTFindParcel extends Job {

    String day;
    DbSimHelper dbhelper;

    public TTFindParcel(String day) {
        super(new Params(AppConstants.PRIORITY4));
        this.day = day;
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new TimeTableStartEvent("Fetching Locally"));
    }

    @Override
    public void onRun() throws Throwable {
        List<TimeTableParcel> parcel = dbhelper.getTimeTable(day);
        if (parcel.isEmpty()) {
            EventBus.getDefault().post(new TimeTableErrorEvent("Loading From Network"));
        }
        EventBus.getDefault().post(new TimeTableSuccessEvent("Loaded offline", parcel));
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new TimeTableErrorEvent("Error Fetching!"));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }

}
