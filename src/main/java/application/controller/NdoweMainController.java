package application.controller;

import application.model.TranslatedWordContent;
import org.springframework.stereotype.Service;
import application.persistence.database.DatabaseManager;

import java.util.List;
import java.util.Optional;

@Service
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

    public List<String> getLanguageList() {
        return databaseManager.getLanguageList();
    }

    public Optional<TranslatedWordContent> getTranslatedWordContent(String searchedWord, String inputLanguage, String outputLanguage) {
        return databaseManager.getTranslatedWordContent(searchedWord, inputLanguage, outputLanguage);
    }

}
