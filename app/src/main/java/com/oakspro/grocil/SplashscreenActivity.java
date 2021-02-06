package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashscreenActivity extends AppCompatActivity {

    private static int SPLASH=5000;
    ImageView logo;
    Animation top_animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        logo=findViewById(R.id.logo);

        top_animation= AnimationUtils.loadAnimation(this, R.anim.top_animation);
        logo.setAnimation(top_animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent i = new Intent(SplashscreenActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH);

    }
}