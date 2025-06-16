package org.example;

import java.util.Scanner;

public class Epilogue {

    public Epilogue() {
    }

    public void show(Scanner scanner) {
        System.out.println("\n--- EPILOOG ---\n");
        String text = """
                De stilte na de storm is oorverdovend.
                
                De NAVO-top is uit elkaar gevallen — niet door kogels, niet door bommen, maar door de ontwrichtende kracht van slecht gefaciliteerde scrum. Wat begon als een persoonlijke kruistocht tegen een opleiding die jou niet waardig achtte, eindigde in een digitale aanval die de fundamenten van internationale samenwerking deed wankelen.
                
                Adolf Gitler staat op het dak van het NAVO-gebouw. De skyline brandt zachtjes in de weerspiegeling van honderden monitors die op blauw scherm zijn gesprongen. Hij kijkt naar beneden — niet met spijt, maar met bevestiging. Hij heeft bewezen wat hij moest bewijzen: dat zelfs zonder diploma, zijn code impact maakt.
                
                Benito Mussolinux is al verdwenen, opgelost in de netwerkkabels van het systeem waar hij ooit werkte. Een echo van zijn stem blijft hangen in de gangen van de serverruimte:
                
                “Code is macht. Macht is structuur. Structuur is controle.”
                
                En jij?
                
                Jij sluit de terminal. Eén laatste enter.
                sudo shutdown -h now
                
                Het scherm wordt zwart.
                
                Missie voltooid. Maar tegen welke prijs?""";
        System.out.println(text);
        System.out.println("\nDruk op Enter om af te sluiten...");
        scanner.nextLine();
    }

    public static void showWin(Scanner scanner) {
        Epilogue epilogue = new Epilogue();
        epilogue.show(scanner);
    }
} 