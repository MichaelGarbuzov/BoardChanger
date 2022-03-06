package com.example.boardchanger.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.boardchanger.R;
import com.example.boardchanger.feed.MainFeedActivity;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText loginEmail, loginPwd;
    private Button loginBtn;
    private TextView loginQn;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.loginEmail);
        loginPwd = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginButton);
        loginQn = (TextView) findViewById(R.id.loginPageQuestion);
        progressBar = findViewById(R.id.login_progress_bar);
        firebaseAuth = FirebaseAuth.getInstance();

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openMainFeed();
            }
        }
        );

        loginQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(register);
                finish();
            }
        });
    }

    private void openMainFeed() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPwd.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            loginEmail.setError("Email is Required.");
            return;
        }
        if (TextUtils.isEmpty(password)){
            loginPwd.setError("Password is Required.");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent feed = new Intent(LoginActivity.this, MainFeedActivity.class);
                    Model.instance.getUserByEmail(new Model.getUserByEmail() {
                        @Override
                        public void onComplete(User user) { }
                    });
                    startActivity(feed);
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this,"Error! Email or Password is incorrect",Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}