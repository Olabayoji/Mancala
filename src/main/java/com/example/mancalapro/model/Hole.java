package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hole in the Mancala Pro game board containing stones.
 *
 * @author Olabayoji Oladepo
 */
public class Hole {
    private List<Stone> stones;

    /**
     * Constructor for Hole class.
     *
     * @param initialPieces the initial number of pieces in the hole
     */
    public Hole(int initialPieces) {
        stones = new ArrayList<>(initialPieces);
        for (int i = 0; i < initialPieces; i++) {
            stones.add(new Stone(Stone.Type.REGULAR));
        }
    }

    /**
     * Returns the number of pieces in the hole.
     *
     * @return the number of pieces
     */
    public int getNumberOfPieces() {
        return stones.size();
    }

    /**
     * Picks up stones from the hole and returns them.
     *
     * @return a list of stones picked up
     */
    public List<Stone> pickUpStones() {
        List<Stone> pickedUpStones = new ArrayList<>(stones);
        stones.clear();
        return pickedUpStones;
    }

    /**
     * Adds a stone to the hole.
     *
     * @param stone the stone to add
     */
    public void addStone(Stone stone) {
        stones.add(stone);
    }

    /**
     * Adds a list of stones to the hole.
     *
     * @param newStones the list of stones to add
     */
    public void addStones(List<Stone> newStones) {
        stones.addAll(newStones);
    }

    /**
     * Returns the last stone in the hole.
     *
     * @return the last stone or null if the hole is empty
     */
    public Stone getLastStone() {
        return stones.isEmpty() ? null : stones.get(stones.size() - 1);
    }

    /**
     * Returns the if hole is empty.
     *
     * @return if the hole is empty
     */
    public boolean isEmpty() {
        return stones.isEmpty();
    }
}
