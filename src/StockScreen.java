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
    private boolean stockViewVisible = false;

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
        stockViewVisible = visible;
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
                            loadStockIntoTable();
                        },
                        "Edit", (ingredient, action) -> {
                            showEditStockDialog(stockManager.getIngredientByName(ingredient));
                        }
                )
        );

        scrollPane = new JScrollPane(stockTablePanel);
        scrollPane.setBounds(50, 130, 700, 400);
        scrollPane.setVisible(stockViewVisible);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private void showEditStockDialog(Ingredient ingredient) {
        if (ingredient == null) return;

        JDialog dialog = new JDialog(this, "Edit Stock", true);
        dialog.setSize(400, 250);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Ingredient:");
        JTextField nameField = new JTextField(ingredient.getName());
        nameField.setEditable(false);

        JLabel quantityLabel = new JLabel("Quantity:");
        JTextField quantityField = new JTextField(String.valueOf(ingredient.getQuantity()));

        JLabel unitLabel = new JLabel("Unit:");
        JTextField unitField = new JTextField(ingredient.getUnit());

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(quantityLabel);
        dialog.add(quantityField);
        dialog.add(unitLabel);
        dialog.add(unitField);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            try {
                double newQuantity = Double.parseDouble(quantityField.getText().trim());
                String newUnit = unitField.getText().trim();

                if (newUnit.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Unit cannot be empty.");
                    return;
                }

                if (newQuantity == ingredient.getQuantity() && newUnit.equals(ingredient.getUnit())) {
                    JOptionPane.showMessageDialog(dialog, "No changes were made.", "Info", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Update stock
                stockManager.deleteIngredient(ingredient.getName());
                stockManager.addIngredient(ingredient.getName(), newQuantity, newUnit);

                JOptionPane.showMessageDialog(dialog, "Stock updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadStockIntoTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void showAddStockDialog() {
        JDialog dialog = new JDialog(this, "Add Stock", true);
        dialog.setSize(400, 250);
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
                JOptionPane.showMessageDialog(dialog, "Stock added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadStockIntoTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for quantity.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
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
}