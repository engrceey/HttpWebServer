package model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {

    @BeforeAll
    @DisplayName("Opens the server before each test")
    static void start() {
        try {
            Server server = new Server(8080);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Queries Html page")
    void testHTMLPage() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080")).build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(response.headers().allValues("content-type").toString(), "[text/html]");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testJsonPage() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/json")).build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode());
            assertEquals(response.headers().allValues("content-type").toString(), "[application/json]");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Checking for incorrect input")
    void testErrorPage() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/index")).build();

        HttpResponse<String> response;

        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(404, response.statusCode());
            assertEquals(response.headers().allValues("content-type").toString(), "[text/html]");
            assertEquals(response.body(), "<b> Not found......");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}