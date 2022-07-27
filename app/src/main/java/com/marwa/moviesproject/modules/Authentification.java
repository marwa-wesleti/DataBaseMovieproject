package com.marwa.moviesproject.modules;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.network.checkConnection;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Authentification extends AppCompatActivity
{
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
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FirebaseAuth auth=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentification_layout);
        ButterKnife.bind(this);
        auth=FirebaseAuth.getInstance();
       sharedPreferences=getSharedPreferences("myprefrences",MODE_PRIVATE);
       editor=sharedPreferences.edit();
        if(sharedPreferences.contains("login")){
            Intent i=new Intent(Authentification.this,AffichageEmissionActivity.class);
            startActivity(i);
        }


       else{
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { create();}
        });

    }
   hide_eye.setImageResource(R.drawable.ic_visibility_off);
   hide_eye.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
           if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
               password.setTransformationMethod(PasswordTransformationMethod.getInstance());
               hide_eye.setImageResource(R.drawable.ic_visibility_off);
           }
           else {
               password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
               hide_eye.setImageResource(R.drawable.ic_baseline_visibility_24);
           }

       }
   });

       forgetpass.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(Authentification.this,ForgetPassword.class));
           }
       });

    }


    private void login() {
        String log=login.getText().toString();
        String pass=password.getText().toString();
        editor.putString("login",log);
        editor.putString("pw",pass);
        editor.commit();

        if (TextUtils.isEmpty(log))
            login.setError("champ obligatoire");
        if (TextUtils.isEmpty(pass)) {
            password.setError("champ obligatoire");
            hide_eye.setVisibility(View.INVISIBLE);
        }
        else if(pass.length()<6) Toast.makeText(this, "password >6 carracter", Toast.LENGTH_SHORT).show();
        else{
            auth.signInWithEmailAndPassword(log,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        verifyAccount();



                    }

                    else{
                        Toast.makeText(Authentification.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }








    private void verifyAccount() {
        String log=login.getText().toString();
        String pass=password.getText().toString();
            if(auth.getCurrentUser().isEmailVerified()) {
                Intent intenttohome=new Intent(Authentification.this,AffichageEmissionActivity.class);
                startActivity(intenttohome);
                finish();
            }
            else
                Toast.makeText(this, "Verifier votre compte", Toast.LENGTH_LONG).show();
        }







    private void create(){
        if(checkConnection.isConnected(Authentification.this)) {
            Intent intentToCreatte = new Intent(Authentification.this, CreateAccount_Activity.class);
            startActivity(intentToCreatte);
        }
        else
            checkConnection.isConnected(Authentification.this);
    }





}
