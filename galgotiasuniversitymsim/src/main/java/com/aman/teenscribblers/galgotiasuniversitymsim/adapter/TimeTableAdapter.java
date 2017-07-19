package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.TimeTableParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

/**
 * Created by amankapoor on 19/07/17.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.ViewHolderTT> {


    private List<TimeTableParcel> parcel;

    public TimeTableAdapter(List<TimeTableParcel> parcel) {
        this.parcel = parcel;
    }

    @Override
    public ViewHolderTT onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderTT(LayoutInflater.from(parent.getContext()).inflate(R.layout.timetable_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolderTT holder, int position) {
        holder.subject.setText(parcel.get(position).getSubject());
        holder.group.setText(parcel.get(position).getGroup());
        holder.timeslot.setText(parcel.get(position).getTimeslot());
        holder.hallno.setText(parcel.get(position).getHallno());
        holder.faculty.setText(parcel.get(position).getFaculty());
        if (parcel.get(position).getGroup().equals("")) {
            holder.group.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return parcel == null ? 0 : parcel.size();
    }

    class ViewHolderTT extends RecyclerView.ViewHolder {
        TextView subject;
        TextView group, faculty, timeslot, hallno;

        ViewHolderTT(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.textView_subject);
            faculty = (TextView) itemView.findViewById(R.id.textView_faculty);
            group = (TextView) itemView.findViewById(R.id.textView_group);
            hallno = (TextView) itemView.findViewById(R.id.textView_hall);
            timeslot = (TextView) itemView.findViewById(R.id.textView_timeslot);
        }
    }
}
