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

    private void closeModal(ActionEvent event) {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the current user's username from ContextManager
        String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");

        // Retrieve the current user's instance from the Database through
        // DatabaseManager
        User currentUser = DatabaseManager.getDatabaseInstance().getUser(currentUsername);

        btnAdd.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
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
                if (user != null && user.getPassword().equals(password) && user.isApproved()
                        && user instanceof Player) {
                    txErrorMsg.setFill(Color.FORESTGREEN);
                    txErrorMsg.setText("Login successful.");

                    user.updateLastLogin();
                    // DatabaseManager.saveDatabaseInstance();
                    ContextManager contextManager = ContextManager.getInstance();

                    // Store the second player's username in ContextManager
                    contextManager.addToContext("secondPlayer", user.getUserName());
                    ;
                    // Close the modal stage
                    Stage modalStage = (Stage) btnAdd.getScene().getWindow();
                    modalStage.close();

                    try {
                        // Navigate to ClassicScreen.fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClassicScreen.fxml"));
                        root = loader.load();
                        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    } catch (/* InterruptedException | */IOException e) {
                        throw new RuntimeException(e);
                    }

                } else if (user != null && user.getPassword().equals(password) && !user.isApproved()) {
                    txErrorMsg.setFill(Color.FIREBRICK);
                    txErrorMsg.setText(
                            "Contact admin.\nPlayer needs to be approved");
                } else {
                    txErrorMsg.setFill(Color.FIREBRICK);
                    txErrorMsg.setText(
                            "Incorrect username/password.");

                }
            }
        });

    }
}
