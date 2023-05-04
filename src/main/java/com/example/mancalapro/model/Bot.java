package com.example.mancalapro.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a bot player in the Mancala Pro application.
 * Inherits from the Player class and has methods specific to bot behavior.
 *
 * @author Olabayoji Oladepo
 */
public class Bot extends Player {

    /**
     * Constructs a new Bot with predefined attributes.
     */
    public Bot() {
        super("Bot", "Bot", "Bot", "", "bot_password", false);
    }

    /**
     * Generates a valid move for the bot based on the current state of the board.
     *
     * @param board The game board.
     * @return An integer representing the chosen move's index, or -1 if no valid
     *         moves are available.
     */
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
