package com.aman.teenscribblers.galgotiasuniversitymsim.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.teenscribblers.galgotiasuniversitymsim.R;

/**
 * Created by aman on 07-11-2014.
 */
public class ListAdapter extends ArrayAdapter<String> {
    String[] a;
    int[] color = {getcolor(R.color.ts_yellow),
            getcolor(R.color.ts_blue),
            getcolor(R.color.ts_pink),
            getcolor(R.color.ts_purple),
            getcolor(R.color.ts_green),
    };

    public ListAdapter(Context context, String[] objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        a = objects;
    }

    int getcolor(int id) {
        return getContext().getResources().getColor(id);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ViewHolder viewHolder;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_choice_1, null);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) v.findViewById(R.id.textView_grid);
            viewHolder.card = (ImageView) v.findViewById(R.id.image_color_block);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }
        if (a != null) {
            viewHolder.name.setText(a[position]);
        }
        viewHolder.card.setBackgroundColor(color[position % 5]);
        return v;
    }

    @Override
    public int getCount() {
        return (a == null) ? 0 : a.length;
    }

    private class ViewHolder {
        public TextView name;
        public ImageView card;
    }
}
