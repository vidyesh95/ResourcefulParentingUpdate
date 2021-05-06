package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityDetailsResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("uploadedimg")
    @Expose
    public ArrayList activities_imgs =null;

    @SerializedName("activityinfo")
    @Expose
    public  Activityinfo activityinfo=null;


    public class Activityinfo implements Serializable {

        @SerializedName("activity_id")
        @Expose
        public String activity_id;
        @SerializedName("activity_description")
        @Expose
        public String activity_description;
        @SerializedName("activity_name")
        @Expose
        public String activity_name;
        @SerializedName("activity_learning")
        @Expose
        public String activity_learning;

        @SerializedName("activity_point")
        @Expose
        public String activity_point;


        @SerializedName("category_name")
        @Expose
        public String category_name;
        @SerializedName("iscompleted")
        @Expose
        public Boolean iscompleted;
        @SerializedName("isalarmset")
        @Expose
        public Boolean isalarmset;
        @SerializedName("child_video")
        @Expose
        public Boolean child_video;
        @SerializedName("activity_image")
        @Expose
        public String activity_image;
        @SerializedName("category_description")
        @Expose
        public String category_description;
    }



}
