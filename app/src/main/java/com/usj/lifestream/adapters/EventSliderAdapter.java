package com.usj.lifestream.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.usj.lifestream.EventActivity;
import com.usj.lifestream.R;
import com.usj.lifestream.model.Event;

import java.util.List;

public class EventSliderAdapter extends RecyclerView.Adapter<EventSliderAdapter.SliderViewHolder>{
    private Context mCtx;
    private List<Event> events;
    private ViewPager2 viewPager2;

    public EventSliderAdapter(Context mCtx, List<Event> events, ViewPager2 viewPager2) {
        this.mCtx = mCtx;
        this.events = events;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.slide_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.setImage(events.get(position));
        if (position == events.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.imageSlide);

            itemView.setOnClickListener(this);
        }

        void setImage(Event event) {
            //if you want to display image from internet you can put code here using glide or picasso
            Glide.with(mCtx).load(event.url).into(imageView);
        }

        @Override
        public void onClick(View v) {
            int p = getAdapterPosition();
            Event c = events.get(p);

            Intent intent = new Intent(mCtx, EventActivity.class);
            intent.putExtra("EVENT",(Parcelable) c);

            mCtx.startActivity(intent);
        }
    }

    private Runnable runnable= new Runnable() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            events.addAll(events);
            notifyDataSetChanged();
        }
    };
}
