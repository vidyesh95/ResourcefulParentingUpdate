/*
package com.resourcefulparenting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.models.Input.MilestoneQuestionSend;
import com.resourcefulparenting.models.MilestoneQuestionsResponse;
import com.resourcefulparenting.models.MilestoneQuestionsSendResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

        public class AdapterQustionListing extends RecyclerView.Adapter<AdapterQustionListing.VH> {

    private Context context;
    ArrayList<MilestoneQuestionsResponse.QuestionDetails> list;
    AppCompatButton btn_register;
    RelativeLayout loading;
    final MilestoneQuestionSend activitySendCheck = new MilestoneQuestionSend();

    public AdapterQustionListing(Context context, ArrayList<MilestoneQuestionsResponse.QuestionDetails> list, AppCompatButton btn_register, RelativeLayout loading){
    //) {
        this.context = context;
        this.list = list;
        this.btn_register = btn_register;
        this.loading = loading;

    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_question_mile, parent, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
    final MilestoneQuestionsResponse.QuestionDetails items=list.get(position);


        holder.tv_activity_name.setText(items.question);
       // holder.points_img_.setText("Points Earned"+" "+items.activity_point);

        if (items.checked) {
            holder.activity_img_.setImageResource(R.drawable.correct_tick_green);
        } else {
            holder.activity_img_.setImageResource(R.drawable.ccirclegreen);
        }



        holder.activity_img_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MilestoneQuestionsResponse.QuestionDetails item = list.get(holder.getAdapterPosition());
                item.checked = !item.checked;
                notifyItemChanged(holder.getAdapterPosition());
               // ((ActivityListingFragment.activities)context).ActivityPage(items.activity_id,child_id);
            }
        });

*/
/*

    holder.tvName.setText(items.message);
    holder.tvDate.setText(items.created_at);
*//*


    }



    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    class VH extends RecyclerView.ViewHolder {


        TextView tv_activity_name;
        ImageView activity_img_;

        VH(View itemView) {
            super(itemView);
            tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
//            points_img_ = itemView.findViewById(R.id.points_img_);
            activity_img_ = itemView.findViewById(R.id.activity_img_);

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkNetWorkComplit();
                }
            });

        }
    }

    private void checkNetWorkComplit() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                setCompletedActivity();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ///hud.dismiss();
            H.T(context,context. getString(R.string.no_internet_connection));
        }
    }
    private void setCompletedActivity() {
        loading.setVisibility(View.VISIBLE);

        JSONObject jObjd=new JSONObject();
        for (int i = 0; i < list.size(); i++) {
            try {

                if (list.get(i).checked)
                {
                    jObjd.put(list.get(i).id, list.get(i).checked);

                }

                //  click=false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        H.L("data"+jObjd);
        loading.setVisibility(View.GONE);

     */
/*   activitySendCheck.login_token= Prefs.getLoginToken(context);
        activitySendCheck.child_id=Prefs.getChildID(context);
        activitySendCheck.questionDetails=jObjd;
        Call<MilestoneQuestionsSendResponse> call = ApiClient.getRetrofit().create(Api.class).milestoneQuestionsSend(activitySendCheck);
        call.enqueue(new Callback<MilestoneQuestionsSendResponse>() {
            @Override
            public void onResponse(Call<MilestoneQuestionsSendResponse> call, Response<MilestoneQuestionsSendResponse> response) {
                H.L("responsennnn=" + new Gson().toJson(response.body()));
                loading.setVisibility(View.GONE);
                MilestoneQuestionsSendResponse response1= response.body();
                if(response1.error.equals("false")){
                    //   H.T(context,response1.message);
                    ((AppCompatActivity)context).finish();


                }else {
                    //  H.T(context,response1.message);
                }
            }

            @Override
            public void onFailure(Call<MilestoneQuestionsSendResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                //Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
*//*


    }

}*/
