package com.resourcefulparenting.models.AddChild;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resourcefulparenting.models.TodayAcyivityResponse;

import java.io.Serializable;

public class AddChildResponse {
    public int status;
    public String error;
    public String message;


    @SerializedName("child_details")
    @Expose
    public ChildDetails  childDetails=null;


    public class ChildDetails implements Serializable
    {
        @SerializedName("id")
        @Expose
        public String id;

    }
}
