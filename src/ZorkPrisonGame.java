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
    private Room cell;
    private Room pub;
    Item key;
    public Character player;
    private StateMethods status;
    Vehicle car;


    public ZorkPrisonGame() {
        createRooms();
        parser = new Parser();
        status = new StateMethods(player, cell);
        status.setGameState(GameState.PLAYING);
    }

    private void createRooms() {
        Room yard, corridor1, infirmary, storageRoomCloset;
        Room storageRoom, kitchen, wardenOffice, cafeteria, prisonExit, BobCell;
        LockedRoom corridor2;
        KeyLockedRoom securityRoom;
        Alarm alarm = new Alarm();

        // create rooms
        cell = new Room("cell","inside your prison cell");
        corridor1 = new Room("corridor1","inside a corridor");
        yard = new Room("yard","outside in the yard");
        Item guardUniform = new Item("GuardUniform", "guard uniform", true);
        AlarmedRoom guardStation = new AlarmedRoom("guardStation","inside the guard station", alarm, guardUniform);
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
        storageRoomCloset = new Room("storageRoomCloset", "in the closet of the storage room");

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

        storageRoomCloset.setExit("west", storageRoom);

        storageRoom.setExit("west", yard);
        storageRoom.setExit("east", storageRoomCloset);
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

        car = new Vehicle("car", "car", "audio\\CarNoise.wav");
        prisonExit.addItemToRoom(car);

        Item beer = new Item("beer", "beer", true);
        pub.addItemToRoom(beer);

        player = new Character("player", cell, new ArrayList<>(), false);

        NPC prisoner = new NPC("Bob", BobCell, new ArrayList<>(),
                "You see another prisoner named Bob. He looks like he has lots to say.",
                "Bob Says: \n'You'll need a guard's uniform if you're trying to escape.\nI can get you one if you have something in return for me. \n" +
                        "Rumour has it there's something valuable hidden in the yard.'");
        NPC chef = new NPC("John", kitchen, new ArrayList<>(), "You meet John, the chef. He's been known to be helpful to the prisoners.",
                "There was a rumour that there is gold hidden underneath the yard. You'll need to inject the guard on duty with this to avoid being caught.");
        Item poison =  new Item("poison", "poison", true);
        kitchen.addNPC(chef);
        chef.addItemToInventory(poison);
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
        CommandTypes methods = new CommandTypes(player, status);
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
                methods.quitCommand(command);
            case "drink":
                methods.drinkCommand(command);
                break;
            case "inventory":
                methods.inventoryCommand(command);
                break;
            case "take":
                methods.takeCommand(command);
                break;
            case "drop":
                methods.dropCommand(command);
                break;
            case "trade":
                methods.tradeCommand(command);
                break;
            case "talk":
                methods.talkCommand(command);
                break;
            case "drive":
                methods.driveCommand(command, pub, car);
                break;
            case "save":
                Save("Saved");
                break;
            case "load":
                Load("Saved");
                break;
            case "enter":
                methods.enterCommand(command, key, cell);
                break;
            case "show":
                if (command.getSecondWord().equals("code")) {
                    player.showCode();
                }
                break;
            case "dig":
                digCommand(command);
            /*case "restart":
                ZorkPrisonGame newGame = new ZorkPrisonGame();
                newGame.play();*/
            default:
                System.out.println("I don't know what you mean...");
                break;
        }
        return false;
    }

    private void printHelp() {
        System.out.println("You are lost. You are alone. You are stuck in this prison.");
        System.out.println("Your command words are: ");
        parser.showCommands();
    }

    private void digCommand(Command command) {
        if(!command.hasSecondWord()) {
            Room currentRoom = player.getCurrentRoom();
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase("shovel")) {
                    for (Item item2 : currentRoom.getItems()) {
                        if (!item2.isVisible()) {
                            player.addItemToInventory(item2);
                        }
                    }
                } else {
                    System.out.println("You don't have a shovel to dig with!");
                }
            }
        } else {
            System.out.println("Just use the word 'dig'.");
        }
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
                boolean itemIntro = false;
                boolean visible = true;
                for (Item item : nextRoom.getItems()) {
                    if(item.isVisible()) {
                        if(!itemIntro) {
                            System.out.println("You see the following items:");
                            itemIntro = true;
                        }
                        System.out.println(item.getDescription());
                    } else {
                        visible = false;
                    }
                }
                if(!visible) {
                    System.out.println("Something seems off about this place. Perhaps more investigation is needed.");
                }
            }
            if(!nextRoom.getNPCs().isEmpty()){
                for(NPC npc : nextRoom.getNPCs()){
                    System.out.println(npc.getIntroduction());
                }
            }
            nextRoom.onEnter(player, cell, status);
        }
    }

    public void Save(String filename){
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))){
            out.writeObject(player);
            out.close();
            System.out.println("Player Saved");
        } catch (IOException e) {
            System.out.println("IOException is caught.");
            System.out.println("Cannot currently save game");
        }
    }

    public void Load(String filename) {
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))){
            player = (Character) in.readObject();
        } catch(IOException e){
            System.out.println("IOException is caught");
            System.out.println("Cannot currently load game");
        } catch(ClassNotFoundException e){
            System.out.println("ClassNotFoundException is caught");
            System.out.println("Cannot currently load game");
        }
    }

    public static void main(String[] args) {
        ZorkPrisonGame game = new ZorkPrisonGame();
        game.play();
    }
}
