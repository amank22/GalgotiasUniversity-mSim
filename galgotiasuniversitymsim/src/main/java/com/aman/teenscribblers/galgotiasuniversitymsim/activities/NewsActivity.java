package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.os.Bundle;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsFragment;

public class NewsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, NewsFragment.newInstance()).commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_news;
    }
}
