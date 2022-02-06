package com.example.boardchanger.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.boardchanger.R;
import com.example.boardchanger.feed.MainFeedActivity;

public class LoginActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText loginEmail, loginPwd;
    private Button loginBtn;
    private TextView loginQn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

       // toolbar = findViewById(R.id.loginToolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Login");


        loginEmail = findViewById(R.id.loginEmail);
        loginPwd = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginButton);
        loginQn = (TextView) findViewById(R.id.loginPageQuestion);

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
        Intent feed = new Intent(LoginActivity.this, MainFeedActivity.class);
        startActivity(feed);
        finish();
    }
}