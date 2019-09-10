package com.workoutlog.workoutlog.database;

public class DatabaseInitializer {

    private DatabaseInitializer(){}
    private static DatabaseInitializer INSTANCE = null;
    public static DatabaseInitializer getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new DatabaseInitializer();
        }
        return INSTANCE;
    }
}
