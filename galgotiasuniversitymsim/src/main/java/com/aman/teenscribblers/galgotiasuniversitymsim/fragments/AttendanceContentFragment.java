package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.CategoryAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;
import com.borax12.materialdaterangepicker.date.DatePickerDialog;

import java.util.Calendar;

/**
 * Created by aman on 16-11-2014.
 */
public class AttendanceContentFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, CategoryAdapter.OnItemClickListener {
    Fragment frag;
    String type;
    private FragmentOpenAtt flisten;

    private static String[] itemTexts;
    private final static int[] itemIcons = new int[]{R.drawable.ic_today, R.drawable.ic_subjects};

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
        itemTexts = getResources().getStringArray(R.array.attendance_content);
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_category_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.listView_choice);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 1);
        list.addItemDecoration(itemDecoration);
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        CategoryAdapter adapter = new CategoryAdapter(itemTexts, itemIcons);
        adapter.addItemClickListener(this);
        list.setAdapter(adapter);
    }

    protected void replaceToAttendance(String fromDate, String toDate) {
        frag = NewAttendanceFragment.newInstance(type, fromDate, toDate);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, frag, "attendance")
                .commit();
        flisten.attopened(frag, type);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {

        String fromDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        String toDate = dayOfMonthEnd + "/" + (monthOfYearEnd + 1) + "/" + yearEnd;
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

    @Override
    public void onItemClick(int position, String text) {
        switch (position) {
            case 1:
                type = AppConstants.ATT_SUBJECT;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        AttendanceContentFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.setAccentColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                dpd.setMaxDate(now);
                dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
                break;
            case 0:
                type = AppConstants.ATT_TODAY;
                replaceToAttendance(null, null);
                break;
        }
    }

    public interface FragmentOpenAtt {
        void attopened(Fragment frag, String tag);
    }

}
