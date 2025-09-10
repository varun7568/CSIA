import java.util.*;

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
}