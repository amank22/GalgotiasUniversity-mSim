package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
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
public class ContentFragment extends Fragment implements AdapterView.OnItemClickListener, DatePickerDialog.OnDateSetListener {
    Fragment frag;
    String type;
    private ListView list;
    private FragmentOpenAtt flisten;
    private ColorChanger changer;

    /**
     * @return a new instance of {@link ContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static ContentFragment newInstance() {
        return new ContentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.pager_item, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String[] att = {"Todays Attendance" ,"Subjects Attendance", "Date-Wise Attendance"};
        String[] att = {"Subjects Attendance", "Date-Wise Attendance"};
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
        dpd.show(getFragmentManager(), "Datepickerdialog");
        switch (i) {
//            case 0:
//                replaceToAttendance(AppConstants.ATT_TODAY, getcolor(R.color.ts_yellow));
//                break;
            case 0:
                type = AppConstants.ATT_SUBJECT;
                break;
            case 1:
                type = AppConstants.ATT_DATE;
                break;
        }
    }

    private int getcolor(int c) {
        return getResources().getColor(c);
    }

    protected void replaceToAttendance(String type, int color, String fromDate, String toDate) {
        frag = NewAttendanceFragment.newInstance(type, fromDate, toDate);
        getFragmentManager().beginTransaction().replace(R.id.frame, frag, "attendance")
                .commit();
        changer.changecolor(color);
        flisten.attopened(frag, type);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            changer = (ColorChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement ColorChanger Listener");
        }
        try {
            flisten = (FragmentOpenAtt) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement TimeTable Fragment backstack Listener");
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String fromDate = dayOfMonth + "/" + monthOfYear + "/" + year;
        String toDate = dayOfMonthEnd + "/" + monthOfYearEnd + "/" + yearEnd;
        int color = R.color.ts_blue;
        if (type.equals(AppConstants.ATT_DATE)) {
            color = R.color.ts_green;
        }
        replaceToAttendance(type, color, fromDate, toDate);
    }

    public interface FragmentOpenAtt {
        void attopened(Fragment frag, String tag);
    }


    public interface ColorChanger {
        void changecolor(int color);
    }

}
