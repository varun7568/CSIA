import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Map;

public class ViewRecipesDialog extends JDialog {
    private Recipes recipes;
    private JComboBox<String> recipeComboBox;
    private JTextArea ingredientsArea;
    private JSpinner numPeopleSpinner;
    private Recipe selectedRecipe;

    public ViewRecipesDialog(JFrame parent, Recipes recipes, Stock stock) {
        super(parent, "View Recipes", true);
        this.recipes = recipes;
        setSize(600, 500);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);

        // Header Panel with controls
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        headerPanel.add(new JLabel("Select Recipe:"));
        recipeComboBox = new JComboBox<>(recipes.getDishNames().toArray(new String[0]));
        headerPanel.add(recipeComboBox);

        headerPanel.add(new JLabel("Num People:"));
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(1, 1, 100, 1);
        numPeopleSpinner = new JSpinner(spinnerModel);
        headerPanel.add(numPeopleSpinner);

        add(headerPanel, BorderLayout.NORTH);

        // Ingredients Display
        ingredientsArea = new JTextArea();
        ingredientsArea.setEditable(false);
        ingredientsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(ingredientsArea);
        add(scrollPane, BorderLayout.CENTER);

        // Add listeners
        recipeComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                updateIngredientsDisplay();
            }
        });

        numPeopleSpinner.addChangeListener(e -> updateIngredientsDisplay());

        // Initial display
        if (recipeComboBox.getSelectedItem() != null) {
            updateIngredientsDisplay();
        }

        setVisible(true);
    }

    private void updateIngredientsDisplay() {
        String selectedDishName = (String) recipeComboBox.getSelectedItem();
        if (selectedDishName != null) {
            selectedRecipe = recipes.getRecipe(selectedDishName);
            int numPeople = (int) numPeopleSpinner.getValue();

            if (selectedRecipe != null) {
                ingredientsArea.setText("Recipe: " + selectedRecipe.getDish().getName() + "\n\n");
                ingredientsArea.append(String.format("%-25s %s\n", "Ingredient", "Quantity (g)"));
                ingredientsArea.append("-------------------------------------------\n");

                for (Map.Entry<Ingredient, Integer> entry : selectedRecipe.getIngredients().entrySet()) {
                    String ingredientName = entry.getKey().getName();
                    int originalQuantity = entry.getValue();
                    int adjustedQuantity = originalQuantity * numPeople;
                    ingredientsArea.append(String.format("%-25s %d\n", ingredientName, adjustedQuantity));
                }
            }
        }
    }
}
