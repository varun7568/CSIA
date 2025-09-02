import java.util.*;
import java.text.*;
import java.time.LocalDate;

public class OrderManager {
    private ArrayList<Order> orders;
    private StockManager stockManager;
    private Recipes recipes;
    private static final String FILE_NAME = "orders.txt";

    public OrderManager(StockManager stockManager, Recipes recipes) {
        this.orders = new ArrayList<>();
        this.stockManager = stockManager;
        this.recipes = recipes;
        loadOrders();
    }

    public void addOrder(Order order) {
        orders.add(order);
        saveOrders();
    }

    public void deleteOrder(int orderId) {
        orders.removeIf(order -> order.getOrderID() == orderId);
        saveOrders();
    }

    public void completeOrder(int orderId) {
        for (Order order : orders) {
            if (order.getOrderID() == orderId && "Upcoming".equals(order.getStatus())) {
                for (Dish dish : order.getDishes()) {
                    stockManager.deductIngredientsForDish(dish.getName(), recipes);
                }
                order.setStatus("Completed");
                order.setCompletionDate(new Date());
                saveOrders();
                break;
            }
        }
    }

    public ArrayList<Order> getOrdersByStatus(String status) {
        ArrayList<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase(status)) {
                filtered.add(order);
            }
        }
        return filtered;
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

            for (Dish dish : order.getDishes()) {
                sb.append(dish.getName()).append("|");
            }

            if (!order.getDishes().isEmpty()) {
                sb.setLength(sb.length() - 1);
            }
            lines.add(sb.toString());
        }

        FileHandler.saveToFile(FILE_NAME, lines, s -> s);
    }

    public void loadOrders() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        ArrayList<String> lines = FileHandler.loadFromFile(FILE_NAME, s -> s);

        for (String line : lines) {
            try {
                String[] parts = line.split(";");
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    String customerName = parts[1];
                    Date date = sdf.parse(parts[2]);
                    String status = parts[3];
                    String[] dishNames = parts[4].split("\\|");

                    // Create customer (simplified - in real app, use CustomerManager)
                    Customer customer = new Customer(customerName, "0000000000", "Unknown");

                    ArrayList<Dish> dishes = new ArrayList<>();
                    for (String dishName : dishNames) {
                        dishes.add(new Dish(dishName, 0)); // Price not stored in order file
                    }

                    Order order = new Order(customer, dishes, date);
                    order.setStatus(status);
                    orders.add(order);

                    if (id >= Order.idCounter) {
                        Order.idCounter = id + 1;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error loading order: " + e.getMessage());
            }
        }
    }
}