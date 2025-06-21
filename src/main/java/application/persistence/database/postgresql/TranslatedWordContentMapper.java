package application.persistence.database.postgresql;

import application.model.TranslatedWordContent;
import application.model.TranslationEntry;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class TranslatedWordContentMapper {

    public TranslatedWordContent mapResultSetToTranslatedWordContent(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            return null; // Or throw an exception, depending on your error handling
        }

        TranslatedWordContent.Builder wordContentBuilder = new TranslatedWordContent.Builder();
        boolean firstRow = true; // Flag to capture common lexical entry details once

        while (rs.next()) {
            if (firstRow) {
                // Populate lexical entry details only once from the first row
                wordContentBuilder.setInputWord(rs.getString("input_word"))
                        .setInputLanguageName(rs.getString("input_language_name"))
                        .setInputWordPhonetic(rs.getString("input_word_phonetic"))
                        .setInputWordAudioUrl(rs.getString("input_word_audio_url"))
                        .setOutputLanguageName(rs.getString("output_language_name"))
                ;

                /*
                // Handle array type for alternative_representations
                String altRepsArrayString = rs.getString("alternative_representations");
                if (altRepsArrayString != null && !altRepsArrayString.equals("{}")) {
                    // Remove braces and split by comma, then trim each item
                    List<String> altReps = Arrays.asList(altRepsArrayString
                            .substring(1, altRepsArrayString.length() - 1)
                            .split(",\\s*"));
                    wordContentBuilder.setAlternativeRepresentations(altReps);
                } else {
                    wordContentBuilder.setAlternativeRepresentations(new ArrayList<>()); // Empty list if null or empty array
                }
                 */

                firstRow = false;
            }

            // Setting currentInputSenseDetails
            TranslationEntry.InputSenseDetails currentInputSenseDetails = new TranslationEntry.InputSenseDetails(
                    rs.getInt("input_sense_order"),
                    rs.getString("input_sense_definition"),
                    rs.getString("input_sense_type_of_lexicon"),
                    rs.getString("input_sense_conceptual_domain"),
                    rs.getString("input_sense_example_sentence"),
                    rs.getString("input_sense_example_audio_url"),
                    rs.getString("input_sense_definition_in_output_language"),
                    rs.getString("input_sense_example_in_output_language")
            );

            // Setting currentTranslatedWordDetails
            TranslationEntry.TranslatedWordDetails currentTranslatedWordDetails = new TranslationEntry.TranslatedWordDetails(
                    rs.getString("translated_word"),
                    rs.getString("translated_word_phonetic"),
                    rs.getString("translated_word_audio_url"),
                    rs.getString("output_language_name")
            );

            // Setting currentTranslatedSenseDetails
            TranslationEntry.TranslatedSenseDetails currentTranslatedSenseDetails = new TranslationEntry.TranslatedSenseDetails(
                    rs.getString("translated_sense_definition"),
                    rs.getString("translated_sense_type_of_lexicon"),
                    rs.getString("translated_sense_conceptual_domain"),
                    rs.getString("translated_sense_example_sentence"),
                    rs.getString("translated_sense_example_audio_url"),
                    rs.getString("translated_sense_definition_in_input_language"),
                    rs.getString("translated_sense_example_in_input_language")
            );

            // Setting currentTranslationEntry
            TranslationEntry currentTranslationEntry = new TranslationEntry(
                    currentInputSenseDetails,
                    currentTranslatedWordDetails,
                    currentTranslatedSenseDetails
            );
            wordContentBuilder.addTranslationEntry(currentTranslationEntry);
        }

        return wordContentBuilder.build();
    }
}
