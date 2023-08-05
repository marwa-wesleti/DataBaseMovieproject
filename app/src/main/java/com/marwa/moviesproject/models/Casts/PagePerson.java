package com.marwa.moviesproject.models.Casts;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.marwa.moviesproject.models.Home.ResultMovie;

import java.util.List;

public class PagePerson {
    @SerializedName("cast")
    @Expose
    private List<ResultMovie> cast = null;
    /*@SerializedName("crew")
    @Expose
    private List<Crew> crew = null;
    @SerializedName("id")
    @Expose
    private Integer id;*/

    public List<ResultMovie> getCast() {
        return cast;
    }

    public void setCast(List<ResultMovie> cast) {
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


