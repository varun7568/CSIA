import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.util.*;

public class Reports {
    private OrderManager om;

    public Reports(){
        Map<YearMonth, Integer> raw = Reports.getMonthlyOrderCounts(om.getOrders());
        Map<String, Integer> monthData = new LinkedHashMap<>();
        for (YearMonth ym : raw.keySet()) {
            String label = ym.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + ym.getYear();
            monthData.put(label, raw.get(ym));
        }

        Map<String, Integer> dishData = Reports.getDishPopularity(om.getOrders());

    }

    public static Map<YearMonth, Integer> getMonthlyOrderCounts(ArrayList<Order> orders) {
        Map<YearMonth, Integer> counts = new TreeMap<>();
        for (Order order : orders) {
            YearMonth ym = YearMonth.from(order.getCompletionDate().toInstant().atZone(ZoneId.systemDefault()));
            counts.put(ym, counts.getOrDefault(ym, 0) + 1);
        }
        return counts;
    }

    public static Map<String, Integer> getDishPopularity(ArrayList<Order> orders) {
        Map<String, Integer> dishCounts = new HashMap<>();
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("Completed")) {
                for (Dish dish : order.getDishes()) {
                    String name = dish.getName();
                    dishCounts.put(name, dishCounts.getOrDefault(name, 0) + 1);
                }
            }
        }
        return dishCounts;
    }
}
