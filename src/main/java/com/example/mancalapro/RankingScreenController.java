package com.example.mancalapro;

import com.example.mancalapro.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * RankingScreenController class handles the ranking screen of the Mancala game
 * application.
 * It displays the leaderboard with players ranked by their win ratio.
 *
 * @author Olabayoji Oladepo
 */
public class RankingScreenController implements Initializable {
    @FXML
    private ImageView btnBack;
    @FXML
    private TableView<LeaderBoardRow> leaderboardTable;
    @FXML
    private TableColumn<LeaderBoardRow, Integer> rankingColumn;
    @FXML
    private TableColumn<LeaderBoardRow, String> usernameColumn;
    @FXML
    private TableColumn<LeaderBoardRow, Integer> totalMatchesColumn;
    @FXML
    private TableColumn<LeaderBoardRow, Integer> totalLossesColumn;
    @FXML
    private TableColumn<LeaderBoardRow, Integer> totalWinsColumn;
    @FXML
    private TableColumn<LeaderBoardRow, Double> winPercentageColumn;
    @FXML
    private TableColumn<LeaderBoardRow, Boolean> favoriteColumn;

    /**
     * Initializes the controller class.
     * 
     * @param location  The location used to resolve relative paths for the root
     *                  object.
     * @param resources The resources used to localize the root object.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableColumns();
        loadLeaderboardData();

        // Navigate back to the main menu
        btnBack.setOnMouseClicked(mouseEvent -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Initializes the table columns for the leaderboard.
     */
    private void initTableColumns() {
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        totalMatchesColumn.setCellValueFactory(new PropertyValueFactory<>("totalMatches"));
        totalLossesColumn.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));
        totalWinsColumn.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
        winPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("winPercentage"));
        favoriteColumn.setCellValueFactory(new PropertyValueFactory<>("favorite"));
    }

    /**
     * Loads and displays the leaderboard data.
     */
    private void loadLeaderboardData() {
        Database database = DatabaseManager.getDatabaseInstance();
        List<Player> players = database.getPlayersSortedByWinRatio();

        ObservableList<LeaderBoardRow> playerRows = FXCollections.observableArrayList();

        int ranking = 1;
        for (Player player : players) {
            DecimalFormat df = new DecimalFormat("###.##");
            double winPercentage = Double.parseDouble(df.format(player.getWinRatio() * 100));
            String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
            Player currentUser = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
            boolean favorite = currentUser.getFavorite().contains(player);

            LeaderBoardRow playerRow = new LeaderBoardRow(ranking, player.getUserName(), player.getNumberOfGames(),
                    player.getNumberOfLosses(), player.getNumberOfWins(), winPercentage,
                    favorite);
            if (player.isApproved()) {
                playerRows.add(playerRow);
                ranking++;
            }

        }

        leaderboardTable.setItems(playerRows);

        // Event handler for row selection
        leaderboardTable.setRowFactory(tv -> {
            String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
            Player currentUser = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
            TableRow<LeaderBoardRow> row = new TableRow<>() {
                @Override
                protected void updateItem(LeaderBoardRow item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setStyle("");
                    } else if (item.getUsername().equals(currentUser.getUserName())) {
                        setStyle("-fx-background-color: #BE8A60; -fx-text-background-color: #fff");
                    } else {
                        setStyle("");
                    }
                }
            };

            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    LeaderBoardRow rowData = row.getItem();
                    Player selectedPlayer = players.stream()
                            .filter(player -> player.getUserName().equals(rowData.getUsername())).findFirst()
                            .orElse(null);
                    if (selectedPlayer.getUserName().equals(currentUser.getUserName())) {
                        selectedPlayer = null;
                    }
                    if (selectedPlayer != null) {
                        // Show the player profile dialog
                        showProfileDialog(selectedPlayer);
                    }
                }
            });
            return row;
        });
    }

    /**
     * Shows the profile dialog for the selected player.
     *
     * @param selectedPlayer The selected player whose profile will be displayed.
     */
    private void showProfileDialog(Player selectedPlayer) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/example/mancalapro/PlayerProfileDialogue.fxml"));
            Parent root = loader.load();

            // Get the controller and set the player
            ProfileDialogueController controller = loader.getController();
            String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
            Player currentUser = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
            controller.setPlayer(selectedPlayer, currentUser);

            // Set the callback to refresh the table
            controller.setOnFavoriteButtonClick(() -> {
                loadLeaderboardData();
            });

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle(selectedPlayer.getUserName() + "'s Profile");
            dialogStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
