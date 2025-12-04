package game;

import java.io.Serializable;
import java.util.ArrayList;

//Npc class One constructor with an inventory and one without
public class NPC extends GameCharacter implements Serializable {
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

    public String getIntroduction() {
        return introduction;
    }

    public String getDialogue() {
        return dialogue;
    }

    public Item getItemToGive() {
        return itemToGive;
    }

    public Item getWantedItem() {
        return wantedItem;
    }

    public void setWantedItem(Item wantedItem) {
        this.wantedItem = wantedItem;
    }

    public void giveItemToUser(NPC npc, GameCharacter player, Item itemToGive) {
        if (itemToGive != null) {
            for (Item item : npc.getInventory()) {
                if (item.getName().equalsIgnoreCase(itemToGive.getName())) {
                    npc.dropFromInventory(itemToGive);
                    player.addItemToInventory(itemToGive);
                    System.out.println("Here's " + itemToGive.getName() + " to help you.");
                }
            }
        }
    }

    public void giveToPlayer(GameCharacter player, Item item) {
        this.dropFromInventory(item);
        player.addItemToInventory(item);
    }

    public void receiveFromPlayer(GameCharacter player, Item item) {
        this.addItemToInventory(item);
        player.dropFromInventory(item);
    }
}


