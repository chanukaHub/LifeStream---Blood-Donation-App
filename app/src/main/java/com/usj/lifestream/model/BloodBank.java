package com.usj.lifestream.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BloodBank implements Parcelable {
    public String name,address,location,hours;
    public double latitude,longitude;

    public BloodBank() {
    }

    public BloodBank(String name) {
        this.name = name;
    }

    public BloodBank(String name, String address, String location, String hours, double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.location = location;
        this.hours = hours;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected BloodBank(Parcel in) {
        name = in.readString();
        address = in.readString();
        location = in.readString();
        hours = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<BloodBank> CREATOR = new Creator<BloodBank>() {
        @Override
        public BloodBank createFromParcel(Parcel in) {
            return new BloodBank(in);
        }

        @Override
        public BloodBank[] newArray(int size) {
            return new BloodBank[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(location);
        dest.writeString(hours);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}
