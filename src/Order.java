import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Order {
    static int idCounter = 1;
    private int orderID;
    private Customer customer;
    private ArrayList<Dish> dishes;
    private Date completionDate;
    private String status; //Upcoming,Ongoing,Completed


    /// intake orders,implement fooddishes
    public Order(int orderID, Customer customer, ArrayList<Dish> dishes, Date completionDate, String status) {
        this.orderID = orderID;
        this.customer = customer;
        this.dishes = dishes;
        this.completionDate = completionDate;
        this.status = status;
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

    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }

    public void markCompleted (Stock stock, Recipes recipes){
        for(Dish dish: dishes){
            ArrayList<Ingredient> ingredients = recipes.getRecipe(dish.getName());
            if(ingredients != null){
                for(Ingredient ingredient: ingredients){
                    stock.deductIngredient(ingredient.getName(), ingredient.getQuantity());
                }
            }
        }
        this.status = "Completed";
        System.out.println("Order" + orderID + "is complete");
    }

    //link orderID to customer
    //track timestamps for reports
}
