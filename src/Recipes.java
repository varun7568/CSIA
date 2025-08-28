// Inside your Recipes class
import java.io.File;
import java.util.*;

public class Recipes {
    private HashMap<String, Recipe> recipeBook;
    private final String RECIPE_FILE = "recipes.txt";

    public Recipes() {
        recipeBook = new HashMap<>();
        loadRecipes();
    }

    // New method to get a recipe by its name
    public Recipe getRecipe(String name) {
        return recipeBook.get(name);
    }

    // New method to handle file uploads
    public boolean addRecipeFromFile(File file) {
        try {
            ArrayList<String> lines = FileHandler.loadFromFile(file.getAbsolutePath(), s -> s);
            if (lines.isEmpty()) {
                return false;
            }

            // Assuming the first line is the dish name and the second is the price
            String dishName = lines.get(0).trim();
            double price = Double.parseDouble(lines.get(1).trim());

            // The rest of the lines are ingredients
            List<String> ingredientLines = lines.subList(2, lines.size());
            Map<Ingredient, Integer> ingredients = new HashMap<>();

            for (String line : ingredientLines) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    int quantity = Integer.parseInt(parts[1].trim());
                    ingredients.put(new Ingredient(name, quantity), quantity);
                }
            }

            Dish newDish = new Dish(dishName, price);
            Recipe newRecipe = new Recipe(newDish, ingredients);

            recipeBook.put(dishName, newRecipe);
            saveRecipes();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Existing load and save methods
    private void loadRecipes() {
        // ... (your existing load logic)
    }

    private void saveRecipes() {
        // ... (your existing save logic)
    }

    public void addRecipe(Recipe recipe) {
        recipeBook.put(recipe.getDish().getName(), recipe);
        saveRecipes();
    }

    public Set<String> getDishNames() {
        return recipeBook.keySet();
    }
}