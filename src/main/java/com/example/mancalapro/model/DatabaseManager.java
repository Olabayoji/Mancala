package com.example.mancalapro.model;

import java.io.File;

/**
 * Manages the database instance, handling its creation and saving.
 * 
 * @author Oluwatobi Gbemile
 */
public class DatabaseManager {
    private static final String DATABASE_FILE = "./src/main/gameData/database.json";
    private static Database database;

    /**
     * Returns the singleton instance of Database, loading it from a file if
     * necessary.
     * 
     * @return The instance of Database.
     */
    public static Database getDatabaseInstance() {
        if (database == null) {
            File file = new File(DATABASE_FILE);
            if (file.exists()) {
                database = Database.getInstance();
                database.loadUsersFromJsonFile(DATABASE_FILE);
            } else {
                database = Database.getInstance();
            }
        }
        System.out.println(database.getPlayers());
        return database;
    }

    /**
     * Saves the database instance to a file if it exists.
     */
    public static void saveDatabaseInstance() {
        if (database != null) {
            database.saveUsersToJsonFile(DATABASE_FILE);
            // System.out.println("I saved");
        }
        // System.out.println("I did not saved");

    }

}
