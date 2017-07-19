package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.CaptchaEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import de.greenrobot.event.EventBus;

/**
 * Created by amankapoor on 04/07/17.
 */

public class CaptchaJob extends Job {
    public CaptchaJob(String groupBy) {
        super(new Params(AppConstants.PRIORITY2).groupBy(groupBy).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        if (!Connection_detect.isConnectingToInternet(getApplicationContext())) {
            throw new Exception(AppConstants.ERROR_NETWORK);
        }
        CaptchaEvent captchaEvent = IonMethods.simCaptcha();
        EventBus.getDefault().post(captchaEvent);
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new CaptchaEvent(null, null));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().equals(AppConstants.ERROR_NETWORK) || runCount == maxRunCount) {
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }
}
