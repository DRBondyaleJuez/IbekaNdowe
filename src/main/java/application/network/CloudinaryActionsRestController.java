package application.network;

import application.model.UpsertedWordContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cloudinary")

public class CloudinaryActionsRestController {
    // POST API to add new details
    @PostMapping("/upload-audio-file")
    public ResponseEntity<Boolean> uploadAudio(@RequestBody UpsertedWordContent upsertedWordContent) {
        mainController.upsertWord(upsertedWordContent);
        System.out.println("UPSERT WORD");
        System.out.println("Lexical term:" + upsertedWordContent.getWordText());
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
