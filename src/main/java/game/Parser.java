package game;

import java.util.Scanner;

public class Parser {
    private CommandWords commands;
    private Scanner reader;

    public Parser() {
        commands = new CommandWords();
    }

    public Command getCommand(String inputLine) {
        System.out.print("> ");

        String word1 = null;
        String word2 = null;
        String word3 = null;
        String word4 = null;
        String word5 = null;

        inputLine = inputLine.toLowerCase();
        Scanner tokenizer = new Scanner(inputLine);
        if (tokenizer.hasNext()) {
            word1 = tokenizer.next();
            if (tokenizer.hasNext()) {
                word2 = tokenizer.next();
                if (tokenizer.hasNext()) {
                    word3 = tokenizer.next();
                    if (tokenizer.hasNext()) {
                        word4 = tokenizer.next();
                        if (tokenizer.hasNext()) {
                            word5 = tokenizer.next();
                        }
                    }
                }
            }

        }

        if (commands.isCommand(word1)) {
            return new Command(word1, word2, word3, word4, word5);
        } else {
            return new Command(null, word2, word3, word4, word5);
        }
    }
    public Command getCommand() {
        reader = new Scanner(System.in);
        System.out.print("> ");
        String inputLine = reader.nextLine();
        return getCommand(inputLine);
    }


    public void showCommands() {
        commands.showAll();
    }
}


