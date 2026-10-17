package org.example;

import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class JokeApiClient {

    private static final String URL =
            "https://v2.jokeapi.dev/joke/Programming"
                    + "?blacklistFlags=nsfw,religious,political,racist,sexist,explicit";

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final Gson gson = new Gson();

    // The shape of JokeAPI's JSON response; Gson fills it in by field name
    private record ApiResponse(boolean error, String type,
                               String joke, String setup, String delivery) {}

    public CompletableFuture<Joke> fetchJoke() {
        HttpRequest request = HttpRequest.newBuilder(URI.create(URL))
                .timeout(Duration.ofSeconds(5))
                .header("Accept", "application/json")
                .GET()
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(this::parse);
    }

    private Joke parse(HttpResponse<String> response) {
        if (response.statusCode() != 200) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        ApiResponse data = gson.fromJson(response.body(), ApiResponse.class);
        if (data == null || data.error()) {
            throw new IllegalStateException("JokeAPI returned an error");
        }
        if ("twopart".equals(data.type())) {
            return new Joke(data.setup(), data.delivery());
        }
        return Joke.fromText(data.joke());
    }
}
