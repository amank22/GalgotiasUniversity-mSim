package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.helper.GlideApp;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 24-10-2015 in Galgotias University(mSim).
 */
public class NewsRecycleAdapter extends RecyclerView.Adapter<NewsRecycleAdapter.ViewHolder> {

    private static final String TAG = "NEWSADAPTER";
    private List<NewsParcel> parcel;
    private Context context;

    private NewsClickListener newsClickListener;


    public NewsRecycleAdapter(List<NewsParcel> parcel, Context c, NewsClickListener newsClickListener) {
        this.parcel = parcel;
        context = c;
        this.newsClickListener = newsClickListener;
    }

    @Override
    public NewsRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_item_layout, parent, false);
        return new ViewHolder(v, newsClickListener, parcel);
    }

    @Override
    public void onBindViewHolder(final NewsRecycleAdapter.ViewHolder holder, int position) {
        holder.mTextView.setText(parcel.get(position).getNote());
        holder.mTextAuthor.setText(parcel.get(position).getAuthor());
        String url = parcel.get(position).getImage_url();
        String authorImageUrl = parcel.get(position).getAuthorPic();

        if (url == null || url.equals("")) {
            holder.mImageView.setVisibility(View.GONE);
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
            GlideApp.with(context)
                    .asBitmap()
                    .load(url)
                    .placeholder(R.drawable.rect_grey)
                    .transforms(new FitCenter(), new CenterInside())
                    .error(R.drawable.rect_grey)
                    .into(holder.mImageView);
        }
        GlideApp.with(context)
                .load(authorImageUrl)
                .placeholder(R.drawable.ic_avatar_admin)
                .transforms(new FitCenter(), new CircleCrop())
                .thumbnail(0.3f)
                .error(R.drawable.ic_avatar_admin)
                .into(holder.mAuthorImageView);

    }

    @Override
    public boolean onFailedToRecycleView(ViewHolder holder) {
//        GlideApp.get(context)
//        Picasso.with(context).cancelRequest(holder.mImageView);
        return super.onFailedToRecycleView(holder);
    }

    @Override
    public long getItemId(int position) {
        return parcel.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return (parcel == null) ? 0 : parcel.size();
    }

    public void insertElements(List<NewsParcel> newParcels) {
        if (parcel == null) {
            parcel = new ArrayList<>();
        }
        if (newParcels == null || newParcels.isEmpty()) {
            return;
        }
        int oldSize = parcel.size();
        parcel.addAll(newParcels);
        notifyItemRangeInserted(oldSize, newParcels.size());
    }

    public int getLastElementId() {
        if (parcel == null || parcel.isEmpty()) {
            return -1;
        }
        return parcel.get(0).getId();
    }

    public void removeAllElements() {
        if (parcel == null || parcel.isEmpty()) {
            return;
        }
        parcel = null;
        notifyDataSetChanged();
    }

    public interface NewsClickListener {
        void onImageClick(ImageView imageView, NewsParcel parcel);

        void onMailClick(View view, NewsParcel parcel);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextAuthor;
        public ImageView mImageView;
        public ImageView mAuthorImageView;
        public ImageButton mAuthorMailButton;

        public ViewHolder(View v, final NewsClickListener newsClickListener, final List<NewsParcel> parcel) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.news_item_note);
            mTextAuthor = (TextView) v.findViewById(R.id.news_item_author);
            mImageView = (ImageView) v.findViewById(R.id.news_item_image);
            mAuthorImageView = (ImageView) v.findViewById(R.id.imageView_news_author);
            mAuthorMailButton = (ImageButton) v.findViewById(R.id.button_news_mail_author);

            mImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newsClickListener != null) {
                        newsClickListener.onImageClick(mImageView, parcel.get(getAdapterPosition()));
                    }
                }
            });
            mAuthorMailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newsClickListener != null) {
                        newsClickListener.onMailClick(mAuthorMailButton, parcel.get(getAdapterPosition()));
                    }
                }
            });
        }
    }

}
