package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.ImageView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.activities.FullScreenImageActivity;
import com.aman.teenscribblers.galgotiasuniversitymsim.adapter.NewsRecycleAdapter;
import com.aman.teenscribblers.galgotiasuniversitymsim.analytics.Analytics;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.NewsEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.DbSimHelper;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.EndlessRecyclerViewScrollListener;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.NewsDBJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsListParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import java.util.Collections;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

public class NewsFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, NewsRecycleAdapter.NewsClickListener {

    private static final String TAG = "NEWSFRAGMENT";
    public static final String FOLLOW_TOPIC_TAG = "followtopic";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private NewsRecycleAdapter mAdapter;
    private FloatingActionButton mFollowButton;
    private LinearLayoutManager mLayoutManager;
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

        final EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                int lastId = mAdapter.getLastElementId();
                if (lastId == -1) {
                    return;
                }
                startNetworkCall();
            }

            @Override
            public void onScrollChange(RecyclerView view, int newState) {

            }
        };
        // Pagination
        mRecyclerView.addOnScrollListener(scrollListener);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (parcel == null) {
            GUApp.getJobManager().addJobInBackground(new NewsDBJob());
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        uselist();
        startNetworkCall();
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFollowTopicsFragment(view);
            }
        });

    }

    private void startNetworkCall() {
        mSwipeRefreshLayout.setRefreshing(true);
        final String admNo = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "").trim();
        String fcmId = FirebaseInstanceId.getInstance().getToken();
        IonMethods.getNewsLists(admNo, fcmId, mAdapter.getItemCount(), new FutureCallback<Response<NewsListParcel>>() {
            @Override
            public void onCompleted(Exception e, Response<NewsListParcel> result) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (e != null) {
                    Snackbar.make(mSwipeRefreshLayout, getString(R.string.error_news), Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onCompleted: " + e.getMessage());
                    return;
                }
                NewsListParcel listParcel = result.getResult();
                if (listParcel.getStatus() == 400 && listParcel.getResult().equals("User not Valid")) {
                    updateGcmOnServer(admNo);
                    return;
                }
                if (listParcel.isError()) {
                    Snackbar.make(mSwipeRefreshLayout, getString(R.string.error_news), Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onCompleted: " + listParcel);
                    return;
                }
                List<NewsParcel> newsList = listParcel.getNews();
                if (newsList == null || newsList.isEmpty()) {
                    return;
                }
                try {
                    List<NewsParcel> insertedElementsList = DbSimHelper.getInstance().addnewnews(newsList);
//                    Collections.reverse(insertedElementsList);
                    mAdapter.insertElements(insertedElementsList);
                } catch (Exception e1) {
                    Log.d(TAG, "onCompleted: " + e1.getMessage());
                }

            }
        });
    }

    private void updateGcmOnServer(final String admNo) {
        ContentValues serverCv = new ContentValues();
        serverCv.put("adm_no", admNo);
        serverCv.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
        IonMethods.postProfiletoServer(serverCv, new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if (e != null) {
                    Log.d(TAG, "onCompleted: " + e.getMessage());
                } else if (!result.getResult().get("error").getAsBoolean()) {
                    startNetworkCall();
                } else {
//                    loader.setVisibility(View.GONE);
                    Log.d(TAG, "onCompleted: Failed to update fcm");
                }
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
            Snackbar.make(mSwipeRefreshLayout, event.getResult(), Snackbar.LENGTH_LONG).show();
        } else {
            parcel = event.getParcel();
            uselist();

        }
    }

    private void uselist() {
        if (getActivity() != null) {
            mAdapter = new NewsRecycleAdapter(parcel, getActivity(), this);
            mRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void onRefresh() {
        DbSimHelper.getInstance().deleteAllNews();
        mAdapter.removeAllElements();
        startNetworkCall();
    }

    @Override
    public void onImageClick(ImageView imageView, NewsParcel parcel) {
        startPhotoActivity(imageView, parcel);
    }

    private void startPhotoActivity(ImageView imageView, NewsParcel parcel) {

        if (getActivity() == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), FullScreenImageActivity.class);
        int location[] = new int[2];

        imageView.getLocationOnScreen(location);
        intent.putExtra("left", location[0]);
        intent.putExtra("top", location[1]);
        intent.putExtra("height", imageView.getHeight());
        intent.putExtra("width", imageView.getWidth());
        intent.putExtra("url", parcel.getImage_url());

        startActivity(intent);
        getActivity().overridePendingTransition(0, 0);

    }

    @Override
    public void onMailClick(View view, NewsParcel parcel) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", parcel.authorEmail, null));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{parcel.authorEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Mail from mSim News");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Regarding news:\n" + parcel.getNote() + "\n");
        startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }
}
