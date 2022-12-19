package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.usj.lifestream.adapters.OnBoardingSliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout mDotLayout;
    OnBoardingSliderAdapter onBoardingSliderAdapter;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        mDotLayout=(LinearLayout) findViewById(R.id.indicator_layout);
        relativeLayout = findViewById(R.id.skip_relative_Layout);

        onBoardingSliderAdapter =new OnBoardingSliderAdapter(this);
        viewPager.setAdapter(onBoardingSliderAdapter);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}