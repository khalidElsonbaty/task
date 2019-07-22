
package com.sonbaty.applicationtask.data.model.getValues;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetValues {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<GetValuesData> data = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GetValuesData> getData() {
        return data;
    }

    public void setData(List<GetValuesData> data) {
        this.data = data;
    }

}
