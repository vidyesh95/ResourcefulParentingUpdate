

package com.resourcefulparenting.models.AddChild;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChildDetails implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("child_name")
    private String child_name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }
}
