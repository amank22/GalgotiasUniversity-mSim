package com.aman.teenscribblers.galgotiasuniversitymsim.jobs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.events.NewsTopicEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsTopicsParcel;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by amankapoor on 07/08/17.
 */

public class NewsTopicsJob extends Job {

    private final String admNo;

    public NewsTopicsJob(String admNo) {
        super(new Params(AppConstants.PRIORITY4).requireNetwork());
        this.admNo = admNo;
    }

    @Override
    public void onAdded() {

    }

    @Override
    public void onRun() throws Throwable {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("News", "AddTopicOnServer: " + token);
        NewsTopicsParcel newsTopicParcel = IonMethods.getNewsTopicLists(admNo, FirebaseInstanceId.getInstance().getToken());
        if (newsTopicParcel.getError()) {
            EventBus.getDefault().post(new NewsTopicEvent(newsTopicParcel.getResult()));
        } else {
            final List<NewsTopicsParcel.NewsTopics> topics = newsTopicParcel.getTopics();
            EventBus.getDefault().post(new NewsTopicEvent(topics));
        }
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        EventBus.getDefault().post(new NewsTopicEvent("We were unable to get the topics. Please retry."));
    }

    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        if (throwable.getMessage().contains("User not Valid")) {
            EventBus.getDefault().post(new NewsTopicEvent("User not Valid"));
            return RetryConstraint.CANCEL;
        }
        return RetryConstraint.RETRY;
    }

    @Override
    protected int getRetryLimit() {
        return 3;
    }
}
