package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;
import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.SeatingPlanParcel;

import java.util.List;

/**
 * Created by amankapoor on 19/07/17.
 */

public class SeatingPlanAdapter extends RecyclerView.Adapter<SeatingPlanAdapter.SeatingPlanViewHolder> {


    private List<SeatingPlanParcel> parcel;

    public SeatingPlanAdapter(List<SeatingPlanParcel> parcel) {
        this.parcel = parcel;
    }

    @Override
    public SeatingPlanViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SeatingPlanViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.seating_plan_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SeatingPlanViewHolder holder, int position) {
        holder.subject.setText(String.format("Date: %s at %s", parcel.get(position).getExamDate(), parcel.get(position).getExamTime()));
        holder.timeslot.setText(String.format("Subject Code: %s", parcel.get(position).getSubjectCode()));
        holder.hallno.setText(String.format("Seat: %s", parcel.get(position).getSeatNumber()));
        holder.faculty.setText(String.format("Room: %s", parcel.get(position).getRoomNumber()));
    }

    @Override
    public int getItemCount() {
        return parcel == null ? 0 : parcel.size();
    }

    class SeatingPlanViewHolder extends RecyclerView.ViewHolder {
        TextView subject;
        TextView faculty, timeslot, hallno;

        SeatingPlanViewHolder(View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.textView_subject);
            faculty = (TextView) itemView.findViewById(R.id.textView_faculty);
            hallno = (TextView) itemView.findViewById(R.id.textView_hall);
            timeslot = (TextView) itemView.findViewById(R.id.textView_timeslot);
        }
    }
}
