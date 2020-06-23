package com.example.saad.hci;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Class_MyListAdapter extends BaseAdapter {
    private Context                         mContext;
    private LayoutInflater                  mInflater;
    private ArrayList<Class_HeritageSite>   all_HeritageSites;

    Class_MyListAdapter(Context context, LayoutInflater inflater) {
        mContext            = context;
        mInflater           = inflater;
        all_HeritageSites   = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return all_HeritageSites.size();
    }

    @Override
    public Object getItem(int position) {
        return all_HeritageSites.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder {
        ImageView    imagebox_image;
        ImageView[]  imagebox_popularity;
        TextView     imagebox_name;
        TextView     imagebox_address;
        TextView     imagebox_label;

        ViewHolder() {
            imagebox_image      = null;
            imagebox_popularity = new ImageView[5];
            imagebox_name       = null;
            imagebox_address    = null;
            imagebox_label      = null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item, parent, false);

            holder = new ViewHolder();
            holder.imagebox_image           = convertView.findViewById(R.id.list_picture);
            holder.imagebox_name            = convertView.findViewById(R.id.list_name);
            holder.imagebox_address         = convertView.findViewById(R.id.list_address);
            holder.imagebox_label           = convertView.findViewById(R.id.list_value);
            holder.imagebox_popularity[0]   = convertView.findViewById(R.id.popularity_icon1);
            holder.imagebox_popularity[1]   = convertView.findViewById(R.id.popularity_icon2);
            holder.imagebox_popularity[2]   = convertView.findViewById(R.id.popularity_icon3);
            holder.imagebox_popularity[3]   = convertView.findViewById(R.id.popularity_icon4);
            holder.imagebox_popularity[4]   = convertView.findViewById(R.id.popularity_icon5);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_HeritageSite heritageSite = (Class_HeritageSite) getItem(position);
        int image_id = mContext.getResources().getIdentifier(heritageSite.getImageMain(), "drawable", mContext.getPackageName());

        holder.imagebox_image.setImageResource(image_id);

        holder.imagebox_name.setText(heritageSite.getName());

        holder.imagebox_address.setText(String.format("Address: %s", heritageSite.getAddress()));

        int f = 5;
        while (heritageSite.getPopularity() < f) {
            f--;
            holder.imagebox_popularity[f].setVisibility(View.INVISIBLE);
        }

        float dist = (float)(((int)(heritageSite.calculateDistance(((Class_MyApplication)mContext.getApplicationContext()).curLoc) * 10))/10.0);

        if (dist < 1000) {
            holder.imagebox_label.setText(String.format("%s m", dist));
        } else {
            dist = dist / 1000;
            holder.imagebox_label.setText(String.format("%s km", dist));
        }

        return convertView;
    }

    void updateData(ArrayList<Class_HeritageSite> _all_HeritageSites) {
        all_HeritageSites = _all_HeritageSites;
        notifyDataSetChanged();
    }
}
