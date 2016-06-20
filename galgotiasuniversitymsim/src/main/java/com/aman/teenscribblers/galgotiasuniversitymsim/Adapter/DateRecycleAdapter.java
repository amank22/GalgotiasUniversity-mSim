package com.aman.teenscribblers.galgotiasuniversitymsim.Adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.Parcels.DateParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

/**
 * Created by aman on 9/6/16.
 */
public class DateRecycleAdapter extends RecyclerView.Adapter<DateRecycleAdapter.ViewHolder> {

    private List<DateParcel> parcel;
    private Resources res;

    public DateRecycleAdapter(Context context, List<DateParcel> parcel) {
        this.parcel = parcel;
        res = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.attendance_list_item_json, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.status.setText(parcel.get(position).getStatus());
        holder.timeslot.setText(parcel.get(position).getTimeSlot());
        holder.date.setText(parcel.get(position).getDate());
        holder.subject.setText(parcel.get(position).getSubjectName());
        if (holder.status.getText().equals("P")) {
            holder.status.setTextColor(res.getColor(R.color.ts_green));
        } else {
            holder.status.setTextColor(res.getColor(R.color.ts_red_dark));
        }
    }

    @Override
    public int getItemCount() {
        return parcel == null ? 0 : this.parcel.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView timeslot;
        public TextView status;
        public TextView subject;

        public ViewHolder(View row) {
            super(row);
            subject = (TextView) row.findViewById(R.id.textView_subject);
            date = (TextView) row.findViewById(R.id.textView_date);
            timeslot = (TextView) row.findViewById(R.id.textView_time_slot);
            status = (TextView) row.findViewById(R.id.textView_status);
        }
    }
}
