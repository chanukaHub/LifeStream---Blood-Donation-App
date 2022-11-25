package com.usj.lifestream.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.usj.lifestream.R;

public class OnBoardingSliderAdapter extends PagerAdapter {
    Context context;


    public OnBoardingSliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images ={
            R.drawable.onboarding1,
            R.drawable.onboarding2,
            R.drawable.onboarding3
    };

    public String[] slide_headings ={
            "Spare lives",
            "Obtain blood",
            "Join the community"
    };

    public String[] slide_descs ={
            "Join us to see urgent requests for blood donors.",
            "To find blood donors nearby, click here.",
            "Become a member to get updates on blood recipients and donors."
    };


    @Override
    public int getCount() {
        return slide_headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater =(LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.onboarding_slide_layout,container,false);

        ImageView sliderImage= (ImageView) view.findViewById(R.id.on_boarding_image);
        TextView sliderHeading =(TextView) view.findViewById(R.id.on_boarding_topic);
        TextView sliderDescription =(TextView) view.findViewById(R.id.on_boarding_description);

        sliderImage.setImageResource(slide_images[position]);
        sliderHeading.setText(slide_headings[position]);
        sliderDescription.setText(slide_descs[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
