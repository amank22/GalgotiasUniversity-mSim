package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.ListAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by aman on 26-11-2014.
 */
public class TimeTableContent extends BaseFragment implements AdapterView.OnItemClickListener {
    Fragment frag;
    private FragmentOpenTimeTable flisten;

    /**
     * @return a new instance of {@link AttendanceContentFragment}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static TimeTableContent newInstance() {
        return new TimeTableContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_category_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String[] tt = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
        ListView list = (ListView) view.findViewById(R.id.listView_choice);
        ListAdapter adapter;
        adapter = new ListAdapter(getActivity(), tt);
        list.setAdapter(adapter);
        list.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                gotimetable("Monday", getcolor(R.color.ts_yellow));
                break;
            case 1:
                gotimetable("Tuesday", getcolor(R.color.ts_blue));
                break;
            case 2:
                gotimetable("Wednesday", getcolor(R.color.ts_pink));
                break;
            case 3:
                gotimetable("Thursday", getcolor(R.color.ts_purple));
                break;
            case 4:
                gotimetable("Friday", getcolor(R.color.ts_green));
                break;
        }
    }

    private int getcolor(int c) {
        return getResources().getColor(c);
    }

    protected void gotimetable(String day, int color) {
        frag = FragmentTimeTable.newInstance(day);
        getFragmentManager().beginTransaction().replace(R.id.frame_options_chooser, frag, "timetable")
                .commit();
        flisten.ttopened(frag, day);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            flisten = (FragmentOpenTimeTable) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement attendance Fragment backstack Listener");
        }
    }

    public interface FragmentOpenTimeTable {
        void ttopened(Fragment frag, String tag);
    }

}
