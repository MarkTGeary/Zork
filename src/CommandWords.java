import java.util.HashMap;
import java.util.Map;

public class CommandWords {
    private final Map<String, String> validCommands;

    public CommandWords() {
        validCommands = new HashMap<>();
        validCommands.put("go", "Move to another room");
        validCommands.put("quit", "End the game");
        validCommands.put("help", "Show help");
        validCommands.put("look", "Look around");
        validCommands.put("drink", "Take a sip of your drink");
        validCommands.put("inventory", "Check inventory");
        validCommands.put("take", "Take an item");
        validCommands.put("drop", "Drop an item");
        validCommands.put("trade", "Trade an item with an NPC");
        validCommands.put("talk", "Talk to an NPC");

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
