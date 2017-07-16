package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.CategoryAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;

/**
 * Created by aman on 26-11-2014.
 */
public class TimeTableContent extends BaseFragment implements CategoryAdapter.OnItemClickListener {
    Fragment frag;
    private FragmentOpenTimeTable flisten;
    private static String[] itemTexts;
    private final static int[] itemIcons = new int[]{R.drawable.ic_monday, R.drawable.ic_tuesday, R.drawable.ic_wednesday,
            R.drawable.ic_thursday, R.drawable.ic_friday, R.drawable.ic_saturday};


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
        itemTexts = getResources().getStringArray(R.array.timetable_content);
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_category_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView list = (RecyclerView) view.findViewById(R.id.listView_choice);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 2);
        list.addItemDecoration(itemDecoration);
        list.setLayoutManager(linearLayoutManager);
        list.setHasFixedSize(true);
        CategoryAdapter adapter = new CategoryAdapter(getActivity(), itemTexts, itemIcons);
        adapter.addItemClickListener(this);
        list.setAdapter(adapter);
    }

    protected void goToSpecificTimeTable(String day) {
        frag = FragmentTimeTable.newInstance(day);
        getFragmentManager().beginTransaction().replace(R.id.frame_options_chooser, frag, "timetable")
                .commit();
        flisten.onTimeTableOpened(frag, day);
    }

    @Override
    public void onAttach(Context activity) {
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

    @Override
    public void onItemClick(int position, String text, int color) {
        if(position!=5) {
            goToSpecificTimeTable(text);
        }else{

        }
    }

    public interface FragmentOpenTimeTable {
        void onTimeTableOpened(Fragment frag, String tag);
    }

}
