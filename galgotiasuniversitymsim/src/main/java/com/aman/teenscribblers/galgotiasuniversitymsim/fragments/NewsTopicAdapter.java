package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsTopicListFragment.OnListFragmentInteractionListener;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.GlideApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsTopicListParcel;

import java.util.List;

public class NewsTopicAdapter extends RecyclerView.Adapter<NewsTopicAdapter.ViewHolder> {

    private final List<NewsTopicListParcel.NewsTopics> mTopics;
    private final OnListFragmentInteractionListener mListener;
    private final ArrayMap<String, Boolean> selectedMap;
    private final Context mContext;

    public NewsTopicAdapter(Context context, List<NewsTopicListParcel.NewsTopics> topics, OnListFragmentInteractionListener listener) {
        mTopics = topics;
        mContext = context;
        mListener = listener;
        selectedMap = new ArrayMap<>(topics.size());
        for (NewsTopicListParcel.NewsTopics topic : topics) {
            selectedMap.put(topic.getName(), topic.isFollows());
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newstopic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Boolean) {
                handleCheckedState(holder);
            }
        } else {
            super.onBindViewHolder(holder, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mTopics.get(position);
        holder.mContentView.setText(holder.mItem.getName());
        GlideApp.with(mContext)
                .asBitmap()
                .load(holder.mItem.getProfilePic())
                .into(holder.mImage);
//        Picasso.with(mContext).load(holder.mItem.getProfilePic()).noFade().into(holder.mImage);
        handleCheckedState(holder);
    }

    private void handleCheckedState(ViewHolder holder) {
        if (selectedMap.get(holder.mItem.getName())) {
            holder.itemView.setScaleY(0.95f);
            holder.itemView.setScaleX(0.95f);
            holder.mCheckMark.setScaleY(1f);
            holder.mCheckMark.setScaleX(1f);
        } else {
            holder.itemView.setScaleY(1f);
            holder.itemView.setScaleX(1f);
            holder.mCheckMark.setScaleY(0f);
            holder.mCheckMark.setScaleX(0f);
        }
    }

    @Override
    public int getItemCount() {
        return mTopics == null ? 0 : mTopics.size();
    }

    public ArrayMap<String, Boolean> getSelectedMap() {
        return selectedMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mContentView;
        public final ImageView mImage;
        public final ImageView mCheckMark;
        public final View mOverlay;
        public NewsTopicListParcel.NewsTopics mItem;

        public ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImage = (ImageView) view.findViewById(R.id.news_topic_image);
            mCheckMark = (ImageView) view.findViewById(R.id.news_topic_check_mark);
            mOverlay = view.findViewById(R.id.news_topic_overlay);
            view.setOnClickListener(this);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View view) {
            if (null != mListener) {
                // Notify the active callbacks interface (the activity, if the
                // fragment is attached to one) that an item has been selected.
                mListener.onListFragmentInteraction(mItem);
            }

            final boolean nextState = !selectedMap.get(mItem.getName());
            selectedMap.put(mItem.getName(), nextState);
            ViewPropertyAnimator t;
            if (nextState) {
                t = view.animate().scaleX(0.9f).scaleY(0.9f).setInterpolator(new AnticipateInterpolator());
                mCheckMark.animate().scaleX(1f).scaleY(1f).setInterpolator(new AnticipateOvershootInterpolator());
            } else {
                t = view.animate().scaleX(1f).scaleY(1f).setInterpolator(new FastOutSlowInInterpolator());
                mCheckMark.animate().scaleX(0f).scaleY(0f).setInterpolator(new AnticipateInterpolator());
            }
            t.setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    notifyItemChanged(getAdapterPosition(), nextState);
                }
            });
        }
    }
}
