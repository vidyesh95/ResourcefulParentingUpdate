package com.resourcefulparenting.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.LoggingBehavior;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.resourcefulparenting.R;
import com.resourcefulparenting.models.QueriesResponse;
import com.resourcefulparenting.models.Register.RegisterCheck;
import com.resourcefulparenting.models.Register.RegisterResponse;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;
import com.resourcefulparenting.util.Progress;

import org.json.JSONObject;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout loading;
    private LoginButton login_button;
    CallbackManager callbackManager;
    private Context context;
    private Button register;
    private TextInputLayout edt_parent_name, edt_mobile_no, edt_email_id, edt_password;
    private String parent_name,mobile_no,email_id,password, login_token;
    private SharedPreferences settings;
    private boolean isloggedin;
    private Progress progress;
    private ConstraintLayout mainLayout;
    String fcmtoken;

    final RegisterCheck registerCheck = new RegisterCheck();
    private String uniqueID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_register);
        context = RegisterActivity.this;
        // Prefs.setCurrentActivity(context, Prefs.CurrentActivity.SIGNINACTIVITY);
        bindview();
        Uniqueid();



    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent back = new Intent(context, SignInActivity.class);
        startActivity(back);
        finish();
    }

    private void bindview() {
        mainLayout = findViewById(R.id.mainLayout);
        progress = new Progress(this, context, mainLayout);
        login_button = findViewById(R.id.login_button);
        login_button.setReadPermissions("email");
        LoginManager.getInstance().logOut();
        loading = findViewById(R.id.loading);
        register = findViewById(R.id.btn_register);
        edt_parent_name = findViewById(R.id.edt_parent_name);
        edt_mobile_no = findViewById(R.id.edt_parent_no);
        edt_email_id = findViewById(R.id.edt_register_email);
        edt_password = findViewById(R.id.edt_register_password);
        register.setOnClickListener(this);
        try {
            fcmtoken = FirebaseInstanceId.getInstance().getToken();
            Log.d("fcm",fcmtoken);
           Prefs.setRegistrationTokenID(this,fcmtoken);

        } catch (Exception e) {
            e.printStackTrace();
        }

        login_button.setLoginBehavior(LoginBehavior.WEB_ONLY);
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //   etpassword.setVisibility(View.GONE);
                //  etrepassword.setVisibility(View.GONE);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {



                        try {
                            FacebookSdk.setIsDebugEnabled(true);
                            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
                            System.out.println("AccessToken.getCurrentAccessToken()" + AccessToken
                                    .getCurrentAccessToken().toString());
                        } catch (Exception e) {
                            //e.printStackTrace();StackTrace();
                        }
                        //Log.e("Json data", object.toString());
                        try {

                            String firstname = object.getString("first_name").trim();
                            String lastname = object.getString("last_name").trim();
                            String email_id=object.getString("email").trim();
                            parent_name = firstname + " "+ lastname;


                            registerCheck.parentName = parent_name;
                            registerCheck.mobileNo = "";
                            registerCheck.email = email_id;
                            registerCheck.password = "";
                            registerCheck.deviceType = "Android";
                            registerCheck.deviceUniqueId =uniqueID;
                            registerCheck.loginType = "2";
                            LoginManager.getInstance().logOut();
                            checkNetWork();


                        } catch (Exception e) {
                            e.printStackTrace();
                            H.T(context,"Izinkan kami untuk menerima alamat email Anda dalam upaya Anda berikutnya");
                        }


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "first_name, last_name, email"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

            }


            @Override
            public void onCancel() {
                H.T(context, "Login Cancel");
                H.L("hhhhhqqq");

            }

            @Override
            public void onError(FacebookException error) {

                H.L("hhhhh");

            }
        });


    }

    public void Uniqueid() {
        if (uniqueID.equalsIgnoreCase(Prefs.getUniqueId(context))) {
            uniqueID = UUID.randomUUID().toString();
            Prefs.setUniqueId(context, uniqueID);
        } else {
            uniqueID = Prefs.getUniqueId(context);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                validation();
                break;

        }
    }

    private void validation() {
        parent_name = edt_parent_name.getEditText().getText().toString();
        mobile_no = edt_mobile_no.getEditText().getText().toString();
        email_id = edt_email_id.getEditText().getText().toString();
        password = edt_password.getEditText().getText().toString();

        registerCheck.parentName = parent_name;
        registerCheck.mobileNo = mobile_no;
        registerCheck.email = email_id;
        registerCheck.password = password;
        registerCheck.deviceType = "Android";
        registerCheck.deviceUniqueId =uniqueID;
        registerCheck.loginType = "1";

        if(parent_name.isEmpty()){
            edt_parent_name.setError("Parent Name Required");
        }else if(email_id.isEmpty()){
            edt_email_id.setError("E-mail Required");
        }else if (!isValidEmail(email_id)) {
            edt_email_id.setError("Invalid E-mail");
        }
        else if (password.isEmpty()) {
            edt_password.setError("Password Required");
        } else {
            checkNetWork();
        }
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                registercall();
            } catch (Exception e) {
                //e.printStackTrace();();
            }
        } else {
            H.T(context, getString(R.string.no_internet_connection));
        }
    }


    private void registercall() {
        try {
            loading.setVisibility(View.VISIBLE);

            Call<RegisterResponse> call = ApiClient.getRetrofit().create(Api.class).registerParent(registerCheck);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    //  //Log.d("response", String.valueOf(response.body()));
                    H.L("response=" + new Gson().toJson(response.body()));
                    loading.setVisibility(View.GONE);

                    RegisterResponse register = response.body();
                    if (register != null) {
                        if (register.error.equals("false"))
                        {
                            H.T(context,register.message);
                            Prefs.setLoginToken(context, register.login_token);
                            Prefs.setEmailID(context, email_id);
                            Prefs.setParantname(context, parent_name);
                            Intent home_page = new Intent(context, AddChildName.class);
                            startActivity(home_page);
                            finish();
                        }
                        else {
                            H.T(context,register.message);
                        }


/*
                        if (!isloggedin) {
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putBoolean("firstRun", true);
                            editor.putString("login_token", response.body().login_token);
                            editor.apply();

                            Intent home_page = new Intent(context, AddChildName.class);
                            home_page.putExtra("login_token", response.body().login_token);
                            startActivity(home_page);
                            finish();
                        }
*/
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    progress.hideProgressBar();
                    H.L("Error=" + t.getLocalizedMessage());
//                    H.TL(context, "Error=" + t.getMessage());
                }
            });



        } catch (Exception e)
        {

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}
