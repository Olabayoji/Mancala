package com.example.mancalapro;

import com.example.mancalapro.model.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

/**
 * ProfileController class handles the user profile interface of the Mancala
 * game application.
 * It displays user information, allows the user to change profile image and set
 * privacy settings.
 *
 * @author Olabayoji Oladepo
 */
public class ProfileController implements Initializable {
    @FXML
    private ImageView btnBack;

    private Parent root;
    private Stage stage;
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
    @FXML
    private Text winRatio;
    @FXML
    private Text ranking;
    @FXML
    private Text privacy;
    @FXML
    private Button btnPrivacy;
    @FXML
    private Button btnChangeImage;
    @FXML
    private ImageView profileImage;

    /**
     * Returns the ranking of the given player among the given sorted list of
     * players.
     *
     * @param player        The player whose ranking is to be determined.
     * @param sortedPlayers The sorted list of players based on their win ratios.
     * @return The ranking of the given player, or -1 if the player is not found in
     *         the list.
     */
    private int getPlayerRanking(Player player, List<Player> sortedPlayers) {
        for (int i = 0; i < sortedPlayers.size(); i++) {
            if (sortedPlayers.get(i).getUserName().equals(player.getUserName())) {
                return i + 1;
            }
        }
        return -1;
    }

    /**
     * Updates the profile image of the given user with the specified new image URL.
     *
     * @param user        The user whose profile image is to be updated.
     * @param newImageUrl The URL of the new profile image.
     */
    public void updateProfileImage(User user, String newImageUrl) {
        User userToUpdate = Database.getInstance().getUser(user.getUserName());

        if (userToUpdate != null) {
            userToUpdate.setProfileImage(newImageUrl);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Retrieve the current user's username from ContextManager
        String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");

        // Retrieve the current user's instance from the Database through
        // DatabaseManager
        User user = DatabaseManager.getDatabaseInstance().getUser(currentUsername);
        if (user instanceof Admin) {
            btnPrivacy.setDisable(true);
        }

        if (user != null) {
            username.setText(user.getUserName());
            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            lastLogin.setText(user.getLastLogin().format(formatter).toString());

            // check if user has a set a profile image
            if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
                profileImage.setImage(new Image(user.getProfileImage()));
            }

            if (user instanceof Player) {
                Player player = (Player) user;
                numberOfGames.setText(Integer.toString(player.getNumberOfGames()));
                numberOfWins.setText(Integer.toString(player.getNumberOfWins()));
                winRatio.setText(Double.toString(player.getWinRatio()));
                List<Player> sortedPlayers = DatabaseManager.getDatabaseInstance().getPlayersSortedByWinRatio();
                int playerRanking = getPlayerRanking(player, sortedPlayers);
                ranking.setText(Integer.toString(playerRanking));
                privacy.setText(player.isPublicProfile() ? "Public" : "Private");

                // if (player.getFavorite().size() > 0) {
                // System.out.println("more than 1");
                // } else {
                // System.out.println("no friends");
                // }
                // for (Player us : player.getFavorite()) {
                // System.out.println(us.getFirstName());
                // }
            } else {
                numberOfGames.setText("--");
                numberOfWins.setText("--");
                winRatio.setText("--");
                ranking.setText("--");
                privacy.setText("--");

            }
        }

        // Button action for going back to home screen
        btnBack.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader;
                if (user instanceof Player) {
                    loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                } else {
                    loader = new FXMLLoader(getClass().getResource("AdminMenu.fxml"));

                }
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }

        });

        // Button action for toggling privacy mode
        btnPrivacy.setOnAction(event -> {
            Player player = (Player) user;
            player.setPublicProfile();
            String privacyText = player.isPublicProfile() ? "Public" : "Private";
            privacy.setText(privacyText);
        });

        // Button action for changing image
        btnChangeImage.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Choose a Profile Image");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                // Update the user's profile image in the User object
                String url = selectedFile.toURI().toString();
                user.setProfileImage(url);

                // Update the displayed image in the ImageView
                profileImage.setImage(new Image(url));

                // Save the changes to the database
                DatabaseManager.getDatabaseInstance().updateProfileImage(user, url);
            }
        });

    }
}
