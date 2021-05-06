package com.resourcefulparenting.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.SignInActivity;
import com.resourcefulparenting.adapter.AdapterActivityListing;
import com.resourcefulparenting.adapter.Child_rv_Adapter;
import com.resourcefulparenting.databinding.FragmentMyAccountGeneralBinding;
import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.CommonResponse;
import com.resourcefulparenting.models.Login.LoginResponse;
import com.resourcefulparenting.models.LogoutCheck;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
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


public class MyAccountGeneralFragment extends Fragment {
    private FragmentMyAccountGeneralBinding binding;

    final LogoutCheck logoutCheck = new LogoutCheck();
    private Context context;
    private String login_token;
    private RelativeLayout loading;
    private Child_rv_Adapter adapter;

    private ArrayList<ChildDetails> childDetails1 = new ArrayList<>();


    public MyAccountGeneralFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMyAccountGeneralBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        context = container.getContext();

        assert getArguments() != null;
        login_token = getArguments().getString("login_token");

        logoutCheck.login_token = Prefs.getLoginToken(context);

        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView milestone =getActivity().findViewById(R.id.milestone_img);

        loading = view.findViewById(R.id.loading);
        milestone.setVisibility(View.GONE);


        TextView setParentsName =view.findViewById(R.id.account_set_parent_name_text);
        TextView change_password =view.findViewById(R.id.account_change_password_text);
        Button logout = view.findViewById(R.id.btn_logout);

        binding.tvAcParentName.setText(Prefs.getParantname(context));
        binding.tvAcEmailId.setText(Prefs.getEmailID(context));
        setParentsName.setOnClickListener(view1 -> ((InsideAccount)getActivity()).setName());
        change_password.setOnClickListener(view12 -> ((InsideAccount)getActivity()).changePassword());
        logout.setOnClickListener(view12 -> {
           loading.setVisibility(View.VISIBLE);
            getLogout();
        });

        binding.accountPolicyText.setOnClickListener(view13 -> ((InsideAccount)getActivity()).privacyPolicy());
        binding.accountFaq.setOnClickListener(view13 -> ((InsideAccount)getActivity()).FAQ());
        binding.accountTermsText.setOnClickListener(view12 -> ((InsideAccount)getActivity()).termsConditions());
        binding.accountAboutusText.setOnClickListener(view14 -> ((InsideAccount)getActivity()).aboutUs());
        try {
            childDetails1.clear();
            JSONArray jsonArray=new JSONArray(Prefs.getChildDetails(context));
            for (int i=0;i<jsonArray.length();i++)
            {
                ChildDetails childDetails=new ChildDetails();
                JSONObject object = jsonArray.getJSONObject(i);
                String id=object.getString("child_id");
                String name=object.getString("child_name");
                childDetails.setId(id);
                childDetails.setChild_name(name);
                childDetails1.add(childDetails);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        binding.rvChildList.setLayoutManager(mLayoutManager);
        adapter = new Child_rv_Adapter(childDetails1, context);
        binding.rvChildList.setAdapter(adapter);

   /*     binding.rvChildList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new Child_rv_Adapter(childDetails,details, context);
        binding.rvChildList.setAdapter(adapter);*/
    }

    private void getLogout() {
        Call<CommonResponse> call = ApiClient.getRetrofit().create(Api.class).Logout(logoutCheck);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                loading.setVisibility(View.GONE);
                CommonResponse response1= response.body();
                if(response1.error.equals("false")){

                    SharedPreferences preferences =getActivity().getSharedPreferences("loginPrefs",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Prefs.setLoginToken(context,"");
                    Prefs.setUniqueId(context,"");
                    Prefs.setChildDetails(context,"");
                    startActivity(new Intent(context, SignInActivity.class));
                    getActivity().finish();

                    H.T(context,response1.message);
                }else {
                    H.T(context,response1.message);
                }

             //   }
            }
            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface InsideAccount{
        void setName();
        void changePassword();
        void changeProfile(String child_id1);
        void privacyPolicy();
        void FAQ();
        void termsConditions();
        void aboutUs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}