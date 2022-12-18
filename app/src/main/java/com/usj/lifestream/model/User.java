package com.usj.lifestream.model;

import java.security.Timestamp;
import java.util.Date;

public class User {
    public String firstName,lastName,telephone,email,addressLine1,addressLine2,town,district,gender,dateOfBirth,bloodGroup,maritalState,occupation,profileURL;
    public boolean isDonor;

    public User() {
    }

    public User(String firstName, String lastName, String telephone, String email, String addressLine1, String addressLine2, String town, String district, String gender, String dateOfBirth, String bloodGroup, String maritalState, String occupation, String profileURL, boolean isDonor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.district = district;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.bloodGroup = bloodGroup;
        this.maritalState = maritalState;
        this.occupation = occupation;
        this.profileURL = profileURL;
        this.isDonor = isDonor;
    }
}