package com.aman.teenscribblers.galgotiasuniversitymsim.Adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by aman on 9/6/16.
 */
//public class SubjectRecycleAdapter extends RecyclerView.Adapter<SubjectRecycleAdapter.ViewHolder> {

//    private static final String TAG = SubjectRecycleAdapter.class.getCanonicalName();
//    private List<SubjectBean> subParcel;
//    private Resources res;
//
//    public SubjectRecycleAdapter(Context context, List<SubjectBean> subParcel) {
//        this.subParcel = subParcel;
//        res = context.getResources();
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.subject_list_item, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        String present = subParcel.get(position).Present;
//        String absent = subParcel.get(position).Absent;
//        String total = subParcel.get(position).Total;
//        String percent = subParcel.get(position).Percentage;
//        percent = format(Double.parseDouble(percent));
//        String subject = subParcel.get(position).Subject;
////        Log.d(TAG, "onBindViewHolder: "+percent);
//        holder.present.setText(String.format(res.getString(R.string.present), present));
//        holder.absent.setText(String.format(res.getString(R.string.absent), absent));
//        holder.total.setText(String.format(res.getString(R.string.total), total));
//        holder.percentage.setText(String.format(res.getString(R.string.percentage), percent));
//        holder.subject.setText(subject);
//        if (Double.parseDouble(percent) >= 75.00) {
//            holder.percentage.setTextColor(res.getColor(R.color.ts_green));
//        } else {
//            holder.percentage.setTextColor(res.getColor(R.color.ts_red));
//        }
//    }
//
//    private String format(double d) {
//        if (d == (long) d) {
//            return String.format("%d", (long) d);
//        } else
//            return String.format("%s", d);
//    }
//
//    @Override
//    public int getItemCount() {
//        return subParcel == null ? 0 : this.subParcel.size();
//    }
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView present;
//        public TextView absent;
//        public TextView total;
//        public TextView percentage;
//        public TextView subject;
//        public View indicator;
//
//        public ViewHolder(View row) {
//            super(row);
//            subject = (TextView) row.findViewById(R.id.textView_subject);
//            percentage = (TextView) row.findViewById(R.id.textView_perc);
//            indicator = row.findViewById(R.id.p_indicator);
//            total = (TextView) row.findViewById(R.id.textView_total);
//            present = (TextView) row.findViewById(R.id.textView_present);
//            absent = (TextView) row.findViewById(R.id.textView_absent);
//        }
//    }
//}
