package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.NewsParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by aman on 19-03-2015 in Galgotias University(mSim).
 */
public class NewsAdapter extends ArrayAdapter<NewsParcel> {

    List<NewsParcel> parcel;

    public NewsAdapter(Context context, List<NewsParcel> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        parcel = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder viewHolder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.news_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.note = (TextView) v.findViewById(R.id.news_item_note);
            viewHolder.image = (ImageView) v.findViewById(R.id.news_item_image);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        viewHolder.note.setText(parcel.get(position).getNote());
        String url = parcel.get(position).getImage_url();
        if (url == null || url.equals("")) {
            viewHolder.image.setVisibility(View.GONE);
        } else {
            Picasso picasso = Picasso.with(this.getContext());
            picasso.setIndicatorsEnabled(false);
            picasso.load(url).placeholder(R.drawable.logo)
                    .priority(Picasso.Priority.NORMAL)
                    .into(viewHolder.image);
        }
        return v;
    }

    @Override
    public int getCount() {
        return (parcel == null) ? 0 : parcel.size();
    }

    private class ViewHolder {
        TextView note;
        ImageView image;
    }
}
