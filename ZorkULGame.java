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
        Room outside, theatre, pub, lab, office;

        // create rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        Room library = new Room("in the university library");

        // initialise room exits
        outside.setExit("east", theatre);
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north", library);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);

        library.setExit("south", outside);

        Item beer = new Item("beer", "An amazing, creamy pint of beer is within your sights.");
        pub.addItemToRoom(beer);
        


        // create the player character and start outside
        player = new Character("player", outside, new ArrayList<>());
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
        System.out.println("Welcome to the University adventure!");
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
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You wander around the university.");
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
        }
    }

    public static void main(String[] args) {
        ZorkULGame game = new ZorkULGame();
        game.play();
    }
}
