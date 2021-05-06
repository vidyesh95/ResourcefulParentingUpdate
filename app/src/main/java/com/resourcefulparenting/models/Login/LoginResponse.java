package com.resourcefulparenting.models.Login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.resourcefulparenting.models.QueriesResponse;
import com.resourcefulparenting.models.Userdetails;

import java.io.Serializable;
import java.io.SerializablePermission;
import java.util.ArrayList;

public class LoginResponse {


    @SerializedName("status")
    @Expose
    public Integer status;

    @SerializedName("error")
    @Expose
    public String error;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("login_token")
    @Expose
    public String login_token;

    @SerializedName("userdetails")
    @Expose
    public Userdetail userdetails=null;

    public class Userdetail implements Serializable{
        @SerializedName("parent_name")
        @Expose
        public String parent_name;
        @SerializedName("user_id")
        @Expose
        public String user_id;

        @SerializedName("childDetails")
        @Expose
        public ArrayList<Userdetail.ChildDetails> result_data = null;

        public class ChildDetails
        {
            @SerializedName("child_id")
            @Expose
            public String child_id;

            @SerializedName("child_name")
            @Expose
            public String child_name;
        }
    }


}
