package com.example.jenil.parsedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by adas on 5/10/2015.
 */
public class YourListAdapter extends BaseAdapter{

    private LayoutInflater mInflater;
    private List<YourListObject> mItems;

    public YourListAdapter(Context context, List<YourListObject> items){
        mInflater = LayoutInflater.from(context);
        mItems = items;
    }

    @Override
    public int getCount(){
        return mItems.size();
    }

    @Override
    public YourListObject getItem(int position){
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = mInflater.inflate(R.layout.your_turn_adapter, parent, false);
            holder = new ViewHolder();
            holder.icon = (ImageView)view.findViewById(R.id.icon);
            holder.name = (TextView)view.findViewById(R.id.list_opponent);
            holder.turn_number = (TextView)view.findViewById(R.id.list_turn_num);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder)view.getTag();
        }

        YourListObject listObject = mItems.get(position);
        holder.name.setText(listObject.getName());
        holder.turn_number.setText(listObject.getTurnNumber());

        return view;
    }

    private class ViewHolder {
        public ImageView icon;
        public TextView name, turn_number;
    }
}
