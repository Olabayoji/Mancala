package com.example.mancalapro.model;

import java.util.List;

/**
 * Represents the game board in the Mancala Pro application.
 * Handles the game board setup, stones, and player stores.
 * 
 * @author Olabayoji Oladepo
 */
public class Board {
    public static final int ROWS = 2;
    public static final int HOLES_PER_ROW = 6;
    private static final int INITIAL_PIECES = 4;

    private Hole[][] holes;
    private int[] stores;

    public Board() {
        initializeBoard();
    }

    private void initializeBoard() {
        holes = new Hole[ROWS][HOLES_PER_ROW];
        stores = new int[ROWS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < HOLES_PER_ROW; col++) {
                holes[row][col] = new Hole(INITIAL_PIECES);
            }
            stores[row] = 0;
        }
    }

    public Hole getHole(int row, int col) {
        return holes[row][col];
    }

    public int getStore(int playerIndex) {
        return stores[playerIndex];
    }

    public void addStoneToStore(int playerIndex, Stone stone, boolean doublePointsMode) {
        if (doublePointsMode) {
            stores[playerIndex] += 2;
        } else {
            stores[playerIndex]++;
        }
    }

    public boolean isSideEmpty(int playerIndex) {

        for (int col = 0; col < HOLES_PER_ROW; col++) {
            if (!holes[playerIndex][col].isEmpty()) {
                System.out.println(holes[playerIndex][col].isEmpty());
                System.out.println("Is side empty called: false");

                return false;
            }
        }
        System.out.println("Is side empty called: true");

        return true;
    }

    public void collectRemainingStones(int playerIndex) {
        for (int col = 0; col < HOLES_PER_ROW; col++) {
            int stones = holes[playerIndex][col].getNumberOfPieces();
            stores[playerIndex] += stones;
            holes[playerIndex][col].pickUpStones();
        }
    }

    public void halfHand(int playerIndex) {
        for (int i = 0; i < HOLES_PER_ROW; i++) {
            Hole hole = getHole(playerIndex, i);
            int originalNumberOfStones = hole.getNumberOfPieces();
            int newNumberOfStones = originalNumberOfStones / 2;
            for (int j = 0; j < originalNumberOfStones - newNumberOfStones; j++) {
                hole.pickUpStones().remove(0);
            }
        }
    }

    public void switchSides() {
        for (int i = 0; i < HOLES_PER_ROW; i++) {
            Hole hole0 = getHole(0, i);
            Hole hole1 = getHole(1, i);
            List<Stone> tempStones = hole0.pickUpStones();
            hole0.addStones(hole1.pickUpStones());
            hole1.addStones(tempStones);
        }
    }

}