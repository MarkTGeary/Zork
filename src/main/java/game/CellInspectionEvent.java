package game;
public class CellInspectionEvent extends RandomGameEvent{
    @Override
    public void trigger() {
        System.out.println("Cell Inspection Event!!!!!!!");
        System.out.println("Make it back to your cell as quick as possible or you could be caught by the guards!");
        PrisonInspection.startMoveCountdown();
    }
}

class PrisonInspection {
    private static int movesLeft = -1;
    private static boolean active = false;

    public static void startMoveCountdown() {
        if (!active) {
            movesLeft = 5;
            active = true;
        }
    }

    public static void inspection(GameCharacter player, Room cell, StateMethods state) {
        if (!active) {
            return;
        }

        if (player.getCurrentRoom().getName().equals(cell.getName())) {
            System.out.println("You made it back to your cell early! The inspection will end automatically.");
            System.out.flush();

            while (movesLeft > 0) {
                System.out.println(movesLeft + " moves until the inspection!");
                movesLeft--;
            }

            active = false;
            System.out.println("You passed the prison inspection. You are free to move around again.");
            return;
        }
        movesLeft--;

        if (movesLeft <= 0) {
            active = false;

            if (player.getCurrentRoom().getName().equals(cell.getName())) {
                System.out.println("You passed the prison inspection. You are free to move around again.");
            } else {
                System.out.println("You were out of your cell at inspection!");
                System.out.println("You can hear the guards' footsteps nearing you...");
                System.out.flush();

                BackgroundSoundStuff.pauseUnpauseMusic();
                SoundStuff.playSound("/audio/caught.wav");
                try {
                    Thread.sleep(11000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SoundStuff.playSound("audio/OofNoise.wav");
                System.out.println("The guards found you!");
                BackgroundSoundStuff.pauseUnpauseMusic();
                state.setGameState(GameState.LOST);
            }
        } else {
            System.out.println(movesLeft + " moves until the inspection!");
        }
    }


    public static boolean isActive() {
        return active;
    }
}
