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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

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
        List<Player> players = database.getPlayers();
        players.sort(Comparator.comparingDouble(Player::getWinRatio).reversed());

        ObservableList<LeaderBoardRow> playerRows = FXCollections.observableArrayList();

        int ranking = 1;
        for (Player player : players) {
            double winPercentage = player.getWinRatio() * 100;
            ContextManager contextManager = ContextManager.getInstance();
            Player currentUser = (Player) contextManager.retrieveFromContext("currentUser");
            boolean favorite = currentUser.getFavorite().contains(player);

            LeaderBoardRow playerRow = new LeaderBoardRow(ranking, player.getUserName(), player.getNumberOfGames(),
                    player.getNumberOfGames() - player.getNumberOfWins(), player.getNumberOfWins(), winPercentage,
                    favorite);
            playerRows.add(playerRow);
            ranking++;
        }

        leaderboardTable.setItems(playerRows);

        // Add event handler for row selection
        leaderboardTable.setRowFactory(tv -> {
            ContextManager contextManager = ContextManager.getInstance();
            Player currentUser = (Player) contextManager.retrieveFromContext("currentUser");

            TableRow<LeaderBoardRow> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                    LeaderBoardRow rowData = row.getItem();
                    // Find the corresponding Player object based on the rowData's username
                    Player selectedPlayer = players.stream()
                            .filter(player -> player.getUserName().equals(rowData.getUsername())).findFirst()
                            .orElse(null);

                    // player cant add themselves as favorite
                    if (selectedPlayer.getUserName().equals(currentUser.getFirstName())) {
                        selectedPlayer = null;
                    }

                    if (selectedPlayer != null) {
                        // Check if selectedPlayer is already in the currentUser's favorite list
                        boolean isFavorite = currentUser.getFavorite().contains(selectedPlayer);

                        if (isFavorite) {
                            // Remove selectedPlayer from the currentUser's favorite list
                            currentUser.removeFavorite(selectedPlayer);
                            database.removeFavoritePlayer(currentUser, selectedPlayer);
                        } else {
                            // Add selectedPlayer to the currentUser's favorite list
                            currentUser.addFavorite(selectedPlayer);
                            database.addFavoritePlayer(currentUser, selectedPlayer);
                        }

                        // Update the rowData's favorite status and refresh the table
                        rowData.setFavorite(!isFavorite);
                        leaderboardTable.refresh();
                    }
                }
            });
            return row;
        });
    }
}
