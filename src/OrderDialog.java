import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderDialog extends JDialog implements ActionListener {
    private OrderManager orderManager;
    private CustomerManager customerManager;
    private Recipes recipes;

    private JTextField nameField, phoneField, addressField, dateField;
    private JComboBox<String> dishComboBox;
    private ArrayList<Dish> selectedDishes;
    private JTextArea orderSummary;

    public OrderDialog(JFrame parent, OrderManager om, CustomerManager cm, Recipes r) {
        super(parent, "New Order", true);
        this.orderManager = om;
        this.customerManager = cm;
        this.recipes = r;
        this.selectedDishes = new ArrayList<>();

        setSize(500, 600);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);

        // Main form panel
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        formPanel.add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);

        formPanel.add(new JLabel("Phone Number:"));
        phoneField = new JTextField();
        formPanel.add(phoneField);

        formPanel.add(new JLabel("Address:"));
        addressField = new JTextField();
        formPanel.add(addressField);

        formPanel.add(new JLabel("Dishes:"));
        dishComboBox = new JComboBox<>(recipes.getDishNames().toArray(new String[0])); // Requires a new method in Recipes
        formPanel.add(dishComboBox);

        formPanel.add(new JLabel("Completion Date (dd/MM/yyyy):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        add(formPanel, BorderLayout.CENTER);

        // Buttons and order summary
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        orderSummary = new JTextArea("Selected Dishes:\n");
        orderSummary.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(orderSummary);
        bottomPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Dish");
        JButton createButton = new JButton("Create Order");

        addButton.addActionListener(this);
        createButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(createButton);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Dish")) {
            String selectedDishName = (String) dishComboBox.getSelectedItem();
            if (selectedDishName != null) {
                // Add the selected dish to the list
                selectedDishes.add(new Dish(selectedDishName, 0.0)); // Price can be set later
                orderSummary.append("- " + selectedDishName + "\n");
            }
        } else if (e.getActionCommand().equals("Create Order")) {
            // Get customer info
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String dateStr = dateField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || selectedDishes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all customer details and select at least one dish.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create or get the customer
            Customer customer = customerManager.getCustomerByName(name);
            if (customer == null) {
                customer = new Customer(name, phone, address);
                customerManager.addCustomer(customer);
            }

            // Parse the date
            Date completionDate = null;
            try {
                completionDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateStr);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date format. Please use dd/MM/yyyy.", "Date Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Create the order
            Order newOrder = new Order(customer, selectedDishes, completionDate);
            orderManager.addOrder(newOrder);

            JOptionPane.showMessageDialog(this, "Order for " + name + " created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            dispose(); // Close the dialog
        }
    }
}