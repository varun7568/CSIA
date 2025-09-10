import java.util.*;

public class Dish {
    private String name;
    private ArrayList<Ingredient> ingredients;

    public Dish(String name) {
        this.name = name;
        this.ingredients = new ArrayList<>();
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(";");
        for (Ingredient ing : ingredients) {
            sb.append(ing.getName()).append(":")
                    .append(ing.getQuantity()).append(":")
                    .append(ing.getUnit()).append(",");
        }
        if (!ingredients.isEmpty()) {
            sb.setLength(sb.length() - 1); // Remove trailing comma
        }
        return sb.toString();
    }

    // Static method to create Dish from string
    public static Dish fromString(String line) {
        String[] parts = line.split(";", 2);
        if (parts.length == 2) {
            Dish dish = new Dish(parts[0]);
            String[] ingredientParts = parts[1].split(",");
            for (String ingStr : ingredientParts) {
                String[] ingDetails = ingStr.split(":");
                if (ingDetails.length == 3) {
                    String name = ingDetails[0];
                    double quantity = Double.parseDouble(ingDetails[1]);
                    String unit = ingDetails[2];
                    dish.addIngredient(new Ingredient(name, quantity, unit));
                }
            }
            return dish;
        }
        return null;
    }
}