package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 */
public class LoginJob extends Job {

    String mUsername, mPassword, mCaptcha;
    Map<String, String> params;

    public LoginJob(String user, String pass, String captcha, Map<String, String> params, String GroupBy) {
        super(new Params(AppConstants.PRIORITY4).groupBy(GroupBy).requireNetwork());
        mUsername = user;
        mPassword = pass;
        mCaptcha = captcha;
        this.params = params;
    }

    @Override
    public void onAdded() {
        if (mUsername.equals("user") || mUsername.equals("")) {
            EventBus.getDefault().post(new LoginEvent("Username-Invalid",true));
        }
    }

    @Override
    public void onRun() throws Throwable {
        IonMethods.simLogin(mUsername, mPassword, mCaptcha, params);
            EventBus.getDefault().post(new LoginEvent("Login Success",false));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {

    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        EventBus.getDefault().post(new LoginEvent(throwable.getMessage(), true));
        return RetryConstraint.CANCEL;
    }


}
