package application.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

public class CloudinaryPropertiesReader {
    private static final Logger logger = LogManager.getLogger(PropertiesReader.class);
    private static final String urlSource = "/secrets.properties";
    private static final String POSTGRES_URL_PROPERTY_KEY = "postgres_url";
    private static final String DATABASE_NAME_PROPERTY_KEY = "database_name";
    private static final String POSTGRES_USER_PROPERTY_KEY = "postgres_user";
    private static final String POSTGRES_PASSWORD_PROPERTY_KEY = "postgres_password";
    private static final HashMap<String, String> propertiesMap = new HashMap<>();

    public void loadAllProperties() {
        readAllProperties();
        checkForMissingProperties();
    }

    public static String getPostgresUrl() {
        return propertiesMap.get(POSTGRES_URL_PROPERTY_KEY);
    }

    public static String getDatabaseName() {
        return propertiesMap.get(DATABASE_NAME_PROPERTY_KEY);
    }

    public static String getPostgresUser() {
        return propertiesMap.get(POSTGRES_USER_PROPERTY_KEY);
    }

    public static String getPostgresPassword() {
        return propertiesMap.get(POSTGRES_PASSWORD_PROPERTY_KEY);
    }

    private void readAllProperties() {
        try (
                InputStream secretsStream = PropertiesReader.class.getResourceAsStream(urlSource);
                BufferedReader secretsReader = new BufferedReader(new InputStreamReader(secretsStream))
        ) {
            String line;
            while ((line = secretsReader.readLine()) != null) {
                String[] keyValuePair = line.split("=", 2);
                if (keyValuePair.length != 2) {
                    logger.warn("Malformed property line in property file, line: \n" + line);
                    continue;
                }

                if (keyValuePair[1].isBlank()) {
                    logger.error("One of the properties has a blank value. Line: " + line);
                    throw new RuntimeException("Property key with blank value, unable to start application.");
                }

                propertiesMap.put(keyValuePair[0].trim(), keyValuePair[1].trim());
            }
        } catch (IOException ioException) {
            logger.error("Unable to read property file", ioException);
            throw new RuntimeException("Unable to read property file, unable to start application.", ioException);
        }
    }

    private void checkForMissingProperties() {
        List<String> propertiesKeys = List.of(
                POSTGRES_URL_PROPERTY_KEY,
                DATABASE_NAME_PROPERTY_KEY,
                POSTGRES_USER_PROPERTY_KEY,
                POSTGRES_PASSWORD_PROPERTY_KEY
        );

        for (String propertyKey : propertiesKeys) {
            if (propertiesMap.get(propertyKey) == null) {
                throwPropertyNotFoundError(propertyKey);
            }
        }
    }

    private void throwPropertyNotFoundError(String propertyKey) {
        logger.error(propertyKey + " property not found in property file");
        throw new RuntimeException(propertyKey + " property not found in property file, unable to start application.");
    }
}
