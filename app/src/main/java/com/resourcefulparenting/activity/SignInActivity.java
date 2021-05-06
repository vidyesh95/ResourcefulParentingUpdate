package com.resourcefulparenting.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.gson.Gson;
import com.resourcefulparenting.R;

import com.resourcefulparenting.models.AddChild.ChildDetails;
import com.resourcefulparenting.models.Login.LoginCheck;
import com.resourcefulparenting.models.Login.LoginResponse;
import com.resourcefulparenting.models.Register.RegisterResponse;
import com.resourcefulparenting.models.Userdetails;
import com.resourcefulparenting.network.Api;
import com.resourcefulparenting.network.ApiClient;
import com.resourcefulparenting.util.CheckNetworkConnection;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    private Context context;
    private Button register, login;
    private TextView forgot_password;
    private TextInputLayout edt_email, edt_password;
    private String email, password, login_token;
    private SharedPreferences settings;
    private LoginButton login_button;
    //  private boolean isloggedin;
    final LoginCheck loginCheck = new LoginCheck();
    private RelativeLayout loading;
    final Userdetails userdetails = new Userdetails();
    private String uniqueID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_signin);
        context = this;
        Prefs.setCurrentActivity(context, Prefs.CurrentActivity.SIGNINACTIVITY);
        Uniqueid();
        ids();
        keyhash();

   /*     settings=getSharedPreferences("prefs",0);
        isloggedin=settings.getBoolean("firstRun",false);
        login_token = settings.getString("login_token", login_token);*/

        //if running for first time
     /*   if(isloggedin){
            userdetails.childDetails = new ArrayList<>();
            if(!userdetails.childDetails.isEmpty() || userdetails.childDetails != null){
                //Log.d("if", userdetails.childDetails.toString());
                Intent home_page = new Intent(context, MainActivity.class);
                startActivity(home_page);
            }else {
                //Log.d("else", userdetails.childDetails.toString());
                Intent a = new Intent(context, AddChildName.class);
                startActivity(a);
            }
        }*/
        register.setOnClickListener(v -> {
            Intent next = new Intent(context, RegisterActivity.class);
            startActivity(next);
            finish();
        });

        forgot_password.setOnClickListener(v -> {
            Intent next = new Intent(context, RetrivePassword.class);
            startActivity(next);
        });

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

                            email = object.getString("email").trim();


                            loginCheck.email = email;
                            loginCheck.password = "";
                            loginCheck.device_type = "Android";
                            loginCheck.device_unique_id = uniqueID;
                            loginCheck.login_type = "2";
                            LoginManager.getInstance().logOut();
                            checkNetWork();


                        } catch (Exception e) {
                            e.printStackTrace();
                            H.T(context, "Izinkan kami untuk menerima alamat email Anda dalam upaya Anda berikutnya");

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
        login.setOnClickListener(v -> {
            email = edt_email.getEditText().getText().toString();
            password = edt_password.getEditText().getText().toString();

            loginCheck.email = email;
            loginCheck.password = password;
            loginCheck.device_type = "Android";
            loginCheck.device_unique_id = uniqueID;
            loginCheck.login_type = "1";

            if (email.isEmpty()) {
                edt_email.setError("E-mail Required");
            } else if (!isValidEmail(email)) {
                edt_email.setError("Invalid E-mail");
            } else if (password.isEmpty()) {
                edt_password.setError("Password Required");
            } else {
                loading.setVisibility(View.VISIBLE);
                checkNetWork();

            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private void checkNetWork() {
        if (CheckNetworkConnection.getInstance(context).haveNetworkConnection()) {
            try {
                getLogin();
            } catch (Exception e) {
                //e.printStackTrace();();
            }
        } else {
            H.T(context, getString(R.string.no_internet_connection));
        }
    }


    private void getLogin() {
        loading.setVisibility(View.VISIBLE);
        Call<LoginResponse> call = ApiClient.getRetrofit().create(Api.class).Login(loginCheck);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                H.L("response=" + new Gson().toJson(response.body()));
                loading.setVisibility(View.GONE);
                LoginResponse loginResponse = response.body();
                //   if(email.equals(loginCheck.email) && password.equals(loginCheck.password)){
                if (loginResponse != null) {
                    if (loginResponse.error.equals("false")) {
                        Prefs.setLoginToken(context, loginResponse.login_token);
                        Prefs.setEmailID(context, email);
                        Prefs.setParantname(context, loginResponse.userdetails.parent_name);
                        LoginResponse.Userdetail result = loginResponse.userdetails;
                        //    H.T(context, result.parent_name);
                        if (result.result_data.isEmpty()) {
                            Intent home_page = new Intent(context, AddChildName.class);
                            startActivity(home_page);
                            finish();
                        } else {
                            JSONArray jsonArray1 = new JSONArray();
                            for (int i = 0; i < result.result_data.size(); i++) {
                                try {
                                    JSONObject object = new JSONObject();
                                    object.put("child_id", result.result_data.get(i).child_id);
                                    object.put("child_name", result.result_data.get(i).child_name);
                                    jsonArray1.put(object);
                                    //Log.d("data",jsonArray1.toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            Prefs.setChildDetails(context, jsonArray1.toString());
                            Intent home_page = new Intent(context, MainActivity.class);
                            startActivity(home_page);
                            finish();
                        }
                    } else {
                        H.T(context, loginResponse.message);
                    }
                } else {
                    Toast.makeText(SignInActivity.this, "Null Response", Toast.LENGTH_SHORT).show();
                }

            }
            //}

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(context, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ids() {
        login_button = findViewById(R.id.login_button);
        login_button.setReadPermissions("email");
        LoginManager.getInstance().logOut();
        register = findViewById(R.id.btn_register1);
        forgot_password = findViewById(R.id.tv_forgot_password);
        edt_email = findViewById(R.id.signin_edt_email);
        edt_password = findViewById(R.id.signin_edt_password);
        login = findViewById(R.id.btn_login);
        loading = findViewById(R.id.loading);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void keyhash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.resourcefulparenting", PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
