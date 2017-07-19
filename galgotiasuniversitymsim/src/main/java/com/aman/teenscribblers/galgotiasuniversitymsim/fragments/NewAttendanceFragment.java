package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.AttendanceAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.AttFindParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.AttendanceJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SimParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;
import com.birbit.android.jobqueue.CancelResult;
import com.birbit.android.jobqueue.TagConstraint;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by amankapoor on 06/07/17.
 */

public class NewAttendanceFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String ARG_TYPE = "att_type";
    private static final String ARG_FROM_DATE = "from_date";
    private static final String ARG_TO_DATE = "to_date";

    private String type, fromDate, toDate;
    private RecyclerView list;
    private List<SimParcel> parcel;
    private TextView loading;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        GUApp.getJobManager().cancelJobsInBackground(new CancelResult.AsyncCancelCallback() {
            @Override
            public void onCancelled(CancelResult cancelResult) {
                GUApp.getJobManager().addJobInBackground(new AttendanceJob(false, type, fromDate, toDate));
            }
        }, TagConstraint.ALL);

    }


    public static NewAttendanceFragment newInstance(String type, String fromDate, String toDate) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TYPE, type);
        bundle.putString(ARG_FROM_DATE, fromDate);
        bundle.putString(ARG_TO_DATE, toDate);
        NewAttendanceFragment fragment = new NewAttendanceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            fromDate = args.getString(ARG_FROM_DATE);
            toDate = args.getString(ARG_TO_DATE);
            type = args.getString(ARG_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_attendance, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout header = (LinearLayout) view.findViewById(R.id.layout_head);
        loading = (TextView) view.findViewById(R.id.textView_att_loading);
        if (type.equals(AppConstants.ATT_TODAY)) {
            header.setVisibility(View.GONE);
        } else {
            header.setVisibility(View.VISIBLE);
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.att_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ts_blue, R.color.ts_green, R.color.ts_pink, R.color.ts_red);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);
        list = (RecyclerView) view.findViewById(R.id.listView_attendance);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 1);
        list.addItemDecoration(itemDecoration);
        if (savedInstanceState != null) {
            type = savedInstanceState.getString(ARG_TYPE);
            fromDate = savedInstanceState.getString(ARG_FROM_DATE);
            toDate = savedInstanceState.getString(ARG_TO_DATE);
        }
        GUApp.getJobManager().addJobInBackground(new AttFindParcel(type, fromDate, toDate));
    }

    private void setAdapter() {
        if (getActivity() != null) {
            list.setAdapter(new AttendanceAdapter(getActivity(), type, parcel));
        } else {
            loading.setText(AppConstants.ERROR_CONTENT_FETCH);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_TYPE, type);
        outState.putString(ARG_FROM_DATE, fromDate);
        outState.putString(ARG_TO_DATE, toDate);
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(AttendanceErrorEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getError());
        mSwipeRefreshLayout.setRefreshing(false);
        if (event.getError().equals(AppConstants.ERROR_LOCAL_CACHE_NOT_FOUND)) {
            GUApp.getJobManager().addJobInBackground(new AttendanceJob(false, type, fromDate, toDate));
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
        mSwipeRefreshLayout.setRefreshing(false);
        if (!event.isError()) {
            GUApp.getJobManager().addJobInBackground(new AttendanceJob(false, type, fromDate, toDate));
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(AttendanceFetchingEvent event) {
        loading.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(true);
        loading.setText(event.getFromNetwork() ? "Looking from Network..." : "Looking Locally...");
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(AttendanceProccesedEvent event) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (event.getParcel() != null) {
            loading.setVisibility(View.GONE);
            parcel = event.getParcel();
            setAdapter();
        } else if (event.getProccessed().equals("FetchedFromNetwork")) {
            GUApp.getJobManager().addJobInBackground(new AttFindParcel(type, fromDate, toDate));
        }
    }


}
