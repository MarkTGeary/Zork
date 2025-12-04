package game;

//Deal With Hints
import java.io.Serializable;

public interface HintMachine {
    void displayHint();
}
class FirstHint implements HintMachine {
    public void displayHint() {
        System.out.println("Go talk to the chef.");
    }
}

class SecondHint implements HintMachine {
    public void displayHint() {
        System.out.println("You hear prisoners talk about old stories of gold hidden under the yard. Maybe you should have a closer look.");
    }
}

class ThirdHint implements HintMachine {
    public void displayHint() {
        System.out.println("Bob will help you get access to the rest of the prison if you give him what he wants.");
    }
}
class FourthHint implements HintMachine {
    public void displayHint() {
        System.out.println("A key to the Warden's Office was lost after a guard was injured on duty.");
    }
}
class FifthHint implements HintMachine {
    public void displayHint() {
        System.out.println("The warden keeps the code to the exit in his office.");
    }
}


class HintMethods implements Serializable {
    private HintLevels currentHint;

    public void setHintLevels(HintLevels currentHint) {
        this.currentHint = currentHint;
    }

    public HintMachine getCurrentHintInterface() {
        switch(currentHint) {
            case LEVEL1:
                return new FirstHint();
            case LEVEL2:
                return new SecondHint();
            case LEVEL3:
                return new ThirdHint();
            case LEVEL4:
                return new FourthHint();
            case LEVEL5:
                return new FifthHint();
            default:
                return null;
        }
    }
}
