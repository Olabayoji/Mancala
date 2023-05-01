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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    ObservableList<String> type = FXCollections.observableArrayList("Player", "Admin");
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
    ChoiceBox auth;
    @FXML
    private PasswordField password;
    @FXML
    private Button btnRegister;


    private Parent root;
    private Stage stage;
    private Scene scene;
    private String databasePath = "./src/main/gameData/database.json";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        auth.setItems(type);
        auth.setValue("Player");

        btnLogin.setOnMouseClicked(mouseEvent -> {
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
        });

        btnRegister.setOnMouseClicked(mouseEvent -> {
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
                    alert.setHeaderText("Username must be at least 3 characters, \nFirst name must be at least 2 characters\nLast name must be at least 2 characters");
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
        });
    }

    // public void initManager
}
