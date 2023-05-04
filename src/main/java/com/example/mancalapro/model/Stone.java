package com.example.mancalapro.model;

import java.util.Random;

/**
 * Represents a stone in the Mancala Pro application with various types.
 *
 * @author Daisy Morrison
 */
public class Stone {

    public enum Type {
        REGULAR,
        HALF_HAND,
        REVERSE_TURN,
        SWITCH_SIDES
    }

    private Type type;

    /**
     * Constructor for Stone class.
     *
     * @param type the type of the stone
     */
    public Stone(Type type) {
        this.type = type;
    }

    /**
     * Returns the type of the stone.
     *
     * @return the type of the stone
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the type of the stone.
     *
     * @param type the type to set
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Creates and returns a special stone with a chance.
     *
     * @return a Stone object with a special or regular type
     */
    public static Stone createSpecialStoneWithChance() {
        int randomValue = new Random().nextInt(10);
        if (randomValue < 3) {
            return new Stone(Type.values()[randomValue + 1]);
        } else {
            return new Stone(Type.REGULAR);
        }
    }
}
