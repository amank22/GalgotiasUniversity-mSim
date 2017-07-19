package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.helper.AppConstants;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SimParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

/**
 * Created by amankapoor on 19/07/17.
 */

public class AttendanceAdapter extends RecyclerView.Adapter {

    private Context context;
    private String type;
    private List<SimParcel> parcel;

    public AttendanceAdapter(Context context, String type, List<SimParcel> parcel) {
        this.context = context;
        this.type = type;
        this.parcel = parcel;
    }

    private static final int TYPE_TODAY = 1;
    private static final int TYPE_SUBJECT = 2;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TODAY) {
            return new ViewHolderToday(LayoutInflater.from(parent.getContext()).inflate(R.layout.todays_att_list_item, parent, false));
        } else {
            return new ViewHolderSubj(LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_list_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderToday) {
            bindTodays((ViewHolderToday) holder, position);
        } else if (holder instanceof ViewHolderSubj) {
            bindSubjects((ViewHolderSubj) holder, position);
        }
    }

    private void bindSubjects(ViewHolderSubj subholder, int position) {
        subholder.subject.setText(String.format("%s - %s", parcel.get(position).getSubject(), parcel.get(position).getSem()));
        subholder.percentage.setText(String.valueOf(parcel.get(
                position).getPercnt()));
        if (parcel.get(position).getPercnt() < 75.0f) {
            subholder.indcator.setBackgroundColor(ContextCompat.getColor(context, R.color.ts_red));
        } else {
            subholder.indcator.setBackgroundColor(ContextCompat.getColor(context, R.color.ts_green));
        }
        subholder.total.setText(String.valueOf(parcel.get(position)
                .getTotal()));
        subholder.present.setText(String.valueOf(parcel.get(
                position).getPresent()));
        subholder.absent.setText(String.valueOf(parcel
                .get(position).getAbsent()));
    }

    private void bindTodays(ViewHolderToday tholder, int position) {
        if (parcel.get(position).getSubject() != null) {
            tholder.subject.setText(parcel.get(position).getSubject());
        }
        if (parcel.get(position).getTimeslot() != null) {
            tholder.timeslot.setText(parcel.get(position).getTimeslot());
        }
        if (parcel.get(position).getClasstype() != null) {
            tholder.atttype.setText(parcel.get(position).getClasstype());
        }
        if (parcel.get(position).getStatus() != null) {
            tholder.status.setText(parcel.get(position).getStatus());
            if (parcel.get(position).getStatus().equals("A")) {
                tholder.status.setBackgroundResource(R.drawable.circle_red);
            } else if (parcel.get(position).getStatus().equals("P")) {
                tholder.status.setBackgroundResource(R.drawable.circle_green);
            } else {
                tholder.status.setBackgroundResource(R.drawable.circle_grey);
            }
        }
    }

    @Override
    public int getItemCount() {
        return parcel == null ? 0 : parcel.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (type.equals(AppConstants.ATT_TODAY)) {
            return TYPE_TODAY;
        } else {
            return TYPE_SUBJECT;
        }
    }

    private class ViewHolderToday extends RecyclerView.ViewHolder {
        TextView subject, timeslot;
        TextView atttype;
        TextView status;

        ViewHolderToday(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.textView_subject);
            timeslot = (TextView) itemView.findViewById(R.id.textView_time_slot);
            atttype = (TextView) itemView.findViewById(R.id.textView_class_type);
            status = (TextView) itemView.findViewById(R.id.textView_status);

        }
    }

    private class ViewHolderSubj extends RecyclerView.ViewHolder {
        TextView subject;
        TextView percentage;
        TextView present, absent, total;
        View indcator;

        ViewHolderSubj(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.textView_subject);
            percentage = (TextView) itemView.findViewById(R.id.textView_perc);
            indcator = itemView.findViewById(R.id.p_indicator);
            total = (TextView) itemView.findViewById(R.id.textView_total);
            present = (TextView) itemView.findViewById(R.id.textView_present);
            absent = (TextView) itemView.findViewById(R.id.textView_absent);
        }
    }

}
