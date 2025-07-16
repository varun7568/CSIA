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
        saveOrders(); // Save immediately
    }

    public ArrayList<Order> getOrders(){
        return orders;
    }

    public void saveOrders() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> lines = new ArrayList<>();

        for (Order order : orders) {
            StringBuilder sb = new StringBuilder();
            sb.append(order.getOrderID()).append(";")
                    .append(order.getCustomer().getName()).append(";")
                    .append(sdf.format(order.getCompletionDate())).append(";")
                    .append(order.getStatus()).append(";");

            ArrayList<Dish> dishes = order.getDishes();
            for (Dish d : dishes) {
                sb.append(d.getName()).append("|");
            }

            // Remove last pipe
            if (!dishes.isEmpty()) sb.setLength(sb.length() - 1);

            lines.add(sb.toString());
        }

        FileHandler.saveToFile(FILE_NAME, lines, s -> s);
    }

    public void loadOrders(CustomerManager cm, Recipes recipes) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> lines = FileHandler.loadFromFile(FILE_NAME, s -> s);

        for (String line : lines) {
            String[] parts = line.split(";");
            if (parts.length >= 5) {
                try {
                    int id = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    Date date = sdf.parse(parts[2]);
                    String status = parts[3];
                    String[] dishNames = parts[4].split("\\|");

                    Customer customer = cm.getCustomerByName(customerName);
                    if (customer == null) continue;

                    ArrayList<Dish> dishes = new ArrayList<>();
                    for (String dishName : dishNames) {
                        ArrayList<Ingredient> ingList = recipes.getRecipe(dishName);
                        dishes.add(new Dish(dishName, 0)); // use dummy price
                    }

                    Order order = new Order(customer, dishes, date);
                    order.setStatus(status);
                    orders.add(order);

                    // Ensure the order ID counter keeps increasing
                    if (id >= Order.idCounter) {
                        Order.idCounter = id + 1;
                    }

                } catch (Exception e) {
                    System.out.println("Error loading order: " + e.getMessage());
                }
            }
        }
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