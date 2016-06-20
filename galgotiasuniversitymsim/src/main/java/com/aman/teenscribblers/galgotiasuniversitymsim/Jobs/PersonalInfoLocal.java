package com.aman.teenscribblers.galgotiasuniversitymsim.Jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.InfoParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 20-02-2015 in Galgotias University(mSim).
 */
public class PersonalInfoLocal extends Job {

    private static final String TAG = "PersonalInfoLocal";

    public PersonalInfoLocal() {
        super(new Params(AppConstants.PRIORITY4));
    }

    @Override
    public void onAdded() {
        Log.d(TAG, "onAdded: ");
    }

    @Override
    public void onRun() throws Throwable {
        Log.d(TAG, "onRun() called with: " + "");
        String fileDate = FileUtil.readFile(getApplicationContext(), AppConstants.FILE_NAME_PERSONAL);
        List<InfoParcel> parsedList = parseFileToMap(fileDate);
        EventBus.getDefault().post(new InfoEvent(parsedList));
    }

    private List<InfoParcel> parseFileToMap(String fileDate) {
        Log.d(TAG, "parseFileToMap() called with: " + "fileDate = [" + fileDate + "]");
        List<InfoParcel> list = new ArrayList<>();
        final String[] split = fileDate.split("==");
        for (String row : split) {
            String[] keyPair = row.split(":");
            if (keyPair.length == 2) {
                list.add(new InfoParcel(keyPair[0], keyPair[1]));
            }
        }
        return list;
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        Log.d(TAG, "onCancel() called with: " + "cancelReason = [" + cancelReason + "]");
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        Log.d(TAG, "shouldReRunOnThrowable() called with: " + "throwable = [" + throwable.getLocalizedMessage() + "], runCount = [" + runCount + "], maxRunCount = [" + maxRunCount + "]");
        EventBus.getDefault().post(new InfoEvent(true, "Error", true));
        return RetryConstraint.CANCEL;
    }

}
