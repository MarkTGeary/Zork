import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Room implements Serializable {
    private String name;
    private String description;
    private Map<String, Room> exits; // Map direction to neighboring Room
    private ArrayList<Item> items; // List of items in the room
    private ArrayList<NPC> npcs;

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

    public void removeNPC(NPC npc){
        npcs.remove(npc);
    }

    public ArrayList<NPC> getNPCs() {
        return npcs;
    }

    public void onEnter(Character player, Room room, StateMethods status) {
    }


}
class Alarm implements Serializable {
    public static void ring(Character player, Room cell, StateMethods state) {
        System.out.println("Oh no! You have set off the alarm!");
        state.setGameState(GameState.LOST);
        Thread temp = new Thread(() -> {
            SoundStuff.playSound("audio\\OofNoise.wav");
        });
        temp.start();
        player.setCurrentRoom(cell);

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


    public void enterCode(String code, Character player, Room cell, StateMethods state) {
        if (code.equalsIgnoreCase(accessCode)) {
            System.out.println("You have entered the correct code!");
            setExit(lockedDirection, lockedDestination);
            System.out.println("You have unlocked a new exit!");
            System.out.println("Exits: " + getExitString());
        }
        else {
            Alarm.ring(player, cell, state);
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

class AlarmedRoom extends Room implements Serializable {
    Alarm alarm;
    Item item;
    AlarmedRoom(String name, String description, Alarm alarm, Item item) {
        super(name, description);
        this.alarm = alarm;
        this.item = item;
    }
    @Override
    public void onEnter(Character player, Room room, StateMethods status) {
            boolean hasUniform = false;
            for (Item item2 : player.getInventory()) {
                if (item2.getName().equalsIgnoreCase(item.getName())) {
                    hasUniform = true;
                    break;
                }
            }
            if (!hasUniform) {
                System.out.println("You just entered the guard station without a uniform!");
                Alarm.ring(player, room, status);
        }
    }
}