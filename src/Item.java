import java.io.Serializable;

public class Item implements Serializable {
    private String description;
    private String name;
    private String location;
    private boolean isVisible;
    private String Code;

    public Item(String name, String description, boolean isVisible, String Code) {
        this.name = name;
        this.description = description;
        this.isVisible = isVisible;
        this.Code = Code;
    }

    public Item(String name, String description, boolean isVisible){
        this.name = name;
        this.description = description;
        this.isVisible = isVisible;
    }

    //Setters and Getters
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public String getCode() {
        return Code;
    }

    //For access code
    public void showCode(Character player){
        if(player.hasItem(this.name)){
            System.out.println("Code is : " + this.Code);
        }
        else {
            System.out.println("You have not found the access code.");
        }
    }


    //For Stringbuilder for multi word items
    @Override
    public String toString() {
        return name;
    }

}

