import java.util.*;

public class StockManager {
    private Map<String, Ingredient> stock;
    private static final String FILE_NAME = "stock.txt";

    public StockManager() {
        stock = new HashMap<>();
        loadStock();
    }

    public void loadStock() {
        ArrayList<String> lines = FileHandler.loadStringsFromFile(FILE_NAME);
        for (String line : lines) {
            Ingredient ingredient = Ingredient.fromString(line);
            if (ingredient != null) {
                stock.put(ingredient.getName(), ingredient);
            }
        }
    }

    public void saveStock() {
        ArrayList<String> lines = new ArrayList<>();
        for (Ingredient ingredient : stock.values()) {
            lines.add(ingredient.toString());
        }
        FileHandler.saveStringsToFile(FILE_NAME, lines);
    }

    public void addIngredient(String name, double quantity, String unit) {
        if (stock.containsKey(name)) {
            stock.get(name).updateQuantity(quantity);
        } else {
            stock.put(name, new Ingredient(name, quantity, unit));
        }
        saveStock();
    }

    // NEW: Proper ingredient deduction method
    public boolean deductIngredient(String name, double quantity) {
        if (stock.containsKey(name)) {
            Ingredient ingredient = stock.get(name);
            if (ingredient.getQuantity() >= quantity) {
                ingredient.updateQuantity(-quantity);
                saveStock();
                return true;
            }
        }
        return false;
    }

    public List<Ingredient> getLowStockItems(double threshold) {
        List<Ingredient> lowStock = new ArrayList<>();
        for (Ingredient ingredient : stock.values()) {
            if (ingredient.getQuantity() <= threshold) {
                lowStock.add(ingredient);
            }
        }
        return lowStock;
    }

    public Map<String, Ingredient> getStock() {
        return stock;
    }

    public void deleteIngredient(String ingredientName) {
        if (stock.containsKey(ingredientName)) {
            stock.remove(ingredientName);
            saveStock();
        }
    }

    public Ingredient getIngredientByName(String name) {
        return stock.get(name);
    }

    // NEW: Check stock levels for all ingredients in a recipe
    public boolean checkStockForRecipe(ArrayList<Ingredient> ingredients) {
        for (Ingredient required : ingredients) {
            Ingredient stockIng = getIngredientByName(required.getName());
            if (stockIng == null || stockIng.getQuantity() < required.getQuantity()) {
                return false;
            }
        }
        return true;
    }
}