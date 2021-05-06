package com.resourcefulparenting.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.databinding.FragmentChangePasswordBinding;
import com.resourcefulparenting.models.AddChild.GarphpointDetails;
import com.resourcefulparenting.models.ChangePasswordCheck;
import com.resourcefulparenting.models.CommonResponse;
import com.resourcefulparenting.models.TodayAcyivityResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private FragmentChangePasswordBinding binding;

    private Context context;
    private TextInputLayout oldPassword, newPassword, confirmPassword;
    private String old_password, new_password, confirm_password, login_token;
    private Button btn_change_password_submit;
    private RelativeLayout loading;
    final ChangePasswordCheck changePasswordCheck = new ChangePasswordCheck();

    public ChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        context = container.getContext();
        oldPassword = view.findViewById(R.id.edt_old_password);
        newPassword = view.findViewById(R.id.edt_new_password);
        confirmPassword = view.findViewById(R.id.edt_confirm_password);
        loading = view.findViewById(R.id.loading);
        btn_change_password_submit = view.findViewById(R.id.btn_password_change_submit);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

         btn_change_password_submit.setOnClickListener(view1 -> {
             old_password = oldPassword.getEditText().getText().toString();
             new_password = newPassword.getEditText().getText().toString();
             confirm_password = confirmPassword.getEditText().getText().toString();

             changePasswordCheck.login_token = Prefs.getLoginToken(getActivity());
             changePasswordCheck.old_password = old_password;
             changePasswordCheck.new_password = new_password;

             if(old_password.isEmpty()){
                 oldPassword.setError("Required");
             }else if(new_password.isEmpty()){
                 newPassword.setError("Required");
             }else if(confirm_password.isEmpty()){
                 confirmPassword.setError("Required");
             }else if(!new_password.equals(confirm_password)){
                 confirmPassword.setError("Password does not match to above password");
             }else {
                 oldPassword.setError(null);
                 newPassword.setError(null);
                 confirmPassword.setError(null);
                 loading.setVisibility(View.VISIBLE);
                 getChangePassword();
             }
         });
    }

    private void getChangePassword() {
        Call<CommonResponse> call = ApiClient.getRetrofit().create(Api.class).ChangePassword(changePasswordCheck);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                H.L("response=" + new Gson().toJson(response.body()));
                loading.setVisibility(View.GONE);
                CommonResponse response1= response.body();
                if(response1.error.equals("false")){
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
        binding = null;
    }
}