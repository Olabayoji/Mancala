package com.example.mancalapro;

import com.example.mancalapro.model.Database;
import com.example.mancalapro.model.Player;
import com.example.mancalapro.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller for the approval screen in the Mancala game application.
 *
 * @author Olabayoji Oladepo
 */
public class ApprovalController implements Initializable {
    @FXML
    private ImageView btnBack;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private Text label;
    @FXML
    private ListView<HBox> unapprovedPlayersListView;

    private Parent root;
    private Stage stage;
    private Scene scene;

    /**
     * Initializes the controller class.
     *
     * @param location  The location used to resolve relative paths for the root
     *                  object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Handle back button click event

        btnBack.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("AdminMenu.fxml"));
                root = loader.load();
                stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (/* InterruptedException | */IOException e) {
                throw new RuntimeException(e);
            }
        });

        // Get unapproved users
        List<User> unapprovedUsers = Database.getInstance().getAllUsers().stream()
                .filter(player -> !player.isApproved())
                .collect(Collectors.toList());

        // If no unapproved players found, display the label
        if (unapprovedUsers.isEmpty()) {
            label.setVisible(true);
        } else {
            label.setVisible(false);
            for (User player : unapprovedUsers) {
                HBox hbox = new HBox(50);
                Label usernameLabel = new Label(player.getUserName());
                usernameLabel.setMaxWidth(100); // Set the maximum width of the label
                usernameLabel.setPrefWidth(100); // Set the preferred width of the label
                usernameLabel.setStyle("-fx-alignment: CENTER;"); // Center the text within the Label

                Label firstNameLabel = new Label(player.getFirstName());
                firstNameLabel.setMaxWidth(100); // Set the maximum width of the label
                firstNameLabel.setPrefWidth(100); // Set the preferred width of the label
                firstNameLabel.setStyle("-fx-alignment: CENTER;"); // Center the text within the Label

                Label lastNameLabel = new Label(player.getLastName());
                lastNameLabel.setMaxWidth(100); // Set the maximum width of the label
                lastNameLabel.setPrefWidth(100); // Set the preferred width of the label
                lastNameLabel.setStyle("-fx-alignment: CENTER;"); // Center the text within the Label

                Label access = new Label(player instanceof Player ? "Player" : "Admin");
                lastNameLabel.setMaxWidth(100); // Set the maximum width of the label
                lastNameLabel.setPrefWidth(100); // Set the preferred width of the label
                lastNameLabel.setStyle("-fx-alignment: CENTER;"); // Center the text within the Label

                Button approveButton = createApproveButton(player);
                approveButton.setMaxWidth(100); // Set the maximum width of the label
                approveButton.setPrefWidth(100); // Set the preferred width of the label
                approveButton.setStyle("-fx-alignment: CENTER;"); // Center the text within the Label
                hbox.getChildren().addAll(usernameLabel, firstNameLabel, lastNameLabel, access, approveButton);
                unapprovedPlayersListView.getItems().add(hbox);
            }
        }
    }

    /**
     * Creates an "Approve" button for the specified player.
     *
     * @param player The player to be approved.
     * @return The "Approve" button.
     */
    private Button createApproveButton(User player) {
        Button approveButton = new Button("Approve");
        approveButton.setOnAction(event -> {
            player.setApproved(true); // Update the approval status in the Player object
            Database.getInstance().editUser(player); // Update the approval status in the database
            unapprovedPlayersListView.getItems().remove(((Node) event.getSource()).getParent()); // Remove the player's
                                                                                                 // HBox from the
                                                                                                 // ListView
        });
        return approveButton;
    }
}
