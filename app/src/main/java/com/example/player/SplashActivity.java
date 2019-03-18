package com.example.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

    private final int SPLASH_DISPLAY_LENTH = 2000; //2s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toMainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(toMainIntent);
                SplashActivity.this.finish();
            }
        },SPLASH_DISPLAY_LENTH);
    }



}
