package org.example;
import java.util.*;

public class GameMap {
    Random random = new Random();
    List<Room> kamers = new ArrayList<>();
    int randomGetalvijftotnegen;
    EvaluationStrategy gemini = new GeminiEvaluationStrategy();
    String[] vraag= {
            "Wat is de rol van de PO?",
            "Wat bespreek je tijdens een Daily Scrum?",
            "Wat toon je tijdens de Sprint Review?"
    };
    String[] naam = {
            "Sprint Planning",
            "Daily Scrum",
            "Sprint Review"
    };

    public GameMap() {
        randomGetalvijftotnegen = (random.nextInt(5) + 5);
        List<Integer> ids = new ArrayList<>();
        boolean floatingroom = true;
        while(floatingroom){
            floatingroom = false;
            for (int i = 1; i <= 9; i++) {
                ids.add(i);
            }
            Collections.shuffle(ids);

            for (int i = 0; i < randomGetalvijftotnegen; i++) {
                for (int y = 0; y < randomGetalvijftotnegen; y++) {
                    if(ids.get(i) != ids.get(y)){
                        if(!(ids.get(i) != ids.get(y+1)||ids.get(i) != ids.get(y-1)||ids.get(i) != ids.get(y+3)||ids.get(i) != ids.get(y-3))){
                            floatingroom = true;

                        }
                    }
                }
            }

        }



        for (int i = 0; i < randomGetalvijftotnegen; i++) {
            int randomkamer = random.nextInt(vraag.length);
            String randomvraag = vraag[randomkamer];
            String randomnaam = naam[randomkamer];


            kamers.add(Room.of(ids.get(i), randomnaam , randomvraag , gemini));
        }
    }

    public void viewmap() {
        for(int i=1; i <= 10; i++){
            boolean gevonden = false;
            for (Room kamer : kamers) {
                if(kamer.id == i){
                    System.out.print("[" + kamer.id + "] " + kamer.name + " ");
                    gevonden = true;
                }
            }
            if(!gevonden){
                System.out.print("[" + i + "] " + "X" + " ");
            }
            if(i%3 == 0){
                System.out.println();
            }
        }
    }
}