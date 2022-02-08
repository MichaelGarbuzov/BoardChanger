package com.example.boardchanger.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardchanger.R;
import com.example.boardchanger.feed.MainFeedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText regEmail, regPwd;
    private Button regBtn;
    private TextView regQn;
    FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        regEmail = findViewById(R.id.RegistrationEmail);
        regPwd = findViewById(R.id.RegistrationPassword);
        regBtn = findViewById(R.id.RegistrationButton);
        regQn = findViewById(R.id.RegistrationQuestion);
        progressBar = findViewById(R.id.reg_progress_Bar);
        firebaseAuth = FirebaseAuth.getInstance();

        regQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(login);
                finish();
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = regEmail.getText().toString().trim();
                String password = regPwd.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    regEmail.setError("Email is Required.");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    regPwd.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6 ){
                    regPwd.setError("Password must be >= 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // reg the user

                firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegistrationActivity.this,"User Created",Toast.LENGTH_SHORT).show();
                            Intent feed = new Intent(RegistrationActivity.this, MainFeedActivity.class);
                            startActivity(feed);
                            finish();
                        }else{
                            Toast.makeText(RegistrationActivity.this,"Error ! " + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            return;

                        }
                    }
                });
            }
        });

    }
}