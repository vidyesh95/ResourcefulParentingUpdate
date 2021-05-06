package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityComListingResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("total_points")
    @Expose
    public String total_points;
    @SerializedName("total_activities_completed")
    @Expose
    public String total_activities_completed;

    @SerializedName("activities")
    @Expose
    public ArrayList<ActivityComListingResponse.Activities> result_data = null;

    public class Activities implements Serializable {
        @SerializedName("activity_id")
        @Expose
        public String activity_id;
        @SerializedName("activity_name")
        @Expose
        public String activity_name;
        @SerializedName("activity_point")
        @Expose
        public String activity_point;
        @SerializedName("category_name")
        @Expose
        public String category_name;
        @SerializedName("Language")
        @Expose
        public String Language;
        @SerializedName("activity_image")
        @Expose
        public String activity_image;

    }
}
