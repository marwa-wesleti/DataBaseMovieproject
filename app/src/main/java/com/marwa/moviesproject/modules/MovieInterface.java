package com.marwa.moviesproject.modules;

import com.marwa.moviesproject.models.PageCast;
import com.marwa.moviesproject.models.PageDetails;
import com.marwa.moviesproject.models.PageMovies;
import com.marwa.moviesproject.models.PageVideos;
import com.marwa.moviesproject.models.ProfilCast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {
    @GET("tv/popular")
    Call<PageMovies> getMovie(@Query("api_key") String key );

    @GET("tv/{tv_id}")
    Call<PageDetails> getDetails(@Path("tv_id") int id, @Query("api_key") String key);

    @GET("tv/{tv_id}/videos")
    Call<PageVideos> getVideos(@Path("tv_id") int id, @Query("api_key") String key);

    @GET("tv/{tv_id}/credits")
    Call<PageCast> getCasts(@Path("tv_id") int id, @Query("api_key") String key);

    @GET("person/{person_id}")
    Call<ProfilCast> getProfileCasts(@Path("person_id") int id, @Query("api_key") String key);




}
