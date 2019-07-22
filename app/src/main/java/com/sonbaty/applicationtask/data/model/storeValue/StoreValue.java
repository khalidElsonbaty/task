
package com.sonbaty.applicationtask.data.model.storeValue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StoreValue {

    @SerializedName("status")
    @Expose
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
