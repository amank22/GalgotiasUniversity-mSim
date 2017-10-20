package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.analytics.Analytics;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.AboutDeveloperFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.AttendanceContentFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.FragmentResultBase;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.InformationFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsTopicListFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.TimeTableContent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimeTableContent.FragmentOpenTimeTable,
        AttendanceContentFragment.FragmentOpenAtt {


    private static final String TAG = "HomeActivity";
    private final FragmentManager fragmentManager = getSupportFragmentManager();
    public ImageView image;
    Fragment afrag = null, tfrag = null;
    private FrameLayout frame;
    private DrawerLayout drawer;

    public ImageView getImage() {
        return image;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frame = (FrameLayout) findViewById(R.id.container);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            assert frame != null;
            frame.setClipToOutline(true);
        }
        frame.setClipToPadding(true);
        frame.setDrawingCacheEnabled(true);
        frame.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setToggleToDrawer(null);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, navigationView, false);
        navigationView.addHeaderView(header);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager.beginTransaction().replace(R.id.container, new InformationFragment()).commit();
        image = (ImageView) header.findViewById(R.id.imageView_nav);
        String gender = PrefUtils.getFromPrefs(this, PrefUtils.PREFS_USER_GENDER_KEY, "Male");
        if (gender.contains("Female")) {
            image.setImageResource(R.drawable.ic_avatar_girl);
        }
    }

    public void setToggleToDrawer(@Nullable Toolbar toolbar) {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
//                frame.setTranslationX(slideOffset * 50);
                frame.setScaleX(1 - slideOffset / 20);
                frame.setScaleY(1 - slideOffset / 20);
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void doBeforeLayout() {

    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        drawer.closeDrawer(GravityCompat.START);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_personal) {
            Analytics.selectContent(this, mFirebaseAnalytics, "Info", "Information Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new InformationFragment())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_att) {
            Analytics.selectContent(this, mFirebaseAnalytics, "Att", "Attendance Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AttendanceContentFragment.newInstance())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_tt) {
            Analytics.selectContent(this, mFirebaseAnalytics, "tt", "TimeTable Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, TimeTableContent.newInstance())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_results) {
            Analytics.selectContent(this, mFirebaseAnalytics, "Res", "Result Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new FragmentResultBase())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_news) {
            Analytics.selectContent(this, mFirebaseAnalytics, "News", "News Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, NewsFragment.newInstance())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_send) {
            Analytics.selectContent(this, mFirebaseAnalytics, "Msg", "Feedback Tab", "Navigation");
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kapoor.aman22@gmail.com", "anmol.agarwal2004@yahoo.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                startActivity(Intent.createChooser(i, "Mail Us"));
            } catch (android.content.ActivityNotFoundException ex) {
                Snackbar.make(frame, "There is no Email Client Installed", Snackbar.LENGTH_LONG).show();
            }
        } else if (id == R.id.nav_about_us) {
            Analytics.selectContent(this, mFirebaseAnalytics, "about", "About us Tab", "Navigation");
            fragmentManager.beginTransaction()
                    .replace(R.id.container, AboutDeveloperFragment.newInstance())
                    .commitAllowingStateLoss();
        } else if (id == R.id.nav_logout) {
            Analytics.selectContent(this, mFirebaseAnalytics, "lgo", "Logout Tab", "Navigation");
            GUApp.logoutUser(HomeActivity.this);
        }
        return true;
    }

    @Override
    public void attopened(Fragment frag, String tag) {
        Analytics.selectContent(this, mFirebaseAnalytics, "att", tag + " att option", "Attendance");
        afrag = frag;
    }

    @Override
    public void onTimeTableOpened(Fragment frag, String tag) {
        Analytics.selectContent(this, mFirebaseAnalytics, "tt", tag + " tt option", "Timetable");
        tfrag = frag;
    }

    @Override
    public void onBackPressed() {
        NewsTopicListFragment followTopics = (NewsTopicListFragment) getSupportFragmentManager().findFragmentByTag(NewsFragment.FOLLOW_TOPIC_TAG);
        if (followTopics != null) {
            followTopics.onBackPressed();
        } else if (afrag != null && afrag.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, AttendanceContentFragment.newInstance()).commit();
        } else if (tfrag != null && tfrag.isVisible()) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, TimeTableContent.newInstance()).commit();
        } else if (!drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.openDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
