package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.List;

/**
 * - getNumberOfPieces(): Returns the number of stones in the hole.
 * - pickUpStones(): Removes and returns all stones from the hole.
 * - addStone(Stone stone): Adds a stone to the hole.
 * - isEmpty(): Returns true if the hole is empty, and false otherwise.
 */
public class Hole {
    private List<Stone> stones;

    public Hole(int initialPieces) {
        stones = new ArrayList<>(initialPieces);
        for (int i = 0; i < initialPieces; i++) {
            stones.add(new Stone(Stone.Type.REGULAR));
        }
    }

    public int getNumberOfPieces() {
        return stones.size();
    }

    public List<Stone> pickUpStones() {
        List<Stone> pickedUpStones = new ArrayList<>(stones);
        stones.clear();
        return pickedUpStones;
    }

    public void addStone(Stone stone) {
        stones.add(stone);
    }

    public void addStones(List<Stone> newStones) {
        stones.addAll(newStones);
    }

    public Stone getLastStone() {
        return stones.isEmpty() ? null : stones.get(stones.size() - 1);
    }

    public boolean isEmpty() {
        return stones.isEmpty();
    }
}
