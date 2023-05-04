package com.example.mancalapro;

import com.example.mancalapro.model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * MultiPlayerModalController class handles the multiplayer login interface of
 * the Mancala game application.
 * It manages the input fields for the second player's username and password,
 * and the login button action.
 *
 * @author Olabayoji Oladepo
 */
public class MultiPlayerModalController implements Initializable {

    @FXML
    private Text txErrorMsg;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnAdd;

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object,
     *                  or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the
     *                  root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the current user's username from ContextManager
        String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");

        // Retrieve the current user's instance from the Database through
        // DatabaseManager
        User currentUser = DatabaseManager.getDatabaseInstance().getUser(currentUsername);

        // Set up the button action for adding a second player (multiplayer mode)
        btnAdd.setOnAction(event -> handleAddSecondPlayer(event, currentUser));
    }

    /**
     * Handles the action of adding a second player in multiplayer mode.
     *
     * @param event       The ActionEvent that triggers this method.
     * @param currentUser The current user who initiated the multiplayer mode.
     */
    private void handleAddSecondPlayer(ActionEvent event, User currentUser) {
        String username = txtUsername.getText().toLowerCase();
        String password = txtPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("Username and password required.");
            return;
        }

        // Check if the user exists in the database and if the password is correct
        Database database = Database.getInstance();
        User user = database.getUser(username);

        if (username.equals(currentUser.getUserName())) {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("You cannot play against yourself");
            return;
        }

        if (user != null && user.getPassword().equals(password) && user.isApproved() && user instanceof Player) {
            handleSuccessfulLogin(event, user);
        } else if (user != null && user.getPassword().equals(password) && !user.isApproved()) {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("Contact admin.\nPlayer needs to be approved");
        } else {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("Incorrect username/password.");
        }
    }

    /**
     * Handles the successful login of the second player.
     *
     * @param event The ActionEvent that triggers this method.
     * @param user  The second player who successfully logged in.
     */
    private void handleSuccessfulLogin(ActionEvent event, User user) {
        txErrorMsg.setFill(Color.FORESTGREEN);
        txErrorMsg.setText("Login successful.");

        user.updateLastLogin();
        ContextManager contextManager = ContextManager.getInstance();

        // Store the second player's username in ContextManager
        contextManager.addToContext("secondPlayer", user.getUserName());
        String gameType = (String) contextManager.retrieveFromContext("type");

        // Close the modal stage
        closeModal(event);

        // Navigate to the appropriate game screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource(gameType.equals("Classic") ? "ClassicScreen.fxml" : "ArcadeScreen.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (/* InterruptedException | */IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the current modal stage.
     *
     * @param event The ActionEvent that triggers this method.
     */
    private void closeModal(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}