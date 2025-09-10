import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HomeScreen extends JFrame implements ActionListener {
    private JLabel labelOutput;
    private JButton buttonOrders;
    private JButton buttonStock;
    private JButton buttonCustomer;
    private JButton buttonAnalytics;
    private JButton buttonRecipes;
    private Stock stock = new Stock();
    private Recipes recipes;
    private CustomerManager customerManager;
    private StockManager stockManager;
    private OrderManager orderManager;

    public HomeScreen(CustomerManager cm, Recipes recipes, StockManager sm, OrderManager om) {
        this.customerManager = cm;
        this.recipes = recipes;
        this.stockManager = sm;
        this.orderManager = om;

        setTitle("Menu Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        this.recipes = new Recipes();

        buttonOrders = new JButton("Manage Orders");
        buttonOrders.addActionListener(e -> new OrderScreen(orderManager));

        buttonStock = new JButton("Stock Management");
        buttonStock.addActionListener(e -> new StockScreen(stockManager));

        buttonCustomer = new JButton("Customer Overview");
        buttonCustomer.addActionListener(e -> new CustomerScreen());

        buttonAnalytics = new JButton("Customer Analytics");
        buttonAnalytics.addActionListener(e -> new ReportsScreen(orderManager));

        buttonRecipes = new JButton("Recipes");
        buttonRecipes.addActionListener(e -> new RecipeScreen());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        mainPanel.add(buttonOrders);
        mainPanel.add(buttonStock);
        mainPanel.add(buttonCustomer);
        mainPanel.add(buttonAnalytics);
        mainPanel.add(buttonRecipes);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        System.out.println("SEQUENCE: HomeScreen created");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle actions if needed
    }
}