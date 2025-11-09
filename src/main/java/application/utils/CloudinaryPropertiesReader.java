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
    private static final String urlSource = "/application.properties";
    private static final String CLOUDINARY_CLOUD_NAME = "cloudinary_cloud_name";
    private static final String CLOUDINARY_API_KEY = "cloudinary_api_key";
    private static final String CLOUDINARY_API_SECRET = "cloudinary_api_secret";
    private static final HashMap<String, String> propertiesMap = new HashMap<>();

    public void loadAllProperties() {
        readAllProperties();
        checkForMissingProperties();
    }

    public static String getCloudinaryCloudName() {
        return propertiesMap.get(CLOUDINARY_CLOUD_NAME);
    }

    public static String getCloudinaryApiKey() {
        return propertiesMap.get(CLOUDINARY_API_KEY);
    }

    public static String getCloudinaryApiSecret() {
        return propertiesMap.get(CLOUDINARY_API_SECRET);
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
                CLOUDINARY_CLOUD_NAME,
                CLOUDINARY_API_KEY,
                CLOUDINARY_API_SECRET
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
