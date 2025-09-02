import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class StockScreen extends JFrame implements ActionListener {
    private JLabel labelStock;
    private JButton addStockButton;
    private JButton viewStockButton;
    private JScrollPane scrollPane;
    private StockManager stockManager;
    private Table stockTablePanel;

    public StockScreen(StockManager stockManager) {
        this.stockManager = stockManager;

        setTitle("Stock Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);

        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        labelStock = new JLabel("Stock Screen");
        labelStock.setBounds(300, 20, 200, 30);
        labelStock.setHorizontalAlignment(SwingConstants.CENTER);
        labelStock.setFont(new Font("Arial", Font.BOLD, 20));

        addStockButton = new JButton("Add Stock");
        addStockButton.setBounds(50, 80, 150, 30);
        addStockButton.addActionListener(this);

        viewStockButton = new JButton("View Stock");
        viewStockButton.setBounds(210, 80, 180, 30);
        viewStockButton.addActionListener(this);

        add(labelStock);
        add(addStockButton);
        add(viewStockButton);

        toggleStockView(false);
    }

    private void toggleStockView(boolean visible) {
        if (scrollPane != null) scrollPane.setVisible(visible);
        addStockButton.setVisible(!visible);
        viewStockButton.setVisible(!visible);
        labelStock.setText(visible ? "Stock Overview" : "Stock Screen");
    }

    private void loadStockIntoTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columnNames = {"Ingredient", "Quantity"};
        ArrayList<Ingredient> stock = stockManager.getAllIngredients();

        Object[][] data = new Object[stock.size()][2];
        for (int i = 0; i < stock.size(); i++) {
            Ingredient ing = stock.get(i);
            data[i][0] = ing.getName();
            data[i][1] = ing.getQuantity();
        }

        stockTablePanel = new Table(
                columnNames,
                data,
                true,
                Map.of(
                        "Delete", (ingredient, action) -> {
                            stockManager.deleteIngredient(ingredient);
                        },
                        "Edit", (ingredient, action) -> {
                            Ingredient ing = stockManager.getIngredientByName(ingredient);
                            if (ing != null) {
                                JOptionPane.showMessageDialog(this,
                                        "Edit feature not implemented yet for Ingredient: " + ing.getName(),
                                        "Edit Stock",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                )
        );

        scrollPane = new JScrollPane(stockTablePanel);
        scrollPane.setBounds(50, 130, 700, 400);
        add(scrollPane);

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addStockButton) {
            JOptionPane.showMessageDialog(this, "Add Stock dialog would open here.");
            loadStockIntoTable();

        } else if (e.getSource() == viewStockButton) {
            toggleStockView(true);
            loadStockIntoTable();
        }
    }
}