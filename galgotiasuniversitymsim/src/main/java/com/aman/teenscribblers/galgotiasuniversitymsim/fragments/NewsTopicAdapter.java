package com.aman.teenscribblers.galgotiasuniversitymsim.fragments;

import android.graphics.Color;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.fragments.NewsTopicListFragment.OnListFragmentInteractionListener;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsListParcel;

import java.util.List;

public class NewsTopicAdapter extends RecyclerView.Adapter<NewsTopicAdapter.ViewHolder> {

    private final List<NewsListParcel.NewsTopics> mTopics;
    private final OnListFragmentInteractionListener mListener;
    private final ArrayMap<String, Boolean> selectedMap;

    public NewsTopicAdapter(List<NewsListParcel.NewsTopics> topics, OnListFragmentInteractionListener listener) {
        mTopics = topics;
        mListener = listener;
        selectedMap = new ArrayMap<>(topics.size());
        for (NewsListParcel.NewsTopics topic : topics) {
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mTopics.get(position);
        holder.mContentView.setText(holder.mItem.getName());
        if (selectedMap.get(holder.mItem.getName())) {
            holder.mContentView.setTextColor(Color.BLUE);
        } else {
            holder.mContentView.setTextColor(Color.BLACK);
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
        public NewsListParcel.NewsTopics mItem;

        public ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
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
            selectedMap.put(mItem.getName(), !selectedMap.get(mItem.getName()));
            notifyItemChanged(getAdapterPosition());
        }
    }
}
