import javax.swing.*;
import java.awt.*;

public class StockScreen extends JFrame {
    public StockScreen() {
        setTitle("Ingredient Screen");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Example ingredients  ////add column for needs restocking; use from main
        String[] columns = {"Item Name", "Quantity", "Needs restocking"};
        Object[][] data = {
                {"", 50, },
                {"", 20,},
                {"", 100, }
        };

        Table ingredientTable = new Table(columns, data, true);
        add(ingredientTable, BorderLayout.CENTER);

        setVisible(true);
    }

}