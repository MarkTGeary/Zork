import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.PrintStream;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;

import java.util.Optional;
import javafx.application.Platform;

public class Main extends Application {

    private ZorkPrisonGame game;
    private TextArea console;
    private TextArea userType;
    private ImageView imageView;
    private ImageView imageView2;

    @Override
    public void start(Stage stage) {
        stage.setMaximized(true);
        stage.setTitle("JavaZork");

        game = new ZorkPrisonGame();
        Pane root = new Pane();

        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        double screenWidth = screenBounds.getWidth();
        double screenHeight = screenBounds.getHeight();

        userType  = new TextArea();
        userType.setEditable(true);
        userType.setWrapText(true);
        userType.setPrefSize(screenWidth-755, 100);
        userType.setLayoutX(screenWidth - userType.getPrefWidth() - 5);
        userType.setLayoutY(screenHeight - userType.getPrefHeight() - 220);
        userType.appendText("\n>");

        console = new TextArea();
        console.setEditable(false);
        console.setWrapText(true);
        console.setPrefSize(screenWidth-755, 150);
        console.setLayoutX(screenWidth - console.getPrefWidth() - 5);
        console.setLayoutY(screenHeight - console.getPrefHeight() - 70);
        String intro = game.printWelcome();
        console.appendText(intro);

        redirectSystemOut();

        userType.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                e.consume();
                String text = userType.getText();
                String[] lines = text.split("\n");
                String lastLine = lines[lines.length - 1];
                String commandText = lastLine.substring(1).trim();
                if (!commandText.isEmpty()) {
                    process(commandText);
                }
                userType.appendText("\n> ");
                userType.positionCaret(userType.getText().length());
            }
        });

        // --- Direction buttons ---
        Button north = new Button("Go North");
        Button south = new Button("Go South");
        Button west = new Button("Go West");
        Button east = new Button("Go East");
        Button takeItem = new Button("Take Item");

        Button saveProgress = new Button("Save Game");
        Button loadProgress = new Button("Load Game");
        Button inventory = new Button("Inventory");
        Button talkToNPC = new Button("Talk to npc");

        Button CodeEnter = new Button("   Enter \nKey/Code");
        Button helpButton =  new Button("    Show \nCommands");

        Button quitButton = new Button("Quit");
        Button tradeButton = new Button("Trade with npc");

        Button hintButton = new Button("Hint");
        Button musicButton = new Button(" Music \nOn/Off");


        double baseX = 5;
        double baseY = screenHeight - 230;
        double gap = 90;

        north.setLayoutX(baseX + gap);
        north.setLayoutY(baseY - gap);

        south.setLayoutX(baseX + gap);
        south.setLayoutY(baseY + gap);

        west.setLayoutX(baseX);
        west.setLayoutY(baseY);

        east.setLayoutX(baseX + 2 * gap);
        east.setLayoutY(baseY);

        takeItem.setLayoutX(baseX + gap);
        takeItem.setLayoutY(baseY);

        double StartX = ((3*baseX) + (gap*3));
        double row1Y = baseY - (gap/1.5);
        double row2Y = baseY + (gap/1.5);

        inventory.setLayoutX(StartX);
        inventory.setLayoutY(row1Y);

        talkToNPC.setLayoutX(StartX + gap);
        talkToNPC.setLayoutY(row1Y);

        saveProgress.setLayoutX(StartX + (gap * 3));
        saveProgress.setLayoutY(row1Y);

        loadProgress.setLayoutX(StartX + (gap * 4));
        loadProgress.setLayoutY(row1Y);

        CodeEnter.setLayoutX(StartX);
        CodeEnter.setLayoutY(row2Y);

        helpButton.setLayoutX(StartX + (gap * 3));
        helpButton.setLayoutY(row2Y);

        quitButton.setLayoutX(StartX + (gap * 4));
        quitButton.setLayoutY(row2Y);

        tradeButton.setLayoutX(StartX + (gap));
        tradeButton.setLayoutY(row2Y);

        hintButton.setLayoutX(StartX + (gap * 2));
        hintButton.setLayoutY(row2Y);

        musicButton.setLayoutX(StartX + (gap *2));
        musicButton.setLayoutY(row1Y);



        for (Button b : new Button[]{north, south, west, east, takeItem, inventory, talkToNPC, saveProgress, loadProgress, CodeEnter, helpButton, quitButton, tradeButton, hintButton, musicButton}) {
            b.setPrefSize(95, 90);
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

            if (currentRoom.getItems().isEmpty()) {
                System.out.println("There is nothing to take here.");
            }
            else if (currentRoom.getItems().size() == 1) {
                String itemName = currentRoom.getItems().get(0).getName();
                process("take " + itemName);
            }
            else {
                ObservableList<Item> itemChoices = FXCollections.observableArrayList(currentRoom.getItems());
                ComboBox<Item> comboBox = new ComboBox<>(itemChoices);
                comboBox.setPromptText("Choose an Item");

                comboBox.setLayoutX(baseX + (gap*2) + 10);
                comboBox.setLayoutY(baseY - gap);

                root.getChildren().add(comboBox);

                comboBox.setOnAction(event -> {
                    Item selected = comboBox.getValue();
                    if (selected != null) {
                        System.out.println("Selected: " + selected.getName());
                        process("take " + selected.getName());
                        root.getChildren().remove(comboBox);
                    }
                });
            }
        });

        inventory.setOnAction(e -> {
            process("inventory");
        });

        talkToNPC.setOnAction(e -> {
            Room currentRoom = game.getCurrentRoom();
            String x = null;
            if (!currentRoom.getNPCs().isEmpty()) {
                x = currentRoom.getNPCs().get(0).getName();
            }
            process("talk to " + x);
        });

        saveProgress.setOnAction(e -> {
            process("save");
        });

        loadProgress.setOnAction(e -> {
            process("load");
        });

        CodeEnter.setOnAction(e -> {
            TextInputDialog choice = new TextInputDialog();
            choice.setHeaderText("Key or Code?");
            Optional<String> choiceResult = choice.showAndWait();
            if (choiceResult.isPresent()) {
                if (choiceResult.get().equalsIgnoreCase("key")) {
                    process("enter key");
                } else if (choiceResult.get().equalsIgnoreCase("code")) {
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setHeaderText("Enter Code");
                    Optional<String> result = dialog.showAndWait();

                    if (result.isPresent()) {
                        String enteredCode = result.get();
                        process("enter code " + enteredCode);
                    }
                }
            }else {
                System.out.println("Error: please enter 'key' or 'code'");
            }
        });

        helpButton.setOnAction(e -> {
            process("help");
        });

        quitButton.setOnAction(e -> {
            process("quit");
            Platform.exit();
        });

        tradeButton.setOnAction(e -> {
            Room currentRoom = game.getCurrentRoom();
            NPC npc = currentRoom.getNPCs().getFirst();
            Item item = npc.getInventory().getFirst();
            ObservableList<Item> itemChoices = FXCollections.observableArrayList(game.player.getInventory());
            ComboBox<Item> comboBox = new ComboBox<>(itemChoices);
            comboBox.setPromptText("Choose an Item");

            comboBox.setLayoutX(baseX + (gap*2) + 10);
            comboBox.setLayoutY(baseY - gap);

            root.getChildren().add(comboBox);

            comboBox.setOnAction(event -> {
                Item selected = comboBox.getValue();
                if (selected != null) {
                    System.out.println("Selected: " + selected.getName());
                    process("trade " + selected.getName() + " for " + item.getName());
                    root.getChildren().remove(comboBox);
                }
            });
        });

        hintButton.setOnAction(e -> {
            process("hint");
        });

        musicButton.setOnAction(e -> {
            process("music");
        });
        imageView = new ImageView();
        imageView.setFitHeight(480);
        imageView.setFitWidth(750);
        imageView.setLayoutX(0);
        imageView.setLayoutY(0);

        updateRoomImage(game.getCurrentRoom());

        imageView2 = new ImageView();
        imageView2.setFitHeight(480);
        imageView2.setFitWidth(screenWidth - 750);
        imageView2.setLayoutX(751);
        imageView2.setLayoutY(0);
        Image mapImage = new Image("file:img/map.png");
        imageView2.setImage(mapImage);

        root.getChildren().addAll(north, south, west, east, takeItem, inventory, console, imageView, saveProgress, loadProgress, talkToNPC, CodeEnter, helpButton, quitButton, tradeButton, userType, hintButton, musicButton, imageView2);

        Scene scene = new Scene(root, screenWidth, screenHeight);
        stage.setScene(scene);
        stage.show();
    }

    private void updateRoomImage(Room room) {
        Image cellImage = new Image("file:img/Cell1.png");
        Image bobCellImage = new Image("file:img/Cell2.png");
        Image corridorImage = new Image("file:img/corridor.png");
        Image yardImage = new Image("file:img/yard.png");
        Image storageRoomImage = new Image("file:img/storageRoom.png");
        Image pubImage = new Image("file:img/pub.png");
        Image infirmaryImage = new Image("file:img/infirmary.png");
        Image cafeteriaImage = new  Image("file:img/cafeteria.png");
        Image wardenOfficeImage = new Image("file:img/wardenOffice.png");
        Image securityRoomImage = new Image("file:img/securityRoom.png");
        Image kitchenImage = new Image("file:img/kitchen.png");
        Image guardStationImage = new Image("file:img/guardStation.png");
        Image prisonExitImage = new Image("file:img/prisonExit.jpg");
        Image corridor2Image = new Image("file:img/corridor2.png");
        Image storageRoomClosetImage = new Image("file:img/closet.png");

        switch(room.getName().toLowerCase()) {
            case "cell":
                imageView.setImage(cellImage);
                break;
            case "bobcell":
                imageView.setImage(bobCellImage);
                break;
            case "corridor1":
                imageView.setImage(corridorImage);
                break;
            case "yard":
                imageView.setImage(yardImage);
                break;
            case "storageroom":
                imageView.setImage(storageRoomImage);
                break;
            case "pub":
                imageView.setImage(pubImage);
                break;
            case "infirmary":
                imageView.setImage(infirmaryImage);
                break;
            case "cafeteria":
                imageView.setImage(cafeteriaImage);
                break;
            case "wardenoffice":
                imageView.setImage(wardenOfficeImage);
                break;
            case "securityroom":
                imageView.setImage(securityRoomImage);
                break;
            case "kitchen":
                imageView.setImage(kitchenImage);
                break;
            case "guardstation":
                imageView.setImage(guardStationImage);
                break;
            case "prisonexit":
                imageView.setImage(prisonExitImage);
                break;
            case "corridor2":
                imageView.setImage(corridor2Image);
                break;
            case "storageroomcloset":
                imageView.setImage(storageRoomClosetImage);
                break;
        }
    }


    private void process(String input) {
        Parser parser = new Parser();
        Command command = parser.getCommand(input);
        game.processCommand(command);
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
