import java.util.ArrayList;
import java.util.List;

public class Character {
    private String name;
    private Room currentRoom;
    private ArrayList<Item> inventory;

    public Character(String name, Room startingRoom, List inventory) {
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

    public List getInventory() {
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
}
