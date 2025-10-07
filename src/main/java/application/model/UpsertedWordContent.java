package application.model;

import java.util.List;

public class UpsertedWordContent {
        private String wordLanguage;
        private String wordText;
        private String wordPhonetic;
        private String wordAudioUrl;
        private List<SenseContent> senseContentFields;

        // Getters and setters
        // (You can use a library like Lombok to auto-generate these)

        public String getWordLanguage() {
            return wordLanguage;
        }

        public void setWordLanguage(String wordLanguage) {
            this.wordLanguage = wordLanguage;
        }

        public String getWordText() {
            return wordText;
        }

        public void setWordText(String wordText) {
            this.wordText = wordText;
        }

        public String getWordPhonetic() {
            return wordPhonetic;
        }

        public void setWordPhonetic(String wordPhonetic) {
            this.wordPhonetic = wordPhonetic;
        }

        public String getWordAudioUrl() {
            return wordAudioUrl;
        }

        public void setWordAudioUrl(String wordAudioUrl) {
            this.wordAudioUrl = wordAudioUrl;
        }

        public List<SenseContent> getSenseContentFields() {
            return senseContentFields;
        }

        public void setSenseContentFields(List<SenseContent> senseContentFields) {
            this.senseContentFields = senseContentFields;
        }

    public static class SenseContent {
        private int id;
        private String lexiconType;
        private String conceptualDomain;
        private String definition;
        private String sentenceExample;
        private String sentenceExampleAudio;
        private List<SenseTranslation> senseTranslations;

        // Getters and setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getLexiconType() {
            return lexiconType;
        }

        public void setLexiconType(String lexiconType) {
            this.lexiconType = lexiconType;
        }

        public String getConceptualDomain() {
            return conceptualDomain;
        }

        public void setConceptualDomain(String conceptualDomain) {
            this.conceptualDomain = conceptualDomain;
        }

        public String getDefinition() {
            return definition;
        }

        public void setDefinition(String definition) {
            this.definition = definition;
        }

        public String getSentenceExample() {
            return sentenceExample;
        }

        public void setSentenceExample(String sentenceExample) {
            this.sentenceExample = sentenceExample;
        }

        public String getSentenceExampleAudio() {
            return sentenceExampleAudio;
        }

        public void setSentenceExampleAudio(String sentenceExampleAudio) {
            this.sentenceExampleAudio = sentenceExampleAudio;
        }

        public List<SenseTranslation> getSenseTranslations() {
            return senseTranslations;
        }

        public void setSenseTranslations(List<SenseTranslation> senseTranslations) {
            this.senseTranslations = senseTranslations;
        }
    }

    public static class SenseTranslation {
        private int id;
        private String translationLanguage;
        private String definitionTranslation;
        private String exampleSentenceTranslation;
        private String exampleSentenceTranslationAudio;

        // Getters and setters

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTranslationLanguage() {
            return translationLanguage;
        }

        public void setTranslationLanguage(String translationLanguage) {
            this.translationLanguage = translationLanguage;
        }

        public String getDefinitionTranslation() {
            return definitionTranslation;
        }

        public void setDefinitionTranslation(String definitionTranslation) {
            this.definitionTranslation = definitionTranslation;
        }

        public String getExampleSentenceTranslation() {
            return exampleSentenceTranslation;
        }

        public void setExampleSentenceTranslation(String exampleSentenceTranslation) {
            this.exampleSentenceTranslation = exampleSentenceTranslation;
        }

        public String getExampleSentenceTranslationAudio() {
            return exampleSentenceTranslationAudio;
        }

        public void setExampleSentenceTranslationAudio(String exampleSentenceTranslationAudio) {
            this.exampleSentenceTranslationAudio = exampleSentenceTranslationAudio;
        }
    }
}
