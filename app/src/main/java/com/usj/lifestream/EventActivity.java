package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.usj.lifestream.model.Event;

public class EventActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textViewName,textViewDate,textViewTime,textViewVenue,textViewDes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        Event event = (Event) intent.getExtras().getParcelable("EVENT");
        //Toast.makeText(this,event.name,Toast.LENGTH_LONG).show();

        imageView = findViewById(R.id.event_imageView);
        textViewName = findViewById(R.id.event_name_textView);
        textViewDate =findViewById(R.id.textView_date);
        textViewTime =findViewById(R.id.textView_time);
        textViewVenue =findViewById(R.id.textView_venue);
        textViewDes =findViewById(R.id.event_dec_textView);

        Glide.with(this).load(event.url).into(imageView);
        textViewName.setText(event.name);
        textViewDate.setText(event.date);
        textViewTime.setText(event.time);
        textViewVenue.setText(event.venue);

        String des = event.description.replace("\\n","\n");
        textViewDes.setText(des);
    }
}