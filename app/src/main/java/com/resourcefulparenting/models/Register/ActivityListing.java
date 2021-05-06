package com.resourcefulparenting.models.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resourcefulparenting.models.ActivityDetailsResponse;

import java.io.Serializable;
import java.util.ArrayList;

public class ActivityListing {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;


    @SerializedName("categoryinfo")
    @Expose
    public ActivityListing.Activityinfo activityinfo=null;

    @SerializedName("activities")
    @Expose
    public ArrayList<ActivityListing.Activities> result_data = null;

    public class Activities implements Serializable {
        @SerializedName("activity_id")
        @Expose
        public String activity_id;
        @SerializedName("activity_name")
        @Expose
        public String activity_name;
        @SerializedName("category_name")
        @Expose
        public String category_name;

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("activity_points")
        @Expose
        public String activity_points;
        @SerializedName("points_earned")
        @Expose
        public String points_earned;
        @SerializedName("iscompleted")
        @Expose
        public Boolean iscompleted;

    }
    public class Activityinfo implements Serializable {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("name")
        @Expose
        public String name;

        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("description")
        @Expose
        public String description;




    }

}
