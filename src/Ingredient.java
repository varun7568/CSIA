import java.util.ArrayList;
import java.util.Date;

public class Ingredient {
    private String name;
    private int quantity;
    //private Date bestBefore;
    //private ArrayList<> ingredients;

    public Ingredient(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateQuantity(int amount){
        this.quantity += amount;
        System.out.println(name + "quantity updated by " + quantity);
    }


}
