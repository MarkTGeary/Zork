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
        validCommands.put("enter", "enter a code");
        validCommands.put("show", "show access code");



    }

    public boolean isCommand(String commandWord) {
        return validCommands.containsKey(commandWord);
    }

    public void showAll() {
        System.out.print("Valid commands are: ");
        for (String command : validCommands.keySet()) {
            System.out.print(command + " ");
        }
        System.out.println();
    }
}
