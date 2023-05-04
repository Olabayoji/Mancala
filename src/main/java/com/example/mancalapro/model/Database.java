package com.example.mancalapro.model;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Manages a database of players and admins.
 * 
 * @author Olabayoji Oladepo
 */
public class Database {
    private static final String DATABASE_FILE = "./src/main/gameData/database.json";
    private static Database instance;

    private List<Player> players;
    private List<Admin> admins;

    /**
     * Constructs a Database object.
     */
    private Database() {
        this.players = new ArrayList<>();
        this.admins = new ArrayList<>();
    }

    /**
     * Returns the singleton instance of Database.
     * 
     * @return The instance of Database.
     */
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Getters, setters

    /**
     * Adds a user to the database.
     * 
     * @param user The user to add.
     * @return true if the user was added successfully, false if the username is a
     *         duplicate.
     */
    public boolean addUser(User user) {

        for (Player existingPlayer : players) {
            if (existingPlayer.userName.equals(user.userName)) {
                return false; // Duplicate username found
            }
        }
        for (Admin existingAdmin : admins) {
            if (existingAdmin.userName.equals(user.userName)) {
                return false; // Duplicate username found
            }
        }
        if (user instanceof Player) {
            players.add((Player) user);

        } else {
            admins.add((Admin) user);
        }
        return true; // User added successfully
    }

    /**
     * Gets a user from the database by username.
     * 
     * @param userName The username of the user to get.
     * @return The User object if found, or null if not found.
     */
    public User getUser(String userName) {
        for (Player player : players) {
            if (player.userName.equals(userName)) {
                return player;
            }
        }
        for (Admin admin : admins) {
            if (admin.userName.equals(userName)) {
                return admin;
            }
        }
        return null;
    }

    /**
     * Deletes a user from the database by username.
     * 
     * @param userName The username of the user to delete.
     * @return true if the user was deleted, false if the user was not found.
     */
    public boolean deleteUser(String userName) {
        User userToRemove = getUser(userName);
        if (userToRemove != null) {
            if (userToRemove instanceof Player) {
                players.remove(userToRemove);
            } else if (userToRemove instanceof Admin) {
                admins.remove(userToRemove);
            }
            return true;
        }
        return false;
    }

    /**
     * Modifies a user from the database by username.
     * 
     * @param userName The username of the user to edit.
     * @return true if the user was edited, false if the user was not found.
     */
    public boolean editUser(User user) {
        User userToEdit = getUser(user.userName);
        if (userToEdit != null) {
            deleteUser(userToEdit.userName);
            addUser(user);
            return true;
        }
        return false;
    }

    /**
     * Gets all users from the database by username.
     * 
     */
    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(players);
        allUsers.addAll(admins);
        return allUsers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public List<Player> getPlayersSortedByWinRatio() {
        List<Player> sortedPlayers = new ArrayList<>(players);
        sortedPlayers.sort(Comparator.comparingDouble(Player::getWinRatio).reversed());
        return sortedPlayers;
    }

    public boolean updateUser(Player player) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getUserName().equals(player.getUserName())) {
                players.set(i, player);
                return true;
            }
        }
        return false;
    }

    /**
     * Loads users from a JSON file into the database.
     * 
     * @param filePath The path of the JSON file.
     */
    public void loadUsersFromJsonFile(String filePath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersJsonArray = new JSONArray(fileContent);

            // First, create and add all the users without favorite players
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userJsonObj = usersJsonArray.getJSONObject(i);
                String type = userJsonObj.getString("type");

                User user;
                if ("player".equals(type)) {
                    user = new Player(
                            userJsonObj.getString("firstName"),
                            userJsonObj.getString("lastName"),
                            userJsonObj.getString("userName"),
                            userJsonObj.getString("profileImage"),
                            userJsonObj.getString("password"),
                            userJsonObj.getBoolean("publicProfile"));
                    ((Player) user).setNumberOfGames(userJsonObj.getInt("numberOfGames"));
                    ((Player) user).setNumberOfLosses(userJsonObj.getInt("numberOfLosses"));
                    ((Player) user).setNumberOfWins(userJsonObj.getInt("numberOfWins"));
                    user.setApproved(userJsonObj.getBoolean("approved"));
                } else if ("admin".equals(type)) {
                    user = new Admin(
                            userJsonObj.getString("firstName"),
                            userJsonObj.getString("lastName"),
                            userJsonObj.getString("userName"),
                            userJsonObj.getString("profileImage"),
                            userJsonObj.getString("password"));
                    user.setApproved(userJsonObj.getBoolean("approved"));

                } else {
                    continue;
                }

                user.setFirstName(userJsonObj.getString("firstName"));
                user.setLastName(userJsonObj.getString("lastName"));
                user.setUserName(userJsonObj.getString("userName"));
                user.setDateCreated(LocalDateTime.parse(userJsonObj.getString("dateCreated")));
                user.setLastLogin(LocalDateTime.parse(userJsonObj.getString("lastLogin")));
                user.setProfileImage(userJsonObj.getString("profileImage"));

                addUser(user);
            }

            // Then, set favorite players for each user
            for (int i = 0; i < usersJsonArray.length(); i++) {
                JSONObject userJsonObj = usersJsonArray.getJSONObject(i);
                String type = userJsonObj.getString("type");

                if ("player".equals(type)) {
                    Player user = (Player) getUser(userJsonObj.getString("userName"));

                    // Load favorite players
                    JSONArray favoriteJsonArray = userJsonObj.getJSONArray("favorite");
                    for (int j = 0; j < favoriteJsonArray.length(); j++) {
                        String favoritePlayerUsername = favoriteJsonArray.getString(j);
                        Player favoritePlayer = (Player) getUser(favoritePlayerUsername);
                        if (favoritePlayer != null) {
                            user.addFavorite(favoritePlayer);
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading users from JSON file: " + e.getMessage());
        }
    }

    /**
     * Saves users to a JSON file.
     * 
     * @param filePath The path of the JSON file.
     */
    public void saveUsersToJsonFile(String filePath) {
        JSONArray usersJsonArray = new JSONArray();

        List<User> allUsers = new ArrayList<>();
        allUsers.addAll(players);
        allUsers.addAll(admins);

        for (User user : allUsers) {
            JSONObject userJsonObj = new JSONObject();
            userJsonObj.put("firstName", user.getFirstName());
            userJsonObj.put("lastName", user.getLastName());
            userJsonObj.put("userName", user.getUserName());
            userJsonObj.put("dateCreated", user.getDateCreated().toString());
            userJsonObj.put("lastLogin", user.getLastLogin().toString());
            userJsonObj.put("profileImage", user.getProfileImage());
            userJsonObj.put("password", user.getPassword());
            userJsonObj.put("approved", user.isApproved());

            if (user instanceof Player) {
                Player player = (Player) user;
                userJsonObj.put("type", "player");
                userJsonObj.put("numberOfGames", player.getNumberOfGames());
                userJsonObj.put("numberOfWins", player.getNumberOfWins());
                userJsonObj.put("numberOfLosses", player.getNumberOfLosses());
                userJsonObj.put("favorite", player.getFavorite());
                userJsonObj.put("publicProfile", player.isPublicProfile());

                // Save favorite players
                JSONArray favoriteJsonArray = new JSONArray();
                Set<String> favoriteUsernames = new HashSet<>(); // Use a Set to store unique favorite player usernames
                for (Player favoritePlayer : player.getFavorite()) {
                    favoriteUsernames.add(favoritePlayer.getUserName());
                }
                for (String favoriteUsername : favoriteUsernames) {
                    favoriteJsonArray.put(favoriteUsername);
                }
                userJsonObj.put("favorite", favoriteJsonArray);

            } else if (user instanceof Admin) {
                userJsonObj.put("type", "admin");
            }

            usersJsonArray.put(userJsonObj);
        }

        try (FileWriter fileWriter = new FileWriter(filePath)) {
            fileWriter.write(usersJsonArray.toString(4));
        } catch (IOException e) {
            System.err.println("Error writing users to JSON file: " + e.getMessage());
        }
    }

    /**
     * Adds a player to the current user's list of favorite players.
     * 
     * @param currentUser    The current user.
     * @param favoritePlayer The player to add to the list of favorite players.
     */
    public void addFavoritePlayer(Player currentUser, Player favoritePlayer) {
        Player userInDatabase = (Player) getUser(currentUser.getUserName());
        if (userInDatabase != null) {
            userInDatabase.addFavorite(favoritePlayer);
            saveUsersToJsonFile(DATABASE_FILE);
        }
    }

    /**
     * Removes a player from the current user's list of favorite players.
     * 
     * @param currentUser    The current user.
     * @param favoritePlayer The player to remove from the list of favorite players.
     */
    public void removeFavoritePlayer(Player currentUser, Player favoritePlayer) {
        Player userInDatabase = (Player) getUser(currentUser.getUserName());
        if (userInDatabase != null) {
            userInDatabase.removeFavorite(favoritePlayer);
            saveUsersToJsonFile(DATABASE_FILE);
        }
    }

    /**
     * Updates the profile image of a user.
     * 
     * @param user        The user whose profile image will be updated.
     * @param newImageUrl The new URL of the profile image.
     */
    public void updateProfileImage(User user, String newImageUrl) {
        User userToUpdate = Database.getInstance().getUser(user.getUserName());

        if (userToUpdate != null) {
            userToUpdate.setProfileImage(newImageUrl);

        }
    }

}
