import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Room {
    private String description;
    private Map<String, Room> exits; // Map direction to neighboring Room
    private List<Item> items; // List of items in the room

    public Room(String description) {
        this.description = description;
        exits = new HashMap<>();
        this.items = new ArrayList<>();
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
        return "You are " + description + ".\nExits: " + getExitString();
    }

    public ArrayList<Item> getItems() {
        return (ArrayList<Item>) items;
    }

    public void addItemToRoom(Item item) {
        item.getDescription();
        items.add(item);
    }

    public void removeItemFromRoom(Item item) {
        items.remove(item);
    }
}
class Alarm {
    public void ring(Character player, Room cell){
        System.out.println("Oh no! You have set off the alarm!");
        player.setCurrentRoom(cell);
        //Set Enum Game Lose that restarts it all
    }
}