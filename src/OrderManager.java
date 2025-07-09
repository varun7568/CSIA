import java.util.ArrayList;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.stream.Collectors;

public class OrderManager {
    private ArrayList<Order> orders;

    public OrderManager(){
        orders = new ArrayList<>();
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public ArrayList<Order> getOrders(){
        return orders;
    }


    public void statusSort(String status){
        for(Order order: orders){
            if(order.getStatus().equalsIgnoreCase(status)){
                System.out.println("Order ID" + order.getOrderID() + "_ Customer:" + order.getCustomer().getName());
            }
        }
    }



    public void createOrder(Scanner scanner, Recipes recipes, CustomerManager customerManager) {
        System.out.println("Enter customer's name");
        String name = scanner.nextLine();

        Customer customer = customerManager.getCustomerByName(name);
        if (customer ==null){
            System.out.println("Customer not found, creating new customer");
            System.out.print("Phone number: ");
            String phoneNum = scanner.nextLine();
            System.out.print("Address: ");
            String address = scanner.nextLine();
            customer = new Customer(name,phoneNum,address);
            customerManager.addCustomer(customer);
        }

        ArrayList<Dish> selectedDishes = new ArrayList<>();
        while(true) {
            System.out.println("Enter dish name or done to finish");
            String dishName = scanner.nextLine();
            if (dishName.equalsIgnoreCase("done")) break;

            ArrayList<Ingredient> recipe = recipes.getRecipe(dishName);
            if(recipe != null) {
                selectedDishes.add(new Dish(dishName, 0 ));
            } else {
                System.out.println("Dish not found in Recipes");
            }
        }

        System.out.println("Enter completion date in the format: dd/mm/yyyy");
        String dateInput = scanner.nextLine();
        Date completionDate = null;
        try {
            completionDate = new SimpleDateFormat("dd/mm/yyyy").parse(dateInput);
        } catch (ParseException e) {
            System.out.println("Invalid date format. Using today's date.");
            completionDate = new Date();
        }

        Order newOrder = new Order(customer, selectedDishes, completionDate);
        addOrder(newOrder);
        System.out.println("Order created with ID: " + newOrder.getOrderID());
    }
}