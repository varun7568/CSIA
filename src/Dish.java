import java.util.HashMap;

public class Dish {
    private String name;
    private double price;
    private HashMap<Ingredient, Integer> ingredients;

    public Dish(String name, double price){
        this.name = name;
        this.price = price;
        this.ingredients = new HashMap<>();
    }

    public void addIngredient(Ingredient ingredient, int quantity){
        ingredients.put(ingredient, quantity);
    }

    public String getName() {
        return name;
    }

    public double getPrice(){
        return price;
    }

    public HashMap<Ingredient, Integer> getIngredients() {
        return ingredients;
    }
}
