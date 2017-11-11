package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SeatingPlanParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TimeTableParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class SeatingPlanLocalJob extends Job {

    DbSimHelper dbhelper;

    public SeatingPlanLocalJob() {
        super(new Params(AppConstants.PRIORITY4));
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new TimeTableStartEvent("Fetching Seating Plan Locally"));
    }

    @Override
    public void onRun() throws Throwable {
        List<SeatingPlanParcel> parcel = dbhelper.getSeatingPlan();
        if (parcel.isEmpty()) {
            throw new Exception(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND);
        }
        EventBus.getDefault().post(new SuccessEvent<>("Loaded offline", parcel));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        EventBus.getDefault().post(new LocalErrorEvent(throwable.getLocalizedMessage()));
        return RetryConstraint.CANCEL;
    }


}
