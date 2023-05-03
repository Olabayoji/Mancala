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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * StartScreenController class is responsible for managing the start screen of
 * the application.
 * It handles user login and registration, and directs users to either the main
 * menu for players or the admin menu for administrators.
 * 
 * @author Olabayoji Oladepo
 */
public class StartScreenController implements Initializable {
    private static final String DATABASE_FILE = "./src/main/gameData/database.json";

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
    private Stage stage;
    private Scene scene;
    private int loginAttempts;

    /**
     * 
     * Initializes the StartScreenController and sets up necessary event
     * handlers.
     * 
     * @param location  The location used to resolve relative paths for the
     *                  root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if
     *                  the root object was
     *                  not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initLoginButton();
        initRegisterButton();
        loginAttempts = 5;
    }

    /**
     * Initializes the login button and its action event handler.
     */
    private void initLoginButton() {
        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                processLogin(event);
            }
        });
    }

    /**
     * Processes the login event when the login button is clicked.
     *
     * @param event the ActionEvent for the login button.
     */
    private void processLogin(ActionEvent event) {
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

        if (user != null && user.getPassword().equals(password) && user.isApproved()) {
            txErrorMsg.setFill(Color.FORESTGREEN);
            txErrorMsg.setText("Login successful.");

            user.updateLastLogin();

            try {
                ContextManager contextManager = ContextManager.getInstance();
                // Store the current user's username in ContextManager
                ContextManager.getInstance().addToContext("currentUser", user.getUserName());
                FXMLLoader loader;

                if (user instanceof Player) {
                    loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("AdminMenu.fxml"));
                }

                root = loader.load();
                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else if (user != null && user.getPassword().equals(password) && !user.isApproved()) {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("Contact admin.\nYou need to be approved");
        } else {
            txErrorMsg.setFill(Color.FIREBRICK);
            txErrorMsg.setText("Incorrect username/password. \n" + --loginAttempts + " login attempts remaining.");

            if (loginAttempts == 0) {
                Platform.exit();
                System.exit(0);
            }
        }
    }

    /**
     * Initializes the register button and its mouse-click event handler.
     */
    private void initRegisterButton() {
        btnRegister.setOnMouseClicked(mouseEvent -> {
            navigateToRegister(mouseEvent);
        });
    }

    /**
     * Navigates to the registration screen when the register button is clicked.
     *
     * @param mouseEvent the MouseEvent for the register button.
     */
    private void navigateToRegister(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            root = loader.load();
            stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}