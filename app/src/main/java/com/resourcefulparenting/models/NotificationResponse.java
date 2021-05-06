package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class NotificationResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;


}
