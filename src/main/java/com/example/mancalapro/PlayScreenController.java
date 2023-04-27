package com.example.mancalapro;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PlayScreenController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ImageView btnMainMenu;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btnMainMenu.setOnMouseClicked(mouseEvent -> {

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to end the game?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Parent newPane = FXMLLoader.load(getClass().getClassLoader().getResource("view/MainMenu.fxml"));
                    rootPane.getChildren().setAll(newPane);
                }
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }

        });
    }
}
