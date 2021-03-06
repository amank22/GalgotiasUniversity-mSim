package com.aman.teenscribblers.galgotiasuniversitymsim.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by aman on 07-11-2014.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private String[] itemTexts;
    private int[] itemIcons;

    private OnItemClickListener listener;

    public CategoryAdapter(String[] itemTexts, int[] itemIcons) {
        this.itemTexts = itemTexts;
        this.itemIcons = itemIcons;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_choice_1, viewGroup, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.name.setText(itemTexts[position]);
        holder.icon.setImageResource(itemIcons[position]);
//        holder.icon.setBackgroundColor(color[0]);
    }

    public void addItemClickListener(OnItemClickListener itemClickListener) {
        listener = itemClickListener;
    }

    @Override
    public int getItemCount() {
        return itemTexts != null ? itemTexts.length : 0;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String text);
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView icon;

        CategoryViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.image_category_icon);
            name = (TextView) itemView.findViewById(R.id.textView_category_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        final int position = getAdapterPosition();
                        listener.onItemClick(position, itemTexts[position]);
                    }
                }
            });
        }
    }
}
