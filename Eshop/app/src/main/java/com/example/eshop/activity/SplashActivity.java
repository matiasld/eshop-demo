package com.example.eshop.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.example.eshop.R;

public class SplashActivity extends AppCompatActivity {

    // Tiempo de Espera
    private final int SPLASH_DISPLAY_LENGTH = 4000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LinearLayout lin = (LinearLayout) findViewById(R.id.linear_splash_lyt);
        Animation aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);
        lin.startAnimation(aniFade);

        //Carga la siguiente Activity con delay:
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(i);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
