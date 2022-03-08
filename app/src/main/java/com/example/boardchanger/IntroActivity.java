package com.example.boardchanger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.boardchanger.auth.LoginActivity;
import com.example.boardchanger.feed.MainFeedActivity;
import com.example.boardchanger.model.Model;
import com.example.boardchanger.model.users.User;
import com.google.firebase.auth.FirebaseAuth;

public class IntroActivity extends AppCompatActivity {
    private static final int SPLASH = 3300;

    Animation topAnim,bottomAnim;
    ImageView imageView;
    TextView textView;
    LottieAnimationView carAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.text);
        imageView.setAnimation(topAnim);
        textView.setAnimation(bottomAnim);
        carAnim = findViewById(R.id.intro_animation_car);
        carAnim.animate().translationX(2000).setDuration(2000).setStartDelay(2500);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!= null){

                    Model.instance.getUserByEmail(new Model.getUserByEmail() {
                        @Override
                        public void onComplete(User user) {
                            Intent intent = new Intent(IntroActivity.this, MainFeedActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }else {
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH);
    }
}