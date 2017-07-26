package com.example.android.wassupworld.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by dell on 7/2/2017.
 */

public class ResultSources {
    @SerializedName("sources")
    private List<Sources> sources;
    @SerializedName("status")
    private String status;

    public ResultSources(List<Sources> sources, String status) {
        this.sources = sources;
        this.status = status;
    }

    public List<Sources> getSources() {
        return sources;
    }

    public void setSources(List<Sources> sources) {
        this.sources = sources;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
