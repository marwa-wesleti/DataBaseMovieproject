package com.marwa.moviesproject.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.marwa.moviesproject.R;
import com.marwa.moviesproject.network.checkConnection;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Password extends AppCompatActivity {
    @BindView(R.id.editemail)
             EditText editEmail;
    @BindView(R.id.next)
             Button next;
    @BindView(R.id.progBar)
            ProgressBar progBar;
    @BindView(R.id.VeriConx)
            TextView check;
    FirebaseAuth auth=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        auth=FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reset();
            }


        });






    }

    private void Reset() {
        String Email=editEmail.getText().toString();
        if(!checkConnection.isConnected(this)) check.setText("Verifier votre connexion");
        else if (TextUtils.isEmpty(Email)) editEmail.setText("champs obligatoire");
        else {
            progBar.setVisibility(View.VISIBLE);
            auth.sendPasswordResetEmail(Email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(Password.this, "reset password link is mailed", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), User.class));
                        progBar.setVisibility(View.INVISIBLE);
                    }

                    else {
                        Toast.makeText(Password.this, "Password Reset mail could not be sent", Toast.LENGTH_LONG).show();
                        progBar.setVisibility(View.INVISIBLE);
                    }
                }
            });


        }





    }
}