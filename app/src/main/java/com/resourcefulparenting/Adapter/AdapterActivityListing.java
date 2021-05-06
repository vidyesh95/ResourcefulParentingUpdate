package com.resourcefulparenting.adapter;

import android.content.Context;
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
import com.resourcefulparenting.models.Register.ActivityListing;

import java.util.ArrayList;

public class AdapterActivityListing extends RecyclerView.Adapter<AdapterActivityListing.VH> {

    private Context context;
    ArrayList<ActivityListing.Activities> list;
    String child_id;

    public AdapterActivityListing(Context context, ArrayList<ActivityListing.Activities> list, String child_id) {
        //) {
        this.context = context;
        this.list = list;
        this.child_id = child_id;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_activitylistingpage, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        final ActivityListing.Activities items = list.get(position);

        holder.tv_activity_name.setText(items.activity_name);
        // holder.points_img_.setText("Points Earned"+" "+items.activity_point);

        if (!items.image.equals("") && !items.image.equals("null")) {
            Glide.with(context)
                    .load(items.image)
                    .into(holder.activity_img_);
        }
        if (items.iscompleted) {
            holder.tick_img_.setImageResource(R.drawable.correct_tick_green);
            holder.tv_category_name.setText(items.activity_points + " points");
        } else {
            holder.tick_img_.setImageResource(R.drawable.ccirclegreen);
            holder.tv_category_name.setText(items.activity_points + " points");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((ActivityListingFragment.activities) context).ActivityPage(items.activity_id, child_id);
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

        TextView tv_activity_name, tv_category_name, points_img_;
        ImageView activity_img_, tick_img_;

        VH(View itemView) {
            super(itemView);
            tv_activity_name = itemView.findViewById(R.id.tv_ac_name);
            tv_category_name = itemView.findViewById(R.id.tv_category_name);
            //  points_img_ = itemView.findViewById(R.id.points_img_);
            activity_img_ = itemView.findViewById(R.id.activity_img_);
            tick_img_ = itemView.findViewById(R.id.tick_img_);
        }
    }
}