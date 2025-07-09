import java.io.*;
import java.util.*;

public class Recipes {

    private HashMap<String, ArrayList<Ingredient>> recipeBook;
    private final String FILE_NAME = "recipes.txt";

    public Recipes(){
        recipeBook = new HashMap<>();
        loadRecipes();
    }

    public ArrayList<Ingredient> getRecipe(String dishName){
        return recipeBook.getOrDefault(dishName, null);
    }


    // create file handler class and abstract from recipes for file access by all classes
    public void loadRecipes(){
        ArrayList<String> lines = FileHandler.loadFromFile(FILE_NAME, line -> line);
        for (String line : lines) {
            String[] parts = line.split(";", 2);
            if (parts.length == 2) {
                String dish = parts[0].trim();
                String[] ingStrs = parts[1].split(",");
                ArrayList<Ingredient> ingredients = new ArrayList<>();
                for (String ing : ingStrs) {
                    String[] ingParts = ing.trim().split(":");
                    if (ingParts.length == 2) {
                        ingredients.add(new Ingredient(ingParts[0].trim(), Integer.parseInt(ingParts[1].trim())));
                    }
                }
                recipeBook.put(dish, ingredients);
            }
        }
    }

    public void saveRecipes(){
        ArrayList<String> lines = new ArrayList<>();
        for (Map.Entry<String, ArrayList<Ingredient>> entry : recipeBook.entrySet()) {
            StringBuilder sb = new StringBuilder(entry.getKey() + ";");
            for (Ingredient ing : entry.getValue()) {
                sb.append(ing.getName()).append(":").append(ing.getQuantity()).append(",");
            }
            lines.add(sb.substring(0, sb.length() - 1)); // remove last comma
        }
        FileHandler.saveToFile(FILE_NAME, lines, s -> s); // identity function
    }

    public void addRecipe(String dishName, ArrayList<Ingredient> ingredients) {
        recipeBook.put(dishName, ingredients);
        saveRecipes();
        System.out.println("Recipe for " + dishName + "added");
    }

    public void displayAllRecipes(){
        if(recipeBook.isEmpty()) {
            System.out.println("No recipes found");
        } else {
            System.out.println("Available recipes");
            for (String dish: recipeBook.keySet()) {
                System.out.println("- " + dish);
            }
        }
    }

}
