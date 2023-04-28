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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTableColumns();
        loadLeaderboardData();

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

    private void initTableColumns() {
        rankingColumn.setCellValueFactory(new PropertyValueFactory<>("ranking"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        totalMatchesColumn.setCellValueFactory(new PropertyValueFactory<>("totalMatches"));
        totalLossesColumn.setCellValueFactory(new PropertyValueFactory<>("totalLosses"));
        totalWinsColumn.setCellValueFactory(new PropertyValueFactory<>("totalWins"));
        winPercentageColumn.setCellValueFactory(new PropertyValueFactory<>("winPercentage"));
        favoriteColumn.setCellValueFactory(new PropertyValueFactory<>("favorite"));
    }


    private void loadLeaderboardData() {
        Database database = DatabaseManager.getDatabaseInstance();
        List<Player> players = database.getPlayersSortedByWinRatio();

        ObservableList<LeaderBoardRow> playerRows = FXCollections.observableArrayList();

        int ranking = 1;
        for (Player player : players) {
            double winPercentage = player.getWinRatio() * 100;
            String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
            Player currentUser = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
            boolean favorite = currentUser.getFavorite().contains(player);

            LeaderBoardRow playerRow = new LeaderBoardRow(ranking, player.getUserName(), player.getNumberOfGames(),
                    player.getNumberOfGames() - player.getNumberOfWins(), player.getNumberOfWins(), winPercentage,
                    favorite);
           if (player.isApproved()){
               playerRows.add(playerRow);
           }
            ranking++;
        }

        leaderboardTable.setItems(playerRows);

        // Add event handler for row selection
        leaderboardTable.setRowFactory(tv -> {
            String currentUsername = (String) ContextManager.getInstance().retrieveFromContext("currentUser");
            Player currentUser = (Player) DatabaseManager.getDatabaseInstance().getUser(currentUsername);
            TableRow<LeaderBoardRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    LeaderBoardRow rowData = row.getItem();
                    Player selectedPlayer = players.stream()
                            .filter(player -> player.getUserName().equals(rowData.getUsername())).findFirst()
                            .orElse(null);

                    if (selectedPlayer != null && !selectedPlayer.getUserName().equals(currentUser.getUserName())) {
                        boolean isFavorite = currentUser.getFavorite().contains(selectedPlayer);

                        // Create a confirmation dialog
                        Alert alert = new Alert(AlertType.CONFIRMATION);
                        alert.setTitle(isFavorite ? "Remove Favorite" : "Add Favorite");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you sure you want to " + (isFavorite ? "remove" : "add") + " " +
                                selectedPlayer.getUserName() + " as a " + (isFavorite ? "non-" : "") + "favorite?");

                        // Show the confirmation dialog and wait for the result
                        alert.showAndWait().ifPresent(result -> {
                            if (result == ButtonType.OK) {
                                if (isFavorite) {
                                    database.removeFavoritePlayer(currentUser, selectedPlayer);
                                } else {
                                    database.addFavoritePlayer(currentUser, selectedPlayer);
                                }

                                // Update the rowData's favorite status and refresh the table
                                rowData.setFavorite(!isFavorite);
                                leaderboardTable.refresh();
                            }
                        });
                    }
                }
            });
            return row;
        });
    }
}
