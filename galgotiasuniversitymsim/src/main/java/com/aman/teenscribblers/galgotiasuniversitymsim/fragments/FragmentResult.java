package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.ResultAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.ResultSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.ResultJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.ResultLocalJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.ResultParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentResult extends BaseFragment {

    public static final String ARG_SEMESTER = "arg_sem";
    private static final String TAG = "Result Fragment";
    private RecyclerView list;
    private ProgressBar pb;
    private String semester;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getActivity()).inflate(R.layout.frag_results_content, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pb = (ProgressBar) view.findViewById(R.id.progressBar_personal);
        list = (RecyclerView) view.findViewById(R.id.listView_personal);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        list.setLayoutManager(linearLayoutManager);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 2);
        list.addItemDecoration(itemDecoration);
        list.setHasFixedSize(true);
        if (savedInstanceState == null) {
            semester = getArguments().getString(ARG_SEMESTER);
        } else {
            semester = savedInstanceState.getString(ARG_SEMESTER);
        }
        GUApp.getJobManager().addJobInBackground(new ResultLocalJob(semester));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_SEMESTER, semester);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final ResultSuccessEvent event) {
        if (semester.equals(event.getSemester())) {
            pb.setVisibility(View.GONE);
            List<ResultParcel> parsedList = event.getParcel();
            setAdapter(parsedList);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final LocalErrorEvent event) {
        String admNo= PrefUtils.getFromPrefs(getContext(),PrefUtils.PREFS_USER_ADMNO_KEY,"").trim();
        GUApp.getJobManager().addJobInBackground(new ResultJob(admNo));
    }

    private void setAdapter(List<ResultParcel> parsedList) {
        ResultAdapter adapter = new ResultAdapter(parsedList);
        list.setAdapter(adapter);
    }

}