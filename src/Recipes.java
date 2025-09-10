import java.util.*;
import java.io.*;

public class Recipes {
    private HashMap<String, ArrayList<Ingredient>> recipeBook;
    private final String FILE_NAME = "recipes.txt";

    public Recipes() {
        recipeBook = new HashMap<>();
        loadRecipes();
    }

    public HashMap<String, ArrayList<Ingredient>> getRecipeBook() {
        return recipeBook;
    }

    public ArrayList<Ingredient> getRecipe(String dishName) {
        return recipeBook.getOrDefault(dishName, new ArrayList<>());
    }

    public void loadRecipes() {
        ArrayList<String> lines = FileHandler.loadFromFile(FILE_NAME, line -> line);
        for (String line : lines) {
            String[] parts = line.split(";", 2);
            if (parts.length == 2) {
                String dish = parts[0].trim();
                String[] ingStrs = parts[1].split(",");
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (String ing : ingStrs) {
                    String[] ingParts = ing.trim().split(":");
                    if (ingParts.length == 3) {
                        ingredients.add(new Ingredient(ingParts[0].trim(),
                                Double.parseDouble(ingParts[1].trim()), ingParts[2].trim()));
                    }
                }
                recipeBook.put(dish, ingredients);
            }
        }
    }

    public void saveRecipes() {
        ArrayList<String> lines = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Ingredient>> entry : recipeBook.entrySet()) {
            StringBuilder sb = new StringBuilder(entry.getKey() + ";");
            for (Ingredient ing : entry.getValue()) {
                sb.append(ing.getName()).append(":")
                        .append(ing.getQuantity()).append(":")
                        .append(ing.getUnit()).append(",");
            }
            if (!entry.getValue().isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            lines.add(sb.toString());
        }
        FileHandler.saveStringsToFile(FILE_NAME, lines);
    }

    public void addRecipe(String dishName, ArrayList<Ingredient> ingredients) {
        recipeBook.put(dishName, ingredients);
        saveRecipes();
        System.out.println("Recipe for " + dishName + " added");
    }

    public void displayAllRecipes() {
        if (recipeBook.isEmpty()) {
            System.out.println("No recipes found");
        } else {
            System.out.println("Available recipes:");
            for (String dish : recipeBook.keySet()) {
                System.out.println("- " + dish);
            }
        }
    }
}