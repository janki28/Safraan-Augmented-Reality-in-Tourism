package com.example.saad.hci;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Class_MyTrailListAdapter extends BaseAdapter {
    private Context                         mContext;
    private LayoutInflater                  mInflater;
    private ArrayList<Class_HeritageSite>   all_HeritageSites;

    public Class_MyTrailListAdapter(Context context, LayoutInflater inflater) {
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
        TextView     imagebox_name;
        TextView     imagebox_description;

        ViewHolder() {
            imagebox_image      = null;
            imagebox_name       = null;
            imagebox_description  = null;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_3, parent, false);

            holder = new ViewHolder();
            holder.imagebox_image       = convertView.findViewById(R.id.list_picture);
            holder.imagebox_name        = convertView.findViewById(R.id.list_name);
            holder.imagebox_description   = convertView.findViewById(R.id.list_description);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Class_HeritageSite heritageSite = (Class_HeritageSite) getItem(position);
        int image_id = mContext.getResources().getIdentifier(heritageSite.getImageMain(), "drawable", mContext.getPackageName());

        holder.imagebox_image.setImageResource(image_id);

        holder.imagebox_name.setText(heritageSite.getName());

        holder.imagebox_description.setText(heritageSite.getDescription());

        return convertView;
    }

    public void updateData(ArrayList<Class_HeritageSite> _all_HeritageSites) {
        all_HeritageSites = _all_HeritageSites;
        notifyDataSetChanged();
    }
}
