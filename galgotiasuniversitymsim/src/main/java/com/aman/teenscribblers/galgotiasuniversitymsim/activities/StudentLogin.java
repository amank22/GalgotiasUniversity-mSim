package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.BroadCastReciever.SimScheduleReceiver;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Connection_detect;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.LoginJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 24-10-2015 in Galgotias University(mSim).
 */
public class StudentLogin extends BaseActivity {

    RelativeLayout container;
    private SimScheduleReceiver myreceiver;
    private ProgressBar p;
    // Values for email and password at the time of the login attempt.
    private String mUsername;
    private String mPassword;
    private boolean CurrentlyRunning = false;
    // UI references.
    private EditText mUserView;
    private EditText mPasswordView;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        container = (RelativeLayout) findViewById(R.id.login_container);
        Connection_detect cd = new Connection_detect(getApplicationContext());
        Boolean isInternetPresent = cd.isConnectingToInternet();
        myreceiver = new SimScheduleReceiver();
        String BROADCAST = "com.teenscribblers.GU.Broadcast";
        IntentFilter inf = new IntentFilter(BROADCAST);
        mUsername = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "Username");
        mPassword = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "Password");
        if (isInternetPresent && checkValidity())
            registerReceiver(myreceiver, inf);
        if (checkValidity()) {
            Intent i = new Intent(StudentLogin.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        p = (ProgressBar) findViewById(R.id.progressBar_login);
        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        mUserView.setHint(mUsername);
        findViewById(R.id.sign_in_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        attemptLogin();

                    }

                });

    }

    private void attemptLogin() {
        if (CurrentlyRunning) {
            alert("Already Attempting Login");
            return;
        }
        CurrentlyRunning = true;
        mUsername = mUserView.getText().toString().trim();
        mPassword = mPasswordView.getText().toString().trim();
        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            alert("Enter Password");
            CurrentlyRunning = false;
            return;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(mUsername)) {
            alert("Enter Username");
            CurrentlyRunning = false;
            return;
        }
        p.setVisibility(View.VISIBLE);
        GUApp.getJobManager().addJobInBackground(new LoginJob(mUsername, mPassword, "MainLogin"));
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LoginEvent event) {
        CurrentlyRunning = false;
        p.setVisibility(View.INVISIBLE);
        if (event.isError()) {
            alert(event.getReason());
        } else {
            alert(event.getReason());
            PrefUtils.saveToPrefs(StudentLogin.this,
                    PrefUtils.PREFS_LOGIN_USERNAME_KEY, mUsername);
            PrefUtils.saveToPrefs(StudentLogin.this,
                    PrefUtils.PREFS_LOGIN_PASSWORD_KEY, mPassword);
            Intent i = new Intent(StudentLogin.this, HomeActivity.class);
            startActivity(i);
            finish();

        }
    }

    private void alert(String s) {
        Snackbar.make(container, s, Snackbar.LENGTH_LONG).show();
    }

    private boolean checkValidity() {
        return (!mUsername.equals("Username") && !mUsername.equals("") && !mPassword.equals("") && !mPassword.equals("Password"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurrentlyRunning = false;
        try {
            unregisterReceiver(myreceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
            t.printStackTrace();
        }
    }
}
