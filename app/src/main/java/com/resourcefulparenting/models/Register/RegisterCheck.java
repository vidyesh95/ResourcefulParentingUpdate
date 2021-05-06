package com.resourcefulparenting.models.Register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterCheck {
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("device_unique_id")
        @Expose
        public String deviceUniqueId;
        @SerializedName("device_type")
        @Expose
        public String deviceType;
        @SerializedName("parent_name")
        @Expose
        public String parentName;
        @SerializedName("mobile no")
        @Expose
        public String mobileNo;
        @SerializedName("login_type")
        @Expose
        public String loginType;
}
