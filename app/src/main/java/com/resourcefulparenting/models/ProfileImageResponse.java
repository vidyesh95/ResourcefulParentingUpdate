package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImageResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("uploadedimg")
    @Expose
    public String uploadedimg;
}
