package com.usj.lifestream.model;

public class BloodRequest {
    public String patientName,mobile,place,bloodGroup,date,time;
    public boolean isEmergency;

    public BloodRequest() {
    }

    public BloodRequest(String patientName, String mobile, String place, String bloodGroup, String date, String time, boolean isEmergency) {
        this.patientName = patientName;
        this.mobile = mobile;
        this.place = place;
        this.bloodGroup = bloodGroup;
        this.date = date;
        this.time = time;
        this.isEmergency = isEmergency;
    }
}
