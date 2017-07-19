package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.CategoryAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.LoginEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.SessionExpiredEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableStartEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.TimeTableSuccessEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.TimeTableJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.transform.RecyclerViewMargin;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by aman on 26-11-2014.
 */
public class TimeTableContent extends BaseFragment implements CategoryAdapter.OnItemClickListener {
    Fragment frag;
    private FragmentOpenTimeTable flisten;
    private static String[] itemTexts;
    private static int[] itemIcons;
    private TextView loading;
    private RecyclerView recyclerView;


    /**
     * @return a new instance of {@link TimeTableContent}, adding the parameters into a bundle and
     * setting them as arguments.
     */
    public static TimeTableContent newInstance() {
        return new TimeTableContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DbSimHelper dbSimHelper = DbSimHelper.getInstance();
        List<String> listOfDays = dbSimHelper.getTimeTableDays();
        if (listOfDays.size() > 0) {
            fillTextAndIcons(listOfDays);
        } else {
            GUApp.getJobManager().addJobInBackground(new TimeTableJob());
        }
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_category_chooser, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loading = (TextView) view.findViewById(R.id.textView_att_loading);
        recyclerView = (RecyclerView) view.findViewById(R.id.listView_choice);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        RecyclerViewMargin itemDecoration = new RecyclerViewMargin(16, 2);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        setRecyclerViewAdapter();
    }

    private void setRecyclerViewAdapter() {
        CategoryAdapter adapter = new CategoryAdapter(itemTexts, itemIcons);
        adapter.addItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    protected void goToSpecificTimeTable(String day) {
        frag = FragmentTimeTable.newInstance(day);
        getFragmentManager().beginTransaction().replace(R.id.frame_options_chooser, frag, "timetable")
                .commit();
        flisten.onTimeTableOpened(frag, day);
    }

    private void fillTextAndIcons(List<String> listOfDays) {
        HashMap<String, Integer> allItems = new LinkedHashMap<>(7);
        allItems.put("Sunday", R.drawable.ic_sunday);
        allItems.put("Monday", R.drawable.ic_monday);
        allItems.put("Tuesday", R.drawable.ic_tuesday);
        allItems.put("Wednesday", R.drawable.ic_wednesday);
        allItems.put("Thursday", R.drawable.ic_thursday);
        allItems.put("Friday", R.drawable.ic_friday);
        allItems.put("Saturday", R.drawable.ic_saturday);
        itemTexts = new String[listOfDays.size()];
        itemIcons = new int[listOfDays.size()];
        for (int i = 0; i < listOfDays.size(); i++) {
            if (allItems.containsKey(listOfDays.get(i))) {
                itemTexts[i] = listOfDays.get(i);
                itemIcons[i] = allItems.get(listOfDays.get(i));
            }
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
            GUApp.getJobManager().addJobInBackground(new TimeTableJob());
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableStartEvent event) {
        loading.setVisibility(View.VISIBLE);
        loading.setText(event.getResponse());
    }

    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(TimeTableSuccessEvent event) {
        loading.setVisibility(View.GONE);
        DbSimHelper dbSimHelper = DbSimHelper.getInstance();
        List<String> listOfDays = dbSimHelper.getTimeTableDays();
        fillTextAndIcons(listOfDays);
        setRecyclerViewAdapter();

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
    public void onItemClick(int position, String text) {
        goToSpecificTimeTable(text);
    }

    public interface FragmentOpenTimeTable {
        void onTimeTableOpened(Fragment frag, String tag);
    }

}
