package com.marwa.moviesproject.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.PageMovies;
import com.marwa.moviesproject.models.ResultMovie;
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

public class Home extends AppCompatActivity implements Adaptermovie.OnItemSelectedListener {
    @BindView(R.id.recyclerView)
            RecyclerView recyclerView;
    @BindView(R.id.refreshlayout)
            SwipeRefreshLayout refreshLayout;
    Adaptermovie adaptermovie;
    List<ResultMovie> moviesList;
    String key="61ed6ee7adb8cc190be7e795b5588f32";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_emission);
        ButterKnife.bind(this);
        sharedPreferences=getSharedPreferences("myprefrences",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        moviesList=new ArrayList<>();
        GridLayoutManager layoutManager=new GridLayoutManager(this,2);

        Retrofit retrofit=new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create()).build();

        MovieInterface movieInterface=retrofit.create(MovieInterface.class);
        Call<PageMovies> call=movieInterface.getMovie(key);


        fetchMovie(call,layoutManager);




        }

    private void fetchMovie(Call<PageMovies> call, GridLayoutManager layoutManager){
        refreshLayout.setRefreshing(true);
        call.clone().enqueue(new Callback<PageMovies>() {
            @Override
            public void onResponse(Call<PageMovies> call, Response<PageMovies> response) {
                refreshLayout.setRefreshing(false);
                List<ResultMovie> m=response.body().getResults();
                for (ResultMovie movie : m ){
                    moviesList.add(movie);
                }
                Log.d("tag","afficher "+moviesList);
                refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Collections.shuffle(moviesList,new Random(System.currentTimeMillis()));
                        refreshLayout.setRefreshing(false);
                    }
                });

                recyclerView.setLayoutManager(layoutManager);
                adaptermovie=new Adaptermovie(Home.this,moviesList, Home.this);
                recyclerView.setAdapter(adaptermovie);

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
    public boolean onCreateOptionsMenu(Menu menu) {
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
               adaptermovie.getFilter().filter(newText);
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
                startActivity(new Intent(getApplicationContext(),Acount.class));
                return true;




        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    public void itemClick(int position) {
        moviesList.get(position);
        Intent intenttodetails=new Intent(Home.this, Series.class);
        intenttodetails.putExtra("id",moviesList.get(position).getId());
        startActivity(intenttodetails);

        Log.d("tag","Clicked: "+position);

    }






}