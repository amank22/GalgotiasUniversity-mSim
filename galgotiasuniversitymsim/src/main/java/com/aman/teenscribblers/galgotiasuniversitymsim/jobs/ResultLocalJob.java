package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.ResultSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.ResultParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 8-July-2017 in Galgotias University(mSim).
 */
public class ResultLocalJob extends Job {

    DbSimHelper dbhelper;
    String semester;

    public ResultLocalJob(String semester) {
        super(new Params(AppConstants.PRIORITY4));
        this.semester = semester;
        dbhelper = DbSimHelper.getInstance();
    }

    @Override
    public void onAdded() {
    }

    @Override
    public void onRun() throws Throwable {
        List<ResultParcel> parcel = dbhelper.getResults(semester);
        if (parcel.isEmpty()) {
            throw new Exception(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND);
        }
        EventBus.getDefault().post(new ResultSuccessEvent(parcel, semester));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
//        EventBus.getDefault().post(new ResultSuccessEvent(null));
        EventBus.getDefault().post(new LocalErrorEvent(throwable == null ? null : throwable.getLocalizedMessage()));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }


}
