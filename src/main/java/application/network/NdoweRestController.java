package application.network;

import application.controller.NdoweMainController;
import application.model.NdoweWordContent;
import application.model.TranslatedWordContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NdoweRestController {

    private final NdoweMainController mainController;

    public NdoweRestController(NdoweMainController mainController) {
        this.mainController = mainController;
    }

    @GetMapping("/test")
    public ResponseEntity<String> testEndpoint() {

        return new ResponseEntity<>("TEST", HttpStatus.OK);
    }

    // GET API to fetch all details
    @GetMapping("/ndowe-word-content")
    public ResponseEntity<NdoweWordContent> getNdoweWordContent(@RequestParam String searchedWord, @RequestParam String inputLanguage, @RequestParam String outputLanguage) {

        Optional<NdoweWordContent> searchedNdoweWordContent = null;

        if (searchedNdoweWordContent.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(searchedNdoweWordContent.get(), HttpStatus.OK);
    }

    // GET API to fetch all translated details
    @GetMapping("/translated-word-content")
    public ResponseEntity<TranslatedWordContent> getTranslatedWordContent(@RequestParam String searchedWord, @RequestParam String inputLanguage, @RequestParam String outputLanguage) {
        System.out.println(searchedWord + " " + inputLanguage + " " + outputLanguage);
        Optional<TranslatedWordContent> searchedTranslatedWordContent = mainController.getTranslatedWordContent(searchedWord, inputLanguage, outputLanguage);

        if (searchedTranslatedWordContent.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(searchedTranslatedWordContent.get(), HttpStatus.OK);
    }

    // POST API to add new details
    @PostMapping("/new-word")
    public String addNewWordContent(@RequestBody String jsonBody) {
        mainController.addNewWord();
        return "Post jsonBody";
    }

    // PUT API to add new details
    @PutMapping("/edit-word")
    public String editWordContent(@RequestBody String jsonBody) {
        mainController.editWord();
        return "Put jsonBody";
    }

}
