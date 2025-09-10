import javax.swing.*;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.List;

public class ReportsScreen extends JFrame {
    private OrderManager orderManager;
    private JTabbedPane tabbedPane;

    public ReportsScreen(OrderManager orderManager) {
        this.orderManager = orderManager;
        setTitle("Analytics Dashboard");
        setSize(1200, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();

        // Monthly Orders Tab
        JPanel monthlyPanel = new JPanel(new BorderLayout());
        monthlyPanel.add(new JLabel("Monthly Order Trends", JLabel.CENTER), BorderLayout.NORTH);
        monthlyPanel.add(new MonthlyOrdersPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Monthly Orders", monthlyPanel);

        // Dish Popularity Tab
        JPanel dishPanel = new JPanel(new BorderLayout());
        dishPanel.add(new JLabel("Dish Popularity Analysis", JLabel.CENTER), BorderLayout.NORTH);
        dishPanel.add(new DishPopularityPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Dish Popularity", dishPanel);

        // Summary Tab
        JPanel summaryPanel = new JPanel(new GridLayout(2, 1));
        summaryPanel.add(createSummaryPanel());
        summaryPanel.add(createLowStockPanel());
        tabbedPane.addTab("Summary", summaryPanel);

        add(tabbedPane, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        add(closeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.setBorder(BorderFactory.createTitledBorder("Order Summary"));

        int totalOrders = orderManager.getOrdersByStatus("Completed").size();
        int upcomingOrders = orderManager.getOrdersByStatus("Upcoming").size();

        panel.add(new JLabel("Total Completed Orders:"));
        panel.add(new JLabel(String.valueOf(totalOrders)));

        panel.add(new JLabel("Upcoming Orders:"));
        panel.add(new JLabel(String.valueOf(upcomingOrders)));

        panel.add(new JLabel("Total Revenue:"));
        panel.add(new JLabel("£" + calculateTotalRevenue()));

        return panel;
    }

    private JPanel createLowStockPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Stock Alerts"));

        JTextArea stockAlerts = new JTextArea();
        stockAlerts.setEditable(false);

        // This would need access to StockManager - you might want to pass it to the constructor
        stockAlerts.setText("Stock alert functionality would be implemented here with access to stock data");

        panel.add(new JScrollPane(stockAlerts), BorderLayout.CENTER);
        return panel;
    }

    private String calculateTotalRevenue() {
        // Simple revenue calculation - you might want to implement proper pricing
        return String.valueOf(orderManager.getOrdersByStatus("Completed").size() * 25);
    }

    class MonthlyOrdersPanel extends JPanel {
        public MonthlyOrdersPanel() {
            setLayout(new BorderLayout());
            add(new BarChartPanel(getMonthlyOrderCounts()), BorderLayout.CENTER);
        }
    }

    class DishPopularityPanel extends JPanel {
        public DishPopularityPanel() {
            setLayout(new BorderLayout());
            add(new PieChartPanel(getDishPopularity()), BorderLayout.CENTER);
        }
    }

    private Map<YearMonth, Integer> getMonthlyOrderCounts() {
        Map<YearMonth, Integer> counts = new TreeMap<>();
        for (Order order : orderManager.getOrdersByStatus("Completed")) {
            if (order.getCompletionDate() != null) {
                YearMonth ym = YearMonth.from(order.getCompletionDate().toInstant()
                        .atZone(ZoneId.systemDefault()).toLocalDate());
                counts.put(ym, counts.getOrDefault(ym, 0) + 1);
            }
        }
        return counts;
    }

    private Map<String, Integer> getDishPopularity() {
        Map<String, Integer> dishCounts = new HashMap<>();
        for (Order order : orderManager.getOrdersByStatus("Completed")) {
            for (Dish dish : order.getDishes()) {
                dishCounts.put(dish.getName(), dishCounts.getOrDefault(dish.getName(), 0) + 1);
            }
        }
        return dishCounts;
    }

    class PieChartPanel extends JPanel {
        private final Map<String, Integer> data;
        private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK};

        public PieChartPanel(Map<String, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data.isEmpty()) {
                g.drawString("No order data available", 50, 50);
                return;
            }
            drawPieChart((Graphics2D) g);
        }

        private void drawPieChart(Graphics2D g2) {
            int total = data.values().stream().mapToInt(i -> i).sum();
            int startAngle = 0;
            int i = 0;

            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Dish Popularity Distribution", 20, 20);

            int size = Math.min(getWidth(), getHeight()) - 150;
            int x = 50, y = 50;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int angle = (int) Math.round(360.0 * entry.getValue() / total);
                g2.setColor(colors[i % colors.length]);
                g2.fill(new Arc2D.Double(x, y, size, size, startAngle, angle, Arc2D.PIE));
                startAngle += angle;
                i++;
            }

            // Legend
            int lx = x + size + 30;
            int ly = y;
            i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                g2.setColor(colors[i % colors.length]);
                g2.fillRect(lx, ly, 20, 20);
                g2.setColor(Color.BLACK);
                g2.drawString(entry.getKey() + " (" + entry.getValue() + " orders)", lx + 30, ly + 15);
                ly += 25;
                i++;
            }
        }
    }

    class BarChartPanel extends JPanel {
        private final Map<YearMonth, Integer> data;

        public BarChartPanel(Map<YearMonth, Integer> data) {
            this.data = data;
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data.isEmpty()) {
                g.drawString("No monthly data available", 50, 50);
                return;
            }
            drawBarChart((Graphics2D) g);
        }

        private void drawBarChart(Graphics2D g2) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Monthly Order Volume", 20, 20);

            int width = getWidth();
            int height = getHeight();

            int padding = 80;
            int barWidth = 40;
            int space = 20;

            List<YearMonth> months = new ArrayList<>(data.keySet());
            months.sort(Comparator.naturalOrder());

            int max = data.values().stream().mapToInt(i -> i).max().orElse(1);
            int chartHeight = height - 2 * padding;

            g2.drawLine(padding, height - padding, padding, height - padding - chartHeight);
            g2.drawLine(padding, height - padding, width - padding, height - padding);

            // Draw bars and labels
            for (int i = 0; i < months.size(); i++) {
                YearMonth month = months.get(i);
                int value = data.get(month);
                int barHeight = (int) ((double) value / max * chartHeight);
                int x = padding + i * (barWidth + space);
                int y = height - padding - barHeight;

                g2.setColor(Color.BLUE);
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                // Rotate text for better readability
                g2.rotate(-Math.PI/4, x + barWidth/2, height - padding + 20);
                g2.drawString(month.toString(), x - 10, height - padding + 20);
                g2.rotate(Math.PI/4, x + barWidth/2, height - padding + 20);

                g2.drawString(String.valueOf(value), x + 5, y - 5);
            }
        }
    }
}