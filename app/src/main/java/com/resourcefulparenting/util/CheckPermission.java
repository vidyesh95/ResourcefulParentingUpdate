package com.resourcefulparenting.util;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class CheckPermission {

    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int CALL_PERMISSION_REQUEST_CODE = 3;
    public static final int READ_SMS_PERMISSION_REQUEST_CODE = 6;
    private Activity context;

    public CheckPermission(Activity context) {
        this.context = context;
    }

    public static boolean checkDeviceOS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


    public boolean checkStoragePermission() {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissionForStorage();
            return false;
        }
    }

    private void requestPermissionForStorage() {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                STORAGE_PERMISSION_REQUEST_CODE);
    }

    public boolean checkCameraPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        int result2 = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissionForCamera();
            return false;
        }
    }

    private void requestPermissionForCamera() {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_REQUEST_CODE);
    }

    private void requestPermissionForCall() {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
    }

    public boolean checkContactsPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissionForCall();
            return false;
        }
    }

    public boolean checkReadSmsPermission() {
        int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS);
        if (result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissionForReadSms();
            return false;
        }
    }

    private void requestPermissionForReadSms() {
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST_CODE);
    }
}