package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.CaptchaDialogFragment;

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
        mUsername = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_USERNAME_KEY, "Username");
        mPassword = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_LOGIN_PASSWORD_KEY, "Password");
        mUserView = (EditText) findViewById(R.id.username);
        mPasswordView = (EditText) findViewById(R.id.password);
        if (checkValidity() && !mPassword.equals("PASSWORD_INCORRECT")) {
            Intent i = new Intent(StudentLogin.this, HomeActivity.class);
            startActivity(i);
            finish();
        } else if (mPassword.equals("PASSWORD_INCORRECT")) {
            mUserView.setText(mUsername);
            alert("Re-enter Login Details");
        }
        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        assert signInButton != null;
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        PrefUtils.saveToPrefs(StudentLogin.this,
                PrefUtils.PREFS_LOGIN_USERNAME_KEY, mUsername);
        PrefUtils.saveToPrefs(StudentLogin.this,
                PrefUtils.PREFS_LOGIN_PASSWORD_KEY, mPassword);
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        captchaDialogFragment.show(getSupportFragmentManager(), "captchaFrag");
    }

    private void alert(String s) {
        Snackbar.make(container, s, Snackbar.LENGTH_LONG).show();
    }

    private boolean checkValidity() {
        return (!mUsername.equals("Username") && !mUsername.equals("") && !mPassword.equals("") && !mPassword.equals("Password"));
    }
}
