package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceFetchingEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceProccesedEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.AttFindParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.AttendanceJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.SimParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.StudentLogin;
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
    private ListView list;
    private List<SimParcel> parcel;
    private TextView loading;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onRefresh() {
        GUApp.getJobManager().cancelJobsInBackground(new CancelResult.AsyncCancelCallback() {
            @Override
            public void onCancelled(CancelResult cancelResult) {
                GUApp.getJobManager().addJobInBackground(new AttendanceJob(false, type, fromDate, toDate));
            }
        }, TagConstraint.ALL);

    }

    static class ViewHolderMonth {
        TextView subject;
        TextView percentage;
        public TextView present, absent, total;
        View indcator;
    }

    static class ViewHolderToday {
        TextView subject, timeslot;
        TextView atttype;
        TextView status;
        View indcator;
    }

    static class ViewHolderSem {
        TextView sem;
        TextView percentage;
        public TextView present, absent, total;
        View indcator;
    }

    static class ViewHolderSubj {
        TextView subject;
        TextView percentage;
        public TextView present, absent, total;
        View indcator;
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
        list = (ListView) view.findViewById(R.id.listView_attendance);
        if (savedInstanceState != null) {
            type = savedInstanceState.getString(ARG_TYPE);
            fromDate = savedInstanceState.getString(ARG_FROM_DATE);
            toDate = savedInstanceState.getString(ARG_TO_DATE);
        }
        GUApp.getJobManager().addJobInBackground(new AttFindParcel(type, fromDate, toDate));
    }

    private void uselist() {
        if (getActivity() != null) {
            list.setAdapter(new CardsAdapter());
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


    public class CardsAdapter extends ArrayAdapter {
        private LayoutInflater mInflater;

        public CardsAdapter() {
            super(getActivity(), R.layout.attendance_list_item);
            mInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public int getCount() {
            return parcel == null ? 0 : parcel.size();
        }

        @Override
        public long getItemId(int pos) {
            return pos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            switch (type) {
                case AppConstants.ATT_SUBJECT:
                    ViewHolderSubj subholder = null;
                    if (row == null) {
                        row = mInflater
                                .inflate(R.layout.attendance_list_item, parent, false);
                        subholder = new ViewHolderSubj();
                        subholder.subject = (TextView) row
                                .findViewById(R.id.textView_subject);
                        subholder.percentage = (TextView) row
                                .findViewById(R.id.textView_perc);
                        subholder.indcator = row.findViewById(R.id.p_indicator);

                        subholder.total = (TextView) row
                                .findViewById(R.id.textView_total);
                        subholder.present = (TextView) row
                                .findViewById(R.id.textView_present);
                        subholder.absent = (TextView) row
                                .findViewById(R.id.textView_absent);
                        row.setTag(subholder);
                    } else {
                        subholder = (ViewHolderSubj) row.getTag();
                    }
                    // setting text data
                    if (parcel != null && !parcel.isEmpty()) {
                        Log.d("GU_DEBUG", "parcel nor null neither empty");
                        subholder.subject
                                .setText(parcel.get(position).getSubject());
                        subholder.percentage.setText(String.valueOf(parcel.get(
                                position).getPercnt()));
                        if (parcel.get(position).getPercnt() < 75.0f) {
                            subholder.indcator.setBackgroundColor(getResources().getColor(R.color.ts_red));
                        } else {
                            subholder.indcator.setBackgroundColor(getResources().getColor(R.color.ts_green));
                        }
                        subholder.total.setText(String.valueOf(parcel.get(position)
                                .getTotal()));
                        subholder.present.setText(String.valueOf(parcel.get(
                                position).getPresent()));
                        subholder.absent.setText(String.valueOf(parcel
                                .get(position).getAbsent()));

                    }
                    break;
                case AppConstants.ATT_TODAY:
                    ViewHolderToday tholder = null;
                    if (row == null) {
                        row = mInflater
                                .inflate(R.layout.todays_att_list_item, parent, false);
                        tholder = new ViewHolderToday();
                        tholder.subject = (TextView) row
                                .findViewById(R.id.textView_subject);
                        tholder.timeslot = (TextView) row
                                .findViewById(R.id.textView_time_slot);
                        tholder.atttype = (TextView) row
                                .findViewById(R.id.textView_class_type);
                        tholder.status = (TextView) row
                                .findViewById(R.id.textView_status);
                        row.setTag(tholder);
                    } else {
                        tholder = (ViewHolderToday) row.getTag();
                    }
                    // setting text data
                    if (parcel != null && !parcel.isEmpty()) {
                        Log.d("GU_DEBUG", "parcel nor null neither empty");
                        if (parcel.get(position).getSubject() != null)
                            tholder.subject.setText(parcel.get(position).getSubject());
                        if (parcel.get(position).getTimeslot() != null)
                            tholder.timeslot
                                    .setText(parcel.get(position).getTimeslot());
                        if (parcel.get(position).getClasstype() != null)
                            tholder.atttype
                                    .setText(parcel.get(position).getClasstype());
                        if (parcel.get(position).getStatus() != null) {
                            tholder.status.setText(parcel.get(position).getStatus());
                            if (parcel.get(position).getStatus().equals("A")) {
                                tholder.status.setBackgroundColor(getResources().getColor(R.color.ts_red));
                            } else if (parcel.get(position).getStatus().equals("P")) {
                                tholder.status.setBackgroundColor(getResources().getColor(R.color.ts_green));
                            } else {
                                tholder.status.setBackgroundColor(getResources().getColor(R.color.ts_grey));
                            }
                        }
                    }

                    break;
            }
            return row;
        }
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
            uselist();
        } else if (event.getProccessed().equals("FetchedFromNetwork")) {
            GUApp.getJobManager().addJobInBackground(new AttFindParcel(type, fromDate, toDate));
        }
    }


}
