package com.marwa.moviesproject.modules.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.Casts.PageCast;
import com.marwa.moviesproject.models.Casts.ResultCast;
import com.marwa.moviesproject.models.Home.PageDetails;
import com.marwa.moviesproject.models.Home.PageMovies;
import com.marwa.moviesproject.models.Home.ResultMovie;
import com.marwa.moviesproject.models.Videos.PageVideos;
import com.marwa.moviesproject.models.Videos.ResultVideos;
import com.marwa.moviesproject.modules.Adapter.AdapterCast;
import com.marwa.moviesproject.modules.Adapter.Adaptermovie;
import com.marwa.moviesproject.modules.DBInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Series extends AppCompatActivity {

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
    WebView trailer;
    @BindView(R.id.releasedate)
            TextView releasedate;
    @BindView(R.id.gender)
            TextView gender;
    @BindView(R.id.recycler)
           RecyclerView recycler;
    @BindView(R.id.recyclerRecommand)
    RecyclerView recyclerRecommand;
    @BindView(R.id.txtRecommand)
    TextView txtRecommand;
    @BindView(R.id.cast)
            TextView actor;
    @BindView(R.id.watch)
        LinearLayout watch;

    AdapterCast adapterCast;
    Adaptermovie adaptermovie;

    List<ResultCast> listCasts;
    List<ResultMovie> listRecomand;
    String baseUrl = "https://api.themoviedb.org/3/", KeyImage = "https://image.tmdb.org/t/p/w500";
    String key = "61ed6ee7adb8cc190be7e795b5588f32",KeyYoutube="AIzaSyAiBTdXkL8skSCG4hWw6L2opcmf0FBhCRQ",type="" ;
    Integer id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_details);
        ButterKnife.bind(this);


        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DBInterface movieInterface = retrofit.create(DBInterface.class);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        Log.d("idi", "id est" + id);
        Log.d("type", "type est" + type);

        type=intent.getStringExtra("type");
       if(type.equals("tv")) {
           Call<PageDetails> callTV = movieInterface.getDetailsTV(id, key);
           fetchdetails(callTV);
           Call<PageVideos> call1=movieInterface.getVideosTV(id,key);
           fetchTrailer(call1);
           Call<PageCast> call2=movieInterface.getCastsTV(id,key);
           fetchCast(call2);
           Call<PageMovies> callRecommandTv=movieInterface.getRecommandTv(id,key);
           fetchRecommand(callRecommandTv);
       }
       else {
           Call<PageDetails> callMovie = movieInterface.getDetailsMovie(id, key);
           fetchdetails(callMovie);
           Call<PageVideos> call1=movieInterface.getVideosMovie(id,key);
           fetchTrailer(call1);
           Call<PageCast> call2=movieInterface.getCastsMovie(id,key);
           fetchCast(call2);
           Call<PageMovies> callRecommandMovie=movieInterface.getRecommandMovie(id,key);
           fetchRecommand(callRecommandMovie);

       }


        }








    private void fetchdetails(Call<PageDetails> call){
        call.enqueue(new Callback<PageDetails>() {
            @Override
            public void onResponse(Call<PageDetails> call, Response<PageDetails> response) {

                if(response.body().getBackdropPath()!=null) {
                    Glide.with(Series.this)
                            .load(KeyImage + response.body().getBackdropPath())
                            .placeholder(R.drawable.prograss_bar)
                            .centerCrop()
                            .into(background);
                }

                               
                if(response.body().getName() == null || response.body().getName().trim().isEmpty()){
                    name.setText(response.body().getTitle());

                }
                else   name.setText(response.body().getName());

                if (response.body().getOverview().length() > 2)
                    overview.setText(response.body().getOverview());
                else {
                    synopsis.setVisibility(View.INVISIBLE);

                }
                if(response.body().getFirstAirDate()!=null && response.body().getFirstAirDate()!="")
                    date.setText(response.body().getFirstAirDate());
                else   date.setText(response.body().getReleaseDate());


                if(response.body().getGenres().size()>=1) {
                    String g = response.body().getGenres().get(0).getName();
                    for (int i = 1; i < response.body().getGenres().size(); i++) {
                        g += "  ,  " + response.body().getGenres().get(i).getName();
                    }
                    genre.setText(g);
                }
                else
                    gender.setVisibility(View.INVISIBLE);
                if (response.body().getEpisodeRunTime() != null && response.body().getEpisodeRunTime().size() != 0)
                    time.setText(response.body().getEpisodeRunTime().get(0).toString()+ " minutes");
                else if (response.body().getRuntime()!=null){
                    int totalMinutes =  response.body().getRuntime(); // Remplacez cette valeur par le nombre de secondes que vous voulez convertir
                    int hours = totalMinutes / 60; // Calculez le nombre d'heures en divisant par 60 (60 minutes dans une heure)
                    int minutes = totalMinutes % 60; // Calculez le nombre de minutes restantes en divisant par 60 (60 secondes dans une minute)

                    time.setText( hours+"h "+minutes+"m" );

                }
                else
                    time.setVisibility(View.INVISIBLE);
                rating.setText("" + Math.round(response.body().getVoteAverage()));

                if (response.body().getNetworks()!=null && response.body().getNetworks().size()>=1){
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
                boolean trailerFound = false; // Variable pour suivre si une vidéo de type "Trailer" a été trouvée

                for (ResultVideos videos : response.body().getResults()) {
                    if (videos.getType().equals("Trailer")) {
                        Log.i("key", videos.getKey());
                        String video = "<html><body><style>iframe { border: none; }</style><iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + videos.getKey() + "\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe></body></html>";                        trailer.loadData(video, "text/html", "utf-8");
                        trailer.getSettings().setJavaScriptEnabled(true);
                        trailer.setWebChromeClient(new WebChromeClient());
                        background.setVisibility(View.INVISIBLE);

                        trailerFound = true; // Marquer que la vidéo "Trailer" a été trouvée
                        break;
                    }

                }

                // Vérifier si une vidéo "Trailer" a été trouvée ou non
                if (!trailerFound) {
                    trailer.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<PageVideos> call, Throwable t) {
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
                if (listCasts.size()>0 && listCasts.get(0).getProfilePath()!=null) {
                    Log.d("size",""+listCasts.size());
                    recycler.setLayoutManager(layoutManager);
                    adapterCast = new AdapterCast(getApplicationContext(), listCasts, new Adaptermovie.OnItemSelectedListener() {
                        @Override
                        public void itemClick(int position) {
                            Intent intenttoCast= new Intent(getApplicationContext(), Actor.class);
                            intenttoCast.putExtra("id",listCasts.get(position).getId());
                            startActivity(intenttoCast);

                        }
                    });
                            recycler.setAdapter(adapterCast);

                }


            }

            @Override
            public void onFailure(Call<PageCast> call, Throwable t) {

            }
        });
    }




    private void fetchRecommand(Call<PageMovies> call ){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        listRecomand=new ArrayList<>();
        call.enqueue(new Callback<PageMovies>() {
            @Override
            public void onResponse(Call<PageMovies> call, Response<PageMovies> response) {
                List<ResultMovie> m=response.body().getResults();
                for (ResultMovie movie : m ){
                    listRecomand.add(movie.clone());
                }
                if (listRecomand.size()>0 ) {
                    recyclerRecommand.setLayoutManager(layoutManager);
                    adaptermovie = new Adaptermovie(getApplicationContext(), listRecomand, new Adaptermovie.OnItemSelectedListener() {
                        @Override
                        public void itemClick(int position) {
                            Intent intenttodetails=new Intent(getApplicationContext(), Series.class);
                            intenttodetails.putExtra("id",listRecomand.get(position).getId());
                            intenttodetails.putExtra("type",listRecomand.get(position).getMediaType());

                            startActivity(intenttodetails);
                        }
                    });
                            recyclerRecommand.setAdapter(adaptermovie);

                }
                else{
                    txtRecommand.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<PageMovies> call, Throwable t) {

            }
        });
    }


}


