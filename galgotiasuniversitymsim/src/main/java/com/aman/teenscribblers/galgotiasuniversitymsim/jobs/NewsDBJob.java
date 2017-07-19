package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.NewsEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 24-03-2015 in Galgotias University(mSim).
 */
public class NewsDBJob extends Job {

    public NewsDBJob() {
        super(new Params(AppConstants.PRIORITY4));
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new NewsEvent("Starting Local Fetch", null, false));
    }

    @Override
    public void onRun() throws Throwable {
        List<NewsParcel> parcel = DbSimHelper.getInstance().getAllNews();
        if (parcel.isEmpty()) {
            EventBus.getDefault().post(new NewsEvent("No News", null, true));
        } else {
            EventBus.getDefault().post(new NewsEvent("Parcel Successfully Fetched", parcel, false));
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

}
