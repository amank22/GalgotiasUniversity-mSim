package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.service.RegistrationIntentService;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

/**
 * Created by amankapoor on 17/07/17.
 */

public class RegisterUserOnServerJob extends Job {

    public RegisterUserOnServerJob() {
        super(new Params(AppConstants.PRIORITY4).requireNetwork());
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        RegistrationIntentService.sendRegistrationToServer(getApplicationContext());
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (runCount == maxRunCount) {
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }
}
