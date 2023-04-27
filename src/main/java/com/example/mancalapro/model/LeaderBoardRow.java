package com.example.mancalapro.model;
public class LeaderBoardRow {
    private int ranking;
    private String username;
    private int totalMatches;
    private int totalLosses;
    private int totalWins;
    private double winPercentage;
    private boolean favorite;

    public LeaderBoardRow(int ranking, String username, int totalMatches, int totalLosses, int totalWins, double winPercentage, boolean favorite) {
        this.ranking = ranking;
        this.username = username;
        this.totalMatches = totalMatches;
        this.totalLosses = totalLosses;
        this.totalWins = totalWins;
        this.winPercentage = winPercentage;
        this.favorite = favorite;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalMatches() {
        return totalMatches;
    }

    public void setTotalMatches(int totalMatches) {
        this.totalMatches = totalMatches;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void setTotalLosses(int totalLosses) {
        this.totalLosses = totalLosses;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public double getWinPercentage() {
        return winPercentage;
    }

    public void setWinPercentage(double winPercentage) {
        this.winPercentage = winPercentage;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
