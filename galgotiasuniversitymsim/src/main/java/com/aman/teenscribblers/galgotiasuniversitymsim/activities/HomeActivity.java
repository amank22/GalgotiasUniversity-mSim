package com.aman.teenscribblers.galgotiasuniversitymsim.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.Rebound;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.ContentFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.FragmentPersonalInfo;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsFragment;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.TimeTableContent;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.CircleTransform;
import com.squareup.picasso.Picasso;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ContentFragment.ColorChanger, TimeTableContent.ColorChangerTimeTable, TimeTableContent.FragmentOpenTimeTable,
        ContentFragment.FragmentOpenAtt {


    Fragment afrag = null, tfrag = null;
    ImageView image;
    private Integer colorFrom = R.color.ts_red;

    private static final String TAG = "HomeActivity";

    private CoordinatorLayout frame;
    private DrawerLayout drawer;
    private final FragmentManager fragmentManager = getFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        frame = (CoordinatorLayout) findViewById(R.id.mainbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            frame.setClipToOutline(true);
        }
        frame.setClipToPadding(true);
        frame.setDrawingCacheEnabled(true);
        frame.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                frame.setScaleX(1 - slideOffset / 10);
                frame.setScaleY(1 - slideOffset / 10);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigationView.addHeaderView(header);
        navigationView.setNavigationItemSelectedListener(this);
        fragmentManager.beginTransaction()
                .replace(R.id.container, new FragmentPersonalInfo())
                .commit();
        String mUsername = PrefUtils.getFromPrefs(HomeActivity.this,
                PrefUtils.PREFS_LOGIN_USERNAME_KEY, "Username");
        image = (ImageView) header.findViewById(R.id.imageView_nav);
        Picasso picasso = Picasso.with(this);
        picasso.setIndicatorsEnabled(false);
        picasso.load(AppConstants.StudentImagebase + mUsername + ".jpg")
                .noPlaceholder()
                .resize(500, 500)
                .centerInside()
                .transform(new CircleTransform())
                .priority(Picasso.Priority.HIGH)
                .into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rebound.AddRebound(image);
            }
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_home;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_personal) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new FragmentPersonalInfo())
                    .commit();
        } else if (id == R.id.nav_att) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, ContentFragment.newInstance())
                    .commit();
        } else if (id == R.id.nav_tt) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, TimeTableContent.newInstance())
                    .commit();
        } else if (id == R.id.nav_news) {
            fragmentManager.beginTransaction()
                    .replace(R.id.container, NewsFragment.newInstance())
                    .commit();
        }  else if (id == R.id.nav_send) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kapoor.aman22@gmail.com", "teenscribblers@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Report of Bugs,Improvements");
            i.putExtra(Intent.EXTRA_TEXT, "I want to say that ");
            try {
                startActivity(Intent.createChooser(i, "Contact Us"));
            } catch (android.content.ActivityNotFoundException ex) {
                Snackbar.make(frame, "There is no Email Client Installed", Snackbar.LENGTH_LONG).show();
            }

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void changecolor(int color) {
        startcolorchange(color);
    }

    @Override
    public void changecolortt(int color) {
        startcolorchange(color);
    }

    private void startcolorchange(int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.start();
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                toolbar.setBackgroundColor((Integer) animator.getAnimatedValue());
            }

        });
        colorFrom = colorTo;
    }

    @Override
    public void attopened(Fragment frag, String tag) {
        afrag = frag;
        if (tag.equals("Subject+Wise+Attendance")) {
            tag = "Subject+Attendance";
        }
//        tophead.setText(tag.replace("+", "\n"));
    }

    @Override
    public void ttopened(Fragment frag, String tag) {
        tfrag = frag;
//        tophead.setText(tag.replace("+", "\n"));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (afrag != null && afrag.isVisible()) {
            getFragmentManager().beginTransaction().replace(R.id.container, ContentFragment.newInstance()).commit();
            startcolorchange(ContextCompat.getColor(HomeActivity.this, R.color.ts_red));
        } else if (tfrag != null && tfrag.isVisible()) {
            getFragmentManager().beginTransaction().replace(R.id.container, TimeTableContent.newInstance()).commit();
            startcolorchange(ContextCompat.getColor(HomeActivity.this, R.color.ts_red));
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(R.layout.activity_about_me);
            builder.show();
            return true;
        } else if (id == R.id.action_logout) {
            PrefUtils.deleteuser(HomeActivity.this);
            GUApp app = GUApp.getInstance();
            app.clearApplicationData();
            Intent i = new Intent(HomeActivity.this, StudentLogin.class);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
