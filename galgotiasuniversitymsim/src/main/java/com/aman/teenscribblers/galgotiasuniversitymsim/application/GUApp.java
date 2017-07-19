package com.aman.teenscribblers.galgotiasuniversitymsim.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.StudentLogin;
import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.config.Configuration;
import com.birbit.android.jobqueue.log.CustomLogger;

import java.io.File;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by aman on 27-12-2014.
 */
public class GUApp extends Application {
    private static GUApp instance;
    private static JobManager jobManager;

    public static JobManager getJobManager() {
        return jobManager;
    }

    public static GUApp getInstance() {
        return instance;
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                boolean success = deleteDir(new File(dir, aChildren));
                if (!success) {
                    return false;
                }
            }
        }

        assert dir != null;
        return dir.delete();
    }

    public static void logoutUser(Activity activity) {
        PrefUtils.deleteuser(activity);
        GUApp app = GUApp.getInstance();
        app.clearApplicationData();
        Intent i = new Intent(activity, StudentLogin.class);
        activity.startActivity(i);
        activity.finish();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
//        MultiDex.install(this);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Brawler_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());
        configureJobManager();
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    private void configureJobManager() {
        Configuration configuration = new Configuration.Builder(this)
                .customLogger(new CustomLogger() {
                    private static final String TAG = "JOBS";

                    @Override
                    public boolean isDebugEnabled() {
                        return true;
                    }

                    @Override
                    public void d(String text, Object... args) {
                        Log.d(TAG, String.format(text, args));
                    }

                    @Override
                    public void e(Throwable t, String text, Object... args) {
                        Log.e(TAG, String.format(text, args), t);
                    }

                    @Override
                    public void e(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }

                    @Override
                    public void v(String text, Object... args) {
                        Log.e(TAG, String.format(text, args));
                    }
                })
                .minConsumerCount(1)//always keep at least one consumer alive
                .maxConsumerCount(5)//up to 5 consumers at a time
                .loadFactor(6)//6 jobs per consumer
                .consumerKeepAlive(120)//wait 2 minute
                .build();
        jobManager = new JobManager(configuration);
    }
}
