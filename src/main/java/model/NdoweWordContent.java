package model;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime; // If you plan to store timestamps in your model

public class NdoweWordContent {
    // Lexical Entry fields
    private final String lexicalTerm;
    private final String phoneticRepresentation;
    private final List<String> alternativeRepresentations;
    private final String lexicalEntryAudioUrl;

    // Sense and Definition fields (a single NdoweWordContent can have multiple senses)
    private List<Sense> senses;

    // You might also want to include contributor info, status, etc.
    // private String contributorEmail;
    // private String status;

    // Private constructor to enforce Builder pattern
    private NdoweWordContent(Builder builder) {
        this.lexicalTerm = builder.lexicalTerm;
        this.phoneticRepresentation = builder.phoneticRepresentation;
        this.alternativeRepresentations = builder.alternativeRepresentations;
        this.lexicalEntryAudioUrl = builder.lexicalEntryAudioUrl;
        this.senses = builder.senses;
    }

    // Getters for all fields
    public String getLexicalTerm() { return lexicalTerm; }
    public String getPhoneticRepresentation() { return phoneticRepresentation; }
    public List<String> getAlternativeRepresentations() { return alternativeRepresentations; }
    public String getLexicalEntryAudioUrl() { return lexicalEntryAudioUrl; }
    public List<Sense> getSenses() { return senses; }

    // Nested classes for complex parts
    public static class Sense {
        private int senseOrder;
        private String definitionNdowe;
        private String typeOfLexicon;
        private String conceptualDomain;
        private String translatedDefinitionText; // Specific to a target language
        private String lexicalTranslationText;  // Specific to a target language
        private List<ExampleSentence> examplesForThisSense; // Better here

        // Constructor, getters (omitted for brevity)
        public Sense(int senseOrder, String definitionNdowe, String typeOfLexicon, String conceptualDomain,
                     String translatedDefinitionText, String lexicalTranslationText) {
            this.senseOrder = senseOrder;
            this.definitionNdowe = definitionNdowe;
            this.typeOfLexicon = typeOfLexicon;
            this.conceptualDomain = conceptualDomain;
            this.translatedDefinitionText = translatedDefinitionText;
            this.lexicalTranslationText = lexicalTranslationText;
            this.examplesForThisSense = new ArrayList<>();
        }

        public int getSenseOrder() { return senseOrder; }
        public String getDefinitionNdowe() { return definitionNdowe; }
        public String getTypeOfLexicon() { return typeOfLexicon; }
        public String getConceptualDomain() { return conceptualDomain; }
        public String getTranslatedDefinitionText() { return translatedDefinitionText; }
        public String getLexicalTranslationText() { return lexicalTranslationText; }
        public List<ExampleSentence> getExamplesForThisSense() { return examplesForThisSense; }
        public void addExampleSentence(ExampleSentence example) { this.examplesForThisSense.add(example); }
    }

    public static class ExampleSentence {
        private String sentenceNdowe;
        private String audioUrlSentence;
        private String translatedSentenceText; // Specific to a target language

        // Constructor, getters (omitted for brevity)
        public ExampleSentence(String sentenceNdowe, String audioUrlSentence, String translatedSentenceText) {
            this.sentenceNdowe = sentenceNdowe;
            this.audioUrlSentence = audioUrlSentence;
            this.translatedSentenceText = translatedSentenceText;
        }

        public String getSentenceNdowe() { return sentenceNdowe; }
        public String getAudioUrlSentence() { return audioUrlSentence; }
        public String getTranslatedSentenceText() { return translatedSentenceText; }
    }

    // Builder class for NdoweWordContent
    public static class Builder {
        private String lexicalTerm;
        private String phoneticRepresentation;
        private List<String> alternativeRepresentations = new ArrayList<>();
        private String lexicalEntryAudioUrl;
        private List<Sense> senses = new ArrayList<>();

        public Builder setLexicalTerm(String lexicalTerm) { this.lexicalTerm = lexicalTerm; return this; }
        public Builder setPhoneticRepresentation(String phoneticRepresentation) { this.phoneticRepresentation = phoneticRepresentation; return this; }
        public Builder setAlternativeRepresentations(List<String> alternativeRepresentations) { this.alternativeRepresentations = alternativeRepresentations; return this; }
        public Builder setLexicalEntryAudioUrl(String lexicalEntryAudioUrl) { this.lexicalEntryAudioUrl = lexicalEntryAudioUrl; return this; }
        public Builder addSense(Sense sense) { this.senses.add(sense); return this; }

        public NdoweWordContent build() {
            return new NdoweWordContent(this);
        }
    }
}
