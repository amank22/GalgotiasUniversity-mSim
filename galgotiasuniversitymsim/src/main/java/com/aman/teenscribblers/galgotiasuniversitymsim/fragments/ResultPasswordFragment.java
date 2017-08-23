package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultPasswordFragment extends Fragment {
    EditText e1;
    Button b1;
    String s1;



    public ResultPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_result_password, container, false);
        e1=(EditText)rootView.findViewById(R.id.resultpassword);

        b1=(Button)rootView.findViewById(R.id.verifybutton);

        b1.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                s1=e1.getText().toString();

                if (s1.contentEquals("fineanmol"))
                {
                    Toast.makeText(getActivity(), "Password Accepted", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,new FragmentResultBase()).commit();


                }
                else
                {
                    Toast.makeText(getActivity(), "Wrong Password", Toast.LENGTH_SHORT).show();
                }






            }
        });
        return rootView;


    }


}






