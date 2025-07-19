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
    private OrderManager om = new OrderManager();
    //private JLabel logoLabel;  For the logo

    public HomeScreen() {
        setTitle("Menu Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        add(createPanel(), BorderLayout.NORTH);

        buttonOrders = new JButton("Manage Orders");
        buttonOrders.setBounds(80, 80, 180, 100);
        buttonOrders.addActionListener(this);

        // Stock Management button
        buttonStock = new JButton("Stock Management");
        buttonStock.setBounds(340, 80, 180, 100);
        buttonStock.addActionListener(this);

        // Customer Overview button
        buttonCustomer = new JButton("Customer Overview");
        buttonCustomer.setBounds(80, 200, 180, 100);
        buttonCustomer.addActionListener(this);

        // Customer Analytics (Reports) button
        buttonAnalytics = new JButton("Customer Analytics");
        buttonAnalytics.setBounds(340, 200, 180, 100);
        buttonAnalytics.addActionListener(this);

        // Recipes button (New)
        buttonRecipes = new JButton("Recipes");
        buttonRecipes.setBounds(210, 310, 180, 40);
        buttonRecipes.addActionListener(this);

        add(buttonOrders);
        add(buttonStock);
        add(buttonCustomer);
        add(buttonAnalytics);
        add(buttonRecipes);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        mainPanel.add(buttonOrders);
        mainPanel.add(buttonStock);
        mainPanel.add(buttonCustomer);
        mainPanel.add(buttonAnalytics);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
        System.out.println("SEQUENCE: HomeScreen created");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Stock Management")) {
            System.out.println("Opening Stock");
            StockScreen stockScreen = new StockScreen(stock); // pass the real stock
        } else if (e.getActionCommand().equals("Manage Orders")) {
            System.out.println("Opening Orders");
            OrderScreen orderScreen = new OrderScreen();
        } else if (e.getActionCommand().equals("Customer Analytics")) {
            System.out.println("Opening Analytics (Reports)");
            ReportsScreen reportsScreen = new ReportsScreen();
        } else if (e.getActionCommand().equals("Customer Overview")) {
            System.out.println("Opening Customer");
            CustomerScreen customerScreen = new CustomerScreen();
        } else if (e.getActionCommand().equals("Recipes")) {
            System.out.println("Opening Recipes Screen");
            RecipeScreen recipeScreen = new RecipeScreen();

        }
    }

    private JPanel createPanel(){
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.setBackground(Color.LIGHT_GRAY);

        JButton homeButton = new JButton("Home");
        JButton stockButton = new JButton("Stock");
        JButton orderButton = new JButton("Orders");
        JButton customerButton = new JButton("Customer");
        JButton reportsButton = new JButton("Reports");
        JButton recipesButton = new JButton("Recipes");
        homeButton.addActionListener(e -> {
            homeButton.setBackground(Color.GRAY);
        });
        stockButton.addActionListener(e -> {
            StockScreen stockScreen = new StockScreen(stock);
            stockButton.setBackground(Color.GRAY);
        } );
        orderButton.addActionListener(e -> {
            OrderScreen orderScreen = new OrderScreen();
            orderButton.setBackground(Color.GRAY);
        } );
        customerButton.addActionListener(e -> {
            CustomerScreen customerScreen = new CustomerScreen();
            customerButton.setBackground(Color.GRAY);
        } );
        //reportsButton.addActionListener(e -> new ReportsScreen());
        recipesButton.addActionListener(e -> {
            RecipeScreen recipeScreen = new RecipeScreen();
            recipesButton.setBackground(Color.GRAY);
        } );

        panel.add(homeButton);
        panel.add(stockButton);
        panel.add(orderButton);
        panel.add(customerButton);
        panel.add(recipesButton);
        //panel.add(reportsButton);

        return panel;
    }
}
