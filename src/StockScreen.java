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

        String[] columnNames = {"Ingredient", "Quantity", "Unit"};
        ArrayList<Ingredient> stock = new ArrayList<>(stockManager.getStock().values());

        Object[][] data = new Object[stock.size()][3];
        for (int i = 0; i < stock.size(); i++) {
            Ingredient ing = stock.get(i);
            data[i][0] = ing.getName();
            data[i][1] = ing.getQuantity();
            data[i][2] = ing.getUnit();
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
                            JOptionPane.showMessageDialog(this,
                                    "Edit feature not implemented yet for Ingredient: " + ingredient,
                                    "Edit Stock",
                                    JOptionPane.INFORMATION_MESSAGE);
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
            showAddStockDialog();
        } else if (e.getSource() == viewStockButton) {
            toggleStockView(true);
            loadStockIntoTable();
        }
    }

    private void showAddStockDialog() {
        JDialog dialog = new JDialog(this, "Add Stock", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Ingredient Name:");
        JTextField nameField = new JTextField();

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField();

        JLabel unitLabel = new JLabel("Unit:");
        JTextField unitField = new JTextField("units");

        JButton addButton = new JButton("Add");
        JButton cancelButton = new JButton("Cancel");

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(unitLabel);
        dialog.add(unitField);
        dialog.add(addButton);
        dialog.add(cancelButton);

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                double quantity = Double.parseDouble(quantityField.getText().trim());
                String unit = unitField.getText().trim();

                if (name.isEmpty() || unit.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                    return;
                }

                stockManager.addIngredient(name, quantity, unit);
                JOptionPane.showMessageDialog(dialog, "Stock added successfully!");
                dialog.dispose();
                loadStockIntoTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity.");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}