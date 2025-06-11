package persistence.database;

import model.NdoweWord;

import java.util.List;
import java.util.Optional;

public interface DatabaseTalker {
    Optional<List<NdoweWord>> getNdoweWordContent(String searchedWord, String inputLanguage, String outputLanguag);
    NdoweWord getNdoweWordTranslation(String wordInput, String inputLanguage, String outputLanguage);
    boolean addNewWord(NdoweWord newNdoweWord);
    boolean editWord(NdoweWord newNdoweWord);
}
