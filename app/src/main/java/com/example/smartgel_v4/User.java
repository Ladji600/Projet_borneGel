package com.example.smartgel_v4;

public class User {
    private String fullName;
    private int id;

    public User(String fullName, int id) {
        this.fullName = fullName;
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getId() {
        return id;
    }
}
