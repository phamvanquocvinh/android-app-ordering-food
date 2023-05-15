package com.example.finalproject.Model;

public class User {
    private String id;
    private String email;
    private String userFullName;
    private String phoneNumber;
    private String address;

    public User(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User(String id, String email, String userFullName, String phoneNumber,  String address) {
        this.id = id;
        this.email = email;
        this.userFullName = userFullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters và setters cho các thuộc tính

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "User{" +
                ", email='" + email + '\'' +
                ", userFullName='" + userFullName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
