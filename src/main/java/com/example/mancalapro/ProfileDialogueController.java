package com.example.mancalapro;

import com.example.mancalapro.model.Database;
import com.example.mancalapro.model.DatabaseManager;
import com.example.mancalapro.model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * ProfileDialogueController class handles the profile dialogue of the Mancala
 * game application.
 * It displays player information, allows the user to add or remove a player
 * from favorites.
 *
 * @author Olabayoji Oladepo
 */
public class ProfileDialogueController {
    @FXML
    private ImageView playerProfileImage;
    @FXML
    private Label playerNameLabel;
    @FXML
    private Label winPercentageLabel;
    @FXML
    private Button closeButton;
    @FXML
    private Button favoriteButton;

    private Player player;
    private Player currentUser;
    private Database database;
    private Runnable onFavoriteButtonClick;

    /**
     * Sets the player and current user instances.
     *
     * @param player      The player whose information will be displayed.
     * @param currentUser The current user interacting with the profile dialogue.
     */
    public void setPlayer(Player player, Player currentUser) {
        this.player = player;
        this.currentUser = currentUser;
        this.database = DatabaseManager.getDatabaseInstance();
        updatePlayerInfo();
    }

    /**
     * Sets the action to be performed when the favorite button is clicked.
     *
     * @param onFavoriteButtonClick The action to be performed.
     */
    public void setOnFavoriteButtonClick(Runnable onFavoriteButtonClick) {
        this.onFavoriteButtonClick = onFavoriteButtonClick;
    }

    /**
     * Updates the player information displayed in the profile dialogue.
     */
    private void updatePlayerInfo() {
        if (player != null) {
            playerNameLabel.setText(player.getUserName());

            winPercentageLabel.setText(String.format("Win Percentage: %.2f%%", player.getWinRatio() * 100));
            if (player.getProfileImage() != null && !player.getProfileImage().equals("")) {
                playerProfileImage.setImage(new Image(player.getProfileImage()));
            }

            // Update favorite button text
            updateFavoriteButtonText();
        }
    }

    /**
     * Updates the favorite button text based on the favorite status of the player.
     */
    private void updateFavoriteButtonText() {
        if (currentUser.getFavorite().contains(player)) {
            favoriteButton.setText("Remove from favorites");
        } else {
            favoriteButton.setText("Add to favorites");
        }
    }

    /**
     * Handles the action to add or remove a player from the favorite list.
     */
    @FXML
    public void handleFavoriteButtonAction() {
        if (currentUser.getFavorite().contains(player)) {
            database.removeFavoritePlayer(currentUser, player);
        } else {
            if (player.isPublicProfile()) { // if player is public, then add player
                database.addFavoritePlayer(currentUser, player);
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("PRIVATE PLAYER");
                alert.setHeaderText("Player is private. \nYou cannot add a private player");
                alert.showAndWait();
                return;
            }
        }
        updateFavoriteButtonText();
    }

    @FXML
    public void initialize() {
        closeButton.setOnAction(event -> {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
        });
        favoriteButton.setOnAction(event -> {
            handleFavoriteButtonAction();
            if (onFavoriteButtonClick != null) {
                onFavoriteButtonClick.run();
            }
        });
    }
}
