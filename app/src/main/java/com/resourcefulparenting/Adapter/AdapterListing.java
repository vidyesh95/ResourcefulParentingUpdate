package com.resourcefulparenting.adapter;

import android.content.Context;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.resourcefulparenting.R;
import com.resourcefulparenting.fragment.ActivityListingFragment;
import com.resourcefulparenting.models.ActivityComListingResponse;

import java.util.ArrayList;

public class AdapterListing extends RecyclerView.Adapter<AdapterListing.VH> {

    private Context context;
    ArrayList<ActivityComListingResponse.Activities> list;
    String child_id;

    public AdapterListing(Context context, ArrayList<ActivityComListingResponse.Activities> list,String child_id){
    //) {
        this.context = context;
        this.list = list;
        this.child_id = child_id;
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_activitylisting, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
    final  ActivityComListingResponse.Activities items=list.get(position);


        holder.tv_activity_name.setText(items.activity_name);
        holder.points_img_.setText(" Poin telah diperoleh"+" "+items.activity_point);
        holder.tv_category_name.setText(items.category_name);
        if (!items.activity_image.equals("") && !items.activity_image.equals("null")) {
            Glide.with(context)
                    .load(items.activity_image)
                    .into(holder.activity_img_);
        }



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ActivityListingFragment.activities)context).ActivityPage(items.activity_id,child_id);
            }
        });
/*

    holder.tvName.setText(items.message);
    holder.tvDate.setText(items.created_at);
*/

    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    class VH extends RecyclerView.ViewHolder {


        TextView tv_activity_name,tv_category_name,points_img_;
        ImageView activity_img_;

        VH(View itemView) {
            super(itemView);
            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            points_img_ = itemView.findViewById(R.id.points_img_);
            activity_img_ = itemView.findViewById(R.id.activity_img_);


        }
    }


}