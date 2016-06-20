package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.CaptchaEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.CaptchaJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.LoginJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.HomeActivity;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.StudentLogin;

import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 11/6/16.
 */
public class CaptchaDialogFragment extends DialogFragment {

    TextView loading;
    EditText mCaptchaView;
    ImageView image;
    Map<String, String> params;
    private boolean CurrentlyRunning = false;
    private boolean captchaRunnig = false;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View root = inflater.inflate(R.layout.dialog_captcha, null);
        loading = (TextView) root.findViewById(R.id.captcha_loading);
        image = (ImageView) root.findViewById(R.id.imageView_captcha);
        mCaptchaView = (EditText) root.findViewById(R.id.captcha);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!captchaRunnig) {
                    final CaptchaJob captchaJob = new CaptchaJob("MainLogin");
                    GUApp.getJobManager().addJobInBackground(captchaJob);
                }
            }
        });
        final CaptchaJob captchaJob = new CaptchaJob("MainLogin");
        GUApp.getJobManager().addJobInBackground(captchaJob);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        Button auth = (Button) root.findViewById(R.id.authorize_button);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (params == null) {
                    loading.setText("Wait for Captcha loading!");
                }
                attemptLogin(params);
            }
        });
        builder.setView(root).setCancelable(false);
        return builder.create();
    }

    private void attemptLogin(Map<String, String> params) {
        if (CurrentlyRunning) {
            loading.setText("Authorization in Progress");
            return;
        }
        CurrentlyRunning = true;
        String mUsername = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "Username");
        String mPassword = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "Password");
        String mCaptcha = mCaptchaView.getText().toString().trim();

        if (TextUtils.isEmpty(mCaptcha)) {
            loading.setText("Enter Captcha");
            CurrentlyRunning = false;
            return;
        }
        if (mCaptcha.length() != 3) {
            loading.setText("Captcha should be of length 3");
            CurrentlyRunning = false;
            return;
        }
        GUApp.getJobManager().addJobInBackground(new LoginJob(mUsername, mPassword, mCaptcha, params, "MainLogin"));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LoginEvent event) {
        CurrentlyRunning = false;
        if (event.isError()) {
            loading.setText(event.getReason());
            PrefUtils.saveToPrefs(getActivity(),
                    PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "PASSWORD_INCORRECT");
            Intent i = new Intent(getActivity(), StudentLogin.class);
            startActivity(i);
            getActivity().finish();
        } else {
            loading.setText(event.getReason());
            if (getActivity() instanceof StudentLogin) {
                Intent i = new Intent(getActivity(), HomeActivity.class);
                startActivity(i);
                getActivity().finish();
            } else {
                getDialog().dismiss();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final CaptchaEvent event) {
        captchaRunnig = false;
        if (event.getBitmap() == null) {
            loading.setText(event.getErrorMsg() + "\nTap on Image to reload.");
        } else {
            loading.setText("Enter Captcha to authorize");
            image.setImageBitmap(event.getBitmap());
            params = event.getParams();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CurrentlyRunning = false;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
            t.printStackTrace();
        }
    }
}
