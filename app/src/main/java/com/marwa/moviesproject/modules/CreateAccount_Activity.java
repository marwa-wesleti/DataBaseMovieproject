package com.marwa.moviesproject.modules;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.marwa.moviesproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateAccount_Activity extends AppCompatActivity {
    @BindView(R.id.signup)
    Button signup;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.editName)
    EditText name;
    @BindView(R.id.editpassword)
    EditText editPassword;
    FirebaseAuth auth = null;
    @BindView(R.id.prog)
    ProgressBar prog;
    @BindView(R.id.hide_eye)
    ImageView hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        hide.setImageResource(R.drawable.ic_visibility_off);
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    hide.setImageResource(R.drawable.ic_visibility_off);
                }
                else {
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    hide.setImageResource(R.drawable.ic_baseline_visibility_24);
                }

            }
        });

    }

    private void register() {
        String mail = email.getText().toString();
        String pass = editPassword.getText().toString();
        String username=name.getText().toString();
        prog.setVisibility(View.VISIBLE);
;
        if (TextUtils.isEmpty(mail))
            email.setError("champ obligatoire");
        if (TextUtils.isEmpty(pass))
            editPassword.setError("champ obligatoire");
        else if (pass.length()<6 ) Toast.makeText(this, "pass > 6 carracter", Toast.LENGTH_SHORT).show();

        else {
            auth.createUserWithEmailAndPassword(mail, pass)
                    .addOnCompleteListener(CreateAccount_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendEmailVerification();
                                prog.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(CreateAccount_Activity.this," hlh", Toast.LENGTH_SHORT).show();
                                prog.setVisibility(View.GONE);
                            }


                        }
                    });


        }


    }
    private void SendEmailVerification(){
        auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(CreateAccount_Activity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intentToAuth=new Intent(CreateAccount_Activity.this,Authentification.class);
                    startActivity(intentToAuth);
                }
                else
                    Toast.makeText(CreateAccount_Activity.this, "jknk", Toast.LENGTH_SHORT).show();
            }
        });
    }
}