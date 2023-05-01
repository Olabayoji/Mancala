package com.example.mancalapro.model;


import java.util.List;
import java.util.Random;

public class MancalaGame {
    private Board board;
    private Player[] players;
    private int currentPlayerIndex;
    private boolean isArcadeMode;
    private Random random;

    public MancalaGame(Player player1, Player player2, boolean isArcadeMode) {
        board = new Board();
        players = new Player[2];
        players[0] = player1;
        players[1] = player2;
        currentPlayerIndex = new Random().nextInt(2);
        this.isArcadeMode = isArcadeMode;
        this.random = new Random();
    }

    public Board getBoard() {
        return board;
    }

    public Player getCurrentPlayer() {
        return players[currentPlayerIndex];
    }

    public boolean usePowerUp(PowerUp powerUp) {
        return getCurrentPlayer().usePowerUp(powerUp);
    }

    public boolean move(int holeIndex, PowerUp powerUp, int side) {

        holeIndex = holeIndex < 6 ? holeIndex : holeIndex - 7;
        // Handle power-ups before making the move
        if (isArcadeMode && powerUp != null) {
            if (!usePowerUp(powerUp)) {
                return false;
            }
        }

        Hole hole = board.getHole(currentPlayerIndex, holeIndex);
        if (hole.isEmpty()) {
            return false;
        }
        int nextPlayerIndex = currentPlayerIndex == 0 ? 0 : 1;

        List<Stone> stones = hole.pickUpStones();
        System.out.println("total number of stones: " + stones.size());
        int position = holeIndex;
        boolean extraTurn = false;
        boolean myStore = true;

        while (!stones.isEmpty()) {
            position = (position + 1) % (Board.HOLES_PER_ROW + 1);
            System.out.println("Postion: " + position + "No of stones: " + stones.size());
//            System.out.println(stones.size());

            if (position == Board.HOLES_PER_ROW) {
                if (myStore) {
                    Stone stone = stones.remove(0);
                    board.addStoneToStore(currentPlayerIndex, stone);
                }
                myStore = !myStore;


                side = (side + 1) % 2;
                if (stones.isEmpty()) { // Check if it's the last stone
                    extraTurn = true;
                }
            } else {
                Hole nextHole = board.getHole(side, position);
                Stone stone = stones.remove(0);
                nextHole.addStone(Stone.createSpecialStoneWithChance());
                Stone lastStone = nextHole.getLastStone();

                if (stones.isEmpty() && nextHole.getNumberOfPieces() > 1) {
                    stones.addAll(nextHole.pickUpStones());
                }

                // Handle special stones if in arcade mode
                if (isArcadeMode && lastStone.getType() != Stone.Type.REGULAR) {
                    handleSpecialStone(lastStone.getType(), currentPlayerIndex);
                    // Remove the special stone after handling it
                    nextHole.pickUpStones().remove(nextHole.getNumberOfPieces() - 1);
                }
            }
        }

        if (!extraTurn) {
            currentPlayerIndex = (currentPlayerIndex + 1) % 2;
        }

        return true;
    }


    public boolean isGameOver() {
        return board.isSideEmpty(0) || board.isSideEmpty(1);
    }

    public int getWinner() {
        if (!isGameOver()) {
            return -1;
        }

        board.collectRemainingStones(0);
        board.collectRemainingStones(1);

        int store0 = board.getStore(0);
        int store1 = board.getStore(1);

        if (store0 > store1) {
            return 0;
        } else if (store1 > store0) {
            return 1;
        } else {
            return -1;
        }
    }

    private PowerUp getSpecialStone() {
        int chance = random.nextInt(10);
        if (chance == 0) {
            int specialStoneIndex = random.nextInt(3);
            switch (specialStoneIndex) {
                case 0:
                    return PowerUp.HALF_HAND;
                case 1:
                    return PowerUp.REVERSE_TURN;
                case 2:
                    return PowerUp.SWITCH_SIDES;
            }
        }
        return null;
    }

    private void handleSpecialStone(Stone.Type specialStone, int playerIndex) {
        switch (specialStone) {
            case HALF_HAND:
                board.halfHand(playerIndex);
                break;
            case REVERSE_TURN:
                currentPlayerIndex = (currentPlayerIndex + 1) % 2;
                break;
            case SWITCH_SIDES:
                board.switchSides();
                break;
        }
    }

}
