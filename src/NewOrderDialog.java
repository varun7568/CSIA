import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewOrderDialog extends JDialog {
    private JTextField customerField, dateField;
    private JList<String> dishesList;
    private JButton addCustomerBtn, createOrderBtn;
    private CustomerManager customerManager;
    private Recipes recipes;
    private OrderManager orderManager;

    public NewOrderDialog(JFrame parent, CustomerManager cm, Recipes recipes, OrderManager om) {
        super(parent, "Create New Order", true);
        this.customerManager = cm;
        this.recipes = recipes;
        this.orderManager = om;

        setSize(500, 400);
        setLayout(new GridLayout(5, 2, 10, 10));
        setLocationRelativeTo(parent);

        // Customer selection
        add(new JLabel("Customer:"));
        customerField = new JTextField();
        add(customerField);

        addCustomerBtn = new JButton("Add New Customer");
        addCustomerBtn.addActionListener(e -> {
            new CustomerInfo(customerManager).setVisible(true);
            updateCustomerList();
        });
        add(addCustomerBtn);

        // Date selection
        add(new JLabel("Completion Date (dd/MM/yyyy):"));
        dateField = new JTextField();
        add(dateField);

        // Dish selection
        add(new JLabel("Select Dishes:"));
        DefaultListModel<String> dishModel = new DefaultListModel<>();
        for (String dish : recipes.getRecipeBook().keySet()) {
            dishModel.addElement(dish);
        }
        dishesList = new JList<>(dishModel);
        dishesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(dishesList));

        // Create order button
        createOrderBtn = new JButton("Create Order");
        createOrderBtn.addActionListener(e -> createOrder());
        add(createOrderBtn);

        setVisible(true);
    }

    private void createOrder() {
        try {
            String customerName = customerField.getText();
            Customer customer = customerManager.getCustomerByName(customerName);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found!");
                return;
            }

            Date completionDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
            List<String> selectedDishes = dishesList.getSelectedValuesList();

            ArrayList<Dish> dishes = new ArrayList<>();
            for (String dishName : selectedDishes) {
                dishes.add(new Dish(dishName, 0)); // Price not used in order
            }

            Order newOrder = new Order(customer, dishes, completionDate);
            orderManager.addOrder(newOrder);

            JOptionPane.showMessageDialog(this, "Order created successfully!");
            dispose();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format!");
        }
    }

    private void updateCustomerList() {
        // Could implement customer dropdown here
    }
}