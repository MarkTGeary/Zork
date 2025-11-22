public class CommandTypes {
    public Character player;
    public StateMethods status;
    public CommandTypes(Character player, StateMethods status) {
        this.player = player;
        this.status = status;
    }
    public void enterCommand(Command command, Item key, Room cell) {
        if (!command.hasSecondWord()) {
            System.out.println("Enter what?");
        } else if ((command.getSecondWord().equalsIgnoreCase("code") && command.hasFourthWord()) || (command.getSecondWord().equalsIgnoreCase("key") && command.hasThirdWord())) {
            System.out.println("Use syntax 'enter code <code>' or 'enter key'");
        } else if (command.getSecondWord().equalsIgnoreCase("key") && !command.hasThirdWord()) {
            KeyLockedRoom room = (KeyLockedRoom) player.getCurrentLockedRoom();
            room.unlockRoom(player, key);
        } else if (command.getSecondWord().equalsIgnoreCase("code")) {
            LockedRoom currentRoom = player.getCurrentLockedRoom();
            String attemptCode = command.getThirdWord();
            if (currentRoom instanceof LockedRoom lockedRoom) {
                lockedRoom.enterCode(attemptCode, player, cell, status);
            }
        } else if(command.getSecondWord().equalsIgnoreCase("car")) {
            player.setInVehicle(true);
            System.out.println("You are now in the car.");
        }
        else {
            System.out.println("Invalid syntax.");
        }
    }

    public void driveCommand (Command command, Room pub, Vehicle vehicle){
        if(!command.hasSecondWord()) {
            if(player.getInVehicle()) {
                vehicle.carNoise(vehicle.getNoise());
                player.setCurrentRoom(pub);
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
            for(NPC Npc: currentRoom.getNPCs()){
                if (Npc.getName().equalsIgnoreCase(NpcName)){
                    System.out.println(Npc.getDialogue());
                }
            }
        }
    }

    public void dropCommand(Command command){
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
    }

    public void takeCommand(Command command){
        if (!command.hasSecondWord()) {
            System.out.println("Take what?");
        } else {
            String itemName = command.getSecondWord();
            Room currentRoom = player.getCurrentRoom();
            Item itemToTake = null;
            for (Item item : currentRoom.getItems()) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    if(item.isVisible()) {
                        itemToTake = item;
                        break;
                    } else {
                        itemName = "item";
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
            return;
        }
        else if (!command.hasThirdWord() || !command.getThirdWord().equalsIgnoreCase("for")) {
            System.out.println("Invalid syntax. Use: 'trade <your item> for <their item>'.");
            return;
        }
        else if (!command.hasFourthWord()) {
            System.out.println("Trade " + command.getSecondWord() + " for what?");
            return;
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
            return;
        }
        NPC npc = null;
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

        npc.giveToPlayer(player, npc, npcItem);     // NPC gives their item to player
        npc.receiveFromPlayer(player, npc, playerItem); // NPC receives player's item
        System.out.println("You traded your " + playerItemName + " for " + npc.getName() + "'s " + npcItem.getDescription() + ".");
    }
}
