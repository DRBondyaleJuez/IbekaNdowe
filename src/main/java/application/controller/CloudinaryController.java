package application.controller;

import application.utils.CloudinaryPropertiesReader;
import application.utils.PropertiesReader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

@Service
public class CloudinaryController {

    public CloudinaryController() {
    }

    public boolean submitAudioFile(File audioFile) {
        //TODO: Cloudinary Course: https://training.cloudinary.com/learn/course/introduction-to-cloudinary-for-java-developers-90-minute-course/before-the-lessons/what-to-expect
        // 1. Define the POST request body (JSON in this example)

        /*String jsonPayload = "{\"title\": \"foo\", \"body\": \"bar\", \"userId\": 1}";

        // 2. Define the target URL
        String url = "https://jsonplaceholder.typicode.com/posts";

        // 3. Create an HttpClient instance
        HttpClient client = HttpClient.newHttpClient();

        // 4. Build the POST request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json") // Specify the content type
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload)) // Set the method and body
                .build();

        try {
            // 5. Send the request and get the response
            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

            // 6. Print the status code and response body
            System.out.println("Response Status Code: " + response.statusCode());
            System.out.println("Response Body:\n" + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
         */
        return true;

    }
}
