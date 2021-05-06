package com.resourcefulparenting.util;

import android.content.Context;
import android.content.Intent;

import com.resourcefulparenting.activity.SignInActivity;



public class Logout {


    public static void L(final Context context)
    {
        try
        {
                   H.T(context, "Login token is missing or mismatched.");
                   logout(context);
        } catch (Exception e) {
            //e.printStackTrace();();
        }

    }

    public static void logout(Context context) {

        Intent a = new Intent(context, SignInActivity.class);
        context.startActivity(a);

        Prefs.setLoginToken(context, "");
        Prefs.setCategory_id(context,"");
        Prefs.setChildID(context,"");
        Prefs.setChildDetails(context,"");
        Prefs.setUniqueId(context,"");


    }


}
