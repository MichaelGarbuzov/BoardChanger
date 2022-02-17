package com.example.boardchanger.feed;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavHost;
import androidx.navigation.ui.NavigationUI;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.boardchanger.auth.LoginActivity;
import com.example.boardchanger.R;
import com.google.firebase.auth.FirebaseAuth;


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (!super.onOptionsItemSelected(item)) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    navCtl.navigateUp();
                    return true;
                case R.id.menuProfileFragment:
                    navCtl.navigate(R.id.action_global_profileFragment);
                    return true;
                case R.id.menu_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                    return true;
            }
        }
        return true;
    }



}