package com.example.mancalapro;

import com.example.mancalapro.model.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    String databasePath = "./src/main/gameData/database.json";
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.getDatabaseInstance().loadUsersFromJsonFile(databasePath);
        Parent root = FXMLLoader.load(MainApp.class.getResource("StartScreen.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(MainApp.class.getResource("main.css").toExternalForm());

        primaryStage.setTitle("Mancala");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        /*
         * Board size: 1156x354
         * Hole size: 117x117
         * space: 96
         */
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseManager.getDatabaseInstance().saveUsersToJsonFile(databasePath);
    }
}
