package com.aman.teenscribblers.galgotiasuniversitymsim.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.AttendanceJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.LoginJob;

import de.greenrobot.event.EventBus;


public class SimService extends Service {

    String mUsername, mPassword;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mUsername = PrefUtils.getFromPrefs(SimService.this,
                PrefUtils.PREFS_LOGIN_USERNAME_KEY, "user");
        mPassword = PrefUtils.getFromPrefs(SimService.this,
                PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "pass");
        EventBus.getDefault().register(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!mUsername.equals("user") && !mUsername.equals("")) {
            GUApp.getJobManager().addJobInBackground(new LoginJob(mUsername, mPassword, AppConstants.GroupAttendance));
            GUApp.getJobManager().addJobInBackground(new AttendanceJob("ctl00$ctl00$MCPH1$SCPH$btntodayAtt",
                    "Today+Attendance", true));
            GUApp.getJobManager().addJobInBackground(new AttendanceJob("ctl00$ctl00$MCPH1$SCPH$btnMonthlyAtt",
                    "Monthly+Attendance", true));
            GUApp.getJobManager().addJobInBackground(new AttendanceJob("ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt",
                    "Subject+Wise+Attendance", true));
            GUApp.getJobManager().addJobInBackground(new AttendanceJob("ctl00$ctl00$MCPH1$SCPH$btnSemAtt",
                    "Semester+Attendance", true));
        }
        return Service.START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
            t.printStackTrace();
        }
    }


}
