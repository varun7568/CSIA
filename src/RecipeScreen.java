import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class RecipeScreen extends JFrame implements ActionListener {
    private Recipes recipes;
    private JList<String> recipeList;
    private DefaultListModel<String> listModel;
    private JButton viewButton, addButton, deleteButton;

    public RecipeScreen() {
        recipes = new Recipes();

        setTitle("Recipe Management");
        setSize(600, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        setupUI();
        loadRecipes();

        setVisible(true);
    }

    private void setupUI() {
        // Recipe list
        listModel = new DefaultListModel<>();
        recipeList = new JList<>(listModel);
        recipeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(recipeList);
        add(scrollPane, BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout());

        viewButton = new JButton("View Recipe");
        addButton = new JButton("Add Recipe");
        deleteButton = new JButton("Delete Recipe");

        viewButton.addActionListener(this);
        addButton.addActionListener(this);
        deleteButton.addActionListener(this);

        buttonPanel.add(viewButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);
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
    }

    private void viewSelectedRecipe() {
        String selected = recipeList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Please select a recipe to view.");
            return;
        }

        ArrayList<Ingredient> ingredients = recipes.getRecipe(selected);
        StringBuilder sb = new StringBuilder("Recipe: " + selected + "\n\nIngredients:\n");

        for (Ingredient ing : ingredients) {
            sb.append("- ").append(ing.getName()).append(": ").append(ing.getQuantity()).append(" ").append(ing.getUnit()).append("\n");
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "Recipe Details", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addNewRecipe() {
        JDialog dialog = new JDialog(this, "Add New Recipe", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));

        JLabel nameLabel = new JLabel("Recipe Name:");
        JTextField nameField = new JTextField();

        JLabel ingLabel = new JLabel("Ingredients (name:quantity, name:quantity):");
        JTextArea ingArea = new JTextArea();

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
            if (parts.length == 2) {
                String name = parts[0].trim();
                double quantity = Double.parseDouble(parts[1].trim());
                ingredients.add(new Ingredient(name, quantity));
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
        }
    }
}