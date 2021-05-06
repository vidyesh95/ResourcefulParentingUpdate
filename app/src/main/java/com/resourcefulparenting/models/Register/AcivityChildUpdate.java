package com.resourcefulparenting.models.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AcivityChildUpdate {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("message")
    @Expose
    public String message;



}
