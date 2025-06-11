package controller;

import persistence.database.DatabaseManager;

public class NdoweMainController {

    private final DatabaseManager databaseManager;

    public NdoweMainController() {
        databaseManager = DatabaseManager.getInstance();
    }

    public boolean addNewWord(){
        return databaseManager.addNewWord();
    }

    public boolean editWord(){
        return databaseManager.editWord();
    }
}
