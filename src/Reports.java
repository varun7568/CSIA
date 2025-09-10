import java.util.*;
import java.time.YearMonth;
import java.time.ZoneId;

public class Reports {

    public static Map<YearMonth, Integer> getMonthlyOrderCounts(ArrayList<Order> orders) {
        Map<YearMonth, Integer> counts = new TreeMap<>();
        for (Order order : orders) {
            if (order.getCompletionDate() != null) {
                YearMonth ym = YearMonth.from(order.getCompletionDate().toInstant().atZone(ZoneId.systemDefault()));
                counts.put(ym, counts.getOrDefault(ym, 0) + 1);
            }
        }
        return counts;
    }

    public static Map<String, Integer> getDishPopularity(ArrayList<Order> orders) {
        Map<String, Integer> dishCounts = new HashMap<>();
        for (Order order : orders) {
            if (order.getStatus().equalsIgnoreCase("Completed") || order.getStatus().equalsIgnoreCase("Previous") || order.getStatus().equalsIgnoreCase("Ongoing")) {
                for (Dish dish : order.getDishes()) {
                    String name = dish.getName();
                    dishCounts.put(name, dishCounts.getOrDefault(name, 0) + 1);
                }
            }
        }
        return dishCounts;
    }
}