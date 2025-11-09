package application.controller;

import application.utils.CloudinaryPropertiesReader;
import application.utils.PropertiesReader;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.Map;
import java.util.Optional;

import static com.cloudinary.utils.ObjectUtils.asMap;

@Service
public class CloudinaryController {

    private Cloudinary cloudinary;
    private static final String CLOUD_NAME = CloudinaryPropertiesReader.getCloudinaryCloudName();
    private static final String CLOUD_API_KEY = CloudinaryPropertiesReader.getCloudinaryApiKey();
    private static final String CLOUD_API_SECRET = CloudinaryPropertiesReader.getCloudinaryApiSecret();


    public CloudinaryController() {
        cloudinary = new Cloudinary(
            ObjectUtils.asMap(
                    "cloud_name", CLOUD_NAME,
                    "api_key", CLOUD_API_KEY,
                    "api_secret", CLOUD_API_SECRET,
                    "secure", true
            )
        );
    }

    public Optional<String> uploadAudioFile(MultipartFile audioFile) throws IOException {
        //TODO: Verify if the file is already uploaded
        System.out.println("UPLOADING AUDIO FILE...");
        try {
            Map<String,Object> uploadResult = cloudinary.uploader().upload(
                    audioFile.getBytes(),
                    ObjectUtils.asMap(
                            "folder","ndowe-audios",
                            "resource_type", "raw"
                    )
            );
            String audioUrl = uploadResult.get("secure_url").toString();
            return Optional.of(audioUrl);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IOException(e);
        }
    }
}
