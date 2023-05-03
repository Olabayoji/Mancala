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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller for the admin menu screen in the Mancala game application.
 * 
 * @author Olabayoji Oladepo
 */
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

    /**
     * Sets the username for the admin.
     * 
     * @param username The admin's username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Initializes the controller class.
     * 
     * @param location  The location used to resolve relative paths for the root
     *                  object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Handle approval button click event
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

        // Handle profile button click event
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

        // Handle logout button click event
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

        // Handle exit button click event
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
