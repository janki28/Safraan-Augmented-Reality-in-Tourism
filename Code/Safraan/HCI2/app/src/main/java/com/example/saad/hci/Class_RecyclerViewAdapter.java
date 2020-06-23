package com.example.saad.hci;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Class_RecyclerViewAdapter extends RecyclerView.Adapter<Class_RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Class_Trail> all_Trails;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView    imagebox_image;
        TextView     imagebox_text;
        FrameLayout  imagebox;

        private ViewHolder(View v) {
            super(v);
            this.imagebox_image  = v.findViewById(R.id.imagebox_image);
            this.imagebox_text   = v.findViewById(R.id.imagebox_text);
            this.imagebox        = v.findViewById(R.id.imagebox);
        }
    }

    public Class_RecyclerViewAdapter(ArrayList<Class_Trail> _all_Trails, Context _mContext) {
        all_Trails   = _all_Trails;
        mContext     = _mContext;
    }


    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_box, viewGroup, false);
        return new ViewHolder(v);
    }


    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Class_Trail trail   = all_Trails.get(i);
        int image_id        = mContext.getResources().getIdentifier(trail.getFirstImageSrc(), "drawable", mContext.getPackageName());

        viewHolder.imagebox_image.setImageResource(image_id);
        viewHolder.imagebox_text.setText(trail.getName());

        if (i == all_Trails.size()-1) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)(viewHolder.imagebox.getLayoutParams());
            params.setMargins(0, 0, 0, 0);
            viewHolder.imagebox.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)(viewHolder.imagebox.getLayoutParams());
            params.setMargins(0, 0, 4, 0);
            viewHolder.imagebox.setLayoutParams(params);
        }
    }


    public long getItemId(int position) {
        return position;
    }


    public int getItemCount() {
        return all_Trails.size();
    }

    public void updateData(ArrayList<Class_Trail> _all_Trails) {
        all_Trails = _all_Trails;
        notifyDataSetChanged();
    }
}
