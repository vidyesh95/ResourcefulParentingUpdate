package com.resourcefulparenting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.adapter.AdapterListing;


import com.resourcefulparenting.models.Input.MilestoneQuestionCheck;
import com.resourcefulparenting.models.Input.MilestoneQuestionSend;
import com.resourcefulparenting.models.MilestoneQuestionsResponse;
import com.resourcefulparenting.models.MilestoneQuestionsSendResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuestionsMilestonesActivity extends AppCompatActivity {

    final MilestoneQuestionCheck activitySendCheck = new MilestoneQuestionCheck();
    LinearLayout llMenu;
    RecyclerView rv_list;
    RelativeLayout loading;
    Context context;
    AdapterQustionListing adapterListing;
    AppCompatButton btn_register;
    HashMap<String, String> hs = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_milestones);
        context=this;

        rv_list=findViewById(R.id.rv_list);
        loading=findViewById(R.id.loading);
        llMenu = findViewById(R.id.llMenu);
        btn_register = findViewById(R.id.btn_register);

       /* btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                H.T(context,"dkjbschj");
            }
        });*/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv_list.setLayoutManager(mLayoutManager);

        llMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        checkNetWorkComplit();
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
            H.T(context, getString(R.string.no_internet_connection));
        }
    }
    private void setCompletedActivity() {
        loading.setVisibility(View.VISIBLE);

        activitySendCheck.login_token= Prefs.getLoginToken(context);
        activitySendCheck.child_id=Prefs.getChildID(context);
        Call<MilestoneQuestionsResponse> call = ApiClient.getRetrofit().create(Api.class).milestoneQuestionsResponse(activitySendCheck);
        call.enqueue(new Callback<MilestoneQuestionsResponse>() {
            @Override
            public void onResponse(Call<MilestoneQuestionsResponse> call, Response<MilestoneQuestionsResponse> response) {
                H.L("responsennnn=" + new Gson().toJson(response.body()));
                loading.setVisibility(View.GONE);
                MilestoneQuestionsResponse response1= response.body();
                if (response1 !=null)
                {
                    if(response1.error.equals("false"))
                    {
                        adapterListing = new AdapterQustionListing(context, response1.result_data,btn_register,loading);
                        rv_list.setAdapter(adapterListing);

                    }
                }

            }

            @Override
            public void onFailure(Call<MilestoneQuestionsResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
                //Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public class AdapterQustionListing extends RecyclerView.Adapter<AdapterQustionListing.ViewHolder> {

        private Context context;
        ArrayList<MilestoneQuestionsResponse.QuestionDetails> list;
        AppCompatButton btn_register;
        RelativeLayout loading;
        final MilestoneQuestionSend activitySendCheck1 = new MilestoneQuestionSend();

        public AdapterQustionListing(Context context, ArrayList<MilestoneQuestionsResponse.QuestionDetails> list, AppCompatButton btn_register, RelativeLayout loading){
            //) {
            this.context = context;
            this.list = list;
            this.btn_register = btn_register;
            this.loading = loading;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_row_question_mile, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final MilestoneQuestionsResponse.QuestionDetails items=list.get(position);
            holder.tv_activity_name.setText(items.question);
            // holder.points_img_.setText("Points Earned"+" "+items.activity_point);

            if (items.checked) {
                holder.ivTick.setVisibility(View.VISIBLE);
                if (items.id.equalsIgnoreCase("1")) {
                    holder.activity_img_.setImageResource(R.drawable.circlelanguage);
                }
                else  if (items.id.equalsIgnoreCase("2")){
                    holder.activity_img_.setImageResource(R.drawable.circlelogic);
                }
                else  if (items.id.equalsIgnoreCase("3")){
                    holder.activity_img_.setImageResource(R.drawable.circlephysical);
                }
                else  if (items.id.equalsIgnoreCase("4")){
                    holder.activity_img_.setImageResource(R.drawable.circleinterapersonal);
                }
                else  if (items.id.equalsIgnoreCase("5")){
                    holder.activity_img_.setImageResource(R.drawable.circleinterpersonal);
                }
                else  if (items.id.equalsIgnoreCase("6")){
                    holder.activity_img_.setImageResource(R.drawable.circlespatial);
                }
                else  if (items.id.equalsIgnoreCase("7")){
                    holder.activity_img_.setImageResource(R.drawable.circlemusic);
                }
                else  if (items.id.equalsIgnoreCase("8")){
                    holder.activity_img_.setImageResource(R.drawable.circleenvironment);
                }
            } else {
                holder.ivTick.setVisibility(View.GONE);
                if (items.id.equalsIgnoreCase("1")) {
                    holder.activity_img_.setImageResource(R.drawable.circlelanguage);
                }
                else  if (items.id.equalsIgnoreCase("2")){
                    holder.activity_img_.setImageResource(R.drawable.circlelogic);
                }
                else  if (items.id.equalsIgnoreCase("3")){
                    holder.activity_img_.setImageResource(R.drawable.circlephysical);
                }
                else  if (items.id.equalsIgnoreCase("4")){
                    holder.activity_img_.setImageResource(R.drawable.circleinterapersonal);
                }
                else  if (items.id.equalsIgnoreCase("5")){
                    holder.activity_img_.setImageResource(R.drawable.circleinterpersonal);
                }
                else  if (items.id.equalsIgnoreCase("6")){
                    holder.activity_img_.setImageResource(R.drawable.circlespatial);
                }
                else  if (items.id.equalsIgnoreCase("7")){
                    holder.activity_img_.setImageResource(R.drawable.circlemusic);
                }
                else  if (items.id.equalsIgnoreCase("8")){
                    holder.activity_img_.setImageResource(R.drawable.circleenvironment);
                }
            }

            holder.activity_img_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MilestoneQuestionsResponse.QuestionDetails item = list.get(holder.getAdapterPosition());
                    item.checked = !item.checked;
                    notifyItemChanged(holder.getAdapterPosition());
                    //((ActivityListingFragment.activities)context).ActivityPage(items.activity_id,child_id);
                }
            });

            btn_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).checked)
                        {
                            hs.put(list.get(i).id, "" + list.get(i).checked);
                        }
                        System.out.println("json " + hs);

                    }
                    checkNetWorkComplit();
                }
            });
        }

        @Override
        public int getItemCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_activity_name;
            ImageView activity_img_,ivTick;

            ViewHolder(View itemView) {
                super(itemView);
                tv_activity_name = itemView.findViewById(R.id.tv_activity_name);
//            points_img_ = itemView.findViewById(R.id.points_img_);
                activity_img_ = itemView.findViewById(R.id.activity_img_);
                ivTick = itemView.findViewById(R.id.ivTick);
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


            //   loading.setVisibility(View.GONE);

            activitySendCheck1.login_token= Prefs.getLoginToken(context);
            activitySendCheck1.child_id=Prefs.getChildID(context);
            activitySendCheck1.questionDetails=hs;
            Call<MilestoneQuestionsSendResponse> call = ApiClient.getRetrofit().create(Api.class).milestoneQuestionsSend(activitySendCheck1);
            call.enqueue(new Callback<MilestoneQuestionsSendResponse>() {
                @Override
                public void onResponse(Call<MilestoneQuestionsSendResponse> call, Response<MilestoneQuestionsSendResponse> response) {
                    H.L("responsennnn=" + new Gson().toJson(response.body()));
                    loading.setVisibility(View.GONE);
                    MilestoneQuestionsSendResponse response1= response.body();
                    if (response1 !=null)
                    {
                        if(response1.error.equals("false")){
                            Intent intent = new Intent(QuestionsMilestonesActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

                        }
                    }

                }

                @Override
                public void onFailure(Call<MilestoneQuestionsSendResponse> call, Throwable t) {
                    loading.setVisibility(View.GONE);
                    //Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}