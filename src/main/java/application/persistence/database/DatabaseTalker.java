package application.persistence.database;

import application.exceptions.WordQuerySQLException;
import application.model.NdoweWord;
import application.model.TranslatedWordContent;
import application.model.UpsertedWordContent;

import java.util.List;
import java.util.Optional;

public interface DatabaseTalker {
    Optional<TranslatedWordContent> getTranslatedWordContent(String searchedWord, String inputLanguage, String outputLanguage) throws WordQuerySQLException;
    List<String> getLanguageList();
    List<String> getLexiconTypeList();
    NdoweWord getNdoweWordTranslation(String wordInput, String inputLanguage, String outputLanguage);
    boolean upsertWord(UpsertedWordContent upsertedWordContent);
    boolean addNewWord(NdoweWord newNdoweWord);
    boolean editWord(NdoweWord newNdoweWord);
}
