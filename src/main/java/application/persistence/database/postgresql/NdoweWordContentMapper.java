package application.persistence.database.postgresql;

import application.model.NdoweWordContent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays; // For splitting array text from DB

public class NdoweWordContentMapper {

    public NdoweWordContent mapResultSetToNdoweWordContent(ResultSet rs) throws SQLException {
        if (!rs.isBeforeFirst()) { // Check if ResultSet is empty
            return null; // Or throw an exception, depending on your error handling
        }

        // Use a map to handle multiple senses and examples for a single word
        // Key: senseOrder -> Value: Sense object
        Map<Integer, NdoweWordContent.Sense> sensesMap = new HashMap<>();

        NdoweWordContent.Builder wordContentBuilder = new NdoweWordContent.Builder();
        boolean firstRow = true; // Flag to capture common lexical entry details once

        while (rs.next()) {
            if (firstRow) {
                // Populate lexical entry details only once from the first row
                wordContentBuilder.setLexicalTerm(rs.getString("lexical_term"))
                        .setPhoneticRepresentation(rs.getString("phonetic_representation"))
                        .setLexicalEntryAudioUrl(rs.getString("lexical_entry_audio_url"));

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

                firstRow = false;
            }

            // --- Process Sense-specific data ---
            int currentSenseOrder = rs.getInt("sense_order");
            NdoweWordContent.Sense currentSense = sensesMap.get(currentSenseOrder);

            if (currentSense == null) {
                // If this is a new sense, create it
                currentSense = new NdoweWordContent.Sense(
                        currentSenseOrder,
                        rs.getString("definition_ndowe"),
                        rs.getString("type_of_lexicon"),
                        rs.getString("conceptual_domain"),
                        rs.getString("translated_definition_text"),
                        rs.getString("translation_text")
                );
                sensesMap.put(currentSenseOrder, currentSense);
                wordContentBuilder.addSense(currentSense); // Add to the main word's senses list
            }

            // --- Process Example Sentence data (if available for this row) ---
            String sentenceNdowe = rs.getString("sentence_ndowe");
            if (sentenceNdowe != null) {
                NdoweWordContent.ExampleSentence example = new NdoweWordContent.ExampleSentence(
                        sentenceNdowe,
                        rs.getString("audio_url_sentence"),
                        rs.getString("translated_sentence_text")
                );
                currentSense.addExampleSentence(example); // Add example to the specific sense
            }
        }

        return wordContentBuilder.build();
    }
}
