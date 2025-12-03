import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public interface GameStateMachine {
    void enter();
}

class PlayingState implements GameStateMachine {
    @Override
    public void enter() {
        System.out.println("Leave your cell and go escape the prison!");
    }
}

class WonState implements GameStateMachine {
    @Override
    public void enter() {
        System.out.println("Congratulations, you won!");
        SoundStuff.playSound("audio/Winner.wav");
    }
}

class LostState implements GameStateMachine {
    private Character player;
    private Room storageRoomCloset;
    private StateMethods State;
    private Room cell;
    LostState(Character player, Room storageRoomCloset, Room cell, StateMethods State) {
        this.player = player;
        this.storageRoomCloset = storageRoomCloset;
        this.cell = cell;
        this.State = State;
    }
    public void enter() {
        List<Item> items = new ArrayList<>(player.getInventory());
        for(Item item: items){
            player.dropFromInventory(item);
            storageRoomCloset.addItemToRoom(item);
        }
        player.setCurrentRoom(cell);
        System.out.println("You have had your items taken away and have been sent back to your cell!");
        System.out.println("Go find your items and continue your escape.");
        State.setGameState(GameState.PLAYING);
    }

}

class StateMethods implements Serializable {
    private GameState currentState;
    private transient GameStateMachine currentStateInterface;

    private Character player;
    private Room storageRoomCloset;
    private Room cell;

    public StateMethods(Character player, Room storageRoomCloset, Room cell) {
        this.player = player;
        this.storageRoomCloset = storageRoomCloset;
        this.cell = cell;
    }

    public void setGameState(GameState newState) {
        this.currentState = newState;
        this.currentStateInterface = getState(newState);
        this.currentStateInterface.enter();
    }

    private GameStateMachine getState(GameState state) {
        switch (state) {
            case PLAYING:
                return new PlayingState();
            case WON:
                return new WonState();
            case LOST:
                return new LostState(player, storageRoomCloset, cell,this);
        }
        return null;
    }
}
