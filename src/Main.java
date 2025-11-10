import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.PrintStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {

    private ZorkPrisonGame game;
    private TextArea console;
    private ImageView imageView;

    @Override
    public void start(Stage stage) {
        stage.setMaximized(true);
        stage.setTitle("JavaZork");

        game = new ZorkPrisonGame();
        Pane root = new Pane();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        // --- Console setup ---
        console = new TextArea();
        console.setEditable(true);
        console.setWrapText(true);
        console.setPrefSize(600, 250);
        console.setLayoutX(screenWidth - console.getPrefWidth() - 5);
        console.setLayoutY(screenHeight - console.getPrefHeight() - 70);
        String intro = game.printWelcome();
        console.appendText(intro);
        console.appendText("\n> ");

        redirectSystemOut();

        // --- Handle Enter key ---
        console.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                String text = console.getText();
                String[] lines = text.split("\n");
                String lastLine = lines[lines.length - 1];
                if (lastLine.startsWith(">")) {
                    String commandText = lastLine.substring(1).trim();
                    if (!commandText.isEmpty()) {
                        process(commandText);
                    }
                }
                console.appendText("\n> ");
                console.positionCaret(console.getText().length());
            }
        });

        // --- Direction buttons ---
        Button north = new Button("Go North");
        Button south = new Button("Go South");
        Button west = new Button("Go West");
        Button east = new Button("Go East");
        Button takeItem = new Button("Pick Up Item");

        double centerX = 150;
        double centerY = 120;
        double spacing = 100;

        north.setLayoutX(centerX);
        north.setLayoutY(centerY - spacing);

        south.setLayoutX(centerX);
        south.setLayoutY(centerY + spacing);

        west.setLayoutX(centerX - spacing);
        west.setLayoutY(centerY);

        east.setLayoutX(centerX + spacing);
        east.setLayoutY(centerY);

        takeItem.setLayoutX(centerX);
        takeItem.setLayoutY(centerY);

        for (Button b : new Button[]{north, south, west, east, takeItem}) {
            b.setPrefSize(90, 90);
        }

        north.setOnAction(e -> {
            process("go north");
        });
        south.setOnAction(e -> {
            process("go south");
        });
        east.setOnAction(e -> {
            process("go east");
        });
        west.setOnAction(e -> {
                process("go west");
                });
        takeItem.setOnAction(e -> {
            Room currentRoom = game.getCurrentRoom();
            if (currentRoom == null) {
                System.out.println("Error: player is not in a room.");
                return;
            }

            if (currentRoom.getItems().isEmpty()) {
                System.out.println("There is nothing to take here.");
            } else if (currentRoom.getItems().size() == 1) {
                String itemName = currentRoom.getItems().get(0).getName();
                process("take " + itemName);
            } else {
                System.out.println("Which item do you want to take? Available: ");
                for (Item item : currentRoom.getItems()) {
                    System.out.println("- " + item.getName());
                }
            }

        });


        imageView = new ImageView();
        imageView.setFitHeight(480);
        imageView.setFitWidth(600);
        imageView.setLayoutX(screenWidth - imageView.getFitWidth() - 5);
        imageView.setLayoutY(0);

        updateRoomImage(game.getCurrentRoom());

        root.getChildren().addAll(north, south, west, east, takeItem, console, imageView);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    private void updateRoomImage(Room room) {
        Image cellImage = new Image("file:img/Cell1.png");
        Image bobCellImage = new Image("file:img/BobCell.png");
        Image corridorImage = new Image("file:img/corridor.png");
        Image yardImage = new Image("file:img/yard.png");
        Image storageRoomImage = new Image("file:img/storageRoom.png");

        switch(room.getName().toLowerCase()) {
            case "cell":
                imageView.setImage(cellImage);
                break;
            case "bobCell":
                imageView.setImage(bobCellImage);
                break;
            case "corridor1":
                imageView.setImage(corridorImage);
                break;
            case "yard":
                imageView.setImage(yardImage);
                break;
            case "storageRoom":
                imageView.setImage(storageRoomImage);
                break;
        }

    }

    private void process(String input) {
        String[] words = input.trim().split("\\s+");
        String word1 = words.length > 0 ? words[0] : null;
        String word2 = words.length > 1 ? words[1] : null;
        String word3 = words.length > 2 ? words[2] : null;
        String word4 = words.length > 3 ? words[3] : null;
        Command command = new Command(word1, word2, word3, word4);

        game.processCommand(command);

        // Update room image after command
        updateRoomImage(game.getCurrentRoom());
    }

    private void redirectSystemOut() {
        PrintStream printStream = new PrintStream(System.out) {
            @Override
            public void println(String x) {
                super.println(x);
                console.appendText(x + "\n");
            }
        };
        System.setOut(printStream);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
