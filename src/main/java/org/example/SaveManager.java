package org.example;

import java.io.*;

public class SaveManager {
    private static final String SAVE_FILE = "gameSave.dat";

    public static Player load() {
        File f = new File(SAVE_FILE);
        if (!f.exists()) return null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))) {
            return (Player) ois.readObject();
        } catch (Exception e) {
            System.err.println("⚠️ Fout bij laden: " + e.getMessage());
            return null;
        }
    }

    public static void save(Player player) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(player);
        } catch (IOException e) {
            System.err.println("⚠️ Fout bij opslaan: " + e.getMessage());
        }
    }

    public static void reset() {
        File f = new File(SAVE_FILE);
        if (f.exists() && !f.delete()) {
            System.err.println("⚠️ Kon save niet verwijderen.");
        }
    }
}
