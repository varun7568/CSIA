import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class NewOrderDialog extends JDialog {
    private JTextField dateField;
    private JList<String> dishesList;
    private JButton addCustomerBtn, createOrderBtn, cancelBtn;
    private CustomerManager customerManager;
    private DishManager dishManager;
    private OrderManager orderManager;
    private JComboBox<String> customerComboBox;

    public NewOrderDialog(JFrame parent, CustomerManager cm, DishManager dm, OrderManager om) {
        super(parent, "Create New Order", true);
        this.customerManager = cm;
        this.dishManager = dm;
        this.orderManager = om;

        setSize(500, 500);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Customer selection
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Customer:"), gbc);

        gbc.gridx = 1; gbc.gridy = 0;
        gbc.gridwidth = 2;
        customerComboBox = new JComboBox<>();
        updateCustomerComboBox();
        mainPanel.add(customerComboBox, gbc);

        gbc.gridx = 3; gbc.gridy = 0;
        gbc.gridwidth = 1;
        addCustomerBtn = new JButton("+");
        addCustomerBtn.setToolTipText("Add New Customer");
        addCustomerBtn.addActionListener(e -> {
            new CustomerInfo(customerManager).setVisible(true);
            updateCustomerComboBox();
        });
        mainPanel.add(addCustomerBtn, gbc);

        // Date selection
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Completion Date (dd/MM/yyyy):"), gbc);

        gbc.gridx = 1; gbc.gridy = 1;
        gbc.gridwidth = 3;
        dateField = new JTextField();
        mainPanel.add(dateField, gbc);

        // Dish selection
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 1;
        mainPanel.add(new JLabel("Select Dishes:"), gbc);

        gbc.gridx = 1; gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.BOTH;
        DefaultListModel<String> dishModel = new DefaultListModel<>();
        for (Dish dish : dishManager.getAllDishes()) {
            dishModel.addElement(dish.getName());
        }
        dishesList = new JList<>(dishModel);
        dishesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane dishScroll = new JScrollPane(dishesList);
        dishScroll.setPreferredSize(new Dimension(300, 150));
        mainPanel.add(dishScroll, gbc);

        // Button panel
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        createOrderBtn = new JButton("Create Order");
        createOrderBtn.addActionListener(e -> createOrder());

        cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        buttonPanel.add(createOrderBtn);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private void updateCustomerComboBox() {
        customerComboBox.removeAllItems();
        for (Customer customer : customerManager.getAllCustomers()) {
            customerComboBox.addItem(customer.getName());
        }
    }

    private void createOrder() {
        try {
            String customerName = (String) customerComboBox.getSelectedItem();
            if (customerName == null) {
                JOptionPane.showMessageDialog(this, "Please select a customer!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer customer = customerManager.getCustomerByName(customerName);
            if (customer == null) {
                JOptionPane.showMessageDialog(this, "Customer not found!");
                return;
            }

            if (dateField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a completion date!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Date completionDate = new SimpleDateFormat("dd/MM/yyyy").parse(dateField.getText());
            List<String> selectedDishNames = dishesList.getSelectedValuesList();

            if (selectedDishNames.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select at least one dish!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            ArrayList<Dish> dishes = new ArrayList<>();
            for (String dishName : selectedDishNames) {
                Dish dish = dishManager.getDishByName(dishName);
                if (dish != null) {
                    dishes.add(dish);
                }
            }

            Order newOrder = new Order(customer, dishes, completionDate);
            orderManager.addOrder(newOrder);

            JOptionPane.showMessageDialog(this, "Order created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Invalid date format! Please use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}