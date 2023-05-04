package com.example.mancalapro.model;

/**
 * Represents an administrator in the Mancala Pro application.
 * Inherits from the User class and has additional methods for managing player
 * accounts.
 * 
 * @author Olabayoji Oladepo
 */
public class Admin extends User {
    public Admin(String firstName, String lastName, String userName, String profileImage, String password) {
        super(firstName, lastName, userName, profileImage, password);
    }

    /**
     * Approves a player's account.
     * 
     * @param player The player whose account will be approved.
     */
    public void approvePlayer(Player player) {
        player.setApproved(true);
    }

}