package com.resourcefulparenting.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Prefs {

    private static final String LOGIN_TOKEN = "login_token";
    private static final String PREF_CURREN_ACTIVITY = "current_activity";
    private static final String UNIQUE_ID = "unique_id";

    private static final String Child = "child";
    private static final String Category_id = "category_id";
    private static final String ChildID = "child_id";
    private static final String PARANTNAME = "parentname";
    private static final String EMAILID = "emailid";
    private static final String PREF_REGISTRATION_TOKEN_ID = "registration_token_id";
    public static void setRegistrationTokenID(Context ctx, String RegistrationTokenID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_REGISTRATION_TOKEN_ID, RegistrationTokenID);
        editor.apply();
    }

    public static String getRegistrationTokenID(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_REGISTRATION_TOKEN_ID, "");
    }

    public static void setCurrentActivity(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_CURREN_ACTIVITY, str);
        editor.apply();
    }

    public static String getCurrentActivity(Context ctx) {
        return getSharedPreferences(ctx).getString(PREF_CURREN_ACTIVITY, "");
    }

    public static class CurrentActivity {
        public static final String ADDCHILDAGE = "AddChildAge";
        public static final String ADDCHILDNAME = "AddChildName";
        public static final String ADDCHILDQUESTIONS = "AddChildQuestions";
        public static final String SIGNINACTIVITY = "SignInActivity";
        public static final String REGISTERACTIVITY = "RegisterActivity";
        public static final String MAINACTIVITY = "MainActivity";
    }


    public static void setCategory_id(Context ctx, String Activity_id1) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Category_id, Activity_id1);
        editor.commit();
    }

    public static String getCategory_id(Context ctx) {
        return getSharedPreferences(ctx).getString(Category_id, "");
    }
    public static void setChildID(Context ctx, String ChildID1 ) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(ChildID, ChildID1);
        editor.commit();
    }

    public static String getChildID(Context ctx) {
        return getSharedPreferences(ctx).getString(ChildID, "");
    }



    public static void setParantname(Context ctx, String pname ) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PARANTNAME, pname);
        editor.commit();
    }

    public static String getParantname(Context ctx) {
        return getSharedPreferences(ctx).getString(PARANTNAME, "");
    }


    public static void setEmailID(Context ctx, String email1 ) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAILID, email1);
        editor.commit();
    }

    public static String getEmailID(Context ctx) {
        return getSharedPreferences(ctx).getString(EMAILID, "");
    }


    public static void setChildDetails(Context ctx, String share) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(Child, share);
        editor.commit();
    }

    public static String getChildDetails(Context ctx) {
        return getSharedPreferences(ctx).getString(Child, "");
    }



    public static void setUniqueId(Context ctx, String string) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(UNIQUE_ID, string);
        editor.apply();
    }

    public static String getUniqueId(Context ctx) {
        return getSharedPreferences(ctx).getString(UNIQUE_ID, "");
    }








    public static void setLoginToken(Context ctx, String str) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LOGIN_TOKEN, str);
        editor.apply();
    }

    public static String getLoginToken(Context ctx) {
        return getSharedPreferences(ctx).getString(LOGIN_TOKEN, "");
    }




    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }






}