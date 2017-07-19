package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.TimeTableAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.TTFindParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.TimeTableJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TimeTableParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 25-12-2014 in ${PROJECT_NAME}.
 */
public class FragmentTimeTable extends BaseFragment {

    TextView loading;
    RecyclerView list;
    String day_type;
    private List<TimeTableParcel> parcel;

    public static FragmentTimeTable newInstance(String day) {
        Bundle bundle = new Bundle();
        bundle.putString("type_key", day);
        FragmentTimeTable fragment = new FragmentTimeTable();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            day_type = args.getString("type_key");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timetable, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = (TextView) view.findViewById(R.id.textView_att_loading);
        list = (RecyclerView) view.findViewById(R.id.listView_timetable);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 1);
        list.addItemDecoration(itemDecoration);
        if (savedInstanceState != null) {
            day_type = savedInstanceState.getString("day");
        }
        GUApp.getJobManager().addJobInBackground(new TTFindParcel(day_type));
    }

    private void uselist() {
        if (getActivity() != null) {
            list.setAdapter(new TimeTableAdapter(parcel));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("day", day_type);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LocalErrorEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
        switch (event.getResponse()) {
            case AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND:
                GUApp.getJobManager().addJobInBackground(new TimeTableJob());
                break;
            case AppConstants.ERROR_CONTENT_FETCH:
                loading.setText(AppConstants.ERROR_TIME_TABLE);
                break;
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(SessionExpiredEvent event) {
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        captchaDialogFragment.show(getActivity().getSupportFragmentManager(), "captchaFrag");
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LoginEvent event) {
        if (!event.isError()) {
            onEventMainThread(new LocalErrorEvent(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableStartEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableSuccessEvent event) {
        if (event.getParcel() == null) {
            GUApp.getJobManager().addJobInBackground(new TTFindParcel(day_type));
        } else {
            loading.setVisibility(View.GONE);
            parcel = event.getParcel();
            uselist();
        }
    }

}



