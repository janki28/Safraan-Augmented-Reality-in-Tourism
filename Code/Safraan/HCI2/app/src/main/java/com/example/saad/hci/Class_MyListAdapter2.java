package com.example.saad.hci;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Class_MyListAdapter2 extends BaseAdapter {
    private Context                     mContext;
    private LayoutInflater              mInflater;
    String _mode;
    private ArrayList<Class_Trail>      all_Trails;


    Class_MyListAdapter2(Context context, LayoutInflater inflater, String _mode) {
        mContext            = context;
        mInflater           = inflater;
        this._mode = _mode;
        all_Trails          = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return all_Trails.size();
    }

    @Override
    public Object getItem(int position) {
        return all_Trails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView    imagebox_image;
        TextView     imagebox_name;
        TextView     imagebox_numPlaces;

        ViewHolder() {
            imagebox_image      = null;
            imagebox_name       = null;
            imagebox_numPlaces  = null;
        }
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_2, parent, false);

            holder = new ViewHolder();
            holder.imagebox_image       = convertView.findViewById(R.id.list_picture);
            holder.imagebox_name        = convertView.findViewById(R.id.list_name);
            holder.imagebox_numPlaces   = convertView.findViewById(R.id.list_numPlaces);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_Trail trail   = (Class_Trail) getItem(position);
        int image_id        = mContext.getResources().getIdentifier(trail.getFirstImageSrc(), "drawable", mContext.getPackageName());

        holder.imagebox_image.setImageResource(image_id);

        holder.imagebox_name.setText(trail.getName());

        holder.imagebox_numPlaces.setText(MessageFormat.format("Places: {0}", trail.getNumPlaces()));

        return convertView;
    }

    void updateData(ArrayList<Class_Trail> _all_Trails) {
        all_Trails = _all_Trails;
        notifyDataSetChanged();
    }
}
