public class Command {
    private String commandWord;
    private String secondWord;
    private String thirdWord;
    private String fourthWord;
    private String fifthWord;

    public Command(String firstWord, String secondWord, String thirdWord, String fourthWord, String fifthWord) {
        this.commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;
        this.fourthWord = fourthWord;
        this.fifthWord = fifthWord;
    }

    public String getCommandWord() {
        return commandWord;
    }

    public String getSecondWord() {
        return secondWord;
    }


    public boolean hasSecondWord() {
        return secondWord != null;
    }

    public boolean hasThirdWord() {
        return thirdWord != null;
    }
    public boolean hasFourthWord() {
        return fourthWord != null;
    }
    public boolean hasFifthWord() { return fifthWord != null; }
    public String getThirdWord() {
        return thirdWord;
    }
    public String getFourthWord() {
        return fourthWord;
    }
    public String getFifthWord() { return fifthWord; }

}
