package com.marwa.moviesproject.modules;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.auth.FirebaseAuth;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.modules.Authentification.User;
import com.marwa.moviesproject.network.checkConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class splash extends AppCompatActivity {
    FirebaseAuth auth=null;

    @BindView(R.id.tv)
    ImageView tv;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.connexion)
            TextView connexion;
    @BindView(R.id.swipeup)
            SwipeRefreshLayout swipeup;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tv.setVisibility(View.VISIBLE);
        YoYo.with(Techniques.FlipInX)
                .duration(2000)
                .onEnd(animator -> {
                    txt.setVisibility(View.VISIBLE);
                    YoYo.with(Techniques.BounceIn)
                        .duration(4000)
                        .playOn(txt);
                })
                .playOn(tv);

        auth= FirebaseAuth.getInstance();
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeup.setRefreshing(true);
                if (checkConnection.isConnected(splash.this)) {
                    swipeup.setRefreshing(false);
                        Intent inttologin = new Intent(splash.this, User.class);
                        startActivity(inttologin);
                        finish();

                }
                else {
                    swipeup.setRefreshing(false);
                    connexion.setVisibility(View.VISIBLE);
                    swipeup.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            if (checkConnection.isConnected(splash.this)) {
                                Intent inttologin = new Intent(splash.this, User.class);
                                startActivity(inttologin);
                                finish();
                            }
                            swipeup.setRefreshing(false);
                        }
                    });



                }
            }

        },6000);



    }


}