package com.marwa.moviesproject.modules.Authentification;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.modules.Home;
import com.marwa.moviesproject.network.checkConnection;

import butterknife.BindView;
import butterknife.ButterKnife;


public class User extends AppCompatActivity {
    @BindView(R.id.editlogin)
    EditText login;
    @BindView(R.id.editpassword)
    EditText password;
    @BindView(R.id.ok)
    Button ok;
    @BindView(R.id.create)
    TextView create;
    @BindView(R.id.hide_eye)
    ImageView hide_eye;
    @BindView(R.id.forgetpass)
    TextView forgetpass;
    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth auth = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentification_layout);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("myprefrences", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.contains("login")) {
            Intent i = new Intent(User.this, Home.class);
            startActivity(i);
            finish();
        } else {
            // login account
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    login();
                    refresh.setRefreshing(true);
                }
            });
            // create acount
            create.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    create();
                }
            });
            // hide eye
            hideEye();
            // forget password
            forgetpass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(User.this, Password.class));
                }
            });




    }

}


    private void login() {
        String log=login.getText().toString();
        String pass=password.getText().toString();


        if (TextUtils.isEmpty(log)) {
            login.setError("champ obligatoire");
            refresh.setRefreshing(false);
        }
        if (TextUtils.isEmpty(pass)) {
            password.setError("champ obligatoire");
            hide_eye.setVisibility(View.INVISIBLE);
            refresh.setRefreshing(false);
        }
        else if(pass.length()<6){ Toast.makeText(this, "password >6 carracter", Toast.LENGTH_SHORT).show();
            refresh.setRefreshing(false);
        }
        else{
            auth.signInWithEmailAndPassword(log,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("mail",auth.getCurrentUser().getEmail());
                            Intent intenttofilm = new Intent(User.this, Home.class);
                            startActivity(intenttofilm);
                            finish();
                            refresh.setRefreshing(false);
                            editor.putString("login", log);
                            editor.putString("pw", pass);
                            editor.commit();




                    }

                    else{
                        refresh.setRefreshing(false);
                        Toast.makeText(User.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.i("e", task.getException().getMessage());

                    }
                }
            });
        }
    }





    private void create(){
        if(checkConnection.isConnected(User.this)) {
            Intent intentToCreatte = new Intent(User.this, CreateAccount_Activity.class);
            startActivity(intentToCreatte);


        }
        else
            checkConnection.isConnected(User.this);
    }

    private void hideEye(){
        hide_eye.setImageResource(R.drawable.ic_visibility_off);
        hide_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide_eye.setImageResource(R.drawable.ic_visibility_off);
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide_eye.setImageResource(R.drawable.ic_baseline_visibility_24);
                }

            }
        });


    }




}
