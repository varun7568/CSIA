import java.util.ArrayList;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.stream.Collectors;

public class OrderManager {
    private ArrayList<Order> orders;
    private static final String FILE_NAME = "orders.txt";

    public OrderManager(){
        orders = new ArrayList<>();
    }

    public void addOrder(Order order){
        orders.add(order);
        saveOrders();
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

    public void saveOrders(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> lines = new ArrayList<>();

        for(Order order: orders){
            StringBuilder sb = new StringBuilder();
            sb.append(order.getOrderID()).append(";").append(order.getCustomer().getName()).append(";").append(sdf.format(order.getCompletionDate())).append(";").append(order.getStatus()).append(";");

            ArrayList<Dish> dishes = order.getDishes();
            for(Dish d: dishes){
                sb.append(d.getName()).append("|");
            }

            if (!dishes.isEmpty()) sb.setLength(sb.length() - 1);
            lines.add(sb.toString());

        }

        FileHandler.saveToFile(FILE_NAME, lines, s -> s);
    }

    public void loadOrders(CustomerManager customerManager, Recipes recipes) {
        ArrayList<String> lines = FileHandler.loadFromFile("orders.txt", s -> s);
        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 6) {
                try {
                    int orderID = Integer.parseInt(parts[0].trim());
                    String customerName = parts[1].trim();
                    String dishesString = parts[2].trim();
                    String completionDateStr = parts[3].trim();
                    String status = parts[4].trim();

                    // Get the customer
                    Customer customer = customerManager.getCustomerByName(customerName);
                    if (customer == null) {
                        System.err.println("Customer not found for order: " + customerName);
                        continue;
                    }

                    // Create a list of dishes from the string
                    ArrayList<Dish> dishes = new ArrayList<>();
                    String[] dishNames = dishesString.split(",");
                    for (String dishName : dishNames) {
                        // We need to get the Dish object from the Recipe
                        Recipe recipe = recipes.getRecipe(dishName.trim());
                        if (recipe != null) {
                            dishes.add(recipe.getDish()); // Add the Dish object to the list
                        }
                    }

                    Date completionDate = new SimpleDateFormat("dd/MM/yyyy").parse(completionDateStr);

                    Order newOrder = new Order(orderID, customer, dishes, completionDate, status);
                    orders.add(newOrder);

                } catch (Exception e) {
                    System.err.println("Error loading order from line: " + line);
                    e.printStackTrace();
                }
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