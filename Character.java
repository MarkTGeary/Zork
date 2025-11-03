import java.util.ArrayList;

public class Character {
    private String name;
    public Room currentRoom;
    private ArrayList<Item> inventory;

    public Character(String name, Room startingRoom, ArrayList<Item> inventory) {
        this.name = name;
        this.currentRoom = startingRoom;
        this.inventory = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public ArrayList<Item> getInventory() {
        return inventory;
    }


    public void move(String direction) {
        Room nextRoom = currentRoom.getExit(direction);
        if (nextRoom != null) {
            currentRoom = nextRoom;
            System.out.println("You moved to: " + currentRoom.getDescription());
        } else {
            System.out.println("You can't go that way!");
        }
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
}
