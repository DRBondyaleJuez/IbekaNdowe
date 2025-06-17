package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the comprehensive content for a searched word, including its own details
 * and a list of all found translation entries (sense-to-sense mappings).
 * This is the top-level object that would be returned by the data access layer.
 */
public class TranslatedWordContent{
    private final String inputWord;
    private final String inputWordPhonetic;
    private final String inputWordAudioUrl;
    private final String inputLanguageName;
    private final String outputLanguageName;
    private final List<TranslationEntry> translationEntries;

    public TranslatedWordContent(Builder builder) {
        this.inputWord = builder.inputWord;
        this.inputWordPhonetic = builder.inputWordPhonetic;
        this.inputWordAudioUrl = builder.inputWordAudioUrl;
        this.inputLanguageName = builder.inputLanguageName;
        this.outputLanguageName = builder.outputLanguageName;
        this.translationEntries = builder.translationEntries;
    }

    public String getInputWord() {
        return inputWord;
    }

    public String getInputWordPhonetic() {
        return inputWordPhonetic;
    }

    public String getInputWordAudioUrl() {
        return inputWordAudioUrl;
    }

    public String getInputLanguageName() {
        return inputLanguageName;
    }

    public String getOutputLanguageName() {
        return outputLanguageName;
    }

    public List<TranslationEntry> getTranslationEntries() {
        return translationEntries;
    }

    public static class Builder {
        private String inputWord;
        private String inputWordPhonetic;
        private String inputWordAudioUrl;
        private String inputLanguageName;
        private String outputLanguageName;
        private List<TranslationEntry> translationEntries = new ArrayList<>(); // Initialize to prevent NullPointerException

        public Builder setInputWord(String inputWord) {
            this.inputWord = inputWord;
            return this;
        }

        public Builder setInputWordPhonetic(String inputWordPhonetic) {
            this.inputWordPhonetic = inputWordPhonetic;
            return this;
        }

        public Builder setInputWordAudioUrl(String inputWordAudioUrl) {
            this.inputWordAudioUrl = inputWordAudioUrl;
            return this;
        }

        public Builder setInputLanguageName(String inputLanguageName) {
            this.inputLanguageName = inputLanguageName;
            return this;
        }

        public Builder setOutputLanguageName(String outputLanguageName) {
            this.outputLanguageName = outputLanguageName;
            return this;
        }

        public Builder addTranslationEntry(TranslationEntry entry) {
            if (this.translationEntries == null) {
                this.translationEntries = new ArrayList<>();
            }
            this.translationEntries.add(entry);
            return this;
        }

        /**
         * Builds and returns a new TranslatedWordContent instance.
         * @return A new TranslatedWordContent object.
         */
        public TranslatedWordContent build() {
            return new TranslatedWordContent(this);
        }
    }
}
