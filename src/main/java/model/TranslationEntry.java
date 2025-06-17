package model;

public class TranslationEntry {

    private InputSenseDetails inputSenseDetails;
    private TranslatedWordDetails translatedWordDetails;
    private TranslatedSenseDetails translatedSenseDetails;

    public TranslationEntry(InputSenseDetails inputSenseDetails, TranslatedWordDetails translatedWordDetails, TranslatedSenseDetails translatedSenseDetails) {
        this.inputSenseDetails = inputSenseDetails;
        this.translatedWordDetails = translatedWordDetails;
        this.translatedSenseDetails = translatedSenseDetails;
    }

    public static class InputSenseDetails {
        Integer inputSenseOrder;
        String inputSenseDefinition;
        String inputSenseTypeOfLexicon;
        String inputSenseConceptualDomain;
        String inputSenseExampleSentence;
        String inputSenseExampleAudioUrl;
        String inputSenseDefinitionInOutputLanguage;
        String inputSenseExampleInOutputLanguage;

        public InputSenseDetails(Integer inputSenseOrder, String inputSenseDefinition, String inputSenseTypeOfLexicon, String inputSenseConceptualDomain, String inputSenseExampleSentence, String inputSenseExampleAudioUrl, String inputSenseDefinitionInOutputLanguage, String inputSenseExampleInOutputLanguage) {
            this.inputSenseOrder = inputSenseOrder;
            this.inputSenseDefinition = inputSenseDefinition;
            this.inputSenseTypeOfLexicon = inputSenseTypeOfLexicon;
            this.inputSenseConceptualDomain = inputSenseConceptualDomain;
            this.inputSenseExampleSentence = inputSenseExampleSentence;
            this.inputSenseExampleAudioUrl = inputSenseExampleAudioUrl;
            this.inputSenseDefinitionInOutputLanguage = inputSenseDefinitionInOutputLanguage;
            this.inputSenseExampleInOutputLanguage = inputSenseExampleInOutputLanguage;
        }
    }

    public static class TranslatedWordDetails {
        String translatedWord;
        String translatedWordPhonetic;
        String translatedWordAudioUrl;
        String translatedLanguageName;

        public TranslatedWordDetails(String translatedWord, String translatedWordPhonetic, String translatedWordAudioUrl, String translatedLanguageName) {
            this.translatedWord = translatedWord;
            this.translatedWordPhonetic = translatedWordPhonetic;
            this.translatedWordAudioUrl = translatedWordAudioUrl;
            this.translatedLanguageName = translatedLanguageName;
        }
    }

    public static class TranslatedSenseDetails {
        String translatedSenseDefinition;
        String translatedSenseTypeOfLexicon;
        String translatedSenseConceptualDomain;
        String translatedSenseExampleSentence;
        String translatedSenseExampleAudioUrl;
        String translatedSenseDefinitionInInputLanguage; // Translated sense's definition in the input language
        String translatedSenseExampleInInputLanguage;

        public TranslatedSenseDetails(String translatedSenseDefinition, String translatedSenseTypeOfLexicon, String translatedSenseConceptualDomain, String translatedSenseExampleSentence, String translatedSenseExampleAudioUrl, String translatedSenseDefinitionInInputLanguage, String translatedSenseExampleInInputLanguage) {
            this.translatedSenseDefinition = translatedSenseDefinition;
            this.translatedSenseTypeOfLexicon = translatedSenseTypeOfLexicon;
            this.translatedSenseConceptualDomain = translatedSenseConceptualDomain;
            this.translatedSenseExampleSentence = translatedSenseExampleSentence;
            this.translatedSenseExampleAudioUrl = translatedSenseExampleAudioUrl;
            this.translatedSenseDefinitionInInputLanguage = translatedSenseDefinitionInInputLanguage;
            this.translatedSenseExampleInInputLanguage = translatedSenseExampleInInputLanguage;
        }
    }
}


