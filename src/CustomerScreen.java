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

    public CustomerScreen() {
        customerManager = new CustomerManager();

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

        toggleExistingCustomersView(false);
    }

    private void toggleExistingCustomersView(boolean visible) {
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

        // ✅ unified table with Delete + Edit actions
        customerTablePanel = new Table(
                columnNames,
                data,
                true,
                Map.of(
                        "Delete", (name, action) -> {
                            customerManager.deleteCustomer(name);
                        },
                        "Edit", (name, action) -> {
                            Customer c = customerManager.getCustomerByName(name);
                            if (c != null) {
                                JOptionPane.showMessageDialog(this,
                                        "Edit feature not implemented yet for:\n"
                                                + "Name: " + c.getName() + "\n"
                                                + "Phone: " + c.getPhoneNum() + "\n"
                                                + "Address: " + c.getAddress(),
                                        "Edit Customer",
                                        JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                )
        );

        scrollPane = new JScrollPane(customerTablePanel);
        scrollPane.setBounds(50, 180, 700, 350);
        add(scrollPane);

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newCustomersButton) {
            CustomerInfo customerInfoDialog = new CustomerInfo(customerManager);
            customerInfoDialog.setVisible(true);
            loadCustomersIntoTable();

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
    }
}
