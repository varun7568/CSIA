import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Map;

public class ReportsScreen extends JFrame {

    private OrderManager orderManager;

    public ReportsScreen(OrderManager orderManager) {
        this.orderManager = orderManager;
        setTitle("Customer Analytics Reports");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.add("Monthly Orders", new MonthlyOrdersPanel(orderManager.getOrders()));
        tabbedPane.add("Dish Popularity", new DishPopularityPanel(orderManager.getOrders()));

        add(tabbedPane);
        setVisible(true);
    }

    // Panel for Monthly Orders
    class MonthlyOrdersPanel extends JPanel {
        private Map<YearMonth, Integer> data;

        public MonthlyOrdersPanel(ArrayList<Order> orders) {
            this.data = Reports.getMonthlyOrderCounts(orders);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBarChart(g, "Monthly Orders", data);
        }
    }

    // Panel for Dish Popularity
    class DishPopularityPanel extends JPanel {
        private Map<String, Integer> data;

        public DishPopularityPanel(ArrayList<Order> orders) {
            this.data = Reports.getDishPopularity(orders);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBarChart(g, "Dish Popularity", data);
        }
    }

    // Generic bar chart drawing
    private void drawBarChart(Graphics g, String title, Map<?, Integer> data) {
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int padding = 50;
        int barWidth = Math.max(10, (width - 2 * padding) / Math.max(data.size(), 1) - 10);
        int maxBarHeight = height - 2 * padding;

        // Draw title
        g2.setFont(new Font("SansSerif", Font.BOLD, 18));
        g2.drawString(title, padding, padding - 10);

        if (data.isEmpty()) {
            g2.setFont(new Font("SansSerif", Font.ITALIC, 16));
            g2.drawString("No data available for this report.", getWidth()/2 - 100, getHeight()/2);
            return;
        }

        // Find max value
        int maxValue = data.values().stream().mapToInt(v -> v).max().orElse(1);

        // Axis
        g2.drawLine(padding, height - padding, width - padding, height - padding); // X-axis
        g2.drawLine(padding, padding, padding, height - padding); // Y-axis

        int x = padding + 10;
        int i = 0;


        for (Map.Entry<?, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            int barHeight = (int) ((double) value / maxValue * maxBarHeight);

            // Bar
            g2.setColor(new Color(100, 150, 240));
            g2.fillRect(x, height - padding - barHeight, barWidth, barHeight);

            // Labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 10));
            String label = entry.getKey().toString();
            if (label.length() > 10) label = label.substring(0, 10) + "...";

            g2.drawString(label, x, height - padding + 15);
            g2.drawString(String.valueOf(value), x, height - padding - barHeight - 5);

            x += barWidth + 10;
            i++;
        }
    }

}