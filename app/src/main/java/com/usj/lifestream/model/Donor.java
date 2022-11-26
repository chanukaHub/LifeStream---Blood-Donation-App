package com.usj.lifestream.model;

import java.util.Date;

public class Donor {
    public Date lastDonateDate;
    public float weight,height;
    public int donateCount;

    public Donor() {
    }

    public Donor(Date lastDonateDate, float weight, float height, int donateCount) {
        this.lastDonateDate = lastDonateDate;
        this.weight = weight;
        this.height = height;
        this.donateCount = donateCount;
    }
}
