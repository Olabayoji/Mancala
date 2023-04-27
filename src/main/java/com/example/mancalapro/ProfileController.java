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
    private Text username;
    private Text firstName;
    private Text lastName;
    private Text lastLogin;
    private Text numberOfGames;
    private Text numberOfWins;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ContextManager contextManager = ContextManager.getInstance();
        boolean admin = contextManager.retrieveFromContext("admin") != null ;
//        User user;
//        System.out.println(contextManager./\);
        if (admin) {
           Admin user = (Admin) contextManager.retrieveFromContext("admin");
        }else if(contextManager != null) {
            System.out.println((Player) contextManager.retrieveFromContext("player"));
//            Player user = (Player) contextManager.retrieveFromContext("player");
////            username.setText(user.getUserName());
//            firstName.setText(user.getFirstName());
//            lastName.setText(user.getLastName());
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////            lastLogin.setText(user.getLastLogin().format(formatter));
//            numberOfGames.setText(Integer.toString(user.getNumberOfGames()));
//            numberOfWins.setText(Integer.toString(user.getNumberOfWins()));
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
