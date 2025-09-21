import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RecipeScreen extends JFrame implements ActionListener {
    private Recipes recipes;
    private JList<String> recipeList;
    private DefaultListModel<String> listModel;
    private JButton viewButton, addButton, deleteButton;
    private JTextArea recipeDetailsArea;
    private JButton backButton;

    public RecipeScreen() {
        recipes = new Recipes();

        setTitle("Recipe Management");
        setSize(800, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        setupUI();
        loadRecipes();

        setVisible(true);
    }

    private void setupUI() {
        // Left panel for recipe list
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(200, 400));

        listModel = new DefaultListModel<>();
        recipeList = new JList<>(listModel);
        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        recipeList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                viewSelectedRecipe();
            }
        });

        JScrollPane listScroll = new JScrollPane(recipeList);
        leftPanel.add(listScroll, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        viewButton = new JButton("View Recipe");
        addButton = new JButton("Add Recipe");
        deleteButton = new JButton("Delete Recipe");

        viewButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Right panel for recipe details
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBorder(BorderFactory.createTitledBorder("Recipe Details"));

        recipeDetailsArea = new JTextArea();
        recipeDetailsArea.setEditable(false);
        recipeDetailsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane detailsScroll = new JScrollPane(recipeDetailsArea);

        rightPanel.add(detailsScroll, BorderLayout.CENTER);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setDividerLocation(250);

        backButton = new JButton("Back to Home");
        backButton.setBounds(600, 80, 150, 30);
        backButton.addActionListener(this);
        add(backButton);

        add(splitPane, BorderLayout.CENTER);
    }

    private void loadRecipes() {
        listModel.clear();
        for (String recipeName : recipes.getRecipeBook().keySet()) {
            listModel.addElement(recipeName);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewButton) {
            viewSelectedRecipe();
        } else if (e.getSource() == addButton) {
            addNewRecipe();
        } else if (e.getSource() == deleteButton) {
            deleteSelectedRecipe();
        }
        if (e.getSource() == backButton) {
            this.dispose();
        }
    }

    private void viewSelectedRecipe() {
        String selected = recipeList.getSelectedValue();
        if (selected == null) {
            recipeDetailsArea.setText("Please select a recipe to view.");
            return;
        }


        ArrayList<Ingredient> ingredients = recipes.getRecipe(selected);
        StringBuilder sb = new StringBuilder();
        sb.append("Recipe: ").append(selected).append("\n\n");
        sb.append("Ingredients:\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-20s %-10s %-10s\n", "Name", "Quantity", "Unit"));
        sb.append("----------------------------------------\n");

        for (Ingredient ing : ingredients) {
            sb.append(String.format("%-20s %-10.2f %-10s\n",
                    ing.getName(), ing.getQuantity(), ing.getUnit()));
        }

        recipeDetailsArea.setText(sb.toString());
    }

    private void addNewRecipe() {
        JDialog dialog = new JDialog(this, "Add New Recipe", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Recipe Name:");
        JTextField nameField = new JTextField();

        JLabel ingLabel = new JLabel("Ingredients (name:quantity:unit, name:quantity:unit):");
        JTextArea ingArea = new JTextArea();
        ingArea.setRows(5);

        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(ingLabel);
        formPanel.add(new JScrollPane(ingArea));

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String ingredientsText = ingArea.getText().trim();

            if (name.isEmpty() || ingredientsText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all fields.");
                return;
            }

            try {
                ArrayList<Ingredient> ingredients = parseIngredients(ingredientsText);
                recipes.addRecipe(name, ingredients);
                loadRecipes();
                dialog.dispose();

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

    private void deleteSelectedRecipe() {
        String selected = recipeList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a recipe to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete recipe '" + selected + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            recipes.getRecipeBook().remove(selected);
            recipes.saveRecipes();
            loadRecipes();
            recipeDetailsArea.setText("");
        }
    }
}