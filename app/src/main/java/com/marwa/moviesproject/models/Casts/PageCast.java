package com.marwa.moviesproject.models.Casts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PageCast {
    @SerializedName("cast")
    @Expose
    private List<ResultCast> cast = null;
    /*@SerializedName("crew")
    @Expose
    private List<Crew> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;*/

    public List<ResultCast> getCast() {
        return cast;
    }

    public void setCast(List<ResultCast> cast) {
        this.cast = cast;
    }

    /*public List<Crew> getCrew() {
        return crew;
    }

    public void setCrew(List<Crew> crew) {
        this.crew = crew;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }*/

}
