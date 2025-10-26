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
import java.util.Map;

@RestController
@RequestMapping("/api/cloudinary")

public class CloudinaryActionsRestController {
    private final CloudinaryController mainController;
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

            // 3. Get the file's content type (e.g., "audio/mpeg")
            String contentType = audioFile.getContentType();

            System.out.println("Received file: " + fileName + " with content type: " + contentType);

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
}
