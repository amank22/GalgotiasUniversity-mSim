package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.parcels.ResultParcel;
import com.aman.teenscribblers.galgotiasuniversitymsim.R;

import java.util.List;

/**
 * Created by aman on 10/6/16.
 */
public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.VHItem> {
    private List<ResultParcel> parsedList;

    public ResultAdapter(List<ResultParcel> parsedList) {
        this.parsedList = parsedList;
    }

    @Override
    public VHItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_result, parent, false);
        return new VHItem(v);
    }

    @Override
    public void onBindViewHolder(VHItem holder, int position) {
        holder.key.setText(parsedList.get(position).getSubject());
        holder.value.setText(parsedList.get(position).getGrade());
    }

    @Override
    public int getItemCount() {
        return parsedList.size();
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
}
