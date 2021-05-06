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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.adapter.AdapterListing;
import com.resourcefulparenting.databinding.FragmentActivityListingBinding;
import com.resourcefulparenting.models.ActivityComListingResponse;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.Input.ActivityComplistCheck;
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

public class ActivityListingFragment extends Fragment {
    private FragmentActivityListingBinding binding;
    private List<ChildDetails> childDetails1 = new ArrayList<>();
      AdapterListing adapterListing;
    private Spinner child;
    int n=1;
    private Context context;
    String child_id="";
    final ActivityComplistCheck activitySendCheck = new ActivityComplistCheck();

    public ActivityListingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityListingBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = container.getContext();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ConstraintLayout constraintLayout = view.findViewById(R.id.test);



        ImageView milestone =getActivity().findViewById(R.id.milestone_img);
        milestone.setVisibility(View.GONE);
        child = binding.spinChildName;
        List<String> childs = new ArrayList<>();


        try {
            childDetails1.clear();
            JSONArray jsonArray=new JSONArray(Prefs.getChildDetails(context));
            //Log.d("Arraym", String.valueOf(jsonArray.length()));
            for (int i=0;i<jsonArray.length();i++)
            {
                ChildDetails childDetails=new ChildDetails();
                JSONObject object = jsonArray.getJSONObject(i);
                String id=object.getString("child_id");
                String name=object.getString("child_name");
                childDetails.setId(id);
                childDetails.setChild_name(name);
                childs.add(name);
                childDetails1.add(childDetails);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, R.layout.spinner_text, childs);
        // attaching data adapter to spinner
        child.setAdapter(dataAdapter);
        if (childs.size() == 1)
        {
            child.setEnabled(false);
            for (int j = 0; j < childDetails1.size(); j++) {
                child_id = childDetails1.get(j).getId();
                Prefs.setChildID(context, child_id);
                checkNetWorkComplit();
                H.L("idddd" + child_id);
                break;
            }

        }
        else
        {
            n=1;
            child.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    String text = parent.getItemAtPosition(pos).toString();
                    JSONArray    jsonArray1 = new JSONArray();
                    for (int j = 0; j < childDetails1.size(); j++) {
                        JSONObject object = new JSONObject();
                        if (text.equalsIgnoreCase(childDetails1.get(j).getChild_name()))
                        {
                            child_id = childDetails1.get(j).getId();
                            H.L("idd"+childDetails1.get(j).getId());
                            Prefs.setChildID(context, child_id);
                            checkNetWorkComplit();
                            try {
                                object.put("child_id", childDetails1.get(j).getId());
                                object.put("child_name", childDetails1.get(j).getChild_name());
                                jsonArray1.put(0,object);
                                // Prefs.setChildDetails(context, jsonArray1.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            // break;
                        }
                        else {
                            try {
                                object.put("child_id", childDetails1.get(j).getId());
                                object.put("child_name", childDetails1.get(j).getChild_name());
                                jsonArray1.put(n,object);
                                n++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    }
                    Prefs.setChildDetails(context, jsonArray1.toString());
                    n=1;

                }
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.rvList.setLayoutManager(mLayoutManager);


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
        activitySendCheck.child_id=child_id;
        Call<ActivityComListingResponse> call = ApiClient.getRetrofit().create(Api.class).ACYIVITY_COM_LISTING_RESPONSE_CALL(activitySendCheck);
        call.enqueue(new Callback<ActivityComListingResponse>() {
            @Override
            public void onResponse(Call<ActivityComListingResponse> call, Response<ActivityComListingResponse> response) {
                H.L("responsennnn=" + new Gson().toJson(response.body()));
                binding.loading.setVisibility(View.GONE);
                ActivityComListingResponse response1= response.body();
                if (response1 !=null){
                if(response1.error.equals("false")){
                 //   H.T(context,response1.message);
                    binding.numOfActivityCompleted.setText(response1.total_activities_completed);
                    binding.numOfPointsEarned.setText(response1.total_points);

                    adapterListing = new AdapterListing(context,response1.result_data,child_id);
                    binding.rvList.setAdapter(adapterListing);
                }else {
                  //  H.T(context,response1.message);
                }
            }
}
            @Override
            public void onFailure(Call<ActivityComListingResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public interface activities{
        void ActivityPage(String activity_id,String child_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
      //  binding = null;
    }
}