package com.example.mancalapro;

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

// comment
public class AdminMenuController implements Initializable {

    @FXML
    private ImageView btnLogout;
    @FXML
    private ImageView btnProfile;
    @FXML
    private ImageView btnApproval;
    @FXML
    private ImageView btnStats;
    @FXML
    private VBox btnExit;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        btnApproval.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Approval.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            } catch (/* InterruptedException | */IOException e) {

                throw new RuntimeException(e);

            }

        });
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

        btnLogout.setOnMouseClicked(mouseEvent -> {
            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mancala Game");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to logout?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
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
