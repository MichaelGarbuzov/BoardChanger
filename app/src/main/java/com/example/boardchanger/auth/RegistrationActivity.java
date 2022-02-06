package com.example.boardchanger.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.boardchanger.R;
import com.example.boardchanger.feed.MainFeedActivity;


public class RegistrationActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText regEmail, regPwd;
    private Button regBtn;
    private TextView regQn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        //toolbar = findViewById(R.id.RegistrationToolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Registration");

        regEmail = findViewById(R.id.RegistrationEmail);
        regPwd = findViewById(R.id.RegistrationPassword);
        regBtn = findViewById(R.id.RegistrationButton);
        regQn = findViewById(R.id.RegistrationQuestion);

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
                Intent feed = new Intent(RegistrationActivity.this, MainFeedActivity.class);
                startActivity(feed);
                finish();
            }
        });

    }
}