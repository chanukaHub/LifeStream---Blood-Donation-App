package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private TextView register;
    private Button googleButton, lifeStreamButton;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferences= getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStartBoarding", true);
        if (firstStart) {
            firstRun();
        }

        register =findViewById(R.id.register_text);
        register.setOnClickListener(this);

        lifeStreamButton = findViewById(R.id.btn_signIn);
        lifeStreamButton.setOnClickListener(this);

    }
    public void firstRun() {
        Intent intent = new Intent(Login.this, OnBoardingActivity.class);
        startActivity(intent);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstStartBoarding", false);
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            //case R.id.btn_google_signIn:
                //signIn();
                //break;
            case R.id.btn_signIn:
                startActivity(new Intent(Login.this,AccountLogin.class));
                break;
            case R.id.register_text:
                startActivity(new Intent(Login.this,AccountRegister.class));
                break;

        }
    }
}