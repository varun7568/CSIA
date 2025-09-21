import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class CustomerScreen extends JFrame implements ActionListener {
    private JLabel labelCustomer;
    private JButton newCustomersButton;
    private JButton existingCustomersButton;
    private JTextField textSearch;
    private JButton searchButton;
    private JButton showAllButton;
    private JScrollPane scrollPane;
    private CustomerManager customerManager;
    private Table customerTablePanel;
    private boolean customersViewVisible = false;
    private JButton backButton;

    public CustomerScreen() {
        customerManager = new CustomerManager();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setTitle("Customer Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);

        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        labelCustomer = new JLabel("Customer Screen");
        labelCustomer.setBounds(300, 20, 200, 30);
        labelCustomer.setHorizontalAlignment(SwingConstants.CENTER);
        labelCustomer.setFont(new Font("Arial", Font.BOLD, 20));

        newCustomersButton = new JButton("New Customers");
        newCustomersButton.setBounds(50, 80, 150, 30);
        newCustomersButton.addActionListener(this);

        existingCustomersButton = new JButton("Existing Customers");
        existingCustomersButton.setBounds(210, 80, 180, 30);
        existingCustomersButton.addActionListener(this);

        textSearch = new JTextField("Enter Customer Name to Search");
        textSearch.setBounds(50, 130, 250, 30);
        textSearch.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textSearch.getText().equals("Enter Customer Name to Search")) {
                    textSearch.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textSearch.getText().isEmpty()) {
                    textSearch.setText("Enter Customer Name to Search");
                }
            }
        });
        textSearch.addActionListener(this);

        searchButton = new JButton("Search");
        searchButton.setBounds(310, 130, 100, 30);
        searchButton.addActionListener(this);

        showAllButton = new JButton("Show All");
        showAllButton.setBounds(420, 130, 100, 30);
        showAllButton.addActionListener(this);

        add(labelCustomer);
        add(newCustomersButton);
        add(existingCustomersButton);
        add(textSearch);
        add(searchButton);
        add(showAllButton);

        backButton = new JButton("Back to Home");
        backButton.setBounds(600, 80, 150, 30);
        backButton.addActionListener(this);
        add(backButton);

        toggleExistingCustomersView(false);
    }

    private void toggleExistingCustomersView(boolean visible) {
        customersViewVisible = visible;
        textSearch.setVisible(visible);
        searchButton.setVisible(visible);
        showAllButton.setVisible(visible);
        if (scrollPane != null) scrollPane.setVisible(visible);
        newCustomersButton.setVisible(!visible);
        existingCustomersButton.setVisible(!visible);
        labelCustomer.setText(visible ? "Customer Overview" : "Customer Screen");
    }

    private void loadCustomersIntoTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columnNames = {"Name", "Phone Number", "Address", "Orders"};
        ArrayList<Customer> customers = customerManager.getAllCustomers();

        Object[][] data = new Object[customers.size()][4];
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getName();
            data[i][1] = c.getPhoneNum();
            data[i][2] = c.getAddress();
            data[i][3] = String.join(", ", c.getOrders());
        }

        customerTablePanel = new Table(
                columnNames,
                data,
                true,
                Map.of(
                        "Delete", (name, action) -> {
                            customerManager.deleteCustomer(name);
                            loadCustomersIntoTable();
                        },
                        "Edit", (name, action) -> {
                            Customer c = customerManager.getCustomerByName(name);
                            if (c != null) {
                                showEditCustomerDialog(c);
                            }
                        }
                )
        );

        scrollPane = new JScrollPane(customerTablePanel);
        scrollPane.setBounds(50, 180, 700, 350);
        scrollPane.setVisible(customersViewVisible);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private void showEditCustomerDialog(Customer customer) {
        JDialog dialog = new JDialog(this, "Edit Customer", true);
        dialog.setSize(400, 350);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField(customer.getName());

        JLabel phoneLabel = new JLabel("Phone:");
        JTextField phoneField = new JTextField(customer.getPhoneNum());

        JLabel addressLabel = new JLabel("Address:");
        JTextField addressField = new JTextField(customer.getAddress());

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        dialog.add(nameLabel);
        dialog.add(nameField);
        dialog.add(phoneLabel);
        dialog.add(phoneField);
        dialog.add(addressLabel);
        dialog.add(addressField);
        dialog.add(saveButton);
        dialog.add(cancelButton);

        saveButton.addActionListener(e -> {
            String newName = nameField.getText().trim();
            String newPhone = phoneField.getText().trim();
            String newAddress = addressField.getText().trim();

            if (newName.isEmpty() || newPhone.isEmpty() || newAddress.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required.");
                return;
            }

            // Check if any changes were made
            if (newName.equals(customer.getName()) &&
                    newPhone.equals(customer.getPhoneNum()) &&
                    newAddress.equals(customer.getAddress())) {
                JOptionPane.showMessageDialog(dialog, "No changes were made.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Update customer details
            customerManager.deleteCustomer(customer.getName());
            Customer updatedCustomer = new Customer(newName, newPhone, newAddress);
            for (String order : customer.getOrders()) {
                updatedCustomer.addOrder(order);
            }
            customerManager.addCustomer(updatedCustomer);

            JOptionPane.showMessageDialog(dialog, "Customer updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dialog.dispose();
            loadCustomersIntoTable();
        });

        cancelButton.addActionListener(e -> dialog.dispose());
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newCustomersButton) {
            CustomerInfo customerInfoDialog = new CustomerInfo(customerManager);
            customerInfoDialog.setVisible(true);
            // Don't automatically show customers table after dialog closes

        } else if (e.getSource() == existingCustomersButton) {
            toggleExistingCustomersView(true);
            loadCustomersIntoTable();
            textSearch.setText("Enter Customer Name to Search");

        } else if (e.getSource() == searchButton || e.getSource() == textSearch) {
            String searchTerm = textSearch.getText().trim();
            if (searchTerm.isEmpty() || searchTerm.equals("Enter Customer Name to Search")) {
                JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Search Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Customer foundCustomer = customerManager.getCustomerByName(searchTerm);
            if (foundCustomer != null) {
                JOptionPane.showMessageDialog(this,
                        "Customer Found:\nName: " + foundCustomer.getName() +
                                "\nPhone: " + foundCustomer.getPhoneNum() +
                                "\nAddress: " + foundCustomer.getAddress() +
                                "\nOrders: " + String.join(", ", foundCustomer.getOrders()),
                        "Customer Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Customer '" + searchTerm + "' not found.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            }

        } else if (e.getSource() == showAllButton) {
            textSearch.setText("Enter Customer Name to Search");
            loadCustomersIntoTable();
        }
        if (e.getSource() == backButton) {
            this.dispose();
        }
    }
}