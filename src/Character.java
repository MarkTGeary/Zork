import java.io.Serializable;
import java.util.ArrayList;

public class Character implements Serializable {
    private final String name;
    public Room currentRoom;
    private ArrayList<Item> inventory;
    private boolean inVehicle;

    public Character(String name, Room startingRoom, ArrayList<Item> inventory, boolean inVehicle) {
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


class NPC extends Character implements Serializable {
    private String introduction;
    private String dialogue;
    private Item itemToGive;
    private Item wantedItem;
    public NPC(String name, Room startingRoom, ArrayList<Item> inventory, String introduction, String dialogue, Item itemToGive, Item wantedItem) {
        super(name, startingRoom, inventory, false);
        this.introduction = introduction;
        this.dialogue = dialogue;
        this.itemToGive = itemToGive;
        this.wantedItem = wantedItem;
    }

    public NPC(String name, Room startingRoom, String introduction, String dialogue) {
        super(name, startingRoom, null, false);
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

    public Item getItemToGive() {
        return itemToGive;
    }

    public Item getWantedItem(){
        return wantedItem;
    }

    public void setWantedItem(Item wantedItem) {
        this.wantedItem = wantedItem;
    }

    public void giveItemToUser(NPC npc, Character player, Item itemToGive){
        if (itemToGive != null) {
            for (Item item : npc.getInventory()) {
                if (item.getName().equalsIgnoreCase(itemToGive.getName())) {
                    npc.dropFromInventory(itemToGive);
                    player.addItemToInventory(itemToGive);
                    System.out.println("Here's "+ itemToGive.getName() +" to help you.");
                }
            }
        }
    }

    public void giveToPlayer(Character player, Item item){
        this.dropFromInventory(item);
        player.addItemToInventory(item);
    }

    public void receiveFromPlayer(Character player, Item item){
        this.addItemToInventory(item);
        player.dropFromInventory(item);
    }

}