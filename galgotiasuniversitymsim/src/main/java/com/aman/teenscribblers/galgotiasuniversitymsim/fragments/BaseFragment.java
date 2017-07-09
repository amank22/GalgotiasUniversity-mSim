package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.HomeActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by aman on 19-02-2015.
 */
public class BaseFragment extends Fragment {

    protected Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            EventBus.getDefault().register(this);
        } catch (Exception ignored) {

        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).setToggleToDrawer(toolbar);
        }
    }

    @Override
    public void onDestroyView() {
        try {
            EventBus.getDefault().unregister(this);
        } catch (Throwable t) {
            //this may crash if registration did not go through. just be safe
            t.printStackTrace();
        }
        super.onDestroyView();
    }

}
