import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DishScreen extends JFrame implements ActionListener {
    private DishManager dishManager;
    private OrderManager orderManager;
    private JList<String> dishList;
    private DefaultListModel<String> listModel;
    private JButton addButton, editButton, deleteButton, analyticsButton;
    private JTextArea dishDetailsArea;
    private JTabbedPane tabbedPane;

    public DishScreen(DishManager dishManager, OrderManager orderManager) {
        this.dishManager = dishManager;
        this.orderManager = orderManager;

        setTitle("Dish Management & Analytics");
        setSize(1000, 700);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        setupUI();
        loadDishes();

        setVisible(true);
    }

    private void setupUI() {
        tabbedPane = new JTabbedPane();

        // Dish Management Tab
        JPanel managementPanel = new JPanel(new BorderLayout());

        // Left panel for dish list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 400));

        listModel = new DefaultListModel<>();
        dishList = new JList<>(listModel);
        dishList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        dishList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSelectedDish();
            }
        });

        JScrollPane listScroll = new JScrollPane(dishList);
        leftPanel.add(listScroll, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        addButton = new JButton("Add Dish");
        editButton = new JButton("Edit Dish");
        deleteButton = new JButton("Delete Dish");
        analyticsButton = new JButton("View Analytics");

        addButton.addActionListener(this);
        editButton.addActionListener(this);
        deleteButton.addActionListener(this);
        analyticsButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(analyticsButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Right panel for dish details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Dish Details & Recipe"));

        dishDetailsArea = new JTextArea();
        dishDetailsArea.setEditable(false);
        dishDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(dishDetailsArea);

        rightPanel.add(detailsScroll, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(300);

        managementPanel.add(splitPane, BorderLayout.CENTER);
        tabbedPane.addTab("Dish Management", managementPanel);

        // Analytics Tab
        JPanel analyticsPanel = new JPanel(new BorderLayout());
        analyticsPanel.add(new AnalyticsPanel(), BorderLayout.CENTER);
        tabbedPane.addTab("Analytics", analyticsPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadDishes() {
        listModel.clear();
        for (Dish dish : dishManager.getAllDishes()) {
            listModel.addElement(dish.getName());
        }
    }

    private void viewSelectedDish() {
        String selected = dishList.getSelectedValue();
        if (selected == null) {
            dishDetailsArea.setText("Please select a dish to view.");
            return;
        }

        Dish dish = dishManager.getDishByName(selected);
        if (dish == null) {
            dishDetailsArea.setText("Dish not found: " + selected);
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DISH: ").append(dish.getName().toUpperCase()).append("\n\n");
        sb.append("RECIPE - Ingredients Required:\n");
        sb.append("══════════════════════════════════════════════════════════════\n");
        sb.append(String.format("%-25s %-12s %-10s\n", "INGREDIENT", "QUANTITY", "UNIT"));
        sb.append("══════════════════════════════════════════════════════════════\n");

        ArrayList<Ingredient> ingredients = dish.getIngredients();
        if (ingredients == null || ingredients.isEmpty()) {
            sb.append("No ingredients defined for this dish.\n");
        } else {
            for (Ingredient ing : ingredients) {
                sb.append(String.format("%-25s %-12.2f %-10s\n",
                        ing.getName(), ing.getQuantity(), ing.getUnit()));
            }
        }

        sb.append("══════════════════════════════════════════════════════════════\n");
        sb.append("Total Ingredients: ").append(ingredients != null ? ingredients.size() : 0);

        dishDetailsArea.setText(sb.toString());
    }

    private void addNewDish() {
        showDishDialog("Add New Dish", null);
    }

    private void editSelectedDish() {
        String selected = dishList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a dish to edit.");
            return;
        }

        Dish dish = dishManager.getDishByName(selected);
        if (dish != null) {
            showDishDialog("Edit Dish", dish);
        }
    }

    private void showDishDialog(String title, Dish existingDish) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(600, 500);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Dish Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Dish Name:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        JTextField nameField = new JTextField();
        if (existingDish != null) {
            nameField.setText(existingDish.getName());
            nameField.setEditable(false);
        }
        formPanel.add(nameField, gbc);

        // Ingredients label
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Ingredients:"), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        JTextArea ingredientsArea = new JTextArea();
        ingredientsArea.setRows(8);
        if (existingDish != null) {
            StringBuilder ingredientsText = new StringBuilder();
            for (Ingredient ing : existingDish.getIngredients()) {
                ingredientsText.append(ing.getName()).append(":")
                        .append(ing.getQuantity()).append(":")
                        .append(ing.getUnit()).append(",");
            }
            if (ingredientsText.length() > 0) {
                ingredientsText.setLength(ingredientsText.length() - 1);
            }
            ingredientsArea.setText(ingredientsText.toString());
        } else {
            ingredientsArea.setText("chicken:2:kg,rice:1:kg,salt:0.5:tsp");
        }

        JScrollPane ingredientsScroll = new JScrollPane(ingredientsArea);
        formPanel.add(ingredientsScroll, gbc);

        // Instructions
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JLabel instructionLabel = new JLabel("Format: name:quantity:unit, name:quantity:unit");
        instructionLabel.setFont(new Font("SansSerif", Font.ITALIC, 10));
        formPanel.add(instructionLabel, gbc);

        // Buttons
        gbc.gridx = 1; gbc.gridy = 4;
        gbc.gridwidth = 1;
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ingredientsText = ingredientsArea.getText().trim();

            if (name.isEmpty() || ingredientsText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                ArrayList<Ingredient> ingredients = parseIngredients(ingredientsText);

                if (existingDish != null) {
                    dishManager.deleteDish(existingDish.getName());
                    Dish updatedDish = new Dish(name);
                    for (Ingredient ingredient : ingredients) {
                        updatedDish.addIngredient(ingredient);
                    }
                    dishManager.addDish(updatedDish);
                } else {
                    Dish newDish = new Dish(name);
                    for (Ingredient ingredient : ingredients) {
                        newDish.addIngredient(ingredient);
                    }
                    dishManager.addDish(newDish);
                }

                loadDishes();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Dish saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error parsing ingredients: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private ArrayList<Ingredient> parseIngredients(String text) {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        String[] pairs = text.split(",");

        for (String pair : pairs) {
            String[] parts = pair.trim().split(":");
            if (parts.length == 3) {
                String name = parts[0].trim();
                double quantity = Double.parseDouble(parts[1].trim());
                String unit = parts[2].trim();
                ingredients.add(new Ingredient(name, quantity, unit));
            }
        }
        return ingredients;
    }

    private void deleteSelectedDish() {
        String selected = dishList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a dish to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete '" + selected + "'?\nThis cannot be undone.",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dishManager.deleteDish(selected);
            loadDishes();
            dishDetailsArea.setText("");
            JOptionPane.showMessageDialog(this, "Dish deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addNewDish();
        } else if (e.getSource() == editButton) {
            editSelectedDish();
        } else if (e.getSource() == deleteButton) {
            deleteSelectedDish();
        } else if (e.getSource() == analyticsButton) {
            tabbedPane.setSelectedIndex(1);
        }
    }

    class AnalyticsPanel extends JPanel {
        public AnalyticsPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JLabel titleLabel = new JLabel("Dish Popularity Analytics", JLabel.CENTER);
            titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            add(titleLabel, BorderLayout.NORTH);

            add(new DishAnalyticsPanel(), BorderLayout.CENTER);
        }
    }

    class DishAnalyticsPanel extends JPanel {
        public DishAnalyticsPanel() {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Map<String, Integer> dishPopularity = getDishPopularity();
            if (dishPopularity.isEmpty()) {
                g.setColor(Color.BLACK);
                g.drawString("No order data available for analytics", getWidth()/2 - 120, getHeight()/2);
                return;
            }

            drawAnalytics((Graphics2D) g, dishPopularity);
        }

        private Map<String, Integer> getDishPopularity() {
            Map<String, Integer> dishCounts = new HashMap<>();

            for (Order order : orderManager.getAllOrders()) {
                if ("Completed".equalsIgnoreCase(order.getStatus())) {
                    for (Dish dish : order.getDishes()) {
                        dishCounts.put(dish.getName(), dishCounts.getOrDefault(dish.getName(), 0) + 1);
                    }
                }
            }

            return dishCounts;
        }

        private void drawAnalytics(Graphics2D g2, Map<String, Integer> dishPopularity) {
            int totalOrders = dishPopularity.values().stream().mapToInt(i -> i).sum();

            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("Dish Popularity Analytics - Total Orders: " + totalOrders, 20, 30);

            // Draw a simple bar chart instead of pie chart
            int barWidth = 30;
            int spacing = 20;
            int chartHeight = 200;
            int startX = 50;
            int startY = 100;

            int maxOrders = dishPopularity.values().stream().max(Integer::compare).orElse(1);

            int i = 0;
            for (Map.Entry<String, Integer> entry : dishPopularity.entrySet()) {
                int barHeight = (int) ((double) entry.getValue() / maxOrders * chartHeight);
                int x = startX + i * (barWidth + spacing);
                int y = startY + chartHeight - barHeight;

                // Use simple alternating colors
                g2.setColor(i % 2 == 0 ? Color.BLUE : Color.GREEN);
                g2.fillRect(x, y, barWidth, barHeight);

                g2.setColor(Color.BLACK);
                g2.drawRect(x, y, barWidth, barHeight);

                // Draw dish name and count
                g2.drawString(entry.getKey(), x, startY + chartHeight + 20);
                g2.drawString(String.valueOf(entry.getValue()), x + 5, y - 5);

                i++;
            }

            // Draw summary text
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString("Most Popular Dish: " + getMostPopularDish(dishPopularity), 50, startY + chartHeight + 60);
            g2.drawString("Total Unique Dishes Ordered: " + dishPopularity.size(), 50, startY + chartHeight + 80);
        }

        private String getMostPopularDish(Map<String, Integer> dishPopularity) {
            return dishPopularity.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(entry -> entry.getKey() + " (" + entry.getValue() + " orders)")
                    .orElse("No data");
        }
    }
}