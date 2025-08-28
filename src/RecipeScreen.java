import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class RecipeScreen extends JFrame implements ActionListener {
    private JButton viewRecipesButton;
    private JButton uploadRecipesButton;
    private Recipes recipes;
    private Stock stock;

    public RecipeScreen(Recipes recipes, Stock stock) {
        this.recipes = recipes;
        this.stock = stock;

        setTitle("Recipes Screen");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 50, 50));

        viewRecipesButton = new JButton("View Recipes");
        uploadRecipesButton = new JButton("Upload Recipes");

        viewRecipesButton.setPreferredSize(new Dimension(250, 150));
        uploadRecipesButton.setPreferredSize(new Dimension(250, 150));

        viewRecipesButton.addActionListener(this);
        uploadRecipesButton.addActionListener(this);

        add(viewRecipesButton);
        add(uploadRecipesButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == viewRecipesButton) {
            new ViewRecipesDialog(this, recipes, stock);
        } else if (e.getSource() == uploadRecipesButton) {
            uploadRecipeFromFile();
        }
    }

    private void uploadRecipeFromFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (recipes.addRecipeFromFile(selectedFile)) { // This is a new method we'll add
                JOptionPane.showMessageDialog(this, "Recipe uploaded successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to upload recipe. Please check the file format.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}