package com.usj.lifestream.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

public class Event implements Parcelable {
    @Exclude
    public String name;
    public String venue;
    public String date;
    public String url;


    public Event() {
    }

    public Event(String name, String venue, String date, String url) {

        this.name = name;
        this.venue = venue;
        this.date = date;
        this.url = url;
    }

    protected Event(Parcel in) {
        name = in.readString();
        venue= in.readString();
        date = in.readString();
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
        dest.writeString(venue);
        dest.writeString(date);
        dest.writeString(url);
    }
}
