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
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by aman on 16-11-2014.
 */
public class ContentFragment extends Fragment implements AdapterView.OnItemClickListener {
    private static final String KEY_TITLE = "title";
    private ListView list;
    String type, typevalue;

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
        String[] att = {"Todays Attendance", "Semester Attendance", "Subjects Attendance", "Monthly Attendance"};
        list = (ListView) view.findViewById(R.id.listView_choice);
        ListAdapter adapter;
//            ftype = args.getCharSequence(KEY_TITLE);
        adapter = new ListAdapter(getActivity(), att);
        setadapter(adapter);
        list.setOnItemClickListener(this);

    }

    private void setadapter(ListAdapter adapter) {
        list.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
//                if (ftype.equals("Attendance")) {
                type = "ctl00$ctl00$MCPH1$SCPH$btntodayAtt";
                typevalue = "Today+Attendance";
                gooon(type, typevalue, getcolor(R.color.ts_yellow));
//                }
                break;
            case 1:
//                if (ftype.equals("Attendance")) {
                type = "ctl00$ctl00$MCPH1$SCPH$btnSemAtt";
                typevalue = "Semester+Attendance";
                gooon(type, typevalue, getcolor(R.color.ts_blue));
//                }
                break;
            case 2:
                type = "ctl00$ctl00$MCPH1$SCPH$btnSubjectWiseAtt";
                typevalue = "Subject+Wise+Attendance";
                gooon(type, typevalue, getcolor(R.color.ts_pink));
                break;
            case 3:
                type = "ctl00$ctl00$MCPH1$SCPH$btnMonthlyAtt";
                typevalue = "Monthly+Attendance";
                gooon(type, typevalue, getcolor(R.color.ts_purple));
                break;
        }
    }

    Fragment frag;

    private int getcolor(int c) {
        return getResources().getColor(c);
    }

    protected void gooon(String type, String typevalue, int color) {
        String[] content = {type, typevalue};
        frag = AttendanceFragment.newInstance(content);
        getFragmentManager().beginTransaction().replace(R.id.frame, frag, "attendance")
                .commit();
        changer.changecolor(color);
        flisten.attopened(frag, typevalue);
    }

    private FragmentOpenAtt flisten;

    public interface FragmentOpenAtt {
        public void attopened(Fragment frag, String tag);
    }

    private ColorChanger changer;

    public interface ColorChanger {
        public void changecolor(int color);
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

}
