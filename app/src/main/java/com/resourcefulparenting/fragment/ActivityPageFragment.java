package com.resourcefulparenting.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.adapter.AdapterActivityListing;
import com.resourcefulparenting.adapter.AdapterListing;
import com.resourcefulparenting.databinding.FragmentActivityPageBinding;
import com.resourcefulparenting.models.ActivityComListingResponse;
import com.resourcefulparenting.models.AcyivitySendComResponse;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.Input.ActivityComplistCheck;
import com.resourcefulparenting.models.Input.ActivityListCheck;
import com.resourcefulparenting.models.Register.ActivityListing;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityPageFragment extends Fragment {
    private FragmentActivityPageBinding binding;
    final ActivityListCheck activitySendCheck = new ActivityListCheck();
    private String login_token, name;
    Context context;
    AdapterActivityListing adapterListing;
    public ActivityPageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityPageBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        name = getArguments().getString("name");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout constraintLayout = view.findViewById(R.id.test1);
        context = constraintLayout.getContext();


        constraintLayout.setOnClickListener(view1 -> {
            ((activityPage)getActivity()).ActivityDetails();
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.rvList.setLayoutManager(mLayoutManager);

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
        binding.loading.setVisibility(View.VISIBLE);

        activitySendCheck.login_token= Prefs.getLoginToken(context);
        activitySendCheck.child_id=Prefs.getChildID(context);
        activitySendCheck.category_id=Prefs.getCategory_id(context);
        Call<ActivityListing> call = ApiClient.getRetrofit().create(Api.class).ACTIVITY_LISTING_CALL(activitySendCheck);
        call.enqueue(new Callback<ActivityListing>() {
            @Override
            public void onResponse(Call<ActivityListing> call, Response<ActivityListing> response) {
                H.L("responsennnn=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ActivityListing response1= response.body();
                if (response1 !=null)
                {
                if(response1.error.equals("false")){
                    //   H.T(context,response1.message);
                    binding.tvActivityName.setText(response1.activityinfo.name);
                    binding.edtShortdescription1.setText(response1.activityinfo.description);

                    adapterListing = new AdapterActivityListing(context,response1.result_data,Prefs.getChildID(context));
                    binding.rvList.setAdapter(adapterListing);
                    if (!response1.activityinfo.image.equals("") && !response1.activityinfo.image.equals("null")) {
                        Glide.with(context)
                                .load(response1.activityinfo.image)
                                .into(binding.activitypageImg);
                    }

                }

                }
            }

            @Override
            public void onFailure(Call<ActivityListing> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface activityPage{
        void ActivityDetails();
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  binding = null;
    }
}