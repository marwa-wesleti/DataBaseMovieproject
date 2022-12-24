package com.marwa.moviesproject.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marwa.moviesproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Acount extends AppCompatActivity {
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.usermail)
            TextView usermail;
    @BindView(R.id.userpass)
            TextView userpass;
    @BindView(R.id.hide_eye)
    ImageView hide_eye;
    @BindView(R.id.delete)
    Button delete;

    SharedPreferences sharedPreferences,shared;
    SharedPreferences.Editor editor , edit;
    private Acount acountactivity ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount);
        ButterKnife.bind(this);
        this.acountactivity=this;

        sharedPreferences = getSharedPreferences("myprefrences", MODE_PRIVATE);
        editor=sharedPreferences.edit();

        String mailuser=sharedPreferences.getString("login",null);
        String passuser=sharedPreferences.getString("pw",null);
        if( mailuser!=null || passuser!=null) {
            usermail.setText(""+mailuser);
            userpass.setText(""+passuser);
        }

        hideEye();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletUser(mailuser,passuser);
            }


        });







    }



    private void deletUser(String mailuser, String passuser) {
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential authCredential= EmailAuthProvider.getCredential(mailuser,passuser);
        if(user!=null){
            user.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() { // role ta3 reauthenticate howa bch t'assuri ili aabd ili yestaaml tawa howa bidou li 3mal compte heka
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    AlertDialog.Builder mybull= new AlertDialog.Builder(acountactivity);

                    mybull.setTitle("Are you sure to delete your account?");
                    mybull.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    startActivity(new Intent(Acount.this, User.class));
                                    editor.clear();
                                    editor.commit();
                                    finish();
                                    Toast.makeText(Acount.this, "Deleted User Successfully", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    });
                    mybull.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    mybull.show();
               }
            });
        }
    }

    private void hideEye(){
        hide_eye.setImageResource(R.drawable.ic_visibility_off2);
        hide_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userpass.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    userpass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide_eye.setImageResource(R.drawable.ic_visibility_off2);
                } else {
                    userpass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide_eye.setImageResource(R.drawable.ic_baseline_visibility_2);
                }

            }
        });
    }
}