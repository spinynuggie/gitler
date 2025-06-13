package org.example;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static String getGeminiApiKey() {
        String key = dotenv.get("GEMINI_API_KEY");
        if (key == null || key.trim().isEmpty()) {
            throw new RuntimeException("GEMINI_API_KEY is not set in the .env file");
        }
        return key;
    }
} 