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
 * Created by Anmol Agarwal on 8/24/2017.
 */

public class SeatingPlanLocal extends Job {
    private static final String TAG = "Seating Plan";
    private Context context;

    public SeatingPlanLocal(Context context) {
        super(new Params(AppConstants.PRIORITY4));
        this.context = context;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String fileDate = FileUtil.readFile(getApplicationContext(), AppConstants.FILE_NAME_SEATING);
        List<InfoParcel> parsedList = parseFileToMap(fileDate);
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_SEATING,parsedList));
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new InfoEvent(InfoEvent.TYPE_SEATING,true, "Error", true));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        return RetryConstraint.CANCEL;
    }

    private List<InfoParcel> parseFileToMap(String fileDate) {
        List<InfoParcel> list = new ArrayList<>();

        return list;
    }

}
