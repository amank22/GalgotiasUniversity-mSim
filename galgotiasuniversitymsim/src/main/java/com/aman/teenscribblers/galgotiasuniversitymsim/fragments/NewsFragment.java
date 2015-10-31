package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.teenscribblers.galgotiasuniversitymsim.Adapter.NewsRecycleAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.Application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.Events.NewsEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.HelperClasses.RecyclerItemClickListener;
import com.aman.teenscribblers.galgotiasuniversitymsim.Jobs.NewsDBJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.NewsParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.NewsDetailActivity;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NEWSFRAGMENT";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private List<NewsParcel> parcel = null;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GUApp.getJobManager().addJobInBackground(new NewsDBJob());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_list_swipe, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.ts_blue, R.color.ts_green, R.color.ts_pink, R.color.ts_red);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setRefreshing(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                Snackbar.make(mSwipeRefreshLayout, "News will be deleted forever", Snackbar.LENGTH_LONG).setCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        Log.d(TAG, "OnDismissed:" + event);
                        if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {

                            Log.d(TAG, "OnDismissed-ITEMID:" + String.valueOf(mAdapter.getItemId(viewHolder.getAdapterPosition())));
                            boolean result = DbSimHelper.getInstance().deleteNews(String.valueOf(mAdapter.getItemId(viewHolder.getAdapterPosition())));
                            if (result)
                                GUApp.getJobManager().addJobInBackground(new NewsDBJob());
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                }).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();


            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //Adding onclick listener
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent newsIntent = new Intent(getActivity(), NewsDetailActivity.class);
                        newsIntent.putExtra("newsContent", parcel.get(position));
                        getActivity().startActivity(newsIntent);
                    }
                })
        );

    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(NewsEvent event) {
        mSwipeRefreshLayout.setRefreshing(false);
        if (event.isError()) {
            //      AlertDialogManager.showAlertDialog(getActivity(), event.getResult() + "--" + lid);
            Snackbar.make(mSwipeRefreshLayout, event.getResult(), Snackbar.LENGTH_LONG).show();
        } else {
            //   AlertDialogManager.showAlertDialog(getActivity(), event.getResult());
            parcel = event.getParcel();
            uselist();

        }
    }

    private void uselist() {
        if (getActivity() != null) {
            mAdapter = new NewsRecycleAdapter(parcel, getActivity());
            mRecyclerView.setAdapter(mAdapter);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        Snackbar.make(mSwipeRefreshLayout, parcel.get(i).getNote(), Snackbar.LENGTH_LONG).show();
////        AlertDialogManager.showAlertDialog(getActivity(), parcel.get(i).getNote(), "News Section", parcel.get(i).getImage_url());
//    }

    @Override
    public void onRefresh() {
        GUApp.getJobManager().addJobInBackground(new NewsDBJob());
    }
}
