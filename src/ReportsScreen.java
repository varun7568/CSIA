import javax.swing.*;
import java.awt.*;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

public class ReportsScreen extends JFrame {

    public ReportsScreen(OrderManager orderManager) {
        setTitle("Reports");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(1, 2)); // 2 columns: bar chart and pie chart

        Map<YearMonth, Integer> raw = Reports.getMonthlyOrderCounts(orderManager.getOrders());

        Map<String, Integer> monthData = new LinkedHashMap<>();
        for (YearMonth ym : raw.keySet()) {
            String label = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear();
            monthData.put(label, raw.get(ym));
        }

        Map<String, Integer> dishData = Reports.getDishPopularity(orderManager.getOrders());

        add(new BarChartPanel(monthData));
        add(new PieChartPanel(dishData));

        setVisible(true);
    }
}
