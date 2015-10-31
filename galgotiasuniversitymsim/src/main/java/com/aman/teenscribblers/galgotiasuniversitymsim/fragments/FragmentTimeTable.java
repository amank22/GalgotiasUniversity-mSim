package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.LoginJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.TTFindParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.TimeTableJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.TimeTableParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.nhaarman.listviewanimations.appearance.simple.SwingRightInAnimationAdapter;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 25-12-2014 in ${PROJECT_NAME}.
 */
public class FragmentTimeTable extends BaseFragment {

    TextView loading;
    private static final int INITIAL_DELAY_MILLIS = 200;
    private List<TimeTableParcel> parcel;
    ListView list;
    String day_type;
    ViewHolderTT holder;

    static class ViewHolderTT {
        TextView subject;
        TextView group, faculty, timeslot, hallno;
    }


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
        list = (ListView) view.findViewById(R.id.listView_attendance);
        if (savedInstanceState != null) {
            day_type = savedInstanceState.getString("day");
        }
        GUApp.getJobManager().addJobInBackground(new TTFindParcel(day_type));
    }

    private void uselist() {
        if (getActivity() != null) {
            SwingRightInAnimationAdapter swingBottomInAnimationAdapter = new SwingRightInAnimationAdapter(
                    new CardsAdapter(getActivity()));
            swingBottomInAnimationAdapter.setAbsListView(list);
            assert swingBottomInAnimationAdapter.getViewAnimator() != null;
            swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(
                    INITIAL_DELAY_MILLIS);
            list.setAdapter(swingBottomInAnimationAdapter);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("day", day_type);
    }

    public class CardsAdapter extends ArrayAdapter {
        private LayoutInflater mInflater;

        public CardsAdapter(Context c) {
            super(c, R.layout.attendance_list_item);
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
            holder = null;
            if (row == null) {
                row = mInflater
                        .inflate(R.layout.timetable_list_item, null);
                holder = new ViewHolderTT();
                holder.subject = (TextView) row
                        .findViewById(R.id.textView_subject);
                holder.faculty = (TextView) row
                        .findViewById(R.id.textView_faculty);
                holder.group = (TextView) row
                        .findViewById(R.id.textView_group);
                holder.hallno = (TextView) row
                        .findViewById(R.id.textView_hall);
                holder.timeslot = (TextView) row
                        .findViewById(R.id.textView_timeslot);
                row.setTag(holder);
            } else {
                holder = (ViewHolderTT) row.getTag();
            }
            if (parcel != null && !parcel.isEmpty()) {
                if (holder.subject != null)
                    holder.subject
                            .setText(parcel.get(position).getSubject());
                if (holder.group != null)
                    holder.group
                            .setText(parcel.get(position).getGroup());
                if (holder.timeslot != null)
                    holder.timeslot
                            .setText(parcel.get(position).getTimeslot());
                if (holder.hallno != null)
                    holder.hallno
                            .setText(parcel.get(position).getHallno());
                if (holder.faculty != null)
                    holder.faculty
                            .setText(parcel.get(position).getFaculty());
                if (holder.group != null && holder.group.getText().equals("")) {
                    holder.group.setVisibility(View.GONE);
                }
            }
            return row;
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableErrorEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
        if (event.getResponse().equals("Expired") || event.getResponse().equals("Error")) {
            String mUsername = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "user");
            String mPassword = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "pass");
            GUApp.getJobManager().addJobInBackground(new LoginJob(mUsername, mPassword, AppConstants.GroupTimeTable));
            GUApp.getJobManager().addJobInBackground(new TimeTableJob(day_type));
        } else if (event.getResponse().equals("Loading From Network")) {
            GUApp.getJobManager().addJobInBackground(new TimeTableJob(day_type));
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



