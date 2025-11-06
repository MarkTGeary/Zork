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
import java.util.ArrayList;

public class ZorkULGame {
    private Parser parser;
    private Character player;

    public ZorkULGame() {
        createRooms();
        parser = new Parser();
    }

    private void createRooms() {
        Room cell, yard, guardStation, corridor1, corridor2, infirmary, securityRoom;
        Room storageRoom, wardenOffice, kitchen, cafeteria, prisonExit, pub;

        // create rooms
        cell = new Room("inside your prison cell");
        corridor1 = new Room("inside a corridor");
        yard = new Room("outside in the yard");
        guardStation = new Room("inside the guard station");
        corridor2 = new Room("inside a corridor");
        kitchen = new Room("inside the kitchen");
        cafeteria = new Room("inside the cafeteria");
        prisonExit = new Room("at the prison exit");
        pub = new Room("inside the pub");
        wardenOffice = new Room("inside the warden office");
        securityRoom = new Room("inside the security room");
        storageRoom = new Room("inside the storage room");
        infirmary = new Room("inside the infirmary");


        cell.setExit("east", corridor1);

        corridor1.setExit("east", cafeteria);
        corridor1.setExit("west", cell);
        corridor1.setExit("south", guardStation);
        corridor1.setExit("north", yard);

        yard.setExit("south", corridor1);
        yard.setExit("east", storageRoom);

        storageRoom.setExit("west", yard);
        Item shovel = new Item("shovel", "shovel");
        storageRoom.addItemToRoom(shovel);

        cafeteria.setExit("west", corridor1);
        cafeteria.setExit("east", kitchen);

        kitchen.setExit("west", cafeteria);

        guardStation.setExit("north", corridor1);
        guardStation.setExit("south", corridor2);
        guardStation.setExit("east", infirmary);

        infirmary.setExit("west", guardStation);

        corridor2.setExit("north", guardStation);
        corridor2.setExit("south", prisonExit);
        corridor2.setExit("west", securityRoom);

        securityRoom.setExit("east", corridor2);
        securityRoom.setExit("south", wardenOffice);

        wardenOffice.setExit("north", securityRoom);


        // create the player character and start in their cell
        player = new Character("player", cell, new ArrayList<>());
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

    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to the Prison Escape!");
        System.out.println("Explore the prison to find items to help you escape. \nDon't get caught by the guards!");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(player.getCurrentRoom().getLongDescription());
    }

    private boolean processCommand(Command command) {
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
                if (!command.hasSecondWord()) {
                    System.out.println("Trade what?");
                } else {
                    String itemName = command.getSecondWord();
                    Item itemToTrade = null;
                    for (Item item : player.getInventory()) {
                        if (item.getName().equalsIgnoreCase(itemName)) {
                            itemToTrade = item;
                            //NPC.tradeWithPlayer();
                        }

                    }
                }
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You are stuck in this prison.");
        //System.out.print("Your command words are: ");
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
        }
    }

    public static void main(String[] args) {
        ZorkULGame game = new ZorkULGame();
        game.play();
    }
}
