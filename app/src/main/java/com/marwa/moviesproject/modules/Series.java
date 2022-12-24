package com.marwa.moviesproject.modules;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.PageCast;
import com.marwa.moviesproject.models.PageDetails;
import com.marwa.moviesproject.models.PageVideos;
import com.marwa.moviesproject.models.ResultCast;
import com.marwa.moviesproject.models.ResultVideos;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Series extends YouTubeBaseActivity implements Adaptermovie.OnItemSelectedListener {

    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.overview)
    TextView overview;
    @BindView(R.id.date)
    TextView date;
    @BindView(R.id.genre)
    TextView genre;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.rating)
    TextView rating;
    @BindView(R.id.synopsis)
    TextView synopsis;
    @BindView(R.id.trailer)
    YouTubePlayerView trailer;
    @BindView(R.id.releasedate)
            TextView releasedate;
    @BindView(R.id.gender)
            TextView gender;
    @BindView(R.id.recycler)
           RecyclerView recycler;
    @BindView(R.id.cast)
            TextView actor;
    @BindView(R.id.watch)
        LinearLayout watch;

    AdapterCast adapterCast;

    List<ResultCast> listCasts;
    YouTubePlayer.OnInitializedListener onInitialazedListener;
    String baseUrl = "https://api.themoviedb.org/3/", KeyImage = "https://image.tmdb.org/t/p/w500";
    String key = "61ed6ee7adb8cc190be7e795b5588f32",KeyYoutube="AIzaSyBpEh9Yux9bGw3tCM3eBAH8qJ8WUSTV2jg";
    Integer id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_details);
        ButterKnife.bind(this);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        MovieInterface movieInterface = retrofit.create(MovieInterface.class);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        Log.d("idi", "id est" + id);
        Call<PageDetails> call = movieInterface.getDetails(id, key);
        fetchdetails(call);
        Call<PageVideos> call1=movieInterface.getVideos(id,key);
        fetchTrailer(call1);

        Call<PageCast> call2=movieInterface.getCasts(id,key);
        fetchCast(call2);
        }








    private void fetchdetails(Call<PageDetails> call){
        call.enqueue(new Callback<PageDetails>() {
            @Override
            public void onResponse(Call<PageDetails> call, Response<PageDetails> response) {
                Log.d("name", "name is :" + response.body().getName());

                Glide.with(Series.this)
                        .load(KeyImage + response.body().getBackdropPath())
                        .placeholder(R.drawable.prograss_bar)
                        .centerCrop()
                        .into(background);
                               

                name.setText(response.body().getName());
                if (response.body().getOverview().length() > 2)
                    overview.setText(response.body().getOverview());
                else {
                    synopsis.setVisibility(View.INVISIBLE);

                }
                if(response.body().getFirstAirDate()!="")
                    date.setText(response.body().getFirstAirDate());
                else
                    releasedate.setVisibility(View.INVISIBLE);
                if(response.body().getGenres().size()>=1) {
                    String g = response.body().getGenres().get(0).getName();
                    for (int i = 1; i < response.body().getGenres().size(); i++) {
                        g += "  ,  " + response.body().getGenres().get(i).getName();
                    }
                    genre.setText(g);
                }
                else
                    gender.setVisibility(View.INVISIBLE);
                if (response.body().getEpisodeRunTime().size() != 0)
                    time.setText(response.body().getEpisodeRunTime().get(0).toString()+ " minutes");
                else
                    time.setVisibility(View.INVISIBLE);
                rating.setText("" + Math.round(response.body().getVoteAverage()));

                if (response.body().getNetworks().size()>=1){
                    Button btn=new Button(getApplicationContext()  );
                    btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    btn.setText(response.body().getNetworks().get(0).getName());
                    watch.addView(btn);
                    for (int i=1;i<response.body().getNetworks().size();i++){
                        Button btn1=new Button(getApplicationContext()  );
                        btn1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        btn1.setText(response.body().getNetworks().get(i).getName());
                        watch.addView(btn1);
                    }


                }






            }

            @Override
            public void onFailure(Call<PageDetails> call, Throwable t) {
                Toast.makeText(Series.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();

            }
        });
    }




    private void fetchTrailer(Call<PageVideos> call){
        call.enqueue(new Callback<PageVideos>() {
            @Override
            public void onResponse(Call<PageVideos> call, Response<PageVideos> response) {

                onInitialazedListener=new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                        String keyimg = response.body().getResults().get(0).getKey();
                        youTubePlayer.loadVideo(keyimg);

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(Series.this,"Faileur", Toast.LENGTH_SHORT).show();
                    }
                };
                if(response.body().getResults().size()>1) {
                    trailer.initialize("AIzaSyCti_gXCbRjHPSqvhw8AltvXILWgSTcnkk", onInitialazedListener);
                    background.setVisibility(View.INVISIBLE);
                }
                else{
                    trailer.setVisibility(View.INVISIBLE);

                }





            }

            @Override
            public void onFailure(Call<PageVideos> call, Throwable t) {
                Toast.makeText(Series.this, t.getMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });

    }




    private void fetchCast(Call<PageCast> call ){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        listCasts=new ArrayList<>();
        call.enqueue(new Callback<PageCast>() {
            @Override
            public void onResponse(Call<PageCast> call, Response<PageCast> response) {
                List<ResultCast> cast=response.body().getCast();

                for (ResultCast cast1:cast){
                    listCasts.add(cast1);


               }
                if (listCasts.get(0).getProfilePath()!=null) {
                    Log.d("size",""+listCasts.size());
                  actor.setText("ACTOR");
                    recycler.setLayoutManager(layoutManager);
                    adapterCast = new AdapterCast(getApplicationContext(), listCasts, (Adaptermovie.OnItemSelectedListener) Series.this);
                    recycler.setAdapter(adapterCast);

                }
                else actor.setText("");


            }

            @Override
            public void onFailure(Call<PageCast> call, Throwable t) {

            }
        });
    }


    @Override
    public void itemClick(int position) {
        Intent intenttoCast= new Intent(getApplicationContext(), Actor.class);
        intenttoCast.putExtra("id",listCasts.get(position).getId());
        startActivity(intenttoCast);

    }


}
