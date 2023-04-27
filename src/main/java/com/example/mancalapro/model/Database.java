package com.example.mancalapro.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private List<Player> players;
    private List<Admin> admins;

    private static Database instance;

    private Database() {
        this.players = new ArrayList<>();
        this.admins = new ArrayList<>();
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // Getters, setters
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

    public boolean editUser(User user) {
        User userToEdit = getUser(user.userName);
        if (userToEdit != null) {
            deleteUser(userToEdit.userName);
            addUser(user);
            return true;
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Admin> getAdmins() {
        return admins;
    }

    public void loadUsersFromJsonFile(String filePath) {
        try {
            String fileContent = new String(Files.readAllBytes(Paths.get(filePath)));
            JSONArray usersJsonArray = new JSONArray(fileContent);

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
                            userJsonObj.getString("password")
                    );
                    ((Player) user).setNumberOfGames(userJsonObj.getInt("numberOfGames"));
                    ((Player) user).setNumberOfWins(userJsonObj.getInt("numberOfWins"));
                    ((Player) user).setApproved(userJsonObj.getBoolean("approved"));
                } else if ("admin".equals(type)) {
                    user = new Admin(
                            userJsonObj.getString("firstName"),
                            userJsonObj.getString("lastName"),
                            userJsonObj.getString("userName"),
                            userJsonObj.getString("profileImage"),
                            userJsonObj.getString("password")
                    );
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
        } catch (IOException e) {
            System.err.println("Error reading users from JSON file: " + e.getMessage());
        }
    }

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

            if (user instanceof Player) {
                Player player = (Player) user;
                userJsonObj.put("type", "player");
                userJsonObj.put("numberOfGames", player.getNumberOfGames());
                userJsonObj.put("numberOfWins", player.getNumberOfWins());
                userJsonObj.put("approved", player.isApproved());
                userJsonObj.put("favorite", player.getFavorite());
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


}
