package network;

import controller.NdoweMainController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class NdoweRestController {

    private final NdoweMainController maincontroller;

    public NdoweRestController() {
        this.maincontroller = new NdoweMainController();
    }

    // GET API to fetch all details
    @GetMapping("/word")
    public String getWordContent() {
        return "hello world";
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
