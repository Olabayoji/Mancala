package com.example.mancalapro.model;

import java.util.Random;

public class Stone {

    public enum Type {
        REGULAR,
        HALF_HAND,
        REVERSE_TURN,
        SWITCH_SIDES
    }

    private Type type;

    public Stone(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
    public static Stone createSpecialStoneWithChance() {
        int randomValue = new Random().nextInt(10);
        if (randomValue < 3) {
            return new Stone(Type.values()[randomValue + 1]);
        } else {
            return new Stone(Type.REGULAR);
        }
    }
}
