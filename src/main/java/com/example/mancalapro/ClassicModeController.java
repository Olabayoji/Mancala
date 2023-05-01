package com.example.mancalapro;

import com.example.mancalapro.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

public class ClassicModeController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView btnMainMenu;
    @FXML
    private Label label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label13;
    @FXML
    private Pane pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the players username from ContextManager
        String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
        String secondPlayer = (String) ContextManager.getInstance().retrieveFromContext("secondPlayer");


        // Retrieve the current user's instance from the Database through
        // DatabaseManager
        Player p1 = (Player) DatabaseManager.getDatabaseInstance().getUser("player1");
        Player p2 = (Player) DatabaseManager.getDatabaseInstance().getUser("opeyemi");
        game = new MancalaGame(p1, p2, false); // false for classic mode
        player1.setText(p1.getUserName());
        player2.setText(p2.getUserName());

        // Initialize labels and pits arrays
        labels = new Label[]{label0, label1, label2, label3, label4, label5, label6, label7, label8, label9, label10, label11, label12, label13};
        pits = new Pane[]{pit0, pit1, pit2, pit3, pit4, pit5, pit6, pit7, pit8, pit9, pit10, pit11, pit12, pit13};

        // Update UI with initial board state
        updateUI(p1, p2);


        // Add click event listeners to pits
        for (int i = 0; i < 14; i++) {

            if (i == 6 || i == 13) continue; // Skip stores
            final int pitIndex = i;
            pits[i].setOnMouseClicked(event -> {
                if (pitIndex > 6 && game.getCurrentPlayer().equals(p1) || pitIndex < 6 && game.getCurrentPlayer().equals(p2)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("INVALID MOVE");
                    alert.setHeaderText("You cannot choose another player's hole.");
                    alert.showAndWait();
                    return;
                }
                if (!game.isGameOver()) {
                    game.move(pitIndex, null, game.getCurrentPlayer().equals(p1) ? 0 : 1); // Pass null for PowerUp in classic mode
                    updateUI(p1, p2);
                }
                // TODO add additional logic to handle the end of the game, player turn change, etc.
            });


        }

        btnMainMenu.setOnMouseClicked(mouseEvent -> {

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to end the game?\nThis will automatically count as a loss");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                    root = loader.load();
                    stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

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

            if (i >= 0 && i <= 5 && game.getCurrentPlayer().getUserName().equals(currentUser.getUserName()) && stoneCount > 0) {
//                pits[i].setStyle("-fx-background-color: #ffffff08;");
                pits[i].setStyle("-fx-background-color: #d6d6d6;");
                labels[i].setStyle("-fx-text-fill: #000");


            } else if (i >= 7 && i <= 12 && !game.getCurrentPlayer().getUserName().equals(currentUser.getUserName()) && stoneCount > 0) {
//                pits[i].setStyle("-fx-background-color: #ffffff08;");
                pits[i].setStyle("-fx-background-color: #d6d6d6;");
                labels[i].setStyle("-fx-text-fill: #000");
            } else if (i == 6 || i == 13) {
                pits[i].setStyle("");
            } else {
//                pits[i].setStyle("-fx-background-color: #d6d6d6;");
                pits[i].setStyle("-fx-background-color: #ffffff08;");
                labels[i].setStyle("-fx-text-fill: #fff");


            }
        }
    }


}
