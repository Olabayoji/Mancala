package com.example.mancalapro;

import com.example.mancalapro.model.DatabaseManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainMenuController class handles the main menu interface of the Mancala game
 * application.
 * It manages button actions for starting a game, viewing rules, profile,
 * ranking, logout, and exit.
 *
 * @author Olabayoji Oladepo
 */
public class MainMenuController implements Initializable {
    @FXML
    private ImageView btnStartGame;
    @FXML
    private ImageView btnLogout;
    @FXML
    private ImageView btnProfile;
    @FXML
    private ImageView btnRanking;
    @FXML
    private ImageView btnRules;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private VBox btnExit;

    private static final String DATABASE_PATH = "./src/main/gameData/database.json";
    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * Initializes the MainMenuController and sets up the button actions.
     *
     * @param location  the location used to resolve relative paths for the root
     *                  object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Start Game button action
        btnStartGame.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ModeSelection.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }

        });
        // Rules button action
        btnRules.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Rules.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (/* InterruptedException | */IOException e) {

                throw new RuntimeException(e);

            }

        });

        // Profile button action
        btnProfile.setOnMouseClicked(mouseEvent -> {
            try {
                // Thread.sleep(2000);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("PlayerProfile.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (/* InterruptedException | */IOException e) {

                throw new RuntimeException(e);

            }

        });

        // Ranking button action
        btnRanking.setOnMouseClicked(mouseEvent -> {
            try {
                // Thread.sleep(2000);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RankingScreen.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                System.out.println(" navigated");
            } catch (/* InterruptedException | */IOException e) {
                System.out.println("not navigated");
                throw new RuntimeException(e);

            }

        });

        // Logout button action
        btnLogout.setOnMouseClicked(mouseEvent -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to logout?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    DatabaseManager.getDatabaseInstance().saveUsersToJsonFile(DATABASE_PATH);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
                    root = loader.load();
                    stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnExit.setOnMouseClicked(mouseEvent -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Mancala Game");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit the game?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Stage stage = (Stage) btnExit.getScene().getWindow();
                stage.close();
            }

        });

    }
}
