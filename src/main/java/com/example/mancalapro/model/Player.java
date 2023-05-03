package com.example.mancalapro.model;

import java.util.*;

/**
 * Represents a player in the Mancala application.
 * Inherits from the User class and has additional properties and methods
 * specific to players.
 * 
 * @author Olabayoji Oladepo
 */
public class Player extends User {
    private int numberOfGames;
    private int numberOfWins;
    private int numberOfLosses;
    private boolean publicProfile;
    private List<Player> favorite;

    /**
     * Constructs a new Player with the given attributes.
     *
     * @param firstName     The first name of the player.
     * @param lastName      The last name of the player.
     * @param userName      The unique username of the player.
     * @param profileImage  The profile image URL of the player.
     * @param password      The password for the player's account.
     * @param publicProfile The visibility status of the player's profile.
     */

    public Player(String firstName, String lastName, String userName, String profileImage, String password,
            boolean publicProfile) {
        super(firstName, lastName, userName, profileImage, password);
        this.numberOfGames = 0;
        this.numberOfWins = 0;
        this.numberOfLosses = 0;
        this.favorite = new ArrayList<>();
        this.publicProfile = publicProfile;

    }

    // Getters and setters
    public int getNumberOfGames() {
        return numberOfGames;
    }

    public void setNumberOfGames(int numberOfGames) {
        this.numberOfGames = numberOfGames;
    }

    public int getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(int numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isPublicProfile() {
        return publicProfile;
    }

    public void setPublicProfile() {
        this.publicProfile = !this.publicProfile;

        Player userToUpdate = (Player) Database.getInstance().getUser(this.userName);

        if (userToUpdate != null) {
            userToUpdate.isPublicProfile();
        }
    }

    public int getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(int numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }

    public List<Player> getFavorite() {
        return favorite;
    }

    /**
     * Adds a player to the favorites list.
     *
     * @param favorite The player to be added to the favorites list.
     */

    public void addFavorite(Player favorite) {
        this.favorite.add(favorite);
    }

    /**
     * Computes the win ratio of the player based on the number of wins and total
     * games played.
     *
     * @return The win ratio as a double value.
     */

    public double getWinRatio() {
        if (getNumberOfGames() == 0) {
            return 0;
        }
        return (double) getNumberOfWins() / (double) getNumberOfGames();
    }

    /**
     * Removes a player from the favorites list.
     *
     * @param selectedPlayer The player to be removed from the favorites list.
     */
    public void removeFavorite(Player selectedPlayer) {
        favorite.remove(selectedPlayer);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Player player = (Player) o;
        return Objects.equals(userName, player.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}