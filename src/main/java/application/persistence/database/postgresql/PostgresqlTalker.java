package application.persistence.database.postgresql;

import application.exceptions.WordQuerySQLException;
import application.model.NdoweWord;
import application.model.TranslatedWordContent;
import application.model.UpsertedWordContent;
import application.utils.PropertiesReader;
import application.persistence.database.DatabaseTalker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PostgresqlTalker implements DatabaseTalker {
    //private static final Logger logger = LogManager.getLogger(PostgresqlTalker.class);
    private static final String POSTGRES_URL = PropertiesReader.getPostgresUrl();
    private static final String DATABASE_NAME = PropertiesReader.getDatabaseName();
    private static final String POSTGRES_USER = PropertiesReader.getPostgresUser();
    private static final String POSTGRES_PASSWORD = PropertiesReader.getPostgresPassword();

    // TODO: I'm creating a new connection every time I want to query the DB, use a connectionPool to manage connections instead.
    // TODO: Currently this class is not thread safe at all, you can close connection in the middle of a query and reopen while closing.
    private Connection connection;

    public PostgresqlTalker() {
        connection = connect().get();
    }

    /**
     * This method demonstrates how to build a PreparedStatement for the complex
     * word translation query provided, replacing literal values with placeholders.
     *
     * @param searchedWord The word to search for (e.g., "TEST_Máí").
     * @param inputLanguage The name of the input language (e.g., "Kombe_TEST").
     * @param outputLanguage The name of the output language (e.g., "Bapuku_TEST").
     * @return An Optional containing the ResultSet if data is found, otherwise empty.
     * @throws SQLException If a database access error occurs.
     */
    @Override
    public Optional<TranslatedWordContent> getTranslatedWordContent(String searchedWord, String inputLanguage, String outputLanguage) {
        //TODO: Add also that the searchedWord could be contained in the alternative representations
        String query = "SELECT\n" +
                "    input_le.lexical_term AS input_word,\n" +
                "    input_le.phonetic_representation AS input_word_phonetic,\n" +
                "    input_le.audio_url AS input_word_audio_url,\n" +
                "    input_lang.language_name AS input_language_name,\n" +
                "    input_s.sense_order AS input_sense_order,\n" +
                "    input_s.definition AS input_sense_definition,\n" +
                "    input_s.type_of_lexicon AS input_sense_type_of_lexicon,\n" +
                "    input_s.conceptual_domain AS input_sense_conceptual_domain,\n" +
                "    input_s.sentence AS input_sense_example_sentence,\n" +
                "    input_s.audio_url_sentence AS input_sense_example_audio_url,\n" +
                "    td_input_to_output.translated_definition_text AS input_sense_definition_in_output_language,\n" +
                "    ts_input_to_output.translated_sentence_text AS input_sense_example_in_output_language,\n" +
                "    ts_input_to_output.translated_sentence_audio_url AS input_sense_example_in_output_language_audio_url,\n" +
                "    output_le.lexical_term AS translated_word,\n" +
                "    output_le.phonetic_representation AS translated_word_phonetic,\n" +
                "    output_le.audio_url AS translated_word_audio_url,\n" +
                "    output_lang.language_name AS output_language_name,\n" +
                "    output_s.definition AS translated_sense_definition,\n" +
                "    output_s.type_of_lexicon AS translated_sense_type_of_lexicon,\n" +
                "    output_s.conceptual_domain AS translated_sense_conceptual_domain,\n" +
                "    output_s.sentence AS translated_sense_example_sentence,\n" +
                "    output_s.audio_url_sentence AS translated_sense_example_audio_url,\n" +
                "    td_output_to_input.translated_definition_text AS translated_sense_definition_in_input_language,\n" +
                "    ts_output_to_input.translated_sentence_text AS translated_sense_example_in_input_language\n" +
                "    ts_output_to_input.translated_sentence_audio-url AS translated_sense_example_in_input_language_audio_url\n" +
                "FROM\n" +
                "    lexical_entries AS input_le\n" +
                "JOIN\n" +
                "    languages AS input_lang\n" +
                "    ON input_le.language_id = input_lang.language_id\n" +
                "    AND input_lang.language_name = ?\n" +
                "JOIN\n" +
                "    senses AS input_s\n" +
                "    ON input_le.lexical_entry_id = input_s.lexical_entry_id\n" +
                "JOIN\n" +
                "    lexical_translation AS lt\n" +
                "    ON input_s.sense_id = lt.sense_id\n" +
                "JOIN\n" +
                "    languages AS lt_target_lang\n" +
                "    ON lt.language_id = lt_target_lang.language_id\n" +
                "    AND lt_target_lang.language_name = ?\n" +
                "JOIN\n" +
                "    senses AS output_s\n" +
                "    ON lt.target_sense_id = output_s.sense_id\n" +
                "JOIN\n" +
                "    lexical_entries AS output_le\n" +
                "    ON output_s.lexical_entry_id = output_le.lexical_entry_id\n" +
                "JOIN\n" +
                "    languages AS output_lang\n" +
                "    ON output_le.language_id = output_lang.language_id\n" +
                "    AND output_lang.language_name = ?\n" +
                "LEFT JOIN\n" +
                "    translated_definitions AS td_output_to_input\n" +
                "    ON output_s.sense_id = td_output_to_input.sense_id\n" +
                "    AND td_output_to_input.language_id = input_lang.language_id\n" +
                "LEFT JOIN\n" +
                "    translated_sentence AS ts_output_to_input\n" +
                "    ON output_s.sense_id = ts_output_to_input.sense_id\n" +
                "    AND ts_output_to_input.language_id = input_lang.language_id\n" +
                "LEFT JOIN\n" +
                "    translated_definitions AS td_input_to_output\n" +
                "    ON input_s.sense_id = td_input_to_output.sense_id\n" +
                "    AND td_input_to_output.language_id = output_lang.language_id\n" +
                "LEFT JOIN\n" +
                "    translated_sentence AS ts_input_to_output\n" +
                "    ON input_s.sense_id = ts_input_to_output.sense_id\n" +
                "    AND ts_input_to_output.language_id = output_lang.language_id\n" +
                "WHERE\n" +
                "    input_le.lexical_term ILIKE ?\n" +
                "ORDER BY\n" +
                "    input_s.sense_order;";

        Optional<PreparedStatement> preparedStatementOptional = createPreparedStatement(query);
        if (preparedStatementOptional.isEmpty()) Optional.empty();
        PreparedStatement preparedStatement = preparedStatementOptional.get();

        try {
            preparedStatement.setObject(1, inputLanguage);
            preparedStatement.setObject(2, outputLanguage);
            preparedStatement.setObject(3, outputLanguage);
            preparedStatement.setObject(4, searchedWord);


            Optional<ResultSet> resultSetOptional = executeQuery(preparedStatement);
            if (resultSetOptional.isEmpty()) {
                return null;
            }

            TranslatedWordContentMapper mapper = new TranslatedWordContentMapper();
            return Optional.of(mapper.mapResultSetToTranslatedWordContent(resultSetOptional.get()));

        } catch (SQLException sqlException) {
            //logger.error("Unable to perform word  translation search", sqlException);
            String errorQuerySummary = "Word search SQL error:" +
                    "- Word input: " + searchedWord + "\n" +
                    "- Input language: " + inputLanguage + "\n" +
                    "- Output language: " + outputLanguage + ";";

            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }
    }

    @Override
    public boolean upsertWord(UpsertedWordContent upsertedWordContent) {
        UUID lexicalEntryId = upsertLexicalContent(upsertedWordContent).get();
        List<UpsertedWordContent.SenseContent> upsertedSenseContentList = upsertedWordContent.getSenseContentFields();
        for (int i = 0; i < upsertedSenseContentList .size(); i++) {
            Integer senseId = upsertSenseContent(upsertedSenseContentList.get(i), lexicalEntryId, i).get();
            List<UpsertedWordContent.SenseTranslation> upsertedSenseTranslationList = upsertedSenseContentList.get(i).getSenseTranslations();
            for (int j = 0; j < upsertedSenseTranslationList .size(); j++) {
                upsertSenseTranslationContent(upsertedSenseTranslationList.get(j), senseId);
            }
        }


        if (lexicalEntryId != null) {
            return true;
        } else {
            return false;
        }
    }

    private Optional<UUID> upsertLexicalContent(UpsertedWordContent upsertedWordContent) {
        UUID lexicalEntryId = UUID.randomUUID();

        // Print the generated UUID
        System.out.println("Lexical entry " + upsertedWordContent.getWordText() + " UUID: " + lexicalEntryId);

        // The SQL query to get the language_id and then perform the upsert
        String query = """
            WITH lang_id AS (
                SELECT language_id
                FROM languages
                WHERE language_name = ?
            )
            INSERT INTO lexical_entries (
                lexical_entry_id, 
                language_id, 
                lexical_term, 
                phonetic_representation, 
                audio_url, 
                created_at, 
                updated_at
            )
            SELECT 
                ?, 
                (SELECT language_id FROM lang_id), 
                ?, 
                ?, 
                ?, 
                CURRENT_TIMESTAMP, 
                CURRENT_TIMESTAMP
            ON CONFLICT (lexical_term, phonetic_representation) DO UPDATE SET 
                audio_url = EXCLUDED.audio_url,
                updated_at = CURRENT_TIMESTAMP
            RETURNING lexical_entry_id;
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set parameters
            preparedStatement.setString(1, upsertedWordContent.getWordLanguage());
            preparedStatement.setObject(2, lexicalEntryId);
            preparedStatement.setString(3, upsertedWordContent.getWordText());
            preparedStatement.setString(4, upsertedWordContent.getWordPhonetic());
            preparedStatement.setString(5, upsertedWordContent.getWordAudioUrl());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Return the UUID of the inserted or existing row
                    return Optional.of((UUID) rs.getObject(1));
                }
            }
        } catch (SQLException sqlException) {
            String errorQuerySummary = "Word upsertion SQL error:" +
                    "- Word input: " + upsertedWordContent.getWordText() + "\n" +
                    "- Input language: " + upsertedWordContent.getWordLanguage() + "\n" + ";";

            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }

        return Optional.empty(); // Return empty if the operation failed
    }

    private Optional<Integer> upsertSenseContent(UpsertedWordContent.SenseContent senseContent, UUID lexicalEntryId, int senseOrder) {
        String query = """
            WITH existing_sense AS (
                SELECT 1
                FROM senses
                WHERE lexical_entry_id = ? AND definition = ?
            )
            , insert_or_update AS (
                INSERT INTO senses (
                    lexical_entry_id, 
                    sense_order, 
                    definition, 
                    type_of_lexicon, 
                    conceptual_domain, 
                    sentence, 
                    audio_url_sentence, 
                    created_at, 
                    updated_at
                )
                SELECT ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
                ON CONFLICT (lexical_entry_id, conceptual_domain, type_of_lexicon) DO UPDATE
                SET 
                    definition = EXCLUDED.definition, 
                    sentence = EXCLUDED.sentence,
                    updated_at = CURRENT_TIMESTAMP
                RETURNING sense_id
            )
            SELECT sense_id FROM insert_or_update;
            """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Check for existing definition first
            preparedStatement.setObject(1, lexicalEntryId);
            preparedStatement.setString(2, senseContent.getDefinition());

            // Parameters for the INSERT part of the query
            preparedStatement.setObject(3, lexicalEntryId);
            preparedStatement.setInt(4, senseOrder);
            preparedStatement.setString(5, senseContent.getDefinition());
            preparedStatement.setString(6, senseContent.getLexiconType());
            preparedStatement.setString(7, senseContent.getConceptualDomain());
            preparedStatement.setString(8, senseContent.getSentenceExample());
            preparedStatement.setString(9, senseContent.getSentenceExampleAudio());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    int value = rs.getInt("sense_id");
                    return Optional.of(rs.getInt("sense_id"));
                }
            }
        } catch (SQLException sqlException) {
            String errorQuerySummary = "Sense upsertion SQL error:" +
                    "- Sense input: " + senseContent.getDefinition() + "\n" +
                    "- Word UUID: " + lexicalEntryId + "\n" + ";";

            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }

        return Optional.empty();
    }

    private boolean upsertSenseTranslationContent(UpsertedWordContent.SenseTranslation senseTranslation, int senseId) {

        try {
            Optional<Integer> definitionTranslationId = upsertDefinitionTranslationContent(senseTranslation, senseId);
            if (definitionTranslationId.isEmpty() || definitionTranslationId.get() <= 0) {
                return false;
            }
            Optional<Integer> sentenceTranslationId = upsertSentenceTranslationContent(senseTranslation, senseId);
            if (sentenceTranslationId.isEmpty() || sentenceTranslationId.get() <= 0) {
                return false;
            }
            return true;
        } catch (SQLException sqlException) {
            String errorQuerySummary = "Defintion or sentence upsertion SQL error:" +
                    "- Definition translation input: " + senseTranslation.getDefinitionTranslation() + "\n" +
                    "- Sentence translation input: " + senseTranslation.getExampleSentenceTranslation() + "\n" +
                    "- Language translation input: " + senseTranslation.getTranslationLanguage() + "\n" +
                    "- Sense ID: " + senseId+ "\n" + ";";

            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }
    }

    private Optional<Integer> upsertDefinitionTranslationContent(UpsertedWordContent.SenseTranslation senseTranslation, int senseId) throws SQLException {
        String query = """
        WITH lang_id AS (
            SELECT language_id
            FROM languages
            WHERE language_name = ?
        )
        INSERT INTO translated_definitions (
            sense_id,
            language_id,
            translated_definition_text,
            created_at,
            updated_at
        )
        SELECT
            ?,
            l.language_id,
            ?,
            NOW(),
            NOW()
        FROM lang_id l
        ON CONFLICT (sense_id, language_id) DO UPDATE SET
            translated_definition_text = EXCLUDED.translated_definition_text,
            updated_at = NOW()
        RETURNING *;
        """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setObject(1, senseTranslation.getTranslationLanguage());
            preparedStatement.setInt(2, senseId);
            preparedStatement.setString(3, senseTranslation.getDefinitionTranslation());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("translated_definition_id"));
                }
            }
        } catch (SQLException sqlException) {
            throw sqlException;
        }

        return Optional.empty();
    }

    private Optional<Integer> upsertSentenceTranslationContent(UpsertedWordContent.SenseTranslation senseTranslation, int senseId) throws SQLException {
        String query = """
                WITH lang_id AS (
                    SELECT language_id
                    FROM languages
                    WHERE language_name = ?
                )
                INSERT INTO translated_sentence (
                    sense_id,
                    language_id,
                    translated_sentence_text,
                    translated_sentence_audio_url,
                    created_at,
                    updated_at
                )
                SELECT
                    ?,
                    l.language_id,
                    ?,
                    ?,
                    NOW(),
                    NOW()
                FROM lang_id l
                ON CONFLICT (sense_id, language_id) DO UPDATE SET
                    translated_sentence_text = EXCLUDED.translated_sentence_text,
                    translated_sentence_audio_url = EXCLUDED."translated_sentence_audio_url",
                    updated_at = NOW()
                RETURNING *;
                """;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setObject(1, senseTranslation.getTranslationLanguage());
            preparedStatement.setInt(2, senseId);
            preparedStatement.setString(3, senseTranslation.getExampleSentenceTranslation());
            preparedStatement.setString(4, senseTranslation.getExampleSentenceTranslationAudio());

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("translated_sentence_id"));
                }
            }
        } catch (SQLException sqlException) {
            throw sqlException;
        }

        return Optional.empty();
    }


    @Override
    public List<String> getLanguageList() {
        String query = "SELECT language_name FROM languages;";
        PreparedStatement preparedStatement = createPreparedStatement(query).get();

        try {
            ResultSet resultSet = executeQuery(preparedStatement).get();
            List<String> languageList = new ArrayList<String>();

            while(resultSet.next()) {
                languageList.add(resultSet.getString("language_name"));
            }
            return languageList;

        } catch (SQLException sqlException) {
            //logger.error("Unable to perform word  translation search", sqlException);
            String errorQuerySummary = "Language list SQL error";

            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }

    }

    @Override
    public List<String> getLexiconTypeList() {
        String query = "SELECT DISTINCT type_of_lexicon FROM senses;";
        PreparedStatement preparedStatement = createPreparedStatement(query).get();

        try {
            ResultSet resultSet = executeQuery(preparedStatement).get();
            List<String> lexiconTypeList = new ArrayList<String>();

            while(resultSet.next()) {
                lexiconTypeList.add(resultSet.getString("type_of_lexicon"));
            }
            return lexiconTypeList;

        } catch (SQLException sqlException) {
            //logger.error("Unable to perform word  translation search", sqlException);
            String errorQuerySummary = "Lexicon type list SQL error";
            throw new WordQuerySQLException(sqlException, errorQuerySummary);
        }
    }

    @Override
    public NdoweWord getNdoweWordTranslation(String wordInput, String inputLanguage, String outputLanguage) {
        return null;
    }

    @Override
    public boolean addNewWord(NdoweWord newNdoweWord) {
        return false;
    }

    @Override
    public boolean editWord(NdoweWord editedNdoweWord) {
        return false;
    }

/*
    @Override
    public void addCardBatchToDatabase(List<Card> cardBatch) {
        checkLimitRowsPerInsert(cardBatch.size());

        String query = "INSERT INTO \"card_data\"(\"id\",\"name\",\"type_line\",\"cmc\",\"mana_cost\",\"power\",\"toughness\",\"rarity\",\"oracle_text\",\"flavor_text\",\"colors\",\"loyalty\",\"special_card_type\",\"defense\",\"collector_number\",\"color_identity\",\"set_name\")\n"
                + "VALUES \n"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),\n".repeat(cardBatch.size() - 1)
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n"
                + "ON CONFLICT(id) DO UPDATE SET\n"
                + "name = EXCLUDED.name,"
                + "type_line = EXCLUDED.type_line,"
                + "cmc = EXCLUDED.cmc,"
                + "mana_cost = EXCLUDED.mana_cost,"
                + "power = EXCLUDED.power,"
                + "toughness = EXCLUDED.toughness,"
                + "rarity = EXCLUDED.rarity,"
                + "oracle_text = EXCLUDED.oracle_text,"
                + "flavor_text = EXCLUDED.flavor_text,"
                + "colors = EXCLUDED.colors,"
                + "loyalty = EXCLUDED.loyalty,"
                + "special_card_type = EXCLUDED.special_card_type,"
                + "defense = EXCLUDED.defense,"
                + "collector_number = EXCLUDED.collector_number,"
                + "color_identity = EXCLUDED.color_identity,"
                + "set_name = EXCLUDED.set_name;";

        Optional<PreparedStatement> preparedStatementOptional = createPreparedStatement(query);
        if (preparedStatementOptional.isEmpty()) return;
        PreparedStatement preparedStatement = preparedStatementOptional.get();

        try {
            int parameterIndex = 1;
            for (Card card : cardBatch) {
                preparedStatement.setObject(parameterIndex, card.getId(), Types.OTHER);
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getName());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getTypeLine());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getCmc());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getManaCost());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getPower());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getToughness());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getRarity());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getOracleText());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getFlavorText());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getColors());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getLoyalty());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getSpecialCardType().getValue());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getDefense());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getCollectorNumber());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getColorIdentity());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, card.getSetName());
                parameterIndex++;
            }
        } catch (SQLException sqlException) {
            logger.error("Unable to create prepared statement while trying to add card batch to the database", sqlException);
            closeConnection();
            return;
        }

        executeUpdate(preparedStatement);
    }

    @Override
    public void addCardAdditionalInfoBatchToDatabase(List<CardAdditionalInfo> cardAdditionalInfoList) {
        checkLimitRowsPerInsert(cardAdditionalInfoList.size());

        String query = "INSERT INTO \"card_additional_info\"(\"id\",\"rulings_uri\",\"scryfall_uri\",\"image_status\",\"eur_price\",\"usd_price\",\"artist\",\"png_image_uris\",\"border_crop_image_uris\",\"art_crop_image_uris\",\"large_image_uris\",\"normal_image_uris\",\"small_image_uris\",\"digital\")\n"
                + "VALUES \n"
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?),\n".repeat(cardAdditionalInfoList.size() - 1)
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)\n"
                + "ON CONFLICT(id) DO UPDATE SET\n"
                + "rulings_uri = EXCLUDED.rulings_uri,"
                + "scryfall_uri = EXCLUDED.scryfall_uri,"
                + "image_status = EXCLUDED.image_status,"
                + "eur_price = EXCLUDED.eur_price,"
                + "usd_price = EXCLUDED.usd_price,"
                + "artist = EXCLUDED.artist,"
                + "png_image_uris = EXCLUDED.png_image_uris,"
                + "border_crop_image_uris = EXCLUDED.border_crop_image_uris,"
                + "art_crop_image_uris = EXCLUDED.art_crop_image_uris,"
                + "large_image_uris = EXCLUDED.large_image_uris,"
                + "normal_image_uris = EXCLUDED.normal_image_uris,"
                + "small_image_uris = EXCLUDED.small_image_uris,"
                + "digital = EXCLUDED.digital;";

        Optional<PreparedStatement> preparedStatementOptional = createPreparedStatement(query);
        if (preparedStatementOptional.isEmpty()) return;
        PreparedStatement preparedStatement = preparedStatementOptional.get();

        try {
            int parameterIndex = 1;
            for (CardAdditionalInfo cardAdditionalInfo : cardAdditionalInfoList) {
                Array pngImageUrisArray = cardAdditionalInfo.getPngImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getPngImageUri());
                Array borderCropImageUrisArray = cardAdditionalInfo.getBorderCropImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getBorderCropImageUri());
                Array artCropImageUrisArray = cardAdditionalInfo.getArtCropImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getArtCropImageUri());
                Array largeImageUrisArray = cardAdditionalInfo.getLargeImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getLargeImageUri());
                Array normalImageUrisArray = cardAdditionalInfo.getNormalImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getNormalImageUri());
                Array smallImageUrisArray = cardAdditionalInfo.getSmallImageUri() == null ? null : preparedStatement.getConnection().createArrayOf("TEXT", cardAdditionalInfo.getSmallImageUri());

                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getId(), Types.OTHER);
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getRulingsUri());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getScryfallUri());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getImage_status().getValue());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getEurPrice());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getUsdPrice());
                parameterIndex++;
                preparedStatement.setObject(parameterIndex, cardAdditionalInfo.getArtist());
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, pngImageUrisArray);
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, borderCropImageUrisArray);
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, artCropImageUrisArray);
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, largeImageUrisArray);
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, normalImageUrisArray);
                parameterIndex++;
                preparedStatement.setArray(parameterIndex, smallImageUrisArray);
                parameterIndex++;
                preparedStatement.setBoolean(parameterIndex, cardAdditionalInfo.isDigital());
                parameterIndex++;
            }
        } catch (SQLException sqlException) {
            logger.error("Unable to create prepared statement while trying to add card additional info batch to the database", sqlException);
            closeConnection();
            return;
        }

        executeUpdate(preparedStatement);
    }

    @Override
    public void insertCardImagesIntoDatabase(String cardId, byte[] imageData, boolean isFrontFaceImage) {
        if (imageData == null || imageData.length == 0) {
            logger.error("Attempted to insert an image to the database with null or no data");
            return;
        }

        String tableName = (isFrontFaceImage) ? FRONT_FACE_CARD_IMAGE_DATABASE_TABLE_NAME : BACK_FACE_CARD_IMAGE_DATABASE_TABLE_NAME;

        String query = "INSERT INTO \"" + tableName + "\"(id, normal)\n"
                + "VALUES \n"
                + "(?, ?)"
                + "ON CONFLICT DO NOTHING;";

        Optional<PreparedStatement> preparedStatementOptional = createPreparedStatement(query);
        if (preparedStatementOptional.isEmpty()) return;
        PreparedStatement preparedStatement = preparedStatementOptional.get();

        try {
            preparedStatement.setObject(1, cardId, Types.OTHER);
            preparedStatement.setBytes(2, imageData);

        } catch (SQLException sqlException) {
            logger.error("Unable to create prepared statement while trying to insert image to the database", sqlException);
            closeConnection();
            return;
        }

        executeUpdate(preparedStatement);
    }

    @Override
    public Optional<List<CardImageUrlInfo>> readALlMissingImagesUrl() {
        String query = "SELECT cai.id, cai.normal_image_uris, cd.special_card_type\n"
                + "FROM card_additional_info as cai\n"
                + "LEFT OUTER JOIN \"CardImages\" AS ci\n"
                + "ON cai.id = ci.id\n"
                + "LEFT OUTER JOIN additional_face_images as afi\n"
                + "ON cai.id = afi.id\n"
                + "INNER JOIN card_data as cd\n"
                + "ON cai.id = cd.id\n"
                + "WHERE ci.id IS NULL\n"
                + "OR (cd.special_card_type in('transform', 'meld', 'modal_double_face', 'reversible') AND afi.id IS NULL);";

        Optional<PreparedStatement> preparedStatementOptional = createPreparedStatement(query);
        if (preparedStatementOptional.isEmpty()) return Optional.empty();
        PreparedStatement preparedStatement = preparedStatementOptional.get();

        return getCardImageUrlInfo(preparedStatement);
    }
*/

    private Optional<Connection> connect() {
        Connection connection;
        try {
            connection = DriverManager.getConnection(POSTGRES_URL + DATABASE_NAME, POSTGRES_USER, POSTGRES_PASSWORD);
            return Optional.of(connection);
        } catch (SQLException sqlException) {
            System.out.println("Unable to connect to the database with the following parameters: URL: " + POSTGRES_URL
                    + ", Database name: " + DATABASE_NAME + "\n" + sqlException);
            return Optional.empty();
        }
    }

    private Optional<PreparedStatement> createPreparedStatement(String query) {
        Optional<Connection> connectionOptional = connect();
        if (connectionOptional.isEmpty()) return Optional.empty();
        connection = connectionOptional.get();

        // TODO: Use try with resources
        try {
            connection.setAutoCommit(true);
            return Optional.of(connection.prepareStatement(query));
        } catch (SQLException sqlException) {
            /*logger.error("Unable to create prepared statement for the query: " + query, sqlException);*/
            closeConnection();
            return Optional.empty();
        }
    }

    private Optional<ResultSet> executeQuery(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            closeConnection();
            return Optional.of(resultSet);
        } catch (SQLException sqlException) {
            /*logger.error("Error while executing QUERY with preparedStatement", sqlException);*/
            closeConnection();
            return Optional.empty();
        }
    }

    private void executeUpdate(PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeUpdate();
            closeConnection();
        } catch (SQLException sqlException) {
            /*logger.error("Unable to execute UPDATE with preparedStatement", sqlException);*/
            closeConnection();
        }
    }



    private void closeConnection() {
        if (connection == null) return;

        try {
            connection.close();
            connection = null;
        } catch (SQLException sqlException) {
            /*logger.error("Unable to close connection to database", sqlException);*/
        }
    }
}
