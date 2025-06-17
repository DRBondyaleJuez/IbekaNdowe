package network;

import controller.NdoweMainController;
import model.NdoweWordContent;
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

    private final NdoweMainController maincontroller;

    public NdoweRestController() {
        this.maincontroller = new NdoweMainController();
    }

    // GET API to fetch all details
    @GetMapping("/ndowe-word-content")
    public ResponseEntity<NdoweWordContent> getNdoweWordContent(@RequestParam String searchedWord, @RequestParam String inputLanguage, @RequestParam String outputLanguage) {

        Optional<NdoweWordContent> searchedNdoweWordContent = maincontroller.getNdoweWordContent(searchedWord, inputLanguage, outputLanguage);

        if (searchedNdoweWordContent.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(searchedNdoweWordContent.get(), HttpStatus.OK);
    }

    // POST API to add new details
    @PostMapping("/new-word")
    public String addNewWordContent(@RequestBody String jsonBody) {
        maincontroller.addNewWord();
        return "Post jsonBody";
    }

    // PUT API to add new details
    @PutMapping("/edit-word")
    public String editWordContent(@RequestBody String jsonBody) {
        maincontroller.editWord();
        return "Put jsonBody";
    }

}
