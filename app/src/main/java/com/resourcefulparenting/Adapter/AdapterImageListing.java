package com.resourcefulparenting.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.ImageZoomViewActivity;
import com.resourcefulparenting.fragment.ActivityListingFragment;
import com.resourcefulparenting.models.AlarmResponse;
import com.resourcefulparenting.models.ImageDeleteResponse;
import com.resourcefulparenting.models.Input.AlarmCheck;
import com.resourcefulparenting.models.Input.ImageDeleteCheck;
import com.resourcefulparenting.models.Register.ActivityListing;
import com.resourcefulparenting.models.TodayAcyivityResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterImageListing extends RecyclerView.Adapter<AdapterImageListing.VH> {

    private Context context;
    ArrayList list;
   // String child_id;
    ImageView homeCamera;
   final ImageDeleteCheck alarmCheck = new ImageDeleteCheck();
    public AdapterImageListing(Context context, ArrayList  list,ImageView homeCamera){
    //) {
        this.context = context;
        this.list = list;
        this.homeCamera = homeCamera;
        H.L("size"+list.size());
       // this.child_id = child_id;
        if (list.size() ==2)
        {
            homeCamera.setVisibility(View.INVISIBLE);
        }
        else {
            homeCamera.setVisibility(View.VISIBLE);
        }

    }



    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_imagelist, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
   // final  ArrayList items=list.get(position);


      //  H.L("image"+list.get(position).toString());





        if (!list.get(position).toString().equals("") && !list.get(position).toString().equals("null")) {
            Glide.with(context)
                    .load(list.get(position).toString())
                    .into(holder.activity_img_);

        }



        holder.cancle_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                H.L("position"+position);
                checkNetWorkAlarm(list.get(position).toString());
                list.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                notifyItemRangeChanged(position, list.size());

            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(context, ImageZoomViewActivity.class);
                String s = list.get(position).toString();
                in.putExtra("image",s);
                context.startActivity(in);

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

        ImageView activity_img_,cancle_image;

        VH(View itemView) {
            super(itemView);

            activity_img_ = itemView.findViewById(R.id.imgView);
            cancle_image = itemView.findViewById(R.id.cancle_image);


        }
    }
    private void checkNetWorkAlarm(String url) {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                setAlram(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context, context. getString(R.string.no_internet_connection));
        }
    }

    private void setAlram(String img_url) {

        alarmCheck.login_token= Prefs.getLoginToken(context);
        alarmCheck.img_url=img_url;

        Call<ImageDeleteResponse> call = ApiClient.getRetrofit().create(Api.class).imageDeleteResponse(alarmCheck);
        call.enqueue(new Callback<ImageDeleteResponse>() {
            @Override
            public void onResponse(Call<ImageDeleteResponse> call, Response<ImageDeleteResponse> response) {
                ImageDeleteResponse response1= response.body();
                H.L("responsealram=" + new Gson().toJson(response.body()));
                if (response1 !=null)
                {
                    if(response1.error.equals("false")){
                        homeCamera.setVisibility(View.VISIBLE);
                        H.T(context,response1.message);
                    }else {
                        H.T(context,response1.message);
                    }
                }

            }

            @Override
            public void onFailure(Call<ImageDeleteResponse> call, Throwable t) {
              //  binding.loading.setVisibility(View.GONE);
            }
        });
    }


}