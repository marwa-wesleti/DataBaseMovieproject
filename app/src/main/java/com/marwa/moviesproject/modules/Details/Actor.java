package com.marwa.moviesproject.modules.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.Constant;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.Casts.PagePerson;
import com.marwa.moviesproject.models.Casts.ProfilCast;
import com.marwa.moviesproject.models.Home.ResultMovie;
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

public class Actor extends AppCompatActivity {
    @BindView(R.id.profilCast)
    ImageView imageCast;
    @BindView(R.id.nomprofile)
    TextView nomprofile;

    @BindView(R.id.bibliographie)
            TextView biblio;
    @BindView(R.id.birth)
    TextView birth;
    @BindView(R.id.place)
    TextView place;
    List<ResultMovie> listFamous;
    @BindView(R.id.recyclerFamous)
    RecyclerView recyclerFamous;
    @BindView(R.id.txtFamous)
    TextView txtFamous;
    Adaptermovie adaptermovie;

    String baseUrl = "https://api.themoviedb.org/3/", KeyImage = "https://image.tmdb.org/t/p/w500";
    String key = "61ed6ee7adb8cc190be7e795b5588f32",KeyYoutube="AIzaSyBpEh9Yux9bGw3tCM3eBAH8qJ8WUSTV2jg";
    Integer id;
    Constant constant=new Constant();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_casts);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        DBInterface movieInterface = retrofit.create(DBInterface.class);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        Log.d("idi", "id est" + id);
        Call<ProfilCast> call = movieInterface.getProfileCasts(id,key);
        call.enqueue(new Callback<ProfilCast>() {
            @Override
            public void onResponse(Call<ProfilCast> call, Response<ProfilCast> response) {
                Glide.with(Actor.this)
                        .load(KeyImage + response.body().getProfilePath())
                        .placeholder(R.drawable.prograss_bar)
                        .centerCrop()
                        .into(imageCast);
                nomprofile.setText(response.body().getName());
                biblio.setText(constant.extractFirstSentence(response.body().getBiography()));
                birth.setText(response.body().getBirthday());
                place.setText(response.body().getPlaceOfBirth());


            }

            @Override
            public void onFailure(Call<ProfilCast> call, Throwable t) {
                Toast.makeText(Actor.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
        Call<PagePerson> callFamousCredit=movieInterface.getCreditPerson(id,key);
        fetchFilms(callFamousCredit);

    }

    private void fetchFilms(Call<PagePerson> call ){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        listFamous=new ArrayList<>();
        call.enqueue(new Callback<PagePerson>() {
            @Override
            public void onResponse(Call<PagePerson> call, Response<PagePerson> response) {
                List<ResultMovie> m=response.body().getCast();
                for (ResultMovie movie : m ){
                    listFamous.add(movie.clone());
                }

                if (listFamous.size()>0 ) {
                    recyclerFamous.setLayoutManager(layoutManager);
                    adaptermovie = new Adaptermovie(getApplicationContext(), listFamous, new Adaptermovie.OnItemSelectedListener() {
                        @Override
                        public void itemClick(int position) {
                            Intent intenttodetails=new Intent(getApplicationContext(), Series.class);
                            intenttodetails.putExtra("id",listFamous.get(position).getId());
                            intenttodetails.putExtra("type",listFamous.get(position).getMediaType());

                            startActivity(intenttodetails);
                        }
                    });
                    recyclerFamous.setAdapter(adaptermovie);

                }
                else{
                    txtFamous.setVisibility(View.INVISIBLE);
                }


            }

            @Override
            public void onFailure(Call<PagePerson> call, Throwable t) {

            }
        });
    }

}