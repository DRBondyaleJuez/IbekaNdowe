package controller;

import model.NdoweWordContent;
import persistence.database.DatabaseManager;

import java.util.Optional;

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

    public Optional<NdoweWordContent> getNdoweWordContent(String searchedWord, String inputLanguage, String outputLanguage) {
        return databaseManager.getNdoweWordContent(searchedWord, inputLanguage, outputLanguage);
    }

}
