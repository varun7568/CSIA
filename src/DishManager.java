import java.util.*;
import java.io.*;

public class DishManager {
    private ArrayList<Dish> dishes;
    private static final String FILE_NAME = "dishes.txt";

    public DishManager() {
        dishes = new ArrayList<>();
        loadDishes();
    }

    public void addDish(Dish dish) {
        if (getDishByName(dish.getName()) == null) {
            dishes.add(dish);
            saveDishes();
        }
    }

    public void deleteDish(String name) {
        dishes.removeIf(dish -> dish.getName().equalsIgnoreCase(name));
        saveDishes();
    }

    public Dish getDishByName(String name) {
        for (Dish dish : dishes) {
            if (dish.getName().equalsIgnoreCase(name)) {
                return dish;
            }
        }
        return null;
    }

    public ArrayList<Dish> getAllDishes() {
        return new ArrayList<>(dishes);
    }

    public void loadDishes() {
        dishes = FileHandler.loadFromFile(FILE_NAME, line -> {
            return Dish.fromString(line);
        });
    }

    public void saveDishes() {
        FileHandler.saveToFile(FILE_NAME, dishes, Dish::toString);
    }
}