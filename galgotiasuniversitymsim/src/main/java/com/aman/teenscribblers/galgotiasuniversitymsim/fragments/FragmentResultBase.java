package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.ResultAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.AttendanceErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LocalErrorEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.ResultSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.ResultJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.ResultLocalJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.ResultParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.StudentLogin;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static android.R.id.list;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentResultBase extends BaseFragment {

    private static final String TAG = "Result Fragment Base";
    private ViewPager mViewPager;
    private ProgressBar pb;
    private View rootview;
    private TabLayout tabLayout;

    private List<String> semesters;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_results, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rootview = view;
        pb = (ProgressBar) view.findViewById(R.id.progressBar_results_tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.results_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        semesters = DbSimHelper.getInstance().getSemestersForResult();
        if (semesters == null || semesters.isEmpty()) {
            GUApp.getJobManager().addJobInBackground(new ResultJob());
        } else {
            setPagerAdapter();
        }

    }

    void setPagerAdapter() {
        pb.setVisibility(View.GONE);
        SemesterPagerAdapter semesterPagerAdapter = new SemesterPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(semesterPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(final InfoEvent event) {
        if (!event.getLocal()) {
            handleNetworkEvent(event);
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(SessionExpiredEvent event) {
        CaptchaDialogFragment captchaDialogFragment = new CaptchaDialogFragment();
        captchaDialogFragment.show(getActivity().getSupportFragmentManager(), "captchaFrag");
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(LoginEvent event) {
        if (event.isError()) {
            PrefUtils.deleteuser(getActivity());
            GUApp app = GUApp.getInstance();
            app.clearApplicationData();
            Intent i = new Intent(getActivity(), StudentLogin.class);
            startActivity(i);
            getActivity().finish();
        } else {
            GUApp.getJobManager().addJobInBackground(new ResultJob());
        }
    }


    private void handleNetworkEvent(InfoEvent event) {
        if (event.getError()) {
            pb.setVisibility(View.GONE);
            Snackbar.make(rootview, event.getData(), Snackbar.LENGTH_INDEFINITE).show();
        } else {
            semesters = DbSimHelper.getInstance().getSemestersForResult();
            setPagerAdapter();
        }
    }


    public class SemesterPagerAdapter extends FragmentStatePagerAdapter {

        public SemesterPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new FragmentResult();
            Bundle args = new Bundle();
            args.putString(FragmentResult.ARG_SEMESTER, getPageTitle(i).toString());
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return semesters != null ? semesters.size() : 0;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return semesters.get(position);
        }
    }
}