import java.io.File;
import java.util.HashMap;
import java.util.ArrayList;

public class Stock {
    private HashMap<String, Ingredient> stock;
    private final int minStock = 5;
    private final String FILE_NAME = "stock.txt";

    public HashMap<String, Ingredient> getStockMap() {
        return stock;
    }

    public int getMinStock() {
        return minStock;
    }

    public Stock() {
        stock = new HashMap<>();
        // Check if the file exists before attempting to load
        File file = new File(FILE_NAME);
        if (file.exists()) {
            loadStock();
        } else {
            // If the file doesn't exist, create a sample stock
            System.out.println("No stock file found. Creating a sample stock.");
            addDefaultIngredients();
            saveStock();
        }
    }

    private void addDefaultIngredients() {
        // Add a few sample ingredients with quantities
        addNewIngredient("Flour", 2500); // 2.5 kg
        addNewIngredient("Sugar", 1000); // 1 kg
        addNewIngredient("Eggs", 24);    // 2 dozen
        addNewIngredient("Milk", 5);     // 5 liters
        addNewIngredient("Butter", 500);  // 500 g
        addNewIngredient("Salt", 1000);  // 1 kg
        addNewIngredient("Chicken", 20); // 20 pieces
        addNewIngredient("Tomatoes", 10); // 10 pieces
        addNewIngredient("Onions", 15);   // 15 pieces
    }

    // New method to load stock from file
    private void loadStock() {
        ArrayList<String> lines = FileHandler.loadFromFile(FILE_NAME, s -> s);
        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 2) {
                try {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    stock.put(name, new Ingredient(name, quantity));
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing stock quantity: " + line);
                }
            }
        }
    }

    // New method to save stock to file
    private void saveStock() {
        ArrayList<String> lines = new ArrayList<>();
        for (Ingredient ingredient : stock.values()) {
            lines.add(ingredient.getName() + "," + ingredient.getQuantity());
        }
        FileHandler.saveToFile(FILE_NAME, lines, s -> s);
    }

    // Updated addIngredient to handle quantity updates and new adds
    public void addNewIngredient(String name, int quantity) {
        if (!stock.containsKey(name.toLowerCase())) {
            stock.put(name.toLowerCase(), new Ingredient(name, quantity));
            saveStock();
            System.out.println("Added new ingredient: " + name);
        } else {
            System.out.println("Ingredient already exists. Use update to change quantity.");
        }
    }

    public void updateIngredientQuantity(String name, int newQuantity) {
        if (stock.containsKey(name.toLowerCase())) {
            stock.get(name.toLowerCase()).setQuantity(newQuantity);
            saveStock();
            System.out.println("Updated quantity for " + name + " to " + newQuantity);
        } else {
            System.out.println(name + " not found in stock.");
        }
    }

    public void deleteIngredient(String name) {
        if (stock.containsKey(name.toLowerCase())) {
            stock.remove(name.toLowerCase());
            saveStock();
            System.out.println("Deleted " + name + " from stock.");
        } else {
            System.out.println(name + " not found in stock.");
        }
    }

    // New public method for adding to quantity
    public void addQuantity(String name, int quantity) {
        if (stock.containsKey(name.toLowerCase())) {
            stock.get(name.toLowerCase()).updateQuantity(quantity);
            saveStock();
        } else {
            System.out.println(name + " not found in stock. Use 'addNewIngredient' to add it first.");
        }
    }

    public void deductIngredient(String name, int quantity) {
        if (stock.containsKey(name.toLowerCase())) {
            Ingredient ingredient = stock.get(name.toLowerCase());
            if (ingredient.getQuantity() >= quantity) {
                ingredient.updateQuantity(-quantity);
                saveStock();
                System.out.println("Deducted " + quantity + " of " + name);
            } else {
                System.out.println("Not enough stock for " + name);
            }
        } else {
            System.out.println(name + " is not found in stock");
        }
    }
}