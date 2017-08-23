package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.PersonalInfoAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.OfficialInfoJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.OfficialInfoLocal;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.SeatingPlanJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.SeatingPlanLocal;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.InfoParcel;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeatingPlanFragment extends Fragment {
    private static final String TAG = "Seating Fragment";
    private RecyclerView list;
    private ProgressBar pb;
    private View rootview;


    public SeatingPlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_seating_plan, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        pb = (ProgressBar) view.findViewById(R.id.progressBar_seating);
        list = (RecyclerView) view.findViewById(R.id.listView_seating);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        GUApp.getJobManager().addJobInBackground(new SeatingPlanLocal(getActivity()));

    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final InfoEvent event) {
        if(!event.getType().equals(InfoEvent.TYPE_SEATING)){
            return;
        }
        if (event.getLocal()) {
            handleLocalEvent(event);
        } else {
            handleNetworkEvent(event);
        }
    }


    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(SessionExpiredEvent event) {
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        captchaDialogFragment.show(getActivity().getSupportFragmentManager(), "captchaFrag");
    }

    private void handleNetworkEvent(InfoEvent event) {
        if (event.getError()) {
            pb.setVisibility(View.GONE);
            Snackbar.make(rootview, event.getData(), Snackbar.LENGTH_INDEFINITE).show();
        } else {
            GUApp.getJobManager().addJobInBackground(new SeatingPlanLocal(getActivity()));
        }
    }

    private void handleLocalEvent(InfoEvent event) {
        if (event.getError()) {
            GUApp.getJobManager().addJobInBackground(new SeatingPlanJob());
        } else {
            pb.setVisibility(View.GONE);
            List<InfoParcel> parsedList = event.getParsedList();
            setAdapter(parsedList);
        }
    }

    private void setAdapter(List<InfoParcel> parsedList) {
        PersonalInfoAdapter adapter = new PersonalInfoAdapter(parsedList);
        list.setAdapter(adapter);
    }

}
