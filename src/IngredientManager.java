import com.sun.jdi.IntegerValue;

import java.io.File;
import java.util.ArrayList;

public class IngredientManager{
    private ArrayList<Ingredient> ingredients;
    private static final String FILE_NAME = "ingredients.txt";


    public IngredientManager(){
    /*


        ingredients = FileHandler.loadFromFile(FILE_NAME, line ->{
            String[] parts = line.split(",", 4);
            if (parts.length >= 2) {
                Ingredient ingredient = new Ingredient(parts[0], parts[1]);
                if (parts.length ==3) {
                    String[] orderArray = parts[2].split("\\|");
                    for (String order : orderArray) {
                        ingredient.addOrder(order);
                    }
                }
                return ingredient;
            }
            return null;
        });
        selectionSortCustomers();
        })*/
    }
}
