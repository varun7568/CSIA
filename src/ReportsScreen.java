import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Map;

public class ReportsScreen extends JFrame {

    private OrderManager orderManager;
    private Recipes recipes;
    private CustomerManager customerManager;

    public ReportsScreen() {
        // Initialize managers to load data
        this.customerManager = new CustomerManager();
        this.recipes = new Recipes();
        this.orderManager = new OrderManager();
        this.orderManager.loadOrders(customerManager, recipes);

        setTitle("Reports Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Use BorderLayout for a better structure

        // Title Label
        JLabel titleLabel = new JLabel("Monthly Order Summary & Dish Popularity");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        // Main Panel for the charts
        JPanel chartsPanel = new JPanel();
        chartsPanel.setLayout(new GridLayout(1, 2, 20, 20)); // 1 row, 2 columns, with gaps
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Get data from the OrderManager
        ArrayList<Order> allOrders = orderManager.getOrders();

        // Prepare data for the charts using the Reports class
        Map<YearMonth, Integer> monthlyOrders = Reports.getMonthlyOrderCounts(allOrders);
        Map<String, Integer> dishPopularity = Reports.getDishPopularity(allOrders);

        // Add the charts to the panel
        chartsPanel.add(new BarChartPanel(monthlyOrders));
        chartsPanel.add(new PieChartPanel(dishPopularity));

        add(chartsPanel, BorderLayout.CENTER);

        setVisible(true);
    }
}