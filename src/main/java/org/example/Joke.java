package org.example;

public record Joke(String title, String body) {

    public static Joke fromText(String text) {
        String[] parts = text.split("\n", 2);
        if (parts.length == 2) {
            return new Joke(parts[0].trim(), parts[1].trim());
        }
        return new Joke(".", text.trim());
    }
}
