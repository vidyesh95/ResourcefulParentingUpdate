package com.resourcefulparenting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.resourcefulparenting.R;
import com.resourcefulparenting.activity.SignInActivity;
import com.resourcefulparenting.databinding.ActivityRetrivePasswordBinding;
import com.resourcefulparenting.models.ForgotPasswordCheck;
import com.resourcefulparenting.models.CommonResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrivePassword extends AppCompatActivity {
    private ActivityRetrivePasswordBinding retrivePasswordBinding;

    private TextInputLayout edt_email_id;
    private Button retrive;
    private Context context;
    private String emailid;
    final ForgotPasswordCheck forgotPasswordCheck = new ForgotPasswordCheck();
    private RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrivePasswordBinding = ActivityRetrivePasswordBinding.inflate(getLayoutInflater());
        View view = retrivePasswordBinding.getRoot();
        setContentView(view);
        context = this;
        ids();

        retrive.setOnClickListener(v -> {
            emailid = edt_email_id.getEditText().getText().toString();
            forgotPasswordCheck.email = emailid;
            if(emailid.isEmpty()){
                edt_email_id.setError("Alamat email wajib diisi");
            }
            else if (!isValidEmail(emailid)) {
                edt_email_id.setError("Masukkan email yang valid");
            }else{

                getForgotPassword();
            }
        });


    }

    private void getForgotPassword() {
        loading.setVisibility(View.VISIBLE);
        Call<CommonResponse> call = ApiClient.getRetrofit().create(Api.class).ForgotPassword(forgotPasswordCheck);
        call.enqueue(new Callback<CommonResponse>() {
            @Override
            public void onResponse(Call<CommonResponse> call, Response<CommonResponse> response) {
                loading.setVisibility(View.GONE);
                if(emailid.equals(forgotPasswordCheck.email)){
                    Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(context, SignInActivity.class));

                }else {
                    Toast.makeText(context, response.body().message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommonResponse> call, Throwable t) {
                loading.setVisibility(View.GONE);
             //   Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }



    private void ids() {
        edt_email_id = findViewById(R.id.edt_recover_email_id);
        retrive = findViewById(R.id.btn_retrive_password);
        loading = findViewById(R.id.loading);
    }


}