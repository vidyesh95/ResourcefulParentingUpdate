package com.resourcefulparenting.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class QueriesResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("queries")
    @Expose
    public ArrayList<Query> result_data = null;


    public class Query {


        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("question")
        @Expose
        public String question;

        public String points ;
        public boolean is_check = false;

    }

}
