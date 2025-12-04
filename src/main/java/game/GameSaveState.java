package game;

import java.io.Serializable;

public class GameSaveState implements Serializable {
    private GameCharacter player;
    private StateMethods status;
    private HintMethods hintStatus;

    public GameSaveState(GameCharacter player, StateMethods status, HintMethods hintStatus) {
        this.player = player;
        this.status = status;
        this.hintStatus = hintStatus;
    }

    public GameCharacter getPlayer() {
        return player;
    }

    public StateMethods getStatus() {
        return status;
    }

    public HintMethods getHintStatus() {
        return hintStatus;
    }
}
