package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.questions.Question;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AdminPanel {
    private static final String QUESTIONS_JSON = "src/main/java/org/example/questions/questions.json";

    public static void show(Scanner scanner) {
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. Voeg vraag toe");
            System.out.println("2. Terug");
            System.out.print("Maak een keuze: ");
            String c = scanner.nextLine().trim();
            if ("1".equals(c)) {
                addQuestion(scanner);
            } else if ("2".equals(c)) {
                System.out.println();
                return;
            } else {
                System.out.println("⚠️ Ongeldige keuze.\n");
            }
        }
    }

    private static void addQuestion(Scanner scanner) {
        try {
            // Get categories from Questions
            List<String> categories = getCategories();
            System.out.println("Kies een categorie:");
            for (int i = 0; i < categories.size(); i++) {
                System.out.printf("%d. %s\n", i + 1, categories.get(i));
            }
            System.out.print("Nummer van categorie (of laat leeg voor nieuwe): ");
            String catChoice = scanner.nextLine().trim();
            String category;
            if (!catChoice.isEmpty()) {
                try {
                    int catIndex = Integer.parseInt(catChoice) - 1;
                    if (catIndex >= 0 && catIndex < categories.size()) {
                        category = categories.get(catIndex);
                    } else {
                        System.out.println("⚠️ Ongeldige keuze. Nieuwe categorie wordt gevraagd.");
                        System.out.print("Nieuwe categorie: ");
                        category = scanner.nextLine().trim();
                    }
                } catch (NumberFormatException e) {
                    System.out.print("Nieuwe categorie: ");
                    category = scanner.nextLine().trim();
                }
            } else {
                System.out.print("Nieuwe categorie: ");
                category = scanner.nextLine().trim();
            }
            System.out.print("Vraag: ");
            String text = scanner.nextLine().trim();

            List<Question> questions;
            String json = Files.readString(Paths.get(QUESTIONS_JSON));
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            questions = gson.fromJson(json, new TypeToken<List<Question>>(){}.getType());
            if (questions == null) questions = new ArrayList<>();

            questions.add(new Question(category, text));

            try (FileWriter writer = new FileWriter(QUESTIONS_JSON)) {
                gson.toJson(questions, writer);
            }
            Questions.reload();
            System.out.println("✅ Vraag toegevoegd!");
        } catch (IOException e) {
            System.err.println("Fout bij toevoegen van vraag: " + e.getMessage());
        }
    }

    // Helper to get categories from Questions
    private static List<String> getCategories() {
        return new ArrayList<>(Questions.allCategories());
    }
} 