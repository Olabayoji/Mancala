package com.example.mancalapro;

import com.example.mancalapro.model.Admin;
import com.example.mancalapro.model.ContextManager;
import com.example.mancalapro.model.Player;
import com.example.mancalapro.model.User;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML
    private ImageView btnBack;

    private Parent root;
    private  Stage stage;
    private Scene scene;
    @FXML
    private Text username;
    @FXML
    private Text firstName;
    @FXML
    private Text lastName;
    @FXML
    private Text lastLogin;
    @FXML
    private Text numberOfGames;
    @FXML
    private Text numberOfWins;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContextManager contextManager = ContextManager.getInstance();
        User user = (User) contextManager.retrieveFromContext("currentUser");

        if (user != null) {
            username.setText(user.getUserName());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            lastLogin.setText(user.getLastLogin().toString());

            if (user instanceof Player) {
                Player player = (Player) user;
                numberOfGames.setText(Integer.toString(player.getNumberOfGames()));
                numberOfWins.setText(Integer.toString(player.getNumberOfWins()));
            }
        }


//        System.out.println(user.getFirstName());
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
    }
}
