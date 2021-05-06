package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;

public class TodayAcyivityResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("total_points")
    @Expose
    public String total_points;
    @SerializedName("badges")
    @Expose
    public String badges;
    @SerializedName("total_activities_completed")
    @Expose
    public String total_activities_completed;
    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("milestone")
    @Expose
    public Boolean milestone;


    @SerializedName("activities")
    @Expose
    public  ActivitiesDetails activitiesDetails=null;

    @SerializedName("activities_imgs")
    @Expose
    public ArrayList activities_imgs =null;
    @SerializedName("graph_point")
    @Expose
    public HashMap graph_point =null;

    public class ActivitiesDetails implements Serializable {

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

        @SerializedName("category_id")
        @Expose
        public String category_id;

        @SerializedName("activity_point")
        @Expose
        public String activity_point;

        @SerializedName("category_name")
        @Expose
        public String category_name;
        @SerializedName("iscompleted")
        @Expose
        public boolean iscompleted;
        @SerializedName("isalarmset")
        @Expose
        public Boolean isalarmset;
        @SerializedName("child_video")
        @Expose
        public Boolean child_video;
    }



}
