package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GeminiService {
    private static final String API_KEY = "AIzaSyCGVQlWdYFBK7iheb4AS1tsnae-wrFmjv0";
    private static final String ENDPOINT =
            "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key="
                    + API_KEY;

    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final Pattern TEXT_PATTERN =
            Pattern.compile("\"text\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);

    private static boolean debug = false;

    /** Turn on/off debug logging. */
    public static void setDebug(boolean mode) {
        debug = mode;
    }

    /** Returns whether debug is currently on */
    public static boolean isDebug() {
        return debug;
    }

    /**
     * Send vraag+antwoord to Gemini and return either
     * "GOED: <one-sentence>" or "FOUT: <one-sentence>"
     */
    public static String evaluate(String vraag, String antwoord) {
        try {
            String prompt =
                    "Je bent een Scrum-trainer. Beoordeel kort. Antwoord ALLEEN:\n"
                            + "GOED: <één zin>\n"
                            + "OF\n"
                            + "FOUT: <één zin>\n"
                            + "Vraag: '" + vraag + "'\n"
                            + "Antwoord: '" + antwoord + "'";

            String safePrompt = prompt.replace("\"", "\\\"");
            String jsonBody = """
                {"contents":[{"parts":[{"text":"%s"}]}]}
                """.formatted(safePrompt);

            if (debug) {
                System.out.println("[DEBUG] Request JSON:");
                System.out.println(jsonBody);
            }

            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(ENDPOINT))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> resp = CLIENT.send(req, HttpResponse.BodyHandlers.ofString());
            String body = resp.body();

            if (debug) {
                System.out.println("[DEBUG] Response status: " + resp.statusCode());
                System.out.println("[DEBUG] Response body:   " + body);
            }

            if (resp.statusCode() != 200) {
                return "FOUT: API returned HTTP " + resp.statusCode();
            }

            Matcher m = TEXT_PATTERN.matcher(body);
            if (m.find()) {
                return m.group(1).replace("\\n", "\n").trim();
            } else {
                return "FOUT: Onleesbare AI-respons.";
            }
        } catch (Exception e) {
            if (debug) e.printStackTrace();
            return "FOUT: Fout bij AI-evaluatie – " + e.getMessage();
        }
    }
}
