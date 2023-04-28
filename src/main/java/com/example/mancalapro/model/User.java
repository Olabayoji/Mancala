package com.example.mancalapro.model;

import java.time.LocalDateTime;
import java.util.Date;

public class User {
    protected String firstName;
    protected String lastName;
    protected String userName;
    protected LocalDateTime dateCreated;
    protected LocalDateTime lastLogin;
    protected String profileImage;

    protected String password;
    protected boolean approved;

    public User(String firstName, String lastName, String userName, String profileImage, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.dateCreated = LocalDateTime.now();
        this.lastLogin = LocalDateTime.now();
        this.profileImage = profileImage;
        this.password = password;
        this.approved = false;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}