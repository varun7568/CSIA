import java.util.HashMap;

public class Stock {
    private HashMap<String, Ingredient> stock;
    private final double minStock = 5.0; // Changed to double

    public HashMap<String, Ingredient> getStockMap() {
        return stock;
    }

    public double getMinStock() { // Changed return type to double
        return minStock;
    }

    public Stock() {
        stock = new HashMap<>();
    }

    public void addIngredient(String name, double quantity) { // Changed to double
        if (stock.containsKey(name)) {
            stock.get(name).updateQuantity(quantity);
        } else {
            stock.put(name, new Ingredient(name, quantity));
        }
    }

    public void checkStockWarning() {
        for (Ingredient ingredient : stock.values()) {
            if (ingredient.getQuantity() < minStock) {
                System.out.println("Warning: Low stock on: " + ingredient.getName());
                System.out.println("Current quantity is: " + ingredient.getQuantity());
            } else {
                System.out.println("The current stock level of " + ingredient.getName() + " is: " + ingredient.getQuantity());
            }
        }
    }

    public void deductIngredient(String name, double quantity) { // Changed to double
        if (stock.containsKey(name)) {
            Ingredient ingredient = stock.get(name);
            if (ingredient.getQuantity() >= quantity) {
                ingredient.updateQuantity(-quantity);
                System.out.println("Deducted " + quantity + " of " + name);
            } else {
                System.out.println("Not enough stock for " + name);
            }
        } else {
            System.out.println(name + " is not found in stock");
        }
    }
}