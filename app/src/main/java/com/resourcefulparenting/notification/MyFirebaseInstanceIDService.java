package com.resourcefulparenting.notification;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.resourcefulparenting.util.H;
import com.resourcefulparenting.util.Prefs;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseInsIDService";

    @Override
    public void onTokenRefresh() {

        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        Prefs.setRegistrationTokenID(MyFirebaseInstanceIDService.this, refreshToken);
        //Log.e("TAG", "Token" + Prefs.getRegistrationTokenID(this));

    }
}
