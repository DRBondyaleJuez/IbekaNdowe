package persistence.database;

import model.NdoweWord;

public interface DatabaseTalker {
    NdoweWord getNdoweWordContent(String searchedWord, String inputLanguage, String outputLanguag);
    NdoweWord getNdoweWordTranslation(String wordInput, String inputLanguage, String outputLanguage);
    boolean addNewWord(NdoweWord newNdoweWord);
    boolean editWord(NdoweWord newNdoweWord);
}
