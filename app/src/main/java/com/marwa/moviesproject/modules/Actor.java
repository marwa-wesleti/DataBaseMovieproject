package com.marwa.moviesproject.modules;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.models.ProfilCast;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Actor extends AppCompatActivity {
    @BindView(R.id.profilCast)
    CircleImageView imageCast;
    @BindView(R.id.nomprofile)
    TextView nomprofile;

    @BindView(R.id.bibliographie)
            TextView biblio;


    String baseUrl = "https://api.themoviedb.org/3/", KeyImage = "https://image.tmdb.org/t/p/w500";
    String key = "61ed6ee7adb8cc190be7e795b5588f32",KeyYoutube="AIzaSyBpEh9Yux9bGw3tCM3eBAH8qJ8WUSTV2jg";
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_casts);
        ButterKnife.bind(this);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()).build();
        MovieInterface movieInterface = retrofit.create(MovieInterface.class);
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
                biblio.setText(response.body().getBiography());


            }

            @Override
            public void onFailure(Call<ProfilCast> call, Throwable t) {
                Toast.makeText(Actor.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}