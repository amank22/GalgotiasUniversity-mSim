package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

public class WebViewActivity extends BaseActivity {

    private static final String TAG = WebViewActivity.class.getCanonicalName();
    private static final String KEY_URL = "key_url";

    public static Intent getActivity(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KEY_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra(KEY_URL);
        Log.d(TAG, "onCreate: " + url);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_chrome_custom_tab;
    }

    @Override
    protected void doBeforeLayout() {

    }
}
