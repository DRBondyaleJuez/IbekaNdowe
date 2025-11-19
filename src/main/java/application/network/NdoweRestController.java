package application.network;

import application.controller.NdoweMainController;
import application.model.NdoweWordContent;
import application.model.TranslatedWordContent;
import application.model.InsertedWordContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
        System.out.println("Input word: " + searchedWord + " in language " + inputLanguage + ". Getting translation in: " + outputLanguage + " ...");
        Optional<TranslatedWordContent> searchedTranslatedWordContent = mainController.getTranslatedWordContent(searchedWord, inputLanguage, outputLanguage);

        if (searchedTranslatedWordContent.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(searchedTranslatedWordContent.get(), HttpStatus.OK);
    }

    // GET API to fetch all languages
    @GetMapping("/languages")
    public ResponseEntity<List<String>> getLanguageList() {
        System.out.println("Get language list...");
        List<String> languages = mainController.getLanguageList();

        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    // GET API to fetch all languages
    @GetMapping("/lexicon-type")
    public ResponseEntity<List<String>> getLexiconTypeList() {
        System.out.println("Get lexicon type list...");
        List<String> languages = mainController.getLexiconTypeList();

        return new ResponseEntity<>(languages, HttpStatus.OK);
    }

    // POST API to add new details
    @PostMapping("/insert-word")
    public ResponseEntity<Boolean> upsertWordContent(@RequestBody InsertedWordContent insertedWordContent) {
        System.out.println("UPSERT WORD");
        System.out.println("Lexical term:" + insertedWordContent.getWordText());
        boolean insertionResponse = mainController.insertWord(insertedWordContent);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // PUT API to add new details
    @PutMapping("/edit-word")
    public String editWordContent(@RequestBody String jsonBody) {
        mainController.editWord();
        return "Put jsonBody";
    }

}
