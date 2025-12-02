

public class CommandTypes {
    public Character player;
    public StateMethods status;
    public HintMethods hintStatus;
    public CommandTypes(Character player, StateMethods status, HintMethods hintStatus) {
        this.player = player;
        this.status = status;
        this.hintStatus = hintStatus;
    }
    public void enterCommand(Command command, Item key, Room cell) {

        if (!command.hasSecondWord()) {
            System.out.println("Enter what?");
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("car") && !command.hasThirdWord()) {
            player.setInVehicle(true);
            System.out.println("You are now in the car.");
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("code") && !command.hasThirdWord()) {
            System.out.println("Use syntax: enter code <code>");
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("key") && command.hasThirdWord()) {
            System.out.println("Use syntax: enter key");
            return;
        }

        Room currentRoom = player.getCurrentRoom();

        if (!(currentRoom instanceof LockedRoom<?>)
                && (command.getSecondWord().equalsIgnoreCase("code")
                || command.getSecondWord().equalsIgnoreCase("key"))) {
            System.out.println("There is nothing locked here.");
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("key")) {
            LockedRoom<?> room = (LockedRoom<?>) currentRoom;
            room.unlockKey(player, key, hintStatus);
            return;
        }

        if (command.getSecondWord().equalsIgnoreCase("code")) {
            LockedRoom<?> room = (LockedRoom<?>) currentRoom;
            String attemptCode = command.getThirdWord();
            room.enterCode(attemptCode, player, cell, status);
            return;
        }

        System.out.println("Invalid syntax.");
    }


    public void driveCommand (Command command, Room pub, Vehicle vehicle){
        if(!command.hasSecondWord()) {
            if(player.getInVehicle()) {
                vehicle.carNoise(vehicle.getNoise());
                player.setCurrentRoom(pub);
                BackgroundSoundStuff.playMusic("audio/Celebration.wav");
                System.out.println("You are " + player.getCurrentRoom().getDescription());
                if (!player.getCurrentRoom().getItems().isEmpty()) {
                    System.out.println("You see the following items:");
                    for (Item item : player.getCurrentRoom().getItems()) {
                        System.out.println(item.getDescription());
                    }
                }
            } else {
                System.out.println("You have to enter a vehicle to drive it!");
            }
        } else {
            System.out.println("Just type 'drive'");
        }
    }

    public void talkCommand (Command command){
        if (!command.hasSecondWord()) {
            System.out.println("Talk to who?");
        }
        else if (!command.hasThirdWord() || !command.getSecondWord().equalsIgnoreCase("to") || command.hasFourthWord()) {
            System.out.println("Invalid syntax. Use: 'talk to <NPC name>'");
        }
        else {
            Room currentRoom = player.getCurrentRoom();
            String NpcName = command.getThirdWord();
            boolean found = false;
            for(NPC Npc: currentRoom.getNPCs()){
                if (Npc.getName().equalsIgnoreCase(NpcName)){
                    found = true;
                    System.out.println(Npc.getName() + " says: ");
                    System.out.println(Npc.getDialogue());
                    Item item = Npc.getItemToGive();
                    if(item != null) {
                        Npc.giveItemToUser(Npc, player, item);
                    }
                }
            }
            if(!found){
                System.out.println("There is nobody to talk with here!");
            }
        }
    }

    public void dropCommand(Command command){
        String itemName;
        if (!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return;
        } else if(command.hasThirdWord()){
            StringBuilder fixMultiWord = new StringBuilder();
            fixMultiWord.append(command.getSecondWord());
            fixMultiWord.append(' ');
            fixMultiWord.append(command.getThirdWord());
            itemName = fixMultiWord.toString();
        } else {
            itemName = command.getSecondWord();
        }
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

    public void takeCommand(Command command) {
        String itemName;
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
            return;
        } else if (command.hasThirdWord()) {
            StringBuilder fixMultiWord = new StringBuilder();
            fixMultiWord.append(command.getSecondWord());
            fixMultiWord.append(' ');
            fixMultiWord.append(command.getThirdWord());
            itemName = fixMultiWord.toString();
        } else {
            itemName = command.getSecondWord();
        }
        Room currentRoom = player.getCurrentRoom();
        Item itemToTake = null;
        for (Item item : currentRoom.getItems()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                if(item.isVisible()) {
                    itemToTake = item;
                    break;
                } else {
                    itemName = "items to be seen";
                }
            }
        }
        if (itemToTake != null) {
            player.addItemToInventory(itemToTake);
            currentRoom.removeItemFromRoom(itemToTake);
        } else {
            System.out.println("There is no " + itemName + " here.");
        }
    }

    public void inventoryCommand(Command command){
        if(!command.hasSecondWord()) {
            System.out.print("You are carrying:");
            if (player.getInventory().isEmpty()) {
                System.out.println(" nothing.");
            } else {
                for (Item item : player.getInventory()) {
                    System.out.println("- " + item.getName());
                }
            }
        } else {
            System.out.println("Just say 'inventory' to check inventory");
        }
    }

    public void drinkCommand(Command command){
        if(!command.hasThirdWord()) {
            if (player.hasItem("beer")) {
                System.out.println("You have reached heaven.");
                status.setGameState(GameState.WON);
            } else {
                System.out.println("You don't have a beer to drink.");
            }
        } else {
            System.out.println("Use the syntax 'drink'");
        }
    }

    public boolean quitCommand(Command command){
        if (command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;
        }
    }

    public void tradeCommand(Command command){
        String npcItemName;
        String itemName;
        Item playerItem = null;
        NPC npc = null;

        if (!command.hasSecondWord()) {
            System.out.println("Trade what?");
            return;
        }
        if (!command.hasThirdWord() || !(command.getThirdWord().equalsIgnoreCase("for") || (command.hasFourthWord() && command.getFourthWord().equalsIgnoreCase("for")))) {
            System.out.println("Invalid syntax. Use: 'trade <your item> for <their item>'.");
            return;
        }
        if (!command.hasFourthWord()) {
            System.out.println("Trade " + command.getSecondWord() + " for what?");
            return;
        }

        if(command.hasFifthWord() && command.getThirdWord().equalsIgnoreCase("for")) {
            StringBuilder fixMultiWord = new StringBuilder();
            fixMultiWord.append(command.getFourthWord());
            fixMultiWord.append(' ');
            fixMultiWord.append(command.getFifthWord());
            npcItemName = fixMultiWord.toString();
            itemName = command.getSecondWord();
        } else if(command.hasFifthWord() && command.getFourthWord().equalsIgnoreCase("for")) {
            StringBuilder fixMultiWord = new StringBuilder();
            fixMultiWord.append(command.getSecondWord());
            fixMultiWord.append(' ');
            fixMultiWord.append(command.getThirdWord());
            itemName = fixMultiWord.toString();
            npcItemName = command.getFifthWord();
        } else {
            npcItemName = command.getFourthWord();
            itemName = command.getSecondWord();
        }

        for (Item item : player.getInventory()) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                playerItem = item;
                break;
            }
        }
        if (playerItem == null) {
            System.out.println("You don't have a " + itemName + " to trade.");
            return;
        }

        for (NPC character : player.getCurrentRoom().getNPCs()) {
            npc = character;
            break;
        }
        if (npc == null) {
            System.out.println("There's no one here to trade with.");
            return;
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
            return;
        }

        if (npc.getWantedItem() != null && playerItem.getName().equalsIgnoreCase(npc.getWantedItem().getName())) {

            npc.giveToPlayer(player, npcItem);
            npc.receiveFromPlayer(player, playerItem);
            npc.setWantedItem(npcItem);
            System.out.println("You traded your " + itemName + " for " + npc.getName() + "'s " + npcItem.getDescription() + ".");
            hintStatus.setHintLevels(HintLevels.LEVEL4);
        } else {
            System.out.println(npc.getName() + " wants " + npc.getWantedItem().getName() + ", not " + itemName);
        }
    }


    public void injectCommand(Command command, Item poison){
        if (command.hasThirdWord() || !command.hasSecondWord()) {
            System.out.println("Use syntax 'inject <name>'");
        } else {
            Room currentRoom = player.getCurrentRoom();
            String npcName = command.getSecondWord();
            boolean poisonFound = false;
            boolean found = false;
            for (Item item : player.getInventory()) {
                if(item.getName().equalsIgnoreCase("poison")) {
                    poisonFound = true;
                    NPC roomNpc = currentRoom.getNPCs().getFirst();
                    if(roomNpc.getName().equalsIgnoreCase(npcName)) {
                        found = true;
                        currentRoom.removeNPC(roomNpc);
                        player.dropFromInventory(poison);
                        System.out.println("You successfully injected " + npcName + " with the poison.");
                        hintStatus.setHintLevels(HintLevels.LEVEL2);
                    }
                }
            }
            if(!poisonFound) {
                System.out.println("You don't have anything to poison with!");
            } else if (!found) {
                System.out.println("There is nobody for you to poison here!");
            }
        }
    }

    public void digCommand(Command command, Room cell) {
        if (!command.hasSecondWord()) {
            Room currentRoom = player.getCurrentRoom();
            for (NPC npc : currentRoom.getNPCs()) {
                if (npc.getName().equalsIgnoreCase("guard")) {
                    System.out.println("You just dug in front of a guard!");
                    Alarm.ring(player, cell, status);
                }
            }
            for (Item item : player.getInventory()) {
                if (item.getName().equalsIgnoreCase("shovel")) {
                    for (Item item2 : currentRoom.getItems()) {
                        if (!item2.isVisible()) {
                            player.addItemToInventory(item2);
                            item2.setVisible(true);
                            hintStatus.setHintLevels(HintLevels.LEVEL3);
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

    public void hintCommand(Command command, HintMethods hintStatus){
        if(command.hasSecondWord()) {
            System.out.println("Just type 'hint'");
        } else {
            HintMachine hint = hintStatus.getCurrentHintInterface();
            hint.displayHint();
        }
    }
}