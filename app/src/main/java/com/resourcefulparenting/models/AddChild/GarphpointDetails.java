

package com.resourcefulparenting.models.AddChild;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GarphpointDetails implements Serializable {

    @SerializedName("id")
    private String id;

    @SerializedName("value")
    private float value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}
