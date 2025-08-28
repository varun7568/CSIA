import java.util.Map;

public class Recipe {
    private Dish dish;
    private Map<Ingredient, Integer> ingredients;

    public Recipe(Dish dish, Map<Ingredient, Integer> ingredients) {
        this.dish = dish;
        this.ingredients = ingredients;
    }

    public Dish getDish() {
        return dish;
    }

    // New method to get the ingredients
    public Map<Ingredient, Integer> getIngredients() {
        return ingredients;
    }
}