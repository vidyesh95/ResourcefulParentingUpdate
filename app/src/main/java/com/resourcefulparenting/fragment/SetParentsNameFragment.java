package com.resourcefulparenting.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.databinding.FragmentSetParentsNameBinding;
import com.resourcefulparenting.models.CommonResponse;
import com.resourcefulparenting.models.SetParentNameCheck;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetParentsNameFragment extends Fragment {
    private FragmentSetParentsNameBinding setParentsNameBinding;

    private String  parents_Name;
    final SetParentNameCheck setParentNameCheck = new SetParentNameCheck();
    private Context context;

    public SetParentsNameFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setParentsNameBinding = FragmentSetParentsNameBinding.inflate(inflater, container,false);
        View view = setParentsNameBinding.getRoot();

        context = container.getContext();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setParentsNameBinding.btnSetParentNameSubmit.setOnClickListener(view1 -> {

            parents_Name = setParentsNameBinding.edtSetParentName.getEditText().getText().toString();

            setParentNameCheck.login_token = Prefs.getLoginToken(context);
            setParentNameCheck.parent_name = parents_Name;

            if(parents_Name.isEmpty()){
                setParentsNameBinding.edtSetParentName.setError("Nama orang tua wajib diisi");
            }else{
                setParentsNameBinding.edtSetParentName.setError(null);

                checkNetWork();
            }
        });
    }
    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getParentName();
            } catch (Exception e) {
                //e.printStackTrace();();
            }
        } else {
            H.T(context, getString(R.string.no_internet_connection));
        }
    }


    private void getParentName() {
        setParentsNameBinding.loading.setVisibility(View.VISIBLE);
        Call<CommonResponse> call = ApiClient.getRetrofit().create(Api.class).ChangeParentName(setParentNameCheck);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                CommonResponse response1= response.body();
                if(response1.error.equals("false")){
                   setParentsNameBinding.loading.setVisibility(View.GONE);
                    Prefs.setParantname(context, parents_Name);

                    getActivity().getSupportFragmentManager().popBackStack();
                    H.T(context,response1.message);
                }else {
                    H.T(context,response1.message);
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        setParentsNameBinding = null;
    }
}