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

    public ReportsScreen(OrderManager orderManager) {
        this.orderManager = orderManager;
        setTitle("Analytics Dashboard");
        setSize(1000, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        // Get actual data from orders
        Map<YearMonth, Integer> monthlyOrders = getMonthlyOrderCounts();
        Map<String, Integer> dishPopularity = getDishPopularity();

        add(new PieChartPanel(dishPopularity));
        add(new BarChartPanel(monthlyOrders));

        setVisible(true);
    }

    private Map<YearMonth, Integer> getMonthlyOrderCounts() {
        Map<YearMonth, Integer> counts = new TreeMap<>();
        for (Order order : orderManager.getOrdersByStatus("Completed")) {
            YearMonth ym = YearMonth.from(order.getCompletionDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
            counts.put(ym, counts.getOrDefault(ym, 0) + 1);
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

    // Inner chart panel classes
    class PieChartPanel extends JPanel {
        private final Map<String, Integer> data;
        private final Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA, Color.CYAN, Color.PINK};

        public PieChartPanel(Map<String, Integer> data) {
            this.data = data;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data.isEmpty()) {
                g.drawString("No data available", 50, 50);
                return;
            }
            drawPieChart((Graphics2D) g);
        }

        private void drawPieChart(Graphics2D g2) {
            int total = data.values().stream().mapToInt(i -> i).sum();
            int startAngle = 0;
            int i = 0;

            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Dish Popularity", 20, 20);

            int size = Math.min(getWidth(), getHeight()) - 100;
            int x = 50, y = 50;

            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                int angle = (int) Math.round(360.0 * entry.getValue() / total);
                g2.setColor(colors[i % colors.length]);
                g2.fill(new Arc2D.Double(x, y, size, size, startAngle, angle, Arc2D.PIE));
                startAngle += angle;
                i++;
            }

            // Legend
            int lx = x + size + 20;
            int ly = y;
            i = 0;
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                g2.setColor(colors[i % colors.length]);
                g2.fillRect(lx, ly, 20, 20);
                g2.setColor(Color.BLACK);
                g2.drawString(entry.getKey() + " (" + entry.getValue() + ")", lx + 30, ly + 15);
                ly += 25;
                i++;
            }
        }
    }

    class BarChartPanel extends JPanel {
        private final Map<YearMonth, Integer> data;

        public BarChartPanel(Map<YearMonth, Integer> data) {
            this.data = data;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (data.isEmpty()) {
                g.drawString("No data available", 50, 50);
                return;
            }
            drawBarChart((Graphics2D) g);
        }

        private void drawBarChart(Graphics2D g2) {
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Monthly Order Count", 20, 20);

            int width = getWidth();
            int height = getHeight();

            int padding = 50;
            int barWidth = 40;
            int space = 20;

            List<YearMonth> months = new ArrayList<>(data.keySet());
            months.sort(Comparator.naturalOrder());

            int max = data.values().stream().mapToInt(i -> i).max().orElse(1);
            int chartHeight = height - 2 * padding;

            for (int i = 0; i < months.size(); i++) {
                YearMonth month = months.get(i);
                int value = data.get(month);
                int barHeight = (int) ((double) value / max * chartHeight);
                int x = padding + i * (barWidth + space);
                int y = height - padding - barHeight;

                g2.setColor(Color.BLUE);
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawString(month.toString(), x - 10, height - padding + 15);
                g2.drawString(String.valueOf(value), x + 5, y - 5);
            }
        }
    }
}