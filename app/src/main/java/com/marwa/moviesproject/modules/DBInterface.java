package com.marwa.moviesproject.modules;

import com.marwa.moviesproject.models.Casts.PageCast;
import com.marwa.moviesproject.models.Casts.PagePerson;
import com.marwa.moviesproject.models.Home.PageDetails;
import com.marwa.moviesproject.models.Home.PageMovies;
import com.marwa.moviesproject.models.Videos.PageVideos;
import com.marwa.moviesproject.models.Casts.ProfilCast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DBInterface {
    @GET("trending/all/day")
    Call<PageMovies> getTrending(@Query("api_key") String key );


// TV
    @GET("tv/{tv_id}")
    Call<PageDetails> getDetailsTV(@Path("tv_id") int id, @Query("api_key") String key);
    @GET("tv/{tv_id}/videos")
    Call<PageVideos> getVideosTV(@Path("tv_id") int id, @Query("api_key") String key);
    @GET("tv/{tv_id}/credits")
    Call<PageCast> getCastsTV(@Path("tv_id") int id, @Query("api_key") String key);

    @GET("tv/popular")
    Call<PageMovies> getUpPopularTV(@Query("api_key") String key);

    @GET("tv/{tv_id}/recommendations")
    Call<PageMovies> getRecommandTv(@Path("tv_id") int id, @Query("api_key") String key);


 // Movie
    @GET("movie/{mv_id}")
    Call<PageDetails> getDetailsMovie(@Path("mv_id") int id, @Query("api_key") String key);

    @GET("movie/{mv_id}/videos")
    Call<PageVideos> getVideosMovie(@Path("mv_id") int id, @Query("api_key") String key);

    @GET("movie/{mv_id}/credits")
    Call<PageCast> getCastsMovie(@Path("mv_id") int id, @Query("api_key") String key);

    @GET("movie/popular")
    Call<PageMovies> getUpPopularMovie(@Query("api_key") String key);

    @GET("movie/{mv_id}/recommendations")
    Call<PageMovies> getRecommandMovie(@Path("mv_id") int id, @Query("api_key") String key);

    // Person
    @GET("person/{person_id}")
    Call<ProfilCast> getProfileCasts(@Path("person_id") int id, @Query("api_key") String key);

    @GET("person/{person_id}/combined_credits")
    Call<PagePerson> getCreditPerson(@Path("person_id") int id, @Query("api_key") String key);






}
