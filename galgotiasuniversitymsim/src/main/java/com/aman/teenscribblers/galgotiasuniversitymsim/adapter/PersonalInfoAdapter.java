package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.InfoParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

/**
 * Created by aman on 10/6/16.
 */
public class PersonalInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    private List<InfoParcel> parsedList;

    public PersonalInfoAdapter(List<InfoParcel> parsedList) {
        this.parsedList = parsedList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_msim, parent, false);
            return new VHItem(v);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_header, parent, false);
            return new VHHeader(v);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InfoParcel parcel=getItem(position);
        if (holder instanceof VHItem) {
            ((VHItem) holder).key.setText(parcel.getKey());
            ((VHItem) holder).value.setText(parcel.getValue());
            //cast holder to VHItem and set data
        } else if (holder instanceof VHHeader) {
            //cast holder to VHHeader and set data for header.
            ((VHHeader) holder).header.setText(parcel.getKey());
        }
    }

    @Override
    public int getItemCount() {
        return parsedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return getItem(position).getValue().equals(AppConstants.PERSONAL_HEADING);
    }

    private InfoParcel getItem(int position) {
        return parsedList.get(position);
    }

    class VHItem extends RecyclerView.ViewHolder {
        TextView key;
        TextView value;

        public VHItem(View itemView) {
            super(itemView);
            key = (TextView) itemView.findViewById(R.id.title);
            value = (TextView) itemView.findViewById(R.id.value);
        }
    }

    class VHHeader extends RecyclerView.ViewHolder {
        TextView header;

        public VHHeader(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.personal_header);
        }
    }
}
