import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JFrame implements ActionListener {
    private JButton buttonOrders;
    private JButton buttonStock;
    private JButton buttonCustomer;
    private JButton buttonDishes;
    private CustomerManager customerManager;
    private DishManager dishManager;
    private StockManager stockManager;
    private OrderManager orderManager;
    private StatusBar statusBar;

    public HomeScreen(CustomerManager cm, DishManager dm, StockManager sm, OrderManager om) {
        this.customerManager = cm;
        this.dishManager = dm;
        this.stockManager = sm;
        this.orderManager = om;

        setTitle("Catering Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // Create status bar
        statusBar = new StatusBar();

        buttonOrders = new JButton("Manage Orders");
        buttonOrders.addActionListener(e -> {
            new OrderScreen(orderManager, dishManager);
            checkStockWarnings(); // Update status when navigating
        });

        buttonStock = new JButton("Stock Management");
        buttonStock.addActionListener(e -> {
            new StockScreen(stockManager);
            checkStockWarnings(); // Update status when navigating
        });

        buttonCustomer = new JButton("Customer Overview");
        buttonCustomer.addActionListener(e -> {
            new CustomerScreen();
            checkStockWarnings(); // Update status when navigating
        });

        buttonDishes = new JButton("Dish Management & Analytics");
        buttonDishes.addActionListener(e -> {
            new DishScreen(dishManager, orderManager);
            checkStockWarnings(); // Update status when navigating
        });

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Style buttons
        Font buttonFont = new Font("Arial", Font.BOLD, 16);
        Color buttonColor = new Color(70, 130, 180);

        buttonOrders.setFont(buttonFont);
        buttonOrders.setBackground(buttonColor);
        buttonOrders.setForeground(Color.WHITE);

        buttonStock.setFont(buttonFont);
        buttonStock.setBackground(buttonColor);
        buttonStock.setForeground(Color.WHITE);

        buttonCustomer.setFont(buttonFont);
        buttonCustomer.setBackground(buttonColor);
        buttonCustomer.setForeground(Color.WHITE);

        buttonDishes.setFont(buttonFont);
        buttonDishes.setBackground(buttonColor);
        buttonDishes.setForeground(Color.WHITE);

        mainPanel.add(buttonOrders);
        mainPanel.add(buttonStock);
        mainPanel.add(buttonCustomer);
        mainPanel.add(buttonDishes);

        // Add title
        JLabel titleLabel = new JLabel("Catering Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 0));

        add(titleLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        // Check stock warnings on startup
        checkStockWarnings();
        setVisible(true);
    }

    private void checkStockWarnings() {
        StringBuilder warnings = new StringBuilder();
        for (Ingredient ingredient : stockManager.getStock().values()) {
            if (ingredient.getQuantity() <= 5.0) { // Low stock threshold
                if (warnings.length() > 0) warnings.append(", ");
                warnings.append(ingredient.getName())
                        .append(" (").append(ingredient.getQuantity())
                        .append(" ").append(ingredient.getUnit()).append(")");
            }
        }

        if (warnings.length() > 0) {
            statusBar.setWarning("Low stock: " + warnings.toString());
        } else {
            statusBar.setNormal("All stock levels are good");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle actions if needed
    }
}