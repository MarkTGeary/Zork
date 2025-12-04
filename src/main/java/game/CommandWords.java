package game;
import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private final Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");
        validCommands.put("drink", "Take a sip of your drink");
        validCommands.put("inventory", "Check inventory");
        validCommands.put("take", "Take an item");
        validCommands.put("drop", "Drop an item");
        validCommands.put("trade", "Trade an item with an NPC");
        validCommands.put("talk", "Talk to an NPC");
        validCommands.put("drive", "Drive to the pub");
        validCommands.put("save", "Save the game");
        validCommands.put("load", "Load the game");
        validCommands.put("enter", "enter a code/key");
        validCommands.put("show", "show access code");
        validCommands.put("hint", "get a hint");
        validCommands.put("inject", "inject poison");
        validCommands.put("dig", "dig for treasure");
        validCommands.put("music", "Pause/Unpause music");
        validCommands.put("winthegame", "skip to the end");



    }

    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        for (String command : validCommands.keySet()) {
            System.out.println(command + " ");
        }
        System.out.println();
    }
}
