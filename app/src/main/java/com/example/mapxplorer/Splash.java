package com.example.mapxplorer;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Space;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class Splash extends AppCompatActivity {

    @Override protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            MainActivity.isDarkTheme = true;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Handler handler = new Handler();
        Intent intent = new Intent(this,MainActivity.class);
        handler.postDelayed(() -> {
            startActivity(intent);
            finish();
            }, 500);
    }
    @Override public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK ;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Отключаем ночной режим
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                startActivity(new Intent(this,MainActivity.class));
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Включаем ночной режим
                MainActivity.isDarkTheme = true;
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                startActivity(new Intent(this,MainActivity.class));
                break;
            default:
                break;
        }
    }
}