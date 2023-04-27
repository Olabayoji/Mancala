package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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

    public void addFavorite(Player favorite) {
        this.favorite.add(favorite);
    }

    public  double getWinRatio() {
        if (getNumberOfGames() == 0){
            return 0;
        }
        return getNumberOfWins() / getNumberOfGames();
    }

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