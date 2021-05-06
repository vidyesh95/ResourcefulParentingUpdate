package com.resourcefulparenting.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CheckNetworkConnection {

    private static Context context;
    private static CheckNetworkConnection instance = new CheckNetworkConnection();
    boolean connected;

    public static CheckNetworkConnection getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }


    public boolean haveNetworkConnection() {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnectedOrConnecting();
            return connected;
        } catch (Exception ignored) {
        }
        return connected;
    }


}
