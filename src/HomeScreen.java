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

    public HomeScreen(CustomerManager cm, DishManager dm, StockManager sm, OrderManager om) {
        this.customerManager = cm;
        this.dishManager = dm;
        this.stockManager = sm;
        this.orderManager = om;

        setTitle("Menu Screen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        buttonOrders = new JButton("Manage Orders");
        buttonOrders.addActionListener(e -> new OrderScreen(orderManager, dishManager));

        buttonStock = new JButton("Stock Management");
        buttonStock.addActionListener(e -> new StockScreen(stockManager));

        buttonCustomer = new JButton("Customer Overview");
        buttonCustomer.addActionListener(e -> new CustomerScreen());

        buttonDishes = new JButton("Dish Management & Analytics");
        buttonDishes.addActionListener(e -> new DishScreen(dishManager, orderManager));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 2, 20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        mainPanel.add(buttonOrders);
        mainPanel.add(buttonStock);
        mainPanel.add(buttonCustomer);
        mainPanel.add(buttonDishes);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Handle actions if needed
    }
}