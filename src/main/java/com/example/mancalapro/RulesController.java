package com.example.mancalapro;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RulesController implements Initializable {
    @FXML
    private ImageView btnBack;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text btnArcade;

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * RulesController class handles the classic rules screen of the Mancala
     * game application.
     * It allows the user to navigate back to the main menu or to the arcade rules
     * screen.
     * This controller is associated with the Rules.fxml file.
     *
     * @author Daisy Morrison
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // back button action - navigates to the main screen
        btnBack.setOnMouseClicked(mouseEvent -> {
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

        // arcade button action - navigates to the arcade rules screen

        btnArcade.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("RulesArcade.fxml"));
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
