package com.resourcefulparenting.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.resourcefulparenting.R;
import com.resourcefulparenting.util.Prefs;

public class SplashScreen extends AppCompatActivity {
    public static int SPLASH_TIME_OUT = 400;
    private SharedPreferences settings;
    private boolean isloggedin;
    private String login_token;
    private Context context;
    private String launch_activity;
    private Intent mainIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE); // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        context = SplashScreen.this;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                launch_activity = Prefs.getCurrentActivity(context);
                if (launch_activity.equalsIgnoreCase(Prefs.CurrentActivity.SIGNINACTIVITY))
                {
                    mainIntent = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(mainIntent);
                }
              /*  else if (launch_activity.equalsIgnoreCase(Prefs.CurrentActivity.REGISTERACTIVITY))
                {
                    mainIntent = new Intent(SplashScreen.this, RegisterActivity.class);
                    startActivity(mainIntent);
                }*/
             /*   else if (launch_activity.equalsIgnoreCase(Prefs.CurrentActivity.ADDCHILDNAME))
                {
                    mainIntent = new Intent(SplashScreen.this, AddChildName.class);
                    startActivity(mainIntent);
                }*/
                else if (launch_activity.equalsIgnoreCase(Prefs.CurrentActivity.MAINACTIVITY))
                {
                    mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(mainIntent);
                }
                else {
                   mainIntent = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(mainIntent);
                }


             /*   if (Prefs.getLoginToken(context).equals("")) {
                    Intent a = new Intent(SplashScreen.this, SignInActivity.class);
                    startActivity(a);
                }else{
                    Intent a = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(a);
                }*/
            }
        },SPLASH_TIME_OUT);
    }
}