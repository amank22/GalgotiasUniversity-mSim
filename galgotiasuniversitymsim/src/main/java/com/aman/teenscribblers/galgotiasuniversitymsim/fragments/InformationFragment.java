package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.ZoomOutPageTransformer;

/**
 * Created by amankapoor on 12/07/17.
 */

public class InformationFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private static final int NUM_PAGES = 2;

    private LinearLayout pagerIndicator;
    private ImageView[] dots;
    int prevPositionOfDot = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_information, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_info);
        pagerIndicator = (LinearLayout) view.findViewById(R.id.viewPagerCountDots);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        final InformationAdapter mPagerAdapter = new InformationAdapter(getChildFragmentManager());
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setAdapter(mPagerAdapter);
        setUiPageViewController();
    }


    private void setUiPageViewController() {

        dots = new ImageView[NUM_PAGES];

        for (int i = 0; i < NUM_PAGES; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pagerIndicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selecteditem_dot));
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        dots[prevPositionOfDot].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonselecteditem_dot));
        prevPositionOfDot = position;
        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * A simple pager adapter that represents 2 info objects, in
     * sequence.
     */
    private class InformationAdapter extends FragmentStatePagerAdapter {
        InformationAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FragmentPersonalInfo();
                case 1:
                    return new FragmentOfficialInfo();
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
