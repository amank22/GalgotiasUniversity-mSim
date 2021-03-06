package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.InfoEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.ResultJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 26-12-2014.
 */
public class FragmentResultBase extends BaseFragment implements ViewPager.OnPageChangeListener {

    private static final String TAG = "Result Fragment Base";
    private ViewPager mViewPager;
    private ProgressBar pb;
    private View rootview;
    private TabLayout tabLayout;

    private TextView textViewCGPA, textViewSGPA, textViewRank, textViewTotalStudents;
    private ImageView imageViewRank;

    private List<String> semesters;
    private SemesterPagerAdapter semesterPagerAdapter;

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
        textViewCGPA = (TextView) view.findViewById(R.id.textView_cgpa);
        textViewSGPA = (TextView) view.findViewById(R.id.textView_sgpa);
        textViewRank = (TextView) view.findViewById(R.id.textView_rank);
        textViewTotalStudents = (TextView) view.findViewById(R.id.textView_total_students);
        imageViewRank = (ImageView) view.findViewById(R.id.imageView_rank);
        semesters = DbSimHelper.getInstance().getSemestersForResult();
        if (semesters == null || semesters.isEmpty()) {
            String admNo= PrefUtils.getFromPrefs(getContext(),PrefUtils.PREFS_USER_ADMNO_KEY,"").trim();
            GUApp.getJobManager().addJobInBackground(new ResultJob(admNo));
        } else {
            setPagerAdapter();
        }

    }

    void setPagerAdapter() {
        pb.setVisibility(View.GONE);
        semesterPagerAdapter = new SemesterPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(semesterPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);
        updateHeader(0);
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
        if (!event.isError()) {
            String admNo= PrefUtils.getFromPrefs(getContext(),PrefUtils.PREFS_USER_ADMNO_KEY,"").trim();
            GUApp.getJobManager().addJobInBackground(new ResultJob(admNo));
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

    private void updateHeader(int position) {
        String sem = semesterPagerAdapter.getPageTitle(position).toString();
        String cgpa = PrefUtils.getFromPrefs(getContext(), (sem + "-" + "CGPA"), "-");
        String sgpa = PrefUtils.getFromPrefs(getContext(), (sem + "-" + "SGPA"), "-");
        String rank = PrefUtils.getFromPrefs(getContext(), (sem + "-" + "Rank"), "-");
        String totalStudents = PrefUtils.getFromPrefs(getContext(), (sem + "-" + "Total No. of Student"), "-");

        textViewCGPA.setText(cgpa);
        textViewSGPA.setText(sgpa);
        textViewRank.setText(rank);
        textViewTotalStudents.setText(totalStudents);
        if (Integer.parseInt(rank) == 1) {
            imageViewRank.setImageResource(R.drawable.ic_award);
        } else {
            imageViewRank.setImageResource(R.drawable.ic_cup);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (semesterPagerAdapter == null) {
            return;
        }
        updateHeader(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    private class SemesterPagerAdapter extends FragmentStatePagerAdapter {

        SemesterPagerAdapter(FragmentManager fm) {
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