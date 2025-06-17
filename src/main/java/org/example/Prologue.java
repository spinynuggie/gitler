package org.example;

import java.util.Scanner;

public class Prologue {

    public Prologue() {
    }

    public void show(Scanner scanner) {
        System.out.println("\n--- PROLOOG ---\n");
        String text = """
                Jaar: 1939
                
                Je bent Adolf Gitler, een gefrustreerde student die zojuist is afgewezen voor de HBO-ICT-opleiding.\s
                Die afwijzing steekt diep — je bent vastbesloten om jezelf te bewijzen als de programmeur die de wereld wél verdient.
                
                Wanneer je hoort dat binnenkort de NAVO-top plaatsvindt (vreemd genoeg al in 1939), zie je je kans schoon.
                 Je plan? Infiltreer het evenement, hack hun systemen en confronteer de aanwezige politici. Want eerlijk gezegd — fuck ze.
                
                Vermomd betreed je het zwaar beveiligde gebouw waar de top plaatsvindt.\s
                Gelukkig heb je hulp van je trouwe vriend Benito Mussolinux — een insider bij het event en meester van command-line chaos.
                 Hij weet je te vertellen dat de aanwezige politici een dodelijke afkeer hebben van… scrum.
                
                Die informatie gebruik je als wapen.
                
                Bewapend met post-its, stand-up meetings en sprint reviews begin je aan je missie:
                 breek door de beveiliging, verspreid agile principes als sabotage, en laat iedereen zien waar je écht toe in staat bent.
                
                Jij bent Gitler.
                En dit is jouw commit.""";
        System.out.println(text);
        System.out.println("\nDruk op Enter om verder te gaan...");
        scanner.nextLine();
    }
} 