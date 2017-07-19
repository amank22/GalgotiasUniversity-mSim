package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.CaptchaDialogFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 24-10-2015 in Galgotias University(mSim).
 */
public class StudentLogin extends BaseActivity {

    RelativeLayout container;
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
        mUsername = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, null);
        mPassword = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, null);
        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        if (mPassword != null) {
            Intent i = new Intent(StudentLogin.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        if (CurrentlyRunning) {
            alert(mUserView, "Already Attempting Login");
            return;
        }
        CurrentlyRunning = true;
        mUsername = mUserView.getText().toString().trim();
        mPassword = mPasswordView.getText().toString().trim();
        // Check for a valid password.
        if (TextUtils.isEmpty(mPassword)) {
            alert(mPasswordView, "Enter Password");
            CurrentlyRunning = false;
            return;
        }

        // Check for a valid username.
        if (TextUtils.isEmpty(mUsername)) {
            alert(mUserView, "Enter Username");
            CurrentlyRunning = false;
            return;
        }
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        Bundle args = new Bundle(2);
        args.putString(PrefUtils.PREFS_LOGIN_USERNAME_KEY, mUsername);
        args.putString(PrefUtils.PREFS_LOGIN_PASSWORD_KEY, mPassword);
        captchaDialogFragment.setArguments(args);
        captchaDialogFragment.show(getSupportFragmentManager(), "captchaFrag");
    }

    private void alert(EditText mView, String s) {
        mView.setError(s);
        mView.requestFocus();
    }

    public void setCurrentlyRunning(boolean currentlyRunning) {
        CurrentlyRunning = currentlyRunning;
    }
}
