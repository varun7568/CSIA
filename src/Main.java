import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize managers
        CustomerManager customerManager = new CustomerManager();
        DishManager dishManager = new DishManager();
        StockManager stockManager = new StockManager();
        OrderManager orderManager = new OrderManager(stockManager, dishManager, new Recipes());

        // Load data
        customerManager.loadCustomers();
        dishManager.loadDishes();
        stockManager.loadStock();
        orderManager.loadOrders();

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new HomeScreen(customerManager, dishManager, stockManager, orderManager);
        });
    }
}