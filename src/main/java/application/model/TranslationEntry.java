package application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Generates getters, setters, toString, equals, hashCode
@NoArgsConstructor // Adds the no-argument constructor required by Jackson
@AllArgsConstructor // Adds a constructor with all fields (useful if you use it)
public class TranslationEntry {

    private InputSenseDetails inputSenseDetails;
    private TranslatedWordDetails translatedWordDetails;
    private TranslatedSenseDetails translatedSenseDetails;

    @Data // Generates getters, setters, toString, equals, hashCode
    @NoArgsConstructor // Adds the no-argument constructor required by Jackson
    @AllArgsConstructor // Adds a constructor with all fields (useful if you use it)
    public static class InputSenseDetails {
        private Integer inputSenseOrder;
        private String inputSenseDefinition;
        private String inputSenseTypeOfLexicon;
        private String inputSenseConceptualDomain;
        private String inputSenseExampleSentence;
        private String inputSenseExampleAudioUrl;
        private String inputSenseDefinitionInOutputLanguage;
        private String inputSenseExampleInOutputLanguage;
        private String inputSenseExampleInOutputLanguageAudioUrl;
    }

    @Data // Generates getters, setters, toString, equals, hashCode
    @NoArgsConstructor // Adds the no-argument constructor required by Jackson
    @AllArgsConstructor // Adds a constructor with all fields (useful if you use it)
    public static class TranslatedWordDetails {
        private String translatedWord;
        private String translatedWordPhonetic;
        private String translatedWordAudioUrl;
        private String translatedLanguageName;
    }

    @Data // Generates getters, setters, toString, equals, hashCode
    @NoArgsConstructor // Adds the no-argument constructor required by Jackson
    @AllArgsConstructor // Adds a constructor with all fields (useful if you use it)
    public static class TranslatedSenseDetails {
        private String translatedSenseDefinition;
        private String translatedSenseTypeOfLexicon;
        private String translatedSenseConceptualDomain;
        private String translatedSenseExampleSentence;
        private String translatedSenseExampleAudioUrl;
        private String translatedSenseDefinitionInInputLanguage; // Translated sense's definition in the input language
        private String translatedSenseExampleInInputLanguage;
        private String translatedSenseExampleInInputLanguageAudioUrl;
    }
}


