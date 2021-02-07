package com.oakspro.grocil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashscreenActivity extends AppCompatActivity {

    private static int SPLASH=5000;
    ImageView logo;
    Animation top_animation;
    SharedPreferences sharedPreferences;
    private boolean loginV;
    private String status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        sharedPreferences=getSharedPreferences("MyUser",MODE_PRIVATE);

        loginV=sharedPreferences.getBoolean("loginS",false);
        status=sharedPreferences.getString("status","0");


        logo=findViewById(R.id.logo);

        top_animation= AnimationUtils.loadAnimation(this, R.anim.top_animation);
        logo.setAnimation(top_animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (loginV==true && status.equals("0")){
                    Intent intent=new Intent(SplashscreenActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else if (loginV==true && status.equals("1")){
                       //create intent for dashbord activity


                }else {
                    Intent intent=new Intent(SplashscreenActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH);

    }
}