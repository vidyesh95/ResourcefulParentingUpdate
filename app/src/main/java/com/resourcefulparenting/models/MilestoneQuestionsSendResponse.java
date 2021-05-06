package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class MilestoneQuestionsSendResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("message")
    @Expose
    public String message;


}
