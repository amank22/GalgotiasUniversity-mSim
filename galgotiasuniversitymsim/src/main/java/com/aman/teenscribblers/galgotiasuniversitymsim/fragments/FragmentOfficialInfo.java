package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.PersonalInfoAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.OfficialInfoJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.OfficialInfoLocal;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.InfoParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentOfficialInfo extends BaseFragment {

    private static final String TAG = "Official Fragment";
    private RecyclerView list;
    private ProgressBar pb;
    private View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_officialinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        pb = (ProgressBar) view.findViewById(R.id.progressBar_personal);
        list = (RecyclerView) view.findViewById(R.id.listView_personal);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        GUApp.getJobManager().addJobInBackground(new OfficialInfoLocal(getActivity()));

    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final InfoEvent event) {
        if(!event.getType().equals(InfoEvent.TYPE_OFFICIAL)){
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
            GUApp.getJobManager().addJobInBackground(new OfficialInfoLocal(getActivity()));
        }
    }


    private void handleLocalEvent(InfoEvent event) {
        if (event.getError()) {
            GUApp.getJobManager().addJobInBackground(new OfficialInfoJob());
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