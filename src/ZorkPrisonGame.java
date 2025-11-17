/* This game is a classic text-based adventure set in a university environment.
   The player starts outside the main entrance and can navigate through different rooms like a 
   lecture theatre, campus pub, computing lab, and admin office using simple text commands (e.g., "go east", "go west").
    The game provides descriptions of each location and lists possible exits.

Key features include:
Room navigation: Moving among interconnected rooms with named exits.
Simple command parser: Recognizes a limited set of commands like "go", "help", and "quit".
Player character: Tracks current location and handles moving between rooms.
Text descriptions: Provides immersive text output describing the player's surroundings and available options.
Help system: Lists valid commands to guide the player.
Overall, it recreates the classic Zork interactive fiction experience with a university-themed setting, 
emphasizing exploration and simple command-driven gameplay
*/
import java.io.*;
import java.util.ArrayList;

public class ZorkPrisonGame {
    private Parser parser;
    private Character player;
    private Room pub;
    private Room cell;
    Item key;

    public ZorkPrisonGame() {
        createRooms();
        parser = new Parser();
    }

    private void createRooms() {
        Room yard, guardStation, corridor1, infirmary;
        Room storageRoom, kitchen, wardenOffice, cafeteria, prisonExit, BobCell;
        LockedRoom corridor2;
        KeyLockedRoom securityRoom;
        Alarm alarm = new Alarm();

        // create rooms
        cell = new Room("cell","inside your prison cell");
        corridor1 = new Room("corridor1","inside a corridor");
        yard = new Room("yard","outside in the yard");
        guardStation = new Room("guardStation","inside the guard station");
        prisonExit = new Room("prisonExit","at the prison exit");
        corridor2 = new LockedRoom("corridor2","inside a corridor", "south", prisonExit, alarm);
        kitchen = new Room("kitchen","inside the kitchen");
        cafeteria = new Room("cafeteria","inside the cafeteria");
        pub = new Room("pub","inside the pub");
        wardenOffice = new Room("wardenOffice","inside the warden office");
        securityRoom = new KeyLockedRoom("securityRoom","inside the security room", "south", wardenOffice, alarm);
        storageRoom = new Room("storageRoom","inside the storage room");
        infirmary = new Room("infirmary","inside the infirmary");
        BobCell = new Room("bobCell","inside Bob's Cell");

        // set exits
        cell.setExit("east", corridor1);
        cell.setExit("south", BobCell);

        BobCell.setExit("north", cell);

        corridor1.setExit("east", cafeteria);
        corridor1.setExit("west", cell);
        corridor1.setExit("south", guardStation);
        corridor1.setExit("north", yard);

        yard.setExit("south", corridor1);
        yard.setExit("east", storageRoom);
        Item gold = new Item("gold", "gold", false);
        yard.addItemToRoom(gold);

        storageRoom.setExit("west", yard);
        Item shovel = new Item("shovel", "shovel", true);
        storageRoom.addItemToRoom(shovel);

        cafeteria.setExit("west", corridor1);
        cafeteria.setExit("east", kitchen);

        kitchen.setExit("west", cafeteria);

        guardStation.setExit("north", corridor1);
        guardStation.setExit("south", corridor2);
        guardStation.setExit("east", infirmary);

        infirmary.setExit("west", guardStation);
        key = new Item("key", "key", true);
        infirmary.addItemToRoom(key);

        corridor2.setExit("north", guardStation);
        corridor2.setExit("west", securityRoom);
        corridor2.setAccessCode("25469371");

        securityRoom.setExit("east", corridor2);

        wardenOffice.setExit("north", securityRoom);
        Item AccessCode = new Item("accessCode", "Access Code to exit", true, "25469371");
        wardenOffice.addItemToRoom(AccessCode);

        Item car = new Item("car", "car", true);
        prisonExit.addItemToRoom(car);

        Item beer = new Item("beer", "beer", true);
        pub.addItemToRoom(beer);

        // create the player character and start in their cell
        player = new Character("player", cell, new ArrayList<>());
        NPC prisoner = new NPC("Bob", BobCell, new ArrayList<>(),
                "You see another prisoner named Bob. He looks like he has lots to say.",
                "You'll need a guard's uniform if you're trying to escape.\nI can get you one if you have something valuable for me.");
        NPC chef = new NPC("John", kitchen, new ArrayList<>(), "You meet John, the chef. He's been known to be helpful to the prisoners.",
                "There was a rumour that there is gold hidden underneath the yard. You'll need to inject the guard on duty with this to avoid being caught.");
        Item poison =  new Item("poison", "poison", true);
        kitchen.addNPC(chef);
        chef.addItemToInventory(poison);
        Item guardUniform = new Item("GuardUniform", "guard uniform", true);
        BobCell.addNPC(prisoner);
        prisoner.addItemToInventory(guardUniform);
    }

    public Room getCurrentRoom() {
        return player.getCurrentRoom();
    }

    public void play() {
        printWelcome();

        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Goodbye.");
    }

    public String printWelcome() {
        return ("Welcome to the Prison Escape! \nExplore the prison to find items to help you escape. \nDon't get caught by the guards!\n" + player.getCurrentRoom().getLongDescription());
    }

    public boolean processCommand(Command command) {
        String commandWord = command.getCommandWord();

        if (commandWord == null) {
            System.out.println("I don't understand your command...");
            return false;
        }

        switch (commandWord) {
            case "help":
                printHelp();
                break;
            case "go":
                goRoom(command);
                break;
            case "quit":
                if (command.hasSecondWord()) {
                    System.out.println("Quit what?");
                    return false;
                } else {
                    return true; // signal to quit
                }
            case "drink":
                if (player.hasItem("beer")) {
                    System.out.println("You have reached heaven.");
                } else {
                    System.out.println("You don't have a beer to drink.");
                }
                break;
            case "inventory":
                System.out.print("You are carrying:");
                if (player.getInventory().isEmpty()) {
                    System.out.println(" nothing.");
                } 
                else {                    
                    for (Item item : player.getInventory()) {
                        System.out.println("- " + item.getName());
                    }
                }
                break;
            case "take":
                if (!command.hasSecondWord()) {
                    System.out.println("Take what?");
                } else {
                    String itemName = command.getSecondWord();
                    Room currentRoom = player.getCurrentRoom();
                    Item itemToTake = null;
                    for (Item item : currentRoom.getItems()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToTake = item;
                            break;
                        }
                    }
                    if (itemToTake != null) {
                        player.addItemToInventory(itemToTake);
                        currentRoom.removeItemFromRoom(itemToTake);
                    } else {
                        System.out.println("There is no " + itemName + " here.");
                    }
                }
                break;
            case "drop":
                if (!command.hasSecondWord()) {
                    System.out.println("Drop what?");
                } else {
                    String itemName = command.getSecondWord();
                    Item itemToDrop = null;
                    for (Item item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToDrop = item;
                            break;
                        }
                    }
                    if (itemToDrop != null) {
                        player.dropFromInventory(itemToDrop);
                        player.getCurrentRoom().addItemToRoom(itemToDrop);
                        System.out.println("You dropped the " + itemName + ".");
                    } else {
                        System.out.println("You don't have a " + itemName + ".");
                    }
                }
                break;
            case "trade":
                String npcItemName;
                if(command.hasFifthWord()) {
                    StringBuilder fixMultiWord = new StringBuilder();
                    fixMultiWord.append(command.getFourthWord());
                    fixMultiWord.append(command.getFifthWord());
                    npcItemName = fixMultiWord.toString();
                } else {
                    npcItemName = command.getFourthWord();
                }

                if (!command.hasSecondWord()) {
                    System.out.println("Trade what?");
                    break;
                }
                else if (!command.hasThirdWord() || !command.getThirdWord().equalsIgnoreCase("for")) {
                    System.out.println("Invalid syntax. Use: 'trade <your item> for <their item>'.");
                    break;
                }
                else if (!command.hasFourthWord()) {
                    System.out.println("Trade " + command.getSecondWord() + " for what?");
                    break;
                }

                String playerItemName = command.getSecondWord();


                Item playerItem = null;
                for (Item item : player.getInventory()) {
                    if (item.getName().equalsIgnoreCase(playerItemName)) {
                        playerItem = item;
                        break;
                    }
                }
                if (playerItem == null) {
                    System.out.println("You don't have a " + playerItemName + " to trade.");
                    break;
                }
                NPC npc = null;
                for (NPC character : player.getCurrentRoom().getNPCs()) {
                    npc = character;
                    break;
                }
                if (npc == null) {
                    System.out.println("There's no one here to trade with.");
                    break;
                }

                Item npcItem = null;
                for (Item item : npc.getInventory()) {
                    if (item.getName().equalsIgnoreCase(npcItemName)) {
                        npcItem = item;
                        break;
                    }
                }
                if (npcItem == null) {
                    System.out.println(npc.getName() + " doesn't have a " + npcItemName + ".");
                    break;
                }

                npc.giveToPlayer(player, npc, npcItem);     // NPC gives their item to player
                npc.receiveFromPlayer(player, npc, playerItem); // NPC receives player's item
                System.out.println("You traded your " + playerItemName + " for " + npc.getName() + "'s " + npcItem.getDescription() + ".");
                break;
            case "talk":
                if (!command.hasSecondWord()) {
                    System.out.println("Talk to who?");
                }
                else if (!command.hasThirdWord() || !command.getSecondWord().equalsIgnoreCase("to") || command.hasFourthWord()) {
                    System.out.println("Invalid syntax. Use: 'talk to <NPC name>'");
                }
                else {
                    Room currentRoom = player.getCurrentRoom();
                    String NpcName = command.getThirdWord();
                    for(NPC Npc: currentRoom.getNPCs()){
                        if (Npc.getName().equalsIgnoreCase(NpcName)){
                            System.out.println(Npc.getDialogue());
                        }
                    }
                }
                break;
            case "drive":
                if(!command.hasSecondWord()) {
                    player.setCurrentRoom(pub);
                    System.out.println("You are " +player.getCurrentRoom().getDescription());
                    if (!getCurrentRoom().getItems().isEmpty()) {
                        System.out.println("You see the following items:");
                        for (Item item : getCurrentRoom().getItems()) {
                            System.out.println(item.getDescription());
                        }
                    }
                }
                break;
            case "save":
                Save("Saved");
                break;
            case "load":
                Load("Saved");
                break;
            case "enter":
                if(!command.hasSecondWord()) {
                    System.out.println("Enter what?");
                } else if((command.getSecondWord().equalsIgnoreCase("code") && command.hasFourthWord()) || (command.getSecondWord().equalsIgnoreCase("key") && command.hasThirdWord())) {
                    System.out.println("Use syntax 'enter code <code>' or 'enter key'");
                } else if (command.getSecondWord().equalsIgnoreCase("key") && !command.hasThirdWord()) {
                    KeyLockedRoom room = (KeyLockedRoom) player.getCurrentLockedRoom();
                    room.unlockRoom(player, key);
                } else if (command.getSecondWord().equalsIgnoreCase("code")) {
                    LockedRoom currentRoom = player.getCurrentLockedRoom();
                    String attemptCode = command.getThirdWord();
                    if (currentRoom instanceof LockedRoom lockedRoom) {
                        lockedRoom.enterCode(attemptCode,player, cell);
                    }
                } else {
                    System.out.println("Invalid syntax.");
                }
                break;
            case "show":
                if (command.getSecondWord().equals("code")) {
                    player.showCode();
                }
                break;
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You are stuck in this prison.");
        System.out.print("Your command words are: ");
        parser.showCommands();
    }

    private void goRoom(Command command) {
        if (!command.hasSecondWord()) {
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        Room nextRoom = player.getCurrentRoom().getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        } else {
            player.setCurrentRoom(nextRoom);
            System.out.println(player.getCurrentRoom().getLongDescription());
            if (!nextRoom.getItems().isEmpty()) {
                System.out.println("You see the following items:");
                for (Item item : nextRoom.getItems()) {
                    System.out.println(item.getDescription());
                }
            }
            if(!nextRoom.getNPCs().isEmpty()){
                for(NPC npc : nextRoom.getNPCs()){
                    System.out.println(npc.getIntroduction());
                }
            }
        }
    }

    public void Save(String filename){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
            out.writeObject(player);
            out.close();
            System.out.println("Player Saved");
        } catch (IOException e) {
            System.out.println("IOException is caught");
        }
    }

    public void Load(String filename) {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
            player = (Character) in.readObject();
        } catch(IOException e){
            System.out.println("IOException is caught");
        } catch(ClassNotFoundException e){
            System.out.println("ClassNotFoundException is caught");
        }
    }

    public static void main(String[] args) {
        ZorkPrisonGame game = new ZorkPrisonGame();
        game.play();
    }
}
