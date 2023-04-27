package com.example.mancalapro.model;

public class Admin extends User {
    public Admin(String firstName, String lastName, String userName, String profileImage, String password) {
        super(firstName, lastName, userName, profileImage, password);
    }

    public void approvePlayer(Player player) {
        player.setApproved(true);
    }

    public void editPlayer(Player player) {

    }
}