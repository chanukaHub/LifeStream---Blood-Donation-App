package com.usj.lifestream;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.usj.lifestream.model.Event;

public class EventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        Event event = (Event) intent.getExtras().getParcelable("EVENT");
        Toast.makeText(this,event.name,Toast.LENGTH_LONG).show();
    }
}