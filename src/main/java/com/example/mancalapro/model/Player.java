package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Player extends User {
    private int numberOfGames;
    private int numberOfWins;
    private boolean approved;
    private List<Player> favorite;

    public Player(String firstName, String lastName, String userName, String profileImage, String password) {
        super(firstName, lastName, userName, profileImage, password);
        this.numberOfGames = 0;
        this.numberOfWins = 0;
        this.approved = false;
        this.favorite = new ArrayList<>();
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

    public List<Player> getFavorite() {
        return favorite;
    }

    public void setFavorite(List<Player> favorite) {
        this.favorite = favorite;
    }

}