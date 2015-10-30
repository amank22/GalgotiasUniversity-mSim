package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.NewsEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.NewsParcel;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

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
    protected void onCancel() {

    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return false;
    }
}
