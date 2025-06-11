package persistence.database.postgresql;

import model.NdoweWord;
import utils.PropertiesReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import persistence.database.DatabaseTalker;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

public class PostgresqlTalker implements DatabaseTalker {
    private static final Logger logger = LogManager.getLogger(PostgresqlTalker.class);
    private static final String POSTGRES_URL = PropertiesReader.getPostgresUrl();
    private static final String DATABASE_NAME = PropertiesReader.getDatabaseName();
    private static final String POSTGRES_USER = PropertiesReader.getPostgresUser();
    private static final String POSTGRES_PASSWORD = PropertiesReader.getPostgresPassword();
    private static final int MAXIMUM_ROWS_PER_INSERT = 1000;
    private static final String FRONT_FACE_CARD_IMAGE_DATABASE_TABLE_NAME = "CardImages";
    private static final String BACK_FACE_CARD_IMAGE_DATABASE_TABLE_NAME = "additional_face_images";

    // TODO: I'm creating a new connection every time I want to query the DB, use a connectionPool to manage connections instead.
    // TODO: Currently this class is not thread safe at all, you can close connection in the middle of a query and reopen while closing.
    private Connection connection;

    public PostgresqlTalker() {
        connection = connect().get();
    }

    @Override
    public NdoweWord getNdoweWordContent(String searchedWord, String inputLanguage, String outputLanguage) {
        return null;
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
            logger.error("Unable to connect to the the database with the following parameters: URL: " + POSTGRES_URL
                    + ", Database name: " + DATABASE_NAME , sqlException);
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
            logger.error("Unable to create prepared statement for the query: " + query, sqlException);
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
            logger.error("Error while executing QUERY with preparedStatement", sqlException);
            closeConnection();
            return Optional.empty();
        }
    }

    private void executeUpdate(PreparedStatement preparedStatement) {
        try {
            preparedStatement.executeUpdate();
            closeConnection();
        } catch (SQLException sqlException) {
            logger.error("Unable to execute UPDATE with preparedStatement", sqlException);
            closeConnection();
        }
    }



    private void closeConnection() {
        if (connection == null) return;

        try {
            connection.close();
            connection = null;
        } catch (SQLException sqlException) {
            logger.error("Unable to close connection to database", sqlException);
        }
    }
}
