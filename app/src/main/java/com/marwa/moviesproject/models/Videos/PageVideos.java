package com.marwa.moviesproject.models.Videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageVideos {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<ResultVideos> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResultVideos> getResults() {
        return results;
    }

    public void setResults(List<ResultVideos> results) {
        this.results = results;
    }
}
