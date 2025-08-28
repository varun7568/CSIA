import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.table.DefaultTableModel;

public class StockScreen extends JFrame {
    private Stock stock;
    private Table ingredientTable;
    private JScrollPane scrollPane;
    private JButton editButton, newButton;

    public StockScreen(Stock stock) {
        this.stock = stock;
        setTitle("Ingredient Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main panel to hold everything
        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("Stock Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Load and display the table
        loadStockTable();

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        editButton = new JButton("Edit Items");
        newButton = new JButton("New item");

        editButton.addActionListener(this::handleEditAction);
        newButton.addActionListener(this::handleNewItemAction);

        buttonPanel.add(editButton);
        buttonPanel.add(newButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void loadStockTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columns = {"Item Name", "Quantity", "Needs restocking"};
        ArrayList<Object[]> rows = new ArrayList<>();

        for (Map.Entry<String, Ingredient> entry : stock.getStockMap().entrySet()) {
            String name = entry.getKey();
            int quantity = entry.getValue().getQuantity();
            String needsRestocking = quantity < stock.getMinStock() ? "Yes" : "No";
            rows.add(new Object[]{name, quantity, needsRestocking});
        }

        ingredientTable = new Table(columns, rows, true, e -> {
            String nameToDelete = e.getActionCommand();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete '" + nameToDelete + "'?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                stock.deleteIngredient(nameToDelete); // Requires a new method in the Stock class
                loadStockTable(); // Reload the table
            }
        });

        scrollPane = new JScrollPane(ingredientTable);
        add(scrollPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void handleNewItemAction(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Enter item name:");
        if (name != null && !name.trim().isEmpty()) {
            try {
                int quantity = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter initial quantity for " + name + ":"));
                stock.addNewIngredient(name.trim(), quantity);
                loadStockTable();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleEditAction(ActionEvent e) {
        int selectedRow = ingredientTable.getTable().getSelectedRow();
        if (selectedRow != -1) {
            String name = (String) ingredientTable.getTable().getValueAt(selectedRow, 0);
            String newQuantityStr = JOptionPane.showInputDialog(this, "Enter new quantity for " + name + ":", ingredientTable.getTable().getValueAt(selectedRow, 1));
            if (newQuantityStr != null) {
                try {
                    int newQuantity = Integer.parseInt(newQuantityStr);
                    stock.updateIngredientQuantity(name, newQuantity); // Requires a new method in the Stock class
                    loadStockTable();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid quantity. Please enter a number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select an item to edit.", "No Item Selected", JOptionPane.WARNING_MESSAGE);
        }
    }
}
