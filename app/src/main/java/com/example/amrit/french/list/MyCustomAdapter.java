package com.example.amrit.french.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.amrit.french.R;

import java.util.List;

/**
 * Created by Amrit on 27-10-2016.
 */

public class MyCustomAdapter extends ArrayAdapter<Word> {

    public class ViewHolder {
        TextView tvfrench;
        TextView tvmeaning;
    }

    public MyCustomAdapter(Context context,List<Word> objects) {
        super(context, R.layout.word_list, objects);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View row;
        ViewHolder viewHolder;

        if(convertView == null) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.word_list, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvfrench = (TextView) row.findViewById(R.id.tvfrench);
            viewHolder.tvmeaning = (TextView) row.findViewById(R.id.tvmeaning);

            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (ViewHolder) row.getTag();
        }

        Word item = getItem(position);

        viewHolder.tvfrench.setText(item.getFrench());
        viewHolder.tvmeaning.setText(item.getMeaning());

        return row;
    }
}