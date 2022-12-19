package com.usj.lifestream.model;

public class BloodRequest {
    public String patientName,mobile,place,bloodGroup,date,time,note;
    public boolean isEmergency;

    public BloodRequest() {
    }

    public BloodRequest(String patientName, String mobile, String place, String bloodGroup, String date, String time, String note, boolean isEmergency) {
        this.patientName = patientName;
        this.mobile = mobile;
        this.place = place;
        this.bloodGroup = bloodGroup;
        this.date = date;
        this.time = time;
        this.note = note;
        this.isEmergency = isEmergency;
    }
}
