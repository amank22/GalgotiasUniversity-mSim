package com.aman.teenscribblers.galgotiasuniversitymsim.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.NewsParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Aman on 24-10-2015 in Galgotias University(mSim).
 */
public class NewsRecycleAdapter extends RecyclerView.Adapter<NewsRecycleAdapter.ViewHolder> {

    private static final String TAG = "NEWSADAPTER";
    private List<NewsParcel> parcel;
    private Context context;


    public NewsRecycleAdapter(List<NewsParcel> parcel, Context c) {
        this.parcel = parcel;
        context = c;
    }

    @Override
    public NewsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsRecycleAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(parcel.get(position).getNote());
        String url = parcel.get(position).getImage_url();
        if (url == null || url.equals("")) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
            Picasso picasso = Picasso.with(context);
            picasso.setIndicatorsEnabled(false);
            picasso.load(url).placeholder(R.drawable.rect_grey)
                    .priority(Picasso.Priority.NORMAL)
                    .into(holder.mImageView);
//            holder.mImageView.
        }

    }

    @Override
    public long getItemId(int position) {
        return parcel.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return (parcel == null) ? 0 : parcel.size();
    }
    

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.news_item_note);
            mImageView = (ImageView) v.findViewById(R.id.news_item_image);
        }
    }

}
