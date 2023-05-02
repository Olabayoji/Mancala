package com.example.mancalapro;

import com.example.mancalapro.model.Bot;
import com.example.mancalapro.model.ContextManager;
import com.example.mancalapro.model.DatabaseManager;
import com.example.mancalapro.model.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnArcade.setOnMouseClicked(mouseEvent -> {

            try {
                // Thread.sleep(2000);
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ArcadeScreen.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }

        });
        btnClassicAI.setOnMouseClicked(mouseEvent -> {

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

        });
        btnClassic.setOnMouseClicked(mouseEvent -> {

            try {
                DatabaseManager.saveDatabaseInstance();
                ContextManager contextManager = ContextManager.getInstance();
                // Store the game mode in ContextManager
                contextManager.addToContext("mode", "human");
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

        });
        btnMainMenu.setOnMouseClicked(mouseEvent -> {
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

        });

    }
}
