import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room implements Serializable {
    private String name;
    private String description;
    private Map<String, Room> exits; // Map direction to neighboring Room
    private List<Item> items; // List of items in the room
    private List<NPC> npcs;

    public Room(String name, String description) {
        this.name = name;
        this.description = description;
        exits = new HashMap<>();
        this.items = new ArrayList<>();
        this.npcs = new ArrayList<>();
    }

    public String getName(){
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setExit(String direction, Room neighbor) {
        exits.put(direction, neighbor);
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }

    public String getExitString() {
        StringBuilder sb = new StringBuilder();
        for (String direction : exits.keySet()) {
            sb.append(direction).append(" ");
        }
        return sb.toString().trim();
    }

    public String getLongDescription() {
        return "\nYou are " + description + ".\nExits: " + getExitString();
    }

    public ArrayList<Item> getItems() {
        return (ArrayList<Item>) items;
    }

    public void addItemToRoom(Item item) {
        items.add(item);
    }


    public void removeItemFromRoom(Item item) {
        items.remove(item);
    }

    public void addNPC(NPC npc){
        npcs.add(npc);
    }

    public List<NPC> getNPCs() {
        return npcs;
    }

}
class Alarm implements Serializable {
    public static void ring(Character player, Room cell){
        System.out.println("Oh no! You have set off the alarm!");
        player.setCurrentRoom(cell);
        //Set Enum Game Lose that restarts it all
    }
}
class LockedRoom extends Room implements Serializable {
    Alarm alarm;
    protected String lockedDirection;
    protected Room lockedDestination;
    private String accessCode;

    public LockedRoom(String name, String description, String lockedDirection, Room lockedDestination, Alarm alarm) {
        super(name, description);
        this.alarm = alarm;
        this.lockedDirection = lockedDirection;
        this.lockedDestination = lockedDestination;

    }

    public String getAccessCode(){
        return accessCode;
    }
    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }


    public void enterCode(String code, Character player, Room cell) {
        if (code.equalsIgnoreCase(accessCode)) {
            System.out.println("You have entered the correct code!");
            setExit(lockedDirection, lockedDestination);
            System.out.println("You have unlocked a new exit!");
            System.out.println("Exits: " + getExitString());
        }
        else {
            Alarm.ring(player, cell);
        }
    }
}

class KeyLockedRoom extends LockedRoom implements Serializable {
    KeyLockedRoom(String name, String description, String lockedDirection, Room lockedDestination, Alarm alarm) {
        super(name, description, lockedDirection, lockedDestination, alarm);
    }

    public void unlockRoom(Character player, Item key){
        boolean hasKey = false;
        for(Item item: player.getInventory()){
            if(item.getName().equalsIgnoreCase(key.getName())){
                hasKey = true;
                break;
            }
        }
        if(hasKey){
            setExit(lockedDirection, lockedDestination);
            System.out.println("You have unlocked a new exit!");
            System.out.println("Exits: " + getExitString());
        } else {
            System.out.println("You don't have the key for the door.");
        }
    }
}