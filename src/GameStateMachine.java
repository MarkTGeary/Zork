import java.util.ArrayList;
import java.util.List;

public interface GameStateMachine {
    void enter();
}

class PlayingState implements GameStateMachine {
    @Override
    public void enter() {
        System.out.println("You are now playing the game!");
    }
}

class WonState implements GameStateMachine {
    @Override
    public void enter() {
        System.out.println("Congratulations, you won!");
        SoundStuff.playSound("audio\\Celebration2.wav");
        //System.out.println("Type 'restart' to restart the game!");
    }
}

class LostState implements GameStateMachine {
    private Character player;
    private Room storageRoomCloset;
    private StateMethods State;
    LostState(Character player, Room storageRoomCloset, StateMethods State) {
        this.player = player;
        this.storageRoomCloset = storageRoomCloset;
        this.State = State;
    }
    public void enter() {
        List<Item> items = new ArrayList<>(player.getInventory());
        for(Item item: items){
            player.dropFromInventory(item);
            storageRoomCloset.addItemToRoom(item);
        }
        System.out.println("You have had your items taken away and have been sent back to your cell!");
        System.out.println("Go find your items and continue your escape.");
        State.setGameState(GameState.PLAYING);
    }

}

class StateMethods {
    private GameState currentState;
    private GameStateMachine currentStateInterface;

    private Character player;
    private Room storageRoomCloset;

    public StateMethods(Character player, Room storageRoomCloset) {
        this.player = player;
        this.storageRoomCloset = storageRoomCloset;
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
                return new LostState(player, storageRoomCloset, this);
        }
        return null;
    }
}
