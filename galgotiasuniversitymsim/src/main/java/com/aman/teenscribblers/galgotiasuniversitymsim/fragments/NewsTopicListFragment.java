package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.application.GUApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.events.NewsTopicEvent;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.IonMethods;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.PrefUtils;
import com.aman.teenscribblers.galgotiasuniversitymsim.jobs.NewsTopicsJob;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsListParcel;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NewsTopicListFragment extends BaseFragment {

    private static final String TAG = NewsTopicListFragment.class.getCanonicalName();
    private static final String ARG_CLICK_X = "click_x";
    private static final String ARG_CLICK_Y = "click_y";
    private static final int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProgressBar loader;
    private NewsTopicAdapter mAdapter;
    private final ArrayMap<String, Boolean> selectedMap = new ArrayMap<>();
    private String admNo;
    private int X, Y;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewsTopicListFragment() {
    }

    public static NewsTopicListFragment newInstance(int x, int y) {
        NewsTopicListFragment fragment = new NewsTopicListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CLICK_X, x);
        args.putInt(ARG_CLICK_Y, y);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            X = getArguments().getInt(ARG_CLICK_X);
            Y = getArguments().getInt(ARG_CLICK_Y);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newstopic_list, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startRevealAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    doOnViewCreated(view);
                }
            });
        } else {
            doOnViewCreated(view);
        }
    }

    private void doOnViewCreated(View view) {
        FloatingActionButton buttontopicsubmit = (FloatingActionButton) view.findViewById(R.id.button_topic_submit);

        // Set the adapter
        Context context = view.getContext();
        recyclerView = (RecyclerView) view.findViewById(R.id.news_topic_list);
        loader = view.findViewById(R.id.progressBar_news_topics);
        recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        admNo = PrefUtils.getFromPrefs(getContext(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, "").trim();
        GUApp.getJobManager().addJobInBackground(new NewsTopicsJob(admNo));
        buttontopicsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                extractTopicsAndCallServer(view, admNo);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startRevealAnimation(Animator.AnimatorListener listener) {

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(X, Y);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(getView(), X, Y, 0, finalRadius);

        // make the view visible and start the animation
        anim.addListener(listener);
        startColorAnimation(getView(), ContextCompat.getColor(getContext(), R.color.colorPrimary), Color.WHITE, 700);
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void endRevealAnimation(Animator.AnimatorListener listener) {

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(X, Y);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(getView(), X, Y, finalRadius, 0);

        // make the view visible and start the animation
        anim.addListener(listener);
        startColorAnimation(getView(), Color.WHITE, ContextCompat.getColor(getContext(), R.color.colorPrimary), 700);
        anim.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    static void startColorAnimation(final View view, final int startColor, final int endColor, int duration) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(startColor, endColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(duration);
        anim.start();
    }

    private void extractTopicsAndCallServer(final View view, final String admNo) {
        if (mAdapter == null) {
            return;
        }
        ArrayMap<String, Boolean> modifiedMap = mAdapter.getSelectedMap();
        final List<String> follow = new ArrayList<>();
        final List<String> unfollow = new ArrayList<>();
        for (Map.Entry<String, Boolean> modifiedEntry : modifiedMap.entrySet()) {
            String key = modifiedEntry.getKey();
            Boolean value = modifiedEntry.getValue();
            if (selectedMap.get(key) != value) {
                if (value) {
                    follow.add(key);
                } else {
                    unfollow.add(key);
                }
            }
        }
        if (follow.isEmpty() && unfollow.isEmpty()) {
            // TODO: 08/08/17 Close fragment or do something properly
            Toast.makeText(getContext(), "Choose some topics to follow or unfollow", Toast.LENGTH_LONG).show();
        } else {
            AddTopicOnServer(view, admNo, follow, unfollow);
        }
    }

    private void AddTopicOnServer(final View view, final String admNo, final List<String> follow, final List<String> unfollow) {
        IonMethods.followTopics(admNo, FirebaseInstanceId.getInstance().getToken(), follow, unfollow, new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if (e != null) {
                    Snackbar.make(view, "Could not follow or unfollow topics", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View viewLocal) {
                            extractTopicsAndCallServer(view, admNo);
                        }
                    }).show();
                    return;
                }
                if (!result.getResult().get("error").getAsBoolean()) {
                    followAndUnfollowTopicsOnFCM(unfollow, follow);
                } else {
                    Snackbar.make(view, "Could not follow or unfollow topics", Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                        @Override
                        public void onClick(View viewLocal) {
                            extractTopicsAndCallServer(view, admNo);
                        }
                    }).show();
                }
            }
        });
    }

    private void followAndUnfollowTopicsOnFCM(List<String> unfollow, List<String> follow) {
        for (String s : unfollow) {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(s);
        }
        for (String s : follow) {
            FirebaseMessaging.getInstance().subscribeToTopic(s);
        }
        getActivity().onBackPressed();
    }


    @SuppressWarnings("UnusedDeclaration")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onEventMainThread(NewsTopicEvent event) {
        if (event.getTopics() != null) {
            mAdapter = new NewsTopicAdapter(getActivity(), event.getTopics(), mListener);
            recyclerView.setAdapter(mAdapter);
            loader.setVisibility(View.GONE);
            for (NewsListParcel.NewsTopics topic : event.getTopics()) {
                selectedMap.put(topic.getName(), topic.isFollows());
            }
        } else if (event.getError().contains("User not Valid")) {
            updateGcmOnServer();
        } else {
            loader.setVisibility(View.GONE);
            Toast.makeText(getActivity(), event.getError(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateGcmOnServer() {
        ContentValues serverCv = new ContentValues();
        serverCv.put("adm_no", admNo);
        serverCv.put("gcm_id", FirebaseInstanceId.getInstance().getToken());
        loader.setVisibility(View.VISIBLE);
        IonMethods.postProfiletoServer(serverCv, new FutureCallback<Response<JsonObject>>() {
            @Override
            public void onCompleted(Exception e, Response<JsonObject> result) {
                if (e != null) {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Could not get the topics", Toast.LENGTH_LONG).show();
                } else if (!result.getResult().get("error").getAsBoolean()) {
                    GUApp.getJobManager().addJobInBackground(new NewsTopicsJob(admNo));
                } else {
                    loader.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Could not get the topics", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getView() != null)
                getView().animate().alpha(0).start();
            endRevealAnimation(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getActivity().getSupportFragmentManager().beginTransaction().remove(NewsTopicListFragment.this).commitNowAllowingStateLoss();
                        }
                    }, 200);
                }
            });
        } else {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(NewsListParcel.NewsTopics item);
    }
}
