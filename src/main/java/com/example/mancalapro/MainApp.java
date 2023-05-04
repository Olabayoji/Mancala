package com.example.mancalapro;

import com.example.mancalapro.model.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * MainApp class is the entry point of the Mancala game application.
 * It loads the database of users, sets up the user interface, and handles the
 * shutdown process.
 *
 * @author Olabayoji Oladepo
 */
public class MainApp extends Application {

    private static final String DATABASE_PATH = "./src/main/gameData/database.json";

    /**
     * Starts the JavaFX application and sets up the initial user interface.
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set
     * @throws Exception if there's an issue loading the user interface or database
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        DatabaseManager.getDatabaseInstance().loadUsersFromJsonFile(DATABASE_PATH);
        Parent root = FXMLLoader.load(MainApp.class.getResource("StartScreen.fxml"));
        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(MainApp.class.getResource("main.css").toExternalForm());

        primaryStage.setTitle("Mancala");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
        // setUpOnCloseHandling(primaryStage);
    }

    // private void setUpOnCloseHandling(Stage primaryStage) {
    // primaryStage.setOnCloseRequest(windowEvent -> {
    // // Save the changes to the database before closing the application
    // DatabaseManager.getDatabaseInstance().saveUsersToJsonFile(DATABASE_PATH);
    // });
    // }

    /**
     * The main method launches the JavaFX application.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * Handles the shutdown process of the application, saving the users to the
     * database file.
     *
     * @throws Exception if there's an issue saving the users to the database
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        DatabaseManager.getDatabaseInstance().saveUsersToJsonFile(DATABASE_PATH);
    }
}
