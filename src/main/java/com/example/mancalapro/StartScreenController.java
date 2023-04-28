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

public class StartScreenController implements Initializable {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text txErrorMsg;
    @FXML
    private Text btnRegister;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    private Parent root;
    private  Stage stage;
    private Scene scene;
    private int loginAttempts;
    String databasePath = "./src/main/gameData/database.json";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loginAttempts = 5;
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String username = txtUsername.getText();
                String password = txtPassword.getText();
                if (username.isEmpty() || password.isEmpty()) {
                    txErrorMsg.setFill(Color.FIREBRICK);
                    txErrorMsg.setText("Username and password required.");
                    return;
                }

                // Check if the user exists in the database and if the password is correct
                Database database = Database.getInstance();
                User user = database.getUser(username);

                if (user != null && user.getPassword().equals(password) && user.isApproved()) {
                    txErrorMsg.setFill(Color.FORESTGREEN);
                    txErrorMsg.setText("Login successful.");
                    System.out.println(user instanceof Player);

                    user.updateLastLogin();
//                    DatabaseManager.saveDatabaseInstance();
                    try {
                        ContextManager contextManager = ContextManager.getInstance();
                        // Store the current user's username in ContextManager
                        ContextManager.getInstance().addToContext("currentUser", user.getUserName());

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
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
                            "Contact admin.\nYou need to be approved");
                } else {
                    txErrorMsg.setFill(Color.FIREBRICK);
                    txErrorMsg.setText(
                            "Incorrect username/password. \n" + --loginAttempts + " login attempts remaining.");
                    if (loginAttempts == 0) {
                        Platform.exit();
                        System.exit(0);
                    }
                }
            }
        });

        btnRegister.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
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

    // public void initManager
}
