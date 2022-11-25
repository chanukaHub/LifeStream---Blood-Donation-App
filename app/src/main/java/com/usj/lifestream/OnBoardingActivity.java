package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.usj.lifestream.adapters.OnBoardingSliderAdapter;

public class OnBoardingActivity extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout mDotLayout;
    OnBoardingSliderAdapter onBoardingSliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        viewPager= (ViewPager) findViewById(R.id.viewpager);
        mDotLayout=(LinearLayout) findViewById(R.id.indicator_layout);

        onBoardingSliderAdapter =new OnBoardingSliderAdapter(this);
        viewPager.setAdapter(onBoardingSliderAdapter);
    }
}