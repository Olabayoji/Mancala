package com.example.mancalapro.model;

import java.io.File;

public class DatabaseManager {
    private static final String DATABASE_FILE = "./src/main/gameData/database.json";
    private static Database database;

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

    public static void saveDatabaseInstance() {
        if (database != null) {
            database.saveUsersToJsonFile(DATABASE_FILE);
//            System.out.println("I saved");
        }
//        System.out.println("I did not saved");

    }

}

