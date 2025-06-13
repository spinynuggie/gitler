package org.example;

import java.util.*;
import java.nio.file.*;
import java.io.IOException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.questions.Question;

public final class Questions {
    private static final String QUESTIONS_JSON = "src/main/java/org/example/questions/questions.json";
    private static List<Question> LIST = new ArrayList<>();

    private Questions() {}

    static {
        loadQuestions();
    }

    private static void loadQuestions() {
        LIST = new ArrayList<>();
        try {
            String json = Files.readString(Paths.get(QUESTIONS_JSON));
            Gson gson = new Gson();
            LIST = gson.fromJson(json, new TypeToken<List<Question>>(){}.getType());
            if (LIST == null) LIST = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Kan questions.json niet lezen: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Fout bij parsen van questions.json: " + e.getMessage());
        }
    }

    public static void reload() {
        loadQuestions();
    }

    public static Question get(int i)      { return LIST.get(i); }

    public static List<Question> byCategory(String category) {
        List<Question> result = new ArrayList<>();
        for (Question q : LIST) {
            if (q.getCategory().equalsIgnoreCase(category)) {
                result.add(q);
            }
        }
        return result;
    }

    public static Set<String> allCategories() {
        Set<String> categories = new LinkedHashSet<>();
        for (Question q : LIST) {
            categories.add(q.getCategory());
        }
        return categories;
    }
}
