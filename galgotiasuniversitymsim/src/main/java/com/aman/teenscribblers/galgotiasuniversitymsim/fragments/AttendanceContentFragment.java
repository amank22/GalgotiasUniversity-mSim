package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.ListAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by aman on 16-11-2014.
 */
public class AttendanceContentFragment extends BaseFragment implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener {
    Fragment frag;
    String type;
    private ListView list;
    private FragmentOpenAtt flisten;

    /**
     * @return a new instance of {@link AttendanceContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static AttendanceContentFragment newInstance() {
        return new AttendanceContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_category_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String[] att = {"Todays Attendance" ,"Subjects Attendance", "Date-Wise Attendance"};
        String[] att = {"Subjects Attendance", "Today Attendance"};
        list = (ListView) view.findViewById(R.id.listView_choice);
        ListAdapter adapter;
        adapter = new ListAdapter(getActivity(), att);
        setadapter(adapter);
        list.setOnItemClickListener(this);
    }

    private void setadapter(ListAdapter adapter) {
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        switch (i) {
            case 0:
                type = AppConstants.ATT_SUBJECT;
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case 1:
                type = AppConstants.ATT_TODAY;
                replaceToAttendance(null, null);
                break;
        }
    }

    protected void replaceToAttendance(String fromDate, String toDate) {
        frag = NewAttendanceFragment.newInstance(type, fromDate, toDate);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, "attendance")
                .commit();
        flisten.attopened(frag, type);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String fromDate = dayOfMonth + "/" + monthOfYear + 1 + "/" + year;
        String toDate = dayOfMonthEnd + "/" + monthOfYearEnd + 1 + "/" + yearEnd;
        replaceToAttendance(fromDate, toDate);
    }


    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            flisten = (FragmentOpenAtt) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimeTable Fragment backstack Listener");
        }
    }

    public interface FragmentOpenAtt {
        void attopened(Fragment frag, String tag);
    }

}
