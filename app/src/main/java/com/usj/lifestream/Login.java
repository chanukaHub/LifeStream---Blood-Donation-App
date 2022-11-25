package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class Login extends AppCompatActivity {
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences= getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStartBoarding", true);
        if (firstStart) {
            firstRun();
        }

    }
    public void firstRun() {
        Intent intent = new Intent(Login.this, OnBoardingActivity.class);
        startActivity(intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStartBoarding", false);
        editor.apply();
    }
}