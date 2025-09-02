import java.util.ArrayList;
import java.util.Date;

public class Order {
    static int idCounter = 1;
    private int orderID;
    private Customer customer;
    private ArrayList<Dish> dishes;
    private Date completionDate;
    private String status;

    public Order(Customer customer, ArrayList<Dish> dishes, Date completionDate) {
        this.orderID = idCounter++;
        this.customer = customer;
        this.dishes = dishes;
        this.status = "Upcoming";
        this.completionDate = completionDate;
    }

    public void markCompleted(Stock stock, Recipes recipes) {
        for (Dish dish : dishes) {
            ArrayList<Ingredient> ingredients = recipes.getRecipe(dish.getName());
            if (ingredients != null) {
                for (Ingredient ingredient : ingredients) {
                    // Cast double to int if needed, or keep as double
                    stock.deductIngredient(ingredient.getName(), (int) ingredient.getQuantity());
                }
            }
        }
        this.status = "Completed";
        System.out.println("Order " + orderID + " is complete");
    }

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    public int getOrderID() {
        return orderID;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public Date getCompletionDate(){
        return completionDate;
    }
    //link orderID to customer
    //track timestamps for reports
}
