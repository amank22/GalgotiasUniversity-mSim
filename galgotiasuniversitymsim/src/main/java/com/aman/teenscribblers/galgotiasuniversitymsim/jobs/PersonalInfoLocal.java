package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.FileUtil;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.InfoParcel;
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
    private Context context;

    public PersonalInfoLocal(Context context) {
        super(new Params(AppConstants.PRIORITY4));
        this.context = context;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String fileDate = FileUtil.readFile(getApplicationContext(), AppConstants.FILE_NAME_PERSONAL);
        List<InfoParcel> parsedList = parseFileToMap(fileDate);
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_PERSONAL, parsedList));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_PERSONAL, true, "Error", true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    private List<InfoParcel> parseFileToMap(String fileDate) {
        List<InfoParcel> list = new ArrayList<>();
        final String[] split = fileDate.split("==");
        for (String row : split) {
            String[] keyPair = row.split(":");
            if (keyPair.length == 2) {
                PrefUtils.saveToPrefs(context, keyPair[0], keyPair[1]);
                list.add(new InfoParcel(keyPair[0], keyPair[1]));
            }
        }
        return list;
    }

}
