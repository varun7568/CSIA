import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class StockScreen extends JFrame {
    public StockScreen(Stock stock) {
        setTitle("Ingredient Screen");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Example ingredients  ////add column for needs restocking; use from main
        String[] columns = {"Item Name", "Quantity", "Needs restocking"};
        ArrayList<Object[]> rows = new ArrayList<>();

        for(Map.Entry<String, Ingredient> entry : stock.getStockMap().entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            String needsRestocking = quantity < stock.getMinStock() ? "Yes" : "No" ;
            rows.add(new Object[]{name, quantity, needsRestocking});
        }

        Object[][] data = new Object[rows.size()][columns.length];
        for(int i = 0; i < rows.size(); i++) {
            data[i] = rows.get(i);
        }

        Table ingredientTable = new Table(columns, data, true);
        add(ingredientTable, BorderLayout.CENTER);

        setVisible(true);
    }

}