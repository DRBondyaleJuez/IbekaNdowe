package application.persistence.database;

import application.model.UpsertedWordContent;
import application.persistence.database.postgresql.PostgresqlTalker;
import application.model.NdoweWord;
import application.model.TranslatedWordContent;

import java.util.List;
import java.util.Optional;

public class DatabaseManager {
    private static DatabaseManager instance = null;
    private final DatabaseTalker databaseTalker;

    private DatabaseManager() {
        databaseTalker = new PostgresqlTalker();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    public synchronized boolean addNewWord(){
        return databaseTalker.addNewWord(new NdoweWord());
    }

    public synchronized boolean editWord(){
        return true;
    }

    public boolean upsertWord(UpsertedWordContent upsertedWordContent) { return databaseTalker.upsertWord(upsertedWordContent); }

    public List<String> getLanguageList() {
        return databaseTalker.getLanguageList();
    }

    public List<String> getLexiconTypeList() {
        return databaseTalker.getLexiconTypeList();
    }

    public synchronized Optional<TranslatedWordContent> getTranslatedWordContent(String searchedWord, String inputLanguage, String outputLanguage) {
        return databaseTalker.getTranslatedWordContent(searchedWord, inputLanguage, outputLanguage);
    }
}
