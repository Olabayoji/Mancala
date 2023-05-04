package com.example.mancalapro;

import com.example.mancalapro.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ClassicModeController is a controller class for the classic mode of the
 * Mancala game.
 * It manages the user interactions, game logic, and updates the UI accordingly.
 *
 * @author Olabayoji Oladepo
 */
public class ClassicModeController implements Initializable {

    @FXML
    private ImageView btnMainMenu;
    @FXML
    private Label label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11,
            label12, label13;
    @FXML
    private Pane pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13;
    @FXML
    private Button btnP1DoublePoint, btnP1ContinueTurn, btnP2ContinueTurn, btnP2DoublePoint;

    @FXML
    private Text player1;
    @FXML
    private Text player2;
    @FXML
    private Text playerTurn;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private MancalaGame game;
    private Label[] labels;
    private Pane[] pits;
    private boolean isArcadeMode;
    private Player p1;
    private Player p2;
    private boolean doublePointActivated = false;
    private boolean continueTurnActivated = false;

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the players username from ContextManager
        String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
        String secondPlayer = (String) ContextManager.getInstance().retrieveFromContext("secondPlayer");
        String mode = (String) ContextManager.getInstance().retrieveFromContext("mode");
        String type = (String) ContextManager.getInstance().retrieveFromContext("type");

        // Retrieve the current user's instance from the Database through
        // DatabaseManager
        p1 = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
        p2 = mode.equals("human") ? (Player) DatabaseManager.getDatabaseInstance().getUser(secondPlayer) : new Bot();
        isArcadeMode = type.equalsIgnoreCase("arcade");
        setupPowerUpButtons();
        game = new MancalaGame(p1, p2, isArcadeMode);
        player1.setText(p1.getUserName());
        player2.setText(p2.getUserName());

        // Update number of gamnes played
        p1.setNumberOfGames(p1.getNumberOfGames() + 1);
        p2.setNumberOfGames(p2.getNumberOfGames() + 1);

        // Update the players' information in the database
        DatabaseManager.getDatabaseInstance().updateUser(p1);
        DatabaseManager.getDatabaseInstance().updateUser(p2);

        // Initialize labels and pits arrays
        labels = new Label[] { label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10,
                label11, label12, label13 };
        pits = new Pane[] { pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13 };

        // Update UI with initial board state
        updateUI(p1, p2);

        // if bot is the starting player
        if (game.getCurrentPlayer() instanceof Bot) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("START GAME");
            alert.setHeaderText("Click any hole on the board to start the game.");
            alert.showAndWait();
        }

        // Add click event listeners to holes
        for (int i = 0; i < 14; i++) {

            if (i == 6 || i == 13)
                continue; // Skip stores
            final int pitIndex = i;
            pits[i].setOnMouseClicked(event -> {
                PowerUp selectedPowerUp = null;
                if (isArcadeMode) {
                    selectedPowerUp = doublePointActivated ? PowerUp.DOUBLE_POINTS
                            : continueTurnActivated ? PowerUp.CONTINUE_TURN : null;
                }
                handlePlayerMove(pitIndex, selectedPowerUp, p1, p2);
                // reset power up
                doublePointActivated = false;
                continueTurnActivated = false;
            });

        }

        // Handles back button click event
        btnMainMenu.setOnMouseClicked(mouseEvent -> {
            try {
                handleMainMenuClick(p1, p2, mouseEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    /**
     * Handles the player's move when a hole is clicked, processes the selected
     * power-up, and updates the UI.
     *
     * @param pitIndex The index of the pit that the player has chosen.
     * @param powerUp  The selected power-up, or null if no power-up is selected.
     * @param p1       The first player.
     * @param p2       The second player.
     */
    private void handlePlayerMove(int pitIndex, PowerUp powerUp, Player p1, Player p2) {
        if (!(game.getCurrentPlayer() instanceof Bot) && (pitIndex > 6 && game.getCurrentPlayer().equals(p1)
                || pitIndex < 6 && game.getCurrentPlayer().equals(p2))) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("INVALID MOVE");
            alert.setHeaderText("You cannot choose another player's hole.");
            alert.showAndWait();
            return;
        }
        game.move(pitIndex, powerUp, game.getCurrentPlayer().equals(p1) ? 0 : 1);
        updateUI(p1, p2);
        boolean gameOver = game.isGameOver();

        if (gameOver) {
            System.out.println("Game over");
            int winner = game.getWinner();
            updateUI(p1, p2);
            showGameOverAlert(winner, p1, p2);
        } else if (game.getCurrentPlayer() instanceof Bot) {
            int botMove = ((Bot) game.getCurrentPlayer()).generateMove(game.getBoard());
            handlePlayerMove(botMove, null, p1, p2);
        }
    }

    /**
     * Sets up power-up buttons.
     * This also makes the power up button invisible in classic mode
     */
    private void setupPowerUpButtons() {
        btnP1DoublePoint.setVisible(isArcadeMode);
        btnP1ContinueTurn.setVisible(isArcadeMode);
        btnP2DoublePoint.setVisible(isArcadeMode);
        btnP2ContinueTurn.setVisible(isArcadeMode);

        // Set up click listeners for player1 double point power-up button
        btnP1DoublePoint.setOnAction(event -> {
            if (game.getCurrentPlayer().equals(p1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText(
                        game.getCurrentPlayer().getUserName() + " Are you sure you want to use this power up? ");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    doublePointActivated = true;
                    btnP1DoublePoint.setDisable(true);
                }
            }
        });

        // Set up click listeners for player2 double point power-up button
        btnP2DoublePoint.setOnAction(event -> {
            if (game.getCurrentPlayer().equals(p2)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText(
                        game.getCurrentPlayer().getUserName() + " Are you sure you want to use this power up? ");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    doublePointActivated = true;
                    btnP2DoublePoint.setDisable(true);
                }
            }
        });

        // Set up click listeners for player2 continue turn power-up button

        btnP2ContinueTurn.setOnAction(event -> {
            if (game.getCurrentPlayer().equals(p2)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText(
                        game.getCurrentPlayer().getUserName() + " Are you sure you want to use this power up? ");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    continueTurnActivated = true;
                    btnP2ContinueTurn.setDisable(true);
                }
            }
        });
        // Set up click listeners for player1 continue turn power-up button

        btnP1ContinueTurn.setOnAction(event -> {
            if (game.getCurrentPlayer().equals(p1)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText(
                        game.getCurrentPlayer().getUserName() + " Are you sure you want to use this power up? ");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    doublePointActivated = true;
                    btnP1ContinueTurn.setDisable(true);
                }
            }
        });

    }

    /**
     * Handles the main menu button click, showing a confirmation alert and,
     * if the user confirms, ending the game and navigating to the mode selection
     * screen.
     *
     * @param p1         the first player
     * @param p2         the second player
     * @param mouseEvent the MouseEvent triggered by the button click
     * @throws IOException if an error occurs during navigation
     */
    private void handleMainMenuClick(Player p1, Player p2, javafx.scene.input.MouseEvent mouseEvent)
            throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Mancala Game");
        alert.setHeaderText(null);
        alert.setContentText(game.isGameOver() ? "Great Game"
                : "Are you sure you want to end the game " + game.getCurrentPlayer().getUserName()
                        + "?\nThis will automatically count as a loss");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // update current user's number of losses if game is cancelled without
            // completion
            handleGameOverWithoutCompletion(p1, p2);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelection.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
            String mode = (String) ContextManager.getInstance().retrieveFromContext("mode");
            if (!mode.equals("human")) {
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    /**
     * Handles the game over condition when a player decides to end the game without
     * completion.
     * Updates the number of wins and losses for both players.
     *
     * @param p1 the first player
     * @param p2 the second player
     */
    private void handleGameOverWithoutCompletion(Player p1, Player p2) {
        if (!game.isGameOver()) {
            Player currentPlayer = game.getCurrentPlayer();
            Player otherPlayer = currentPlayer == p1 ? p2 : p1;

            currentPlayer.setNumberOfLosses(currentPlayer.getNumberOfLosses() + 1);
            otherPlayer.setNumberOfWins(otherPlayer.getNumberOfWins() + 1);

            // Update the players' information in the database
            DatabaseManager.getDatabaseInstance().updateUser(currentPlayer);
            if (!(otherPlayer instanceof Bot)) {
                DatabaseManager.getDatabaseInstance().updateUser(otherPlayer);
            }
        }
    }

    /**
     * Updates the user interface to reflect the current state of the game,
     * including the displayed board, player turn, and stone counts.
     *
     * @param currentUser  the current player
     * @param secondPlayer the other player
     */
    private void updateUI(Player currentUser, Player secondPlayer) {
        Board board = game.getBoard();
        if (game.getCurrentPlayer().getUserName().equals(currentUser.getUserName())) {
            playerTurn.setText(currentUser.getUserName());
        } else {
            playerTurn.setText(secondPlayer.getUserName());
        }
        for (int i = 0; i < 14; i++) {

            int rowIndex = i < 6 ? 0 : 1;
            int columnIndex = i < 6 ? i : i - 7;
            int stoneCount;
            if (i == 6) {
                stoneCount = board.getStore(0);
            } else if (i == 13) {
                stoneCount = board.getStore(1);
            } else {
                stoneCount = board.getHole(rowIndex, columnIndex).getNumberOfPieces();
            }
            labels[i].setText(Integer.toString(stoneCount));

            if (i >= 0 && i <= 5 && game.getCurrentPlayer().getUserName().equals(currentUser.getUserName())
                    && stoneCount > 0) {
                // pits[i].setStyle("-fx-background-color: #ffffff08;");
                pits[i].setStyle("-fx-background-color: #d6d6d6;");
                labels[i].setStyle("-fx-text-fill: #000");

            } else if (i >= 7 && i <= 12 && !game.getCurrentPlayer().getUserName().equals(currentUser.getUserName())
                    && stoneCount > 0 && !(game.getCurrentPlayer() instanceof Bot)) {
                // pits[i].setStyle("-fx-background-color: #ffffff08;");
                pits[i].setStyle("-fx-background-color: #d6d6d6;");
                labels[i].setStyle("-fx-text-fill: #000");
            } else if (i == 6 || i == 13) {
                pits[i].setStyle("");
            } else {
                // pits[i].setStyle("-fx-background-color: #d6d6d6;");
                pits[i].setStyle("-fx-background-color: #ffffff08;");
                labels[i].setStyle("-fx-text-fill: #fff");

            }
        }
    }

    /**
     * Shows an alert to inform the user that the game is over, displaying the
     * winner
     * and updating the players' win and loss records in the database.
     *
     * @param winner the index of the winning player (-1 for draw, 0 for p1, 1 for
     *               p2)
     * @param p1     the first player
     * @param p2     the second player
     */
    private void showGameOverAlert(int winner, Player p1, Player p2) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        String mode = (String) ContextManager.getInstance().retrieveFromContext("mode");
        if (winner == -1) {
            alert.setHeaderText("The game is a draw!");
        } else {
            Player winningPlayer = winner == 0 ? p1 : p2;
            Player losingPlayer = winner == 0 ? p2 : p1;

            winningPlayer.setNumberOfWins(winningPlayer.getNumberOfWins() + 1);
            losingPlayer.setNumberOfLosses(losingPlayer.getNumberOfLosses() + 1);

            // Update the players' information in the database
            DatabaseManager.getDatabaseInstance().updateUser(winningPlayer);
            if (mode.equals("human")) {// only update other player if
                DatabaseManager.getDatabaseInstance().updateUser(losingPlayer);
            }
            alert.setHeaderText("Player " + winningPlayer.getUserName() + " wins the game!");
        }
        alert.setOnCloseRequest(dialogEvent -> {
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.close();
        });

        alert.showAndWait();
    }

}
