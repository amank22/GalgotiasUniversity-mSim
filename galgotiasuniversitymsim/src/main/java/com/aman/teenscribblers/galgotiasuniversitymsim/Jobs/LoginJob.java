package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.IonMethods;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 */
public class LoginJob extends Job {

    String mUsername, mPassword;
    private String error = "NULL";

    public LoginJob(String user, String pass, String GroupBy) {
        super(new Params(AppConstants.PRIORITY3).groupBy(GroupBy).requireNetwork());
        mUsername = user;
        mPassword = pass;
    }

    @Override
    public void onAdded() {
        if (mUsername.equals("user") || mUsername.equals("")) {
            EventBus.getDefault().post(new LoginEvent("Username-Invalid",true));
        }
    }

    @Override
    public void onRun() throws Throwable {
        boolean sucess;
        try {
            sucess=IonMethods.simlogin(mUsername, mPassword);
        } catch (Exception e) {
            sucess=false;
            error = e.getLocalizedMessage();
            throw e;
        }
        if (sucess){
            EventBus.getDefault().post(new LoginEvent("Login Success",false));
        }
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new LoginEvent(error,true));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        return true;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }

}
