package com.example.mancalapro;

import com.example.mancalapro.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * RegisterController class handles the registration screen of the Mancala
 * game application.
 * It allows the user to register as a player or admin and navigate to the
 * login screen.
 * This controller is associated with the Register.fxml file.
 *
 * @author Olabayoji Oladepo
 */
public class RegisterController implements Initializable {
    // Class constants
    private static final String DATABASE_PATH = "./src/main/gameData/database.json";

    // Instance variables
    private ObservableList<String> type = FXCollections.observableArrayList("Player", "Admin");
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text txErrorMsg;
    @FXML
    private Text btnLogin;
    @FXML
    private TextField username;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private ChoiceBox auth;
    @FXML
    private PasswordField password;
    @FXML
    private Button btnRegister;

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAuthChoiceBox();
        initLoginButton();
        initRegisterButton();
    }

    /**
     * Initializes the auth choice box with default values.
     */
    private void initAuthChoiceBox() {
        auth.setItems(type);
        auth.setValue("Player");
    }

    /**
     * Initializes the login button and its mouse-click event handler.
     */
    private void initLoginButton() {
        btnLogin.setOnMouseClicked(mouseEvent -> {
            navigateToLoginScreen(mouseEvent);
        });
    }

    /**
     * Navigates to the login screen when the login button is clicked.
     *
     * @param mouseEvent the MouseEvent for the login button.
     */
    private void navigateToLoginScreen(MouseEvent mouseEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
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
     * Initializes the register button and its mouse-click event handler.
     */
    private void initRegisterButton() {
        btnRegister.setOnMouseClicked(mouseEvent -> {
            registerUser(mouseEvent);
        });
    }

    /**
     * Registers a user when the register button is clicked.
     *
     * @param mouseEvent the MouseEvent for the register button.
     */
    private void registerUser(MouseEvent mouseEvent) {
        try {
            String firstname = firstName.getText().trim().toLowerCase();
            String username = this.username.getText().trim().toLowerCase();
            String lastname = this.lastName.getText().trim().toLowerCase();
            String password = this.password.getText();

            // Validation checks
            if (firstname.isEmpty() || username.isEmpty() || lastname.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("REGISTRATION ERROR");
                alert.setHeaderText("Fields cannot be empty");
                alert.showAndWait();
                return;
            }

            // Validation checks
            if (firstname.length() < 2 || username.length() < 3 || lastname.length() < 3) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("REGISTRATION ERROR");
                alert.setHeaderText(
                        "Username must be at least 3 characters, \nFirst name must be at least 2 characters\nLast name must be at least 2 characters");
                alert.showAndWait();
                return;
            }

            if (password.length() < 5) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("REGISTRATION ERROR");
                alert.setHeaderText("Password must be at least 4 characters");
                alert.showAndWait();
                return;
            }

            if (auth.getValue().equals("Player")) {
                Player player = new Player(firstname, lastname, username, "", password, true);
                Database database = DatabaseManager.getDatabaseInstance();
                if (!database.addUser(player)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("REGISTRATION ERROR");
                    alert.setHeaderText("Username already exists");
                    alert.showAndWait();
                    return;
                }

                DatabaseManager.saveDatabaseInstance();
                var contextManager = ContextManager.getInstance();
                contextManager.addToContext("player", player);
            } else {
                Admin admin = new Admin(firstname, lastname, username, "", password);
                Database database = DatabaseManager.getDatabaseInstance();
                if (!database.addUser(admin)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("REGISTRATION ERROR");
                    alert.setHeaderText("Username already exists");
                    alert.showAndWait();
                    return;
                }

                DatabaseManager.saveDatabaseInstance();
                var contextManager = ContextManager.getInstance();
                contextManager.addToContext("player", admin);
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("StartScreen.fxml"));
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
