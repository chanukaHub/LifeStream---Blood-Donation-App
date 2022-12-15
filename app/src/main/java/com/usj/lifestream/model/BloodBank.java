package com.usj.lifestream.model;

public class BloodBank {
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
}
