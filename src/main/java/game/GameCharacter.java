package game;

import java.io.Serializable;
import java.util.ArrayList;

public class GameCharacter implements Serializable {
    private final String name;
    public Room currentRoom;
    private ArrayList<Item> inventory;
    private boolean inVehicle;

    public GameCharacter(String name, Room startingRoom, ArrayList<Item> inventory, boolean inVehicle) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
        this.inVehicle = inVehicle;
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public LockedRoom getCurrentLockedRoom() {
        if (currentRoom instanceof LockedRoom) {
            return (LockedRoom) currentRoom;
        }
        return null;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }

    public boolean getInVehicle() {
        return inVehicle;
    }

    public void setInVehicle(boolean inVehicle) {
        this.inVehicle = inVehicle;
    }


    
    public void addItemToInventory(Item item) {
        inventory.add(item);
        System.out.println(item.getName() + " added to inventory.");
    }

    public void dropFromInventory(Item item) {
        inventory.remove(item);
    }

    public boolean hasItem(String itemName) {
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return true;
            }
        }
        return false;
    }
    public void showCode() {
        boolean found = false;
        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase("accessCode")) {
                System.out.println("Code is: " + item.getCode());
                found = true;
                break;
            }
        }
        if (!found) {
            System.out.println("You don't have any access code.");
        }
    }

}
