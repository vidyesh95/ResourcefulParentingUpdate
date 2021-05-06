package com.resourcefulparenting.models.AddChild;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Queries {

  //  List<Data> data = new ArrayList<>();

    @SerializedName("id1")
    @Expose
    private int id;
    @SerializedName("id2")
    @Expose
    private String points;

    public Queries(int id, String points) {
        this.id = id;
        this.points = points;
    }


}
