import java.io.Serializable;
import java.util.ArrayList;

public class Character implements Serializable {
    private final String name;
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

class NPC extends Character {
    private String introduction;
    private String dialogue;
    public NPC(String name, Room startingRoom, ArrayList<Item> inventory, String introduction, String dialogue) {
        super(name, startingRoom, inventory);
        this.introduction = introduction;
        this.dialogue = dialogue;
    }
    public NPC(String name, Room startingRoom, String introduction, String dialogue) {
        super(name, startingRoom, null);
        this.introduction = introduction;
        this.dialogue = dialogue;
    }

    @Override
    public void addItemToInventory(Item item) {
        getInventory().add(item);
    }

    public String  getIntroduction() {
        return introduction;
    }
    public String getDialogue() {
        return dialogue;
    }

    public void giveToPlayer(Character player, NPC npc, Item item){
        npc.dropFromInventory(item);
        player.addItemToInventory(item);
    }

    public void receiveFromPlayer(Character player, NPC npc, Item item){
        npc.addItemToInventory(item);
        player.dropFromInventory(item);
    }

}