package com.example.mancalapro;

import com.example.mancalapro.model.ContextManager;
import com.example.mancalapro.model.DatabaseManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.ResourceBundle;

public class ModeSelectionController implements Initializable {
    @FXML
    public ImageView btnMainMenu;
    @FXML
    public Group btnClassic;
    @FXML
    public Group btnClassicAI;
    @FXML
    public Group btnArcade;
    @FXML
    public Group btnArcadeAI;
    @FXML
    public AnchorPane rootPane;

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * ModeSelectionController class handles the mode selection interface of the
     * Mancala game application.
     * It manages button actions for selecting different game modes and navigating
     * back to the main menu.
     *
     * @author Olabayoji Oladepo
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnArcade.setOnMouseClicked(mouseEvent -> handleArcadeModeVsHuman());
        btnClassicAI.setOnMouseClicked(mouseEvent -> handleClassicModeVsAI(mouseEvent));
        btnClassic.setOnMouseClicked(mouseEvent -> handleClassicModeVsHuman());
        btnMainMenu.setOnMouseClicked(mouseEvent -> handleBackToMainMenu(mouseEvent));
    }

    /**
     * Handles the action of selecting arcade mode vs Human.
     *
     */
    private void handleArcadeModeVsHuman() {
        try {
            DatabaseManager.saveDatabaseInstance();
            ContextManager contextManager = ContextManager.getInstance();
            // Store the game mode in ContextManager
            contextManager.addToContext("mode", "human");
            contextManager.addToContext("type", "Arcade");
            // Load and display the MultiPlayerModal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiPlayerModal.fxml"));
            Parent modalRoot = loader.load();
            Stage modalStage = new Stage();

            // Set up the modal stage
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(rootPane.getScene().getWindow());
            modalStage.setTitle("Arcade Mode (VS Human)");

            Scene modalScene = new Scene(modalRoot);
            modalStage.setScene(modalScene);

            // Show the modal and wait for it to be closed
            modalStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action of selecting classic mode vs AI.
     *
     * @param mouseEvent  The ActionEvent that triggers this method.
     * @param currentUser The current user who initiated the multiplayer mode.
     */
    private void handleClassicModeVsAI(EventObject mouseEvent) {

        try {
            DatabaseManager.saveDatabaseInstance();
            ContextManager contextManager = ContextManager.getInstance();

            // Store the game mode in ContextManager
            contextManager.addToContext("mode", "AI");
            contextManager.addToContext("secondPlayer", "Bot");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassicScreen.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (/* InterruptedException | */IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action of selecting classic mode vs Human.
     * 
     */
    private void handleClassicModeVsHuman() {
        try {
            DatabaseManager.saveDatabaseInstance();
            ContextManager contextManager = ContextManager.getInstance();
            // Store the game mode in ContextManager
            contextManager.addToContext("mode", "human");
            contextManager.addToContext("type", "Classic");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("MultiPlayerModal.fxml"));
            Parent modalRoot = loader.load();
            Stage modalStage = new Stage();

            // Set up the modal stage
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.initOwner(rootPane.getScene().getWindow());
            modalStage.setTitle("Classic Mode (VS Human)");

            Scene modalScene = new Scene(modalRoot);
            modalStage.setScene(modalScene);

            // Show the modal and wait for it to be closed
            modalStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action of clicking back button .
     *
     * @param mouseEvent The ActionEvent that triggers this method.
     * 
     */
    private void handleBackToMainMenu(EventObject mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (/* InterruptedException | */IOException e) {
            throw new RuntimeException(e);
        }

    }
}
