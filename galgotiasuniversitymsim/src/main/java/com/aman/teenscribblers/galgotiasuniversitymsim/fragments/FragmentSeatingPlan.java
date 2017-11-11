package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.SeatingPlanAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.SeatingPlanJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.SeatingPlanLocalJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SeatingPlanParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 11-11-2017 in GALGOTIAS UNIVERSITY.
 */
public class FragmentSeatingPlan extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    TextView loading;
    RecyclerView list;
    private List<SeatingPlanParcel> parcel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean tryLoginInOnce = false;

    public static FragmentSeatingPlan newInstance() {
        Bundle bundle = new Bundle();
        FragmentSeatingPlan fragment = new FragmentSeatingPlan();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_with_refresh, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = (TextView) view.findViewById(R.id.textView_loading);
        list = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ts_blue, R.color.ts_green, R.color.ts_pink, R.color.ts_red);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 1);
        list.addItemDecoration(itemDecoration);
        GUApp.getJobManager().addJobInBackground(new SeatingPlanLocalJob());
    }

    private void uselist() {
        if (getActivity() != null) {
            list.setAdapter(new SeatingPlanAdapter(parcel));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LocalErrorEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
        mSwipeRefreshLayout.setRefreshing(true);
        switch (event.getResponse()) {
            case AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND:
                GUApp.getJobManager().addJobInBackground(new SeatingPlanJob());
                break;
            case AppConstants.ERROR_CONTENT_FETCH:
                if (tryLoginInOnce) {
                    loading.setText(AppConstants.ERROR_SEATING_PLAN);
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    tryLoginInOnce = true;
                    onEventMainThread(new SessionExpiredEvent());
                }
                break;
        }

    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(SessionExpiredEvent event) {
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        captchaDialogFragment.show(getActivity().getSupportFragmentManager(), "captchaFrag");
        mSwipeRefreshLayout.setRefreshing(false);
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LoginEvent event) {
        if (!event.isError()) {
            mSwipeRefreshLayout.setRefreshing(true);
            onEventMainThread(new LocalErrorEvent(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND));
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableStartEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(SuccessEvent<SeatingPlanParcel> event) {
        if (event.getParcel() == null) {
            GUApp.getJobManager().addJobInBackground(new SeatingPlanLocalJob());
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            loading.setVisibility(View.GONE);
            parcel = event.getParcel();
            uselist();
        }
    }

    @Override
    public void onRefresh() {
        GUApp.getJobManager().addJobInBackground(new SeatingPlanJob());
        mSwipeRefreshLayout.setRefreshing(true);
    }
}



