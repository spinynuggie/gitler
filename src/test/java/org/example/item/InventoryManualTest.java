package org.example.item;

import java.util.Optional;

public class InventoryManualTest {
    private Inventory inventory;

    static class ItemProviderStub {
        public Item getItem() {
            return new Joker();
        }
    }

    static class InventoryLoggerMock {
        private boolean logCalled = false;

        public void log(String message) {
            logCalled = true;
        }

        public boolean isLogCalled() {
            return logCalled;
        }
    }

    public void setUp() {
        inventory = new Inventory();
    }

    public void testAddItemWithStub() {
        setUp();
        ItemProviderStub itemProvider = new ItemProviderStub();
        Item item = itemProvider.getItem();
        inventory.addItem(item);
        if (inventory.hasItem(Joker.class)) {
            System.out.println("testAddItemWithStub: GESLAAGD");
        } else {
            System.out.println("testAddItemWithStub: GEFAALD");
        }
    }

    public void testRemoveItemWithMock() {
        setUp();
        InventoryLoggerMock logger = new InventoryLoggerMock();
        Joker joker = new Joker();
        inventory.addItem(joker);
        inventory.removeItem(joker);
        logger.log("Item removed");
        boolean removed = !inventory.hasItem(Joker.class);
        boolean logCalled = logger.isLogCalled();
        if (removed && logCalled) {
            System.out.println("testRemoveItemWithMock: GESLAAGD");
        } else {
            System.out.println("testRemoveItemWithMock: GEFAALD");
        }
    }

    public void testGetItem() {
        setUp();
        Joker joker = new Joker();
        inventory.addItem(joker);
        Optional<Joker> retrievedJoker = inventory.getItem(Joker.class);
        if (retrievedJoker.isPresent() && joker.equals(retrievedJoker.get())) {
            System.out.println("testGetItem: GESLAAGD");
        } else {
            System.out.println("testGetItem: GEFAALD");
        }
    }

    public void testGetItemNotPresent() {
        setUp();
        Optional<Sword> sword = inventory.getItem(Sword.class);
        if (sword.isEmpty()) {
            System.out.println("testGetItemNotPresent: GESLAAGD");
        } else {
            System.out.println("testGetItemNotPresent: GEFAALD");
        }
    }

    public static void main(String[] args) {
        InventoryManualTest test = new InventoryManualTest();
        test.testAddItemWithStub();
        test.testRemoveItemWithMock();
        test.testGetItem();
        test.testGetItemNotPresent();
    }
} 