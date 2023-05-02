package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Bot extends Player {
    public Bot() {
        super("Bot", "Bot", "Bot", "", "bot_password", false);
    }

    public int generateMove(Board board) {

        // Get a list of valid moves
        List<Integer> validMoves = new ArrayList<>();
        for (int i = 0; i < Board.HOLES_PER_ROW; i++) {
            Hole hole = board.getHole(1, i);
            if (!hole.isEmpty()) {
                validMoves.add(i);
            }
        }

        // If no valid moves are available, return -1
        if (validMoves.isEmpty()) {
            return -1;
        }

        // Choose a random valid move
        Random random = new Random();
        int randomMoveIndex = random.nextInt(validMoves.size());
        return validMoves.get(randomMoveIndex);
    }

}
