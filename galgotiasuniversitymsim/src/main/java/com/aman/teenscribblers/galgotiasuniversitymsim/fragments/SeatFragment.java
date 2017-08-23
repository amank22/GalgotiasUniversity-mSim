package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeatFragment extends Fragment {
    WebView wv;



    public SeatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_seat, container, false);
        wv=(WebView)rootView.findViewById(R.id.webview);
        wv.loadUrl("http://182.71.87.38/ISIM/Student/frmStdSeatingPlan");
        return rootView;
    }

}
