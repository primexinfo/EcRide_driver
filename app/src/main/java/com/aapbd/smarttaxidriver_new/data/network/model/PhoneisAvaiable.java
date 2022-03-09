package com.aapbd.smarttaxidriver_new.data.network.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhoneisAvaiable {

    @SerializedName("isAvaiable")
    @Expose
    private Boolean isAvaiable;

    public Boolean getIsAvaiable() {
        return isAvaiable;
    }

    public void setIsAvaiable(Boolean isAvaiable) {
        this.isAvaiable = isAvaiable;
    }

}