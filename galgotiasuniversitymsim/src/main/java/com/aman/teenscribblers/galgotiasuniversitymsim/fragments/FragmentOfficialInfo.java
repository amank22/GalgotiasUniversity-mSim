package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;


import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.OfficialInfoAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.OfficalInfoLocal;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.OfficialInfoJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.InfoParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOfficialInfo extends BaseFragment {


    private static final String TAG = "PersonalInfo Fragment";
    private RecyclerView list;
    private ProgressBar pb;
    public ImageView image;
    private View rootview;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_officialinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        pb = (ProgressBar) view.findViewById(R.id.progressBar_official);
        list = (RecyclerView) view.findViewById(R.id.listView_official);
        image = (ImageView) view.findViewById(R.id.imageView_nav_official);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        GUApp.getJobManager().addJobInBackground(new OfficalInfoLocal(getActivity()));
        //setProfilePicture();

    }

    /*
   private void setProfilePicture() {
        String url = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_IMAGE, "");
        if (!url.equals("")) {
            Picasso picasso = Picasso.with(getContext());
            picasso.setIndicatorsEnabled(false);
            picasso.load(url)
                    .noPlaceholder()
                    .centerInside()
                    .resize(400, 400)
                    .transform(new CircleTransform())
                    .priority(Picasso.Priority.HIGH)
                    .into(image);
        } else {
            String gender = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_GENDER_KEY, "Male");
            if (gender.equals("Female")) {
                image.setImageResource(R.drawable.ic_avatar_girl);
            }
        }
    }

 */



    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final InfoEvent event) {
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
            GUApp.getJobManager().addJobInBackground(new OfficalInfoLocal(getActivity()));
        }
    }
    private void handleLocalEvent(InfoEvent event) {
        if (event.getError()) {
            GUApp.getJobManager().addJobInBackground(new OfficialInfoJob());
        } else {
            pb.setVisibility(View.GONE);
//            AddGcmRegistrationToPref();
            List<InfoParcel> parsedList = event.getParsedList();
            setAdapter(parsedList);
//            setProfilePicture();
        }
    }
    private void setAdapter(List<InfoParcel> parsedList) {
        OfficialInfoAdapter adapter = new OfficialInfoAdapter(parsedList);
        list.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mRegistrationBroadcastReceiver);
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(getActivity(), resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                Snackbar.make(rootview, "This device is not supported for Notifications.", Snackbar.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }


}
