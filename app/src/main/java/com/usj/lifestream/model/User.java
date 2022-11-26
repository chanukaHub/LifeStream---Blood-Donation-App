package com.usj.lifestream.model;

import java.util.Date;

public class User {
    public String firstName,lastName,telephone,email,addressLine1,addressLine2,town,district,gender,bloodGroup,password,maritalState,occupation;
    public Date birthDay;
    public int age;
    public boolean isDonor;

    public User() {
    }

    public User(String firstName, String lastName, String telephone, String email, String addressLine1, String addressLine2, String town, String district, String gender, String bloodGroup, String password, String maritalState, String occupation, Date birthDay, int age, boolean isDonor) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telephone = telephone;
        this.email = email;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.town = town;
        this.district = district;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.password = password;
        this.maritalState = maritalState;
        this.occupation = occupation;
        this.birthDay = birthDay;
        this.age = age;
        this.isDonor = isDonor;
    }
}