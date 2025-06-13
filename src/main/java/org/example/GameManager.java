package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.example.item.Key;
import org.example.map.MapConsoleRenderer;

public class GameManager {
    private final Scanner scanner;
    private final Player player;
    private GameMap gameMap;
    private final Map<Integer, Monster> monstersPerRoom = new HashMap<>();
    private final MapConsoleRenderer mapConsoleRenderer = new MapConsoleRenderer();

    public GameManager(Scanner scanner, Player player) {
        this.scanner = scanner;
        this.player = player;
        this.gameMap = player.getMap();
    }

    public void run() {
        if (gameMap == null) {
            initializeNewMap();
        }

        while (true) {
            if (handleExit()) {
                if (player.stage > 3) {
                    endGame();
                    return;
                }
                initializeNewMap();
                continue;
            }

            if (handleInput()) {
                return;
            }
        }
    }

    private void initializeNewMap() {
        int gridSize = player.stage + 2;
        gameMap = new GameMap(gridSize);
        player.setMap(gameMap);
        player.currentRoom = gameMap.startRoomId;
        monstersPerRoom.clear();
        player.completedRooms.clear();
        SaveManager.save(player);
    }

    private boolean handleExit() {
        if (player.currentRoom == gameMap.exitId) {
            int keysToRemove = gameMap.keysRequired;
            player.getInventory().removeItems(Key.class, keysToRemove);
            System.out.printf("%d sleutel(s) gebruikt om de deur te openen.\n", keysToRemove);
            player.stage++;
            return true;
        }
        return false;
    }

    private void endGame() {
        System.out.println("Gefeliciteerd! Je hebt alle stages voltooid. Nu de eindbaas!");
        finalBossBattle(scanner, player);
    }

    private boolean handleInput() {
        mapConsoleRenderer.viewMap(gameMap, player);
        System.out.printf(Messages.HP_SCORE, player.hp, player.score);
        System.out.print(Messages.KEUZE_PROMPT);

        String input = scanner.nextLine().trim().toUpperCase();
        if ("EXIT".equalsIgnoreCase(input)) {
            System.out.println(Messages.TERUG_NAAR_MENU);
            SaveManager.save(player);
            return true; // stop game loop
        }
        if (input.isEmpty()) {
            return false;
        }

        int targetId = calculateTargetId(input.charAt(0));
        if (targetId == -1) {
            System.out.println("Je kunt niet die kant op bewegen, daar is een muur.");
            return false;
        }

        if (targetId == gameMap.exitId) {
            return handleExitAttempt();
        }

        handleRoomEntry(targetId);
        return false;
    }

    private int calculateTargetId(char move) {
        int currentId = player.currentRoom;
        int gridSize = gameMap.getGridSize();
        return switch (move) {
            case 'W' -> currentId > gridSize ? currentId - gridSize : -1;
            case 'A' -> (currentId - 1) % gridSize != 0 ? currentId - 1 : -1;
            case 'S' -> currentId <= (gridSize * gridSize) - gridSize ? currentId + gridSize : -1;
            case 'D' -> currentId % gridSize != 0 ? currentId + 1 : -1;
            default -> -1;
        };
    }

    private boolean handleExitAttempt() {
        long keyCount = player.getInventory().countItems(Key.class);
        if (keyCount >= gameMap.keysRequired) {
            System.out.println("Je hebt de uitgang gevonden! Je gaat naar de volgende stage.");
            SaveManager.save(player);
            player.currentRoom = gameMap.exitId;
        } else {
            System.out.printf("De uitgang is op slot! Je hebt %d sleutel(s) nodig, maar je hebt er %d.\n", gameMap.keysRequired, keyCount);
        }
        return false;
    }

    private void handleRoomEntry(int targetId) {
        player.currentRoom = targetId;
        Room selectedRoom = gameMap.getRoomById(targetId);
        if (selectedRoom == null) {
            return;
        }

        if (player.completedRooms.contains(targetId)) {
            System.out.println(Messages.KAMER_VOLTOOID);
            return;
        }

        Player.JokerResult jokerResult = player.vraagEnVerwerkJoker(scanner, "kamer");
        if (jokerResult == Player.JokerResult.GEBRUIKT) {
            player.completedRooms.add(targetId);
            player.score += 10;
            player.tryAcquireSword(targetId, gameMap.getSwordRoomId());
            if (selectedRoom.hasItem()) {
                player.getInventory().addItem(selectedRoom.takeItem());
                System.out.println("Je hebt een sleutel gevonden!");
            }
            SaveManager.save(player);
            return;
        } else if (jokerResult == Player.JokerResult.GEEN_JOKER_MEER) {
            return;
        }

        Monster monster = monstersPerRoom.computeIfAbsent(targetId, k -> MonsterFactory.createMonsterFor());
        Battle battle = new Battle(scanner, player, monster, selectedRoom, gameMap);
        Battle.BattleResult result = battle.start();
        handleBattleResult(result, targetId, selectedRoom);
    }

    private void handleBattleResult(Battle.BattleResult result, int targetId, Room room) {
        if (result == Battle.BattleResult.WIN) {
            System.out.printf(Messages.KAMER_SCORE, targetId);
            player.completedRooms.add(targetId);
            player.score += 10;
            player.tryAcquireSword(targetId, gameMap.getSwordRoomId());
            if (room.hasItem()) {
                player.getInventory().addItem(room.takeItem());
                System.out.println("Je hebt een sleutel gevonden!");
            }
            SaveManager.save(player);
        }
    }

    private void finalBossBattle(Scanner scanner, Player player) {
        System.out.println("De eindbaas verschijnt!");
        Monster finalBoss = new Monster("Eindbaas", 5, 25, new AttackStrategy());
        Battle battle = new Battle(scanner, player, finalBoss, null, player.getMap());
        battle.startFinalBossBattle();

        if (player.hp > 0) {
            System.out.println("Gefeliciteerd! Je hebt de eindbaas verslagen en het spel uitgespeeld!");
        } else {
            System.out.println("Helaas, de eindbaas was te sterk. Game over.");
        }
    }
} 