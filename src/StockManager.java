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

    public boolean deductIngredientsForDish(String dishName, Recipes recipes) {
        ArrayList<Ingredient> required = recipes.getRecipe(dishName);
        if (required == null || required.isEmpty()) {
            return false;
        }

        // Check availability first
        for (Ingredient requiredIng : required) {
            Ingredient stockIng = stock.get(requiredIng.getName());
            if (stockIng == null || stockIng.getQuantity() < requiredIng.getQuantity()) {
                return false;
            }
        }

        // Then deduct
        for (Ingredient requiredIng : required) {
            Ingredient stockIng = stock.get(requiredIng.getName());
            stockIng.updateQuantity(-requiredIng.getQuantity());
        }

        saveStock();
        return true;
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
}