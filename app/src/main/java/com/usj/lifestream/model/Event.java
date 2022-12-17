package com.usj.lifestream.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Event implements Parcelable {

    public String name,date,time,venue,description,url;

    public Event() {
    }

    public Event(String name, String date, String time, String venue, String description, String url) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.venue = venue;
        this.description = description;
        this.url = url;
    }


    protected Event(Parcel in) {
        name = in.readString();
        date = in.readString();
        time = in.readString();
        venue = in.readString();
        description = in.readString();
        url = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(date);
        dest.writeString(time);
        dest.writeString(venue);
        dest.writeString(description);
        dest.writeString(url);
    }
}
