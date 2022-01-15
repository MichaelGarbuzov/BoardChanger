package com.example.boardchanger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;


import android.os.Bundle;


public class MainFeedActivity extends AppCompatActivity {
        NavController navCtl;
        Toolbar toolbar;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_feed);
            toolbar = findViewById(R.id.mainfeed_toolbar);
            setSupportActionBar(toolbar);
            NavHost navHost = (NavHost) getSupportFragmentManager().findFragmentById(R.id.mainfeed_navhost);
            navCtl = navHost.getNavController();

            NavigationUI.setupActionBarWithNavController(this, navCtl);
        }
    }