package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.NewsRecycleAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.analytics.Analytics;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.NewsEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.NewsDBJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;

import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NEWSFRAGMENT";
    public static final String FOLLOW_TOPIC_TAG = "followtopic";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton mFollowButton;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<NewsParcel> parcel = null;

    private ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
            Snackbar.make(mSwipeRefreshLayout, "News will be deleted forever", Snackbar.LENGTH_LONG).addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                @Override
                public void onDismissed(Snackbar transientBottomBar, int event) {
                    super.onDismissed(transientBottomBar, event);
                    Log.d(TAG, "OnDismissed:" + event);
                    if (event != Snackbar.Callback.DISMISS_EVENT_ACTION) {
                        Analytics.selectContent(getContext(), mFirebaseAnalytics, String.valueOf(mAdapter.getItemId(viewHolder.getAdapterPosition())), "onDismissed", "News");
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


    public NewsFragment() {
    }

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_list_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mFollowButton = view.findViewById(R.id.fab_follow_topics);
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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
        //Adding onclick listener
        // TODO: 04/07/17 Removed to make app run. Need to done. Important
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Intent newsIntent = new Intent(getActivity(), NewsDetailActivity.class);
//                        newsIntent.putExtra("newsContent", parcel.get(position));
//                        getActivity().startActivity(newsIntent);
//                    }
//                })
//        );
        if (parcel == null) {
            GUApp.getJobManager().addJobInBackground(new NewsDBJob());
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            uselist();
        }

        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFollowTopicsFragment(view);
            }
        });

    }

    private void openFollowTopicsFragment(View view) {
        int[] position = new int[2];
        view.getLocationInWindow(position);
        int cx = position[0] + view.getWidth() / 2;
        int cy = position[1] + view.getHeight() / 2;
        final NewsTopicListFragment fragment = NewsTopicListFragment.newInstance(cx, cy);
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(FOLLOW_TOPIC_TAG).add(R.id.container, fragment, FOLLOW_TOPIC_TAG).commit();
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

    @Override
    public void onRefresh() {
        GUApp.getJobManager().addJobInBackground(new NewsDBJob());
    }
}
