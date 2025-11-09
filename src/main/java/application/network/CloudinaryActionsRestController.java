package application.network;

import application.controller.CloudinaryController;
import application.model.UpsertedWordContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")

public class CloudinaryActionsRestController {
    private final CloudinaryController mainController;
    // Define acceptable audio MIME types for validation
    private static final List<String> ACCEPTED_AUDIO_TYPES = Arrays.asList(
            "audio/mpeg",
            "audio/wav",
            "audio/webm",
            "audio/webm;codecs=opus",
            "audio/ogg",
            "audio/mp4" // Sometimes used for audio-only MP4 files
    );
    private static final String DUMMY_UPLOAD_DIR = "src/test-dummy-content/";

    public CloudinaryActionsRestController(CloudinaryController cloudinaryController) {
        this.mainController = cloudinaryController;
    }
    // POST API to add new details
    @PostMapping("/upload-audio-file")
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile audioFile) {

        if (audioFile.isEmpty()) {
            return new ResponseEntity("File upload failed: The submitted file is empty.",HttpStatus.BAD_REQUEST);
        }

        try {
            // 1. Get the file's binary data (as a byte array)
            byte[] fileBytes = audioFile.getBytes();

            // 2. Get the original filename (useful for logging or naming)
            String fileName = audioFile.getOriginalFilename();
            //TODO: Normalize audio Filename with the word recorded and the date

            // 3. Get the file's content type (e.g., "audio/mpeg")
            String contentType = audioFile.getContentType();

            System.out.println("Received file: " + fileName + " with content type: " + contentType);

            // ----------------------------------------------------------------------
            // ✅ 1. VALIDATION: Check if the file is an acceptable audio type
            // ----------------------------------------------------------------------
            if (!isAudioFile(contentType)) {
                String errorMsg = "File upload failed: Invalid file type: " + contentType + ". Only audio files are allowed.";
                System.err.println(errorMsg);
                return new ResponseEntity(errorMsg, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
            }

            // ----------------------------------------------------------------------
            // ✅ 2. DUMMY SAVE: Save the file to the local test folder for inspection
            // ----------------------------------------------------------------------
            Path uploadPath = Paths.get(DUMMY_UPLOAD_DIR);
            // Create the directory if it doesn't exist
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Create a unique file name to avoid overwriting (optional but recommended)
            String savedFileName = System.currentTimeMillis() + "_" + fileName;
            Path filePath = uploadPath.resolve(savedFileName);

            // Save the file
            Files.copy(audioFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Dummy file saved successfully to: " + filePath.toAbsolutePath());

            // ----------------------------------------------------------------------------------
            // --- NEXT STEP: Implement the Cloudinary upload logic here!
            // ----------------------------------------------------------------------------------

            // ************ Placeholder for Cloudinary Upload Service Call ************
            // You would typically pass 'fileBytes', 'fileName', or the whole 'file' object
            // to a separate service layer that handles the actual Cloudinary API call.

            // Example of a successful response object (you'd get this from your Cloudinary service)
            String secureUrl = "https://res.cloudinary.com/your-cloud/video/upload/v123456789/uploaded_audio_TEST_TEST.mp3";

            // ************************************************************************

            // Return a success response with the resulting Cloudinary URL
            return ResponseEntity.ok(secureUrl);

        } catch (IOException e) {
            // Handle errors during file processing (e.g., cannot read bytes)
            return ResponseEntity.internalServerError().body("Could not process file.");
        }
    }

    private boolean isAudioFile(String contentType) {
        return contentType != null && ACCEPTED_AUDIO_TYPES.contains(contentType.toLowerCase());
    }
}
