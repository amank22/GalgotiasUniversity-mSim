package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.PersonalInfoAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.GlideApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.PersonalInfoJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.PersonalInfoLocal;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.RegisterUserOnServerJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.InfoParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.service.RegistrationIntentService;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentPersonalInfo extends BaseFragment {

    private static final String TAG = "PersonalInfo Fragment";
    private RecyclerView list;
    private ProgressBar pb;
    public ImageView image;
    private View rootview;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_personalinfo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        pb = (ProgressBar) view.findViewById(R.id.progressBar_personal);
        list = (RecyclerView) view.findViewById(R.id.listView_personal);
        image = (ImageView) view.findViewById(R.id.imageView_nav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        GUApp.getJobManager().addJobInBackground(new PersonalInfoLocal(getActivity()));
        setProfilePicture();

    }

    private void setProfilePicture() {
        String url = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_IMAGE, "");
        String gender = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_GENDER_KEY, "Male");
        if (gender.contains("Female")) {
            image.setImageResource(R.drawable.ic_avatar_girl);
        }
        if (!url.equals("")) {
            GlideApp.with(this)
                    .load(url)
                    .transforms(new FitCenter(), new CircleCrop())
                    .into(image);
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final InfoEvent event) {
        if (!event.getType().equals(InfoEvent.TYPE_PERSONAL)) {
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
            GUApp.getJobManager().addJobInBackground(new PersonalInfoLocal(getActivity()));
            logUser();
            GCMCalls();
        }
    }

    private void logUser() {
        String admNo = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_ADMNO_KEY, null);
        String email = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_EMAIL_KEY, null);
        String name = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_USER_NAME_KEY, null);
        if (admNo != null)
            Crashlytics.setUserIdentifier(admNo);
        if (email != null)
            Crashlytics.setUserEmail(email);
        if (name != null)
            Crashlytics.setUserName(name);
    }


    private void handleLocalEvent(InfoEvent event) {
        if (event.getError()) {
            GUApp.getJobManager().addJobInBackground(new PersonalInfoJob());
        } else {
            pb.setVisibility(View.GONE);
            List<InfoParcel> parsedList = event.getParsedList();
            setAdapter(parsedList);
            setProfilePicture();
        }
    }

    private void setAdapter(List<InfoParcel> parsedList) {
        PersonalInfoAdapter adapter = new PersonalInfoAdapter(parsedList);
        list.setAdapter(adapter);
    }

    private void GCMCalls() {
        GUApp.getJobManager().addJobInBackground(new RegisterUserOnServerJob());
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            RegistrationIntentService.subscribeTopics(getActivity(), null);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
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