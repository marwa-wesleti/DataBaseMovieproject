package com.marwa.moviesproject.modules;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.Home.PageMovies;
import com.marwa.moviesproject.models.Home.ResultMovie;
import com.marwa.moviesproject.modules.Adapter.Adaptermovie;
import com.marwa.moviesproject.modules.Authentification.Acount;
import com.marwa.moviesproject.modules.Authentification.User;
import com.marwa.moviesproject.modules.Details.Series;
import com.marwa.moviesproject.network.checkConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Home extends AppCompatActivity  {
    @BindView(R.id.recyclerView)
            RecyclerView recyclerView;
    @BindView(R.id.recyclerPopularMovie)
    RecyclerView recyclerPopularMovie;
    @BindView(R.id.recyclerPopularTv)
    RecyclerView recyclerPopularTv;
    @BindView(R.id.refreshlayout)
            SwipeRefreshLayout refreshLayout;
    @BindView(R.id.txttrend)
    TextView txttrend;
    @BindView(R.id.txtPopularMovie)
    TextView txtPopularMovie;
    @BindView(R.id.txtPopulartv)
    TextView txtPopulartv;

    List<ResultMovie> moviesList=new ArrayList<>();
    List<ResultMovie> popularMoviesList=new ArrayList<>();
    List<ResultMovie> popularTvList=new ArrayList<>();


    String key="61ed6ee7adb8cc190be7e795b5588f32";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int id;
    Adaptermovie adapterPopularMovie;
    Adaptermovie adapterTrendMovie;
    Adaptermovie getAdapterPopularTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_emission);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("myprefrences",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        DBInterface movieInterface=retrofit.create(DBInterface.class);
        Call<PageMovies> call=movieInterface.getTrending(key);

        fetchTrending(call,layoutManager);
        Call<PageMovies> callUpCommingMovie=movieInterface.getUpPopularMovie(key);
        //
        LinearLayoutManager layoutManagerUpPopularMovie=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);

        fetchUpPopularMovie(callUpCommingMovie,layoutManagerUpPopularMovie);

        LinearLayoutManager layoutManagerUpPopularTV=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        Call<PageMovies> callUpCommingTV=movieInterface.getUpPopularTV(key);

        fetchUpPopularTv(callUpCommingTV,layoutManagerUpPopularTV);




        }

    private void fetchTrending(Call<PageMovies> call, LinearLayoutManager layoutManager){
        refreshLayout.setRefreshing(true);
        call.clone().enqueue(new Callback<PageMovies>() {
            @Override
            public void onResponse(Call<PageMovies> call, Response<PageMovies> response) {
                refreshLayout.setRefreshing(false);
                List<ResultMovie> m=response.body().getResults();
                for (ResultMovie movie : m ){
                    moviesList.add(movie.clone());
                }
                Log.d("tag","afficher "+moviesList);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Collections.shuffle(moviesList,new Random(System.currentTimeMillis()));
                        refreshLayout.setRefreshing(false);
                    }
                });
                adapterTrendMovie = new Adaptermovie(getApplicationContext(), moviesList, new Adaptermovie.OnItemSelectedListener() {
                    @Override
                    public void itemClick(int position) {
                        moviesList.get(position);
                        Intent intenttodetails=new Intent(Home.this, Series.class);
                        intenttodetails.putExtra("id",moviesList.get(position).getId());
                        intenttodetails.putExtra("type",moviesList.get(position).getMediaType());

                        startActivity(intenttodetails);
                    }
                });

                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(adapterTrendMovie);


            }

            @Override
            public void onFailure(Call<PageMovies> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                Log.e("erreur",t.getMessage(),t);

            }
        });

    }

    private void fetchUpPopularMovie(Call<PageMovies> call, LinearLayoutManager layoutManager){
        refreshLayout.setRefreshing(true);
        call.clone().enqueue(new Callback<PageMovies>() {
            @Override
            public void onResponse(Call<PageMovies> call, Response<PageMovies> response) {
                refreshLayout.setRefreshing(false);
                List<ResultMovie> m=response.body().getResults();
                for (ResultMovie movie : m ){
                    popularMoviesList.add(movie.clone());


                }
                Log.d("tag","afficher "+popularMoviesList);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Collections.shuffle(popularMoviesList,new Random(System.currentTimeMillis()));
                        refreshLayout.setRefreshing(false);
                    }
                });
                adapterPopularMovie = new Adaptermovie(getApplicationContext(), popularMoviesList, new Adaptermovie.OnItemSelectedListener() {
                    @Override
                    public void itemClick(int position) {
                        Intent intenttodetails=new Intent(Home.this, Series.class);
                        intenttodetails.putExtra("id",popularMoviesList.get(position).getId());
                        intenttodetails.putExtra("type","movie");

                        startActivity(intenttodetails);
                    }
                });

                recyclerPopularMovie.setLayoutManager(layoutManager);
                recyclerPopularMovie.setAdapter(adapterPopularMovie);

            }

            @Override
            public void onFailure(Call<PageMovies> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                Log.e("erreur",t.getMessage(),t);

            }
        });

    }

    private void fetchUpPopularTv(Call<PageMovies> call, LinearLayoutManager layoutManager){
        refreshLayout.setRefreshing(true);
        call.clone().enqueue(new Callback<PageMovies>() {
            @Override
            public void onResponse(Call<PageMovies> call, Response<PageMovies> response) {
                refreshLayout.setRefreshing(false);
                List<ResultMovie> m=response.body().getResults();
                for (ResultMovie movie : m ){
                    popularTvList.add(movie.clone());


                }
                Log.d("tag","afficher "+popularTvList);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Collections.shuffle(popularTvList,new Random(System.currentTimeMillis()));
                        refreshLayout.setRefreshing(false);
                    }
                });
                getAdapterPopularTv = new Adaptermovie(getApplicationContext(), popularTvList, new Adaptermovie.OnItemSelectedListener() {
                    @Override
                    public void itemClick(int position) {
                        Intent intenttodetails=new Intent(Home.this, Series.class);
                        intenttodetails.putExtra("id",popularTvList.get(position).getId());
                        intenttodetails.putExtra("type","tv");

                        startActivity(intenttodetails);
                    }
                });

                recyclerPopularTv.setLayoutManager(layoutManager);
                recyclerPopularTv.setAdapter(getAdapterPopularTv);

            }

            @Override
            public void onFailure(Call<PageMovies> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                Log.e("erreur",t.getMessage(),t);

            }
        });

    }




    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        getMenuInflater().inflate(R.menu.menu_activity,menu);
        MenuItem item=menu.findItem(R.id.search);// jebna item
        SearchView searchView=(SearchView) item.getActionView();// interface de recherche bch utilisateur yekteb haja li ylawej alha
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {//detecteur de text
            @Override
            public boolean onQueryTextSubmit(String query) {
                 return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {// w ena kaada nekteb kaaad yaaml fel filtrage
               adapterTrendMovie.getFilter().filter(newText);
               adapterPopularMovie.getFilter().filter(newText);
               getAdapterPopularTv.getFilter().filter(newText);
                if(newText.equals("")){
                    txttrend.setVisibility(View.VISIBLE);
                    txtPopularMovie.setVisibility(View.VISIBLE);
                    txtPopulartv.setVisibility(View.VISIBLE);
                }
                else {
                    txttrend.setVisibility(View.INVISIBLE);
                    txtPopularMovie.setVisibility(View.INVISIBLE);
                    txtPopulartv.setVisibility(View.INVISIBLE);
                }

               return false;
            }
        });


        return super.onCreateOptionsMenu(menu);


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                if(checkConnection.isConnected(Home.this)) {
                    editor.clear();
                    editor.commit();
                    Intent intentlog = new Intent(Home.this, User.class);
                    startActivity(intentlog);
                    finish();
                    return true;
                }
            case R.id.acount:
                startActivity(new Intent(getApplicationContext(), Acount.class));
                return true;




        }


        return super.onOptionsItemSelected(item);
    }









}