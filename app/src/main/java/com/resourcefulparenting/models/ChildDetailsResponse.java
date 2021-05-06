package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resourcefulparenting.models.Register.ActivityListing;

import java.io.Serializable;

public class ChildDetailsResponse {
    @SerializedName("status")
    @Expose
    public Integer status;
    @SerializedName("error")
    @Expose
    public String error;
    @SerializedName("childinfo")
    @Expose
    public ChildDetailsResponse.Childinfo childinfo=null;

    public class Childinfo implements Serializable {

        @SerializedName("profile_pic")
        @Expose
        public String profile_pic;
        @SerializedName("child_name")
        @Expose
        public String child_name;
        @SerializedName("height")
        @Expose
        public String height;
        @SerializedName("weight")
        @Expose
        public String weight;
        @SerializedName("child_birth_date")
        @Expose
        public String child_birth_date;
        @SerializedName("child_birth_month")
        @Expose
        public String child_birth_month;
        @SerializedName("child_birth_year")
        @Expose
        public String child_birth_year;

    }


}
