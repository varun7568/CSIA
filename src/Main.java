import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Initialize managers
        CustomerManager customerManager = new CustomerManager();
        Recipes recipes = new Recipes();
        StockManager stockManager = new StockManager();
        OrderManager orderManager = new OrderManager(stockManager, recipes);

        // Load data
        customerManager.loadCustomers(); // Add this method to CustomerManager
        recipes.loadRecipes();
        stockManager.loadStock();
        orderManager.loadOrders();

        // Start UI
        SwingUtilities.invokeLater(() -> {
            new HomeScreen(customerManager, recipes, stockManager, orderManager);
        });
    }
}