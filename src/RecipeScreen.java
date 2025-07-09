import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RecipeScreen extends JFrame implements ActionListener {
    private JButton selectOrder;
    private JButton viewRecipes;
    private JButton uploadRecipes;

    public RecipeScreen(){
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        selectOrder = new JButton("Customer Screen");
        selectOrder.setBounds(300, 20, 200, 30);
        selectOrder.setHorizontalAlignment(SwingConstants.LEFT);


    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
