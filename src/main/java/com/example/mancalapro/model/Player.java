package com.example.mancalapro.model;

import java.util.*;

public class Player extends User {
    private int numberOfGames;
    private int numberOfWins;
    private int numberOfLosses;
    private boolean publicProfile;
    private List<Player> favorite;
    private Map<PowerUp, Integer> powerUps;


    public Player(String firstName, String lastName, String userName, String profileImage, String password, boolean publicProfile) {
        super(firstName, lastName, userName, profileImage, password);
        this.numberOfGames = 0;
        this.numberOfWins = 0;
        this.numberOfLosses = 0;
        this.favorite = new ArrayList<>();
        this.publicProfile = publicProfile;
        powerUps = new HashMap<>();
        powerUps.put(PowerUp.CONTINUE_TURN, 1);
        powerUps.put(PowerUp.DOUBLE_POINTS, 1);
    }


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

    public Map<PowerUp, Integer> getPowerUps() {
        return powerUps;
    }

    public void setPowerUps(Map<PowerUp, Integer> powerUps) {
        this.powerUps = powerUps;
    }

    public List<Player> getFavorite() {
        return favorite;
    }

    public void addFavorite(Player favorite) {
        this.favorite.add(favorite);
    }

    public double getWinRatio() {
        if (getNumberOfGames() == 0) {
            return 0;
        }
        return getNumberOfWins() / getNumberOfGames();
    }

    public void removeFavorite(Player selectedPlayer) {
        favorite.remove(selectedPlayer);
    }
    public boolean usePowerUp(PowerUp powerUp) {
        int count = powerUps.getOrDefault(powerUp, 0);
        if (count > 0) {
            powerUps.put(powerUp, count - 1);
            return true;
        }
        return false;
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