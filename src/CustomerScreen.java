import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
        setLayout(null);

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

        scrollPane = new JScrollPane(); // placeholder until loaded

        add(labelCustomer);
        add(newCustomersButton);
        add(existingCustomersButton);
        add(textSearch);
        add(searchButton);
        add(showAllButton);
        add(scrollPane);

        toggleExistingCustomersView(false);
        setVisible(true);
    }

    private void toggleExistingCustomersView(boolean visible) {
        textSearch.setVisible(visible);
        searchButton.setVisible(visible);
        showAllButton.setVisible(visible);
        scrollPane.setVisible(visible);
        newCustomersButton.setVisible(!visible);
        existingCustomersButton.setVisible(!visible);
        labelCustomer.setText(visible ? "Customer Overview" : "Customer Screen");
    }

    private void loadCustomersIntoTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columnNames = {"Name", "Phone Number", "Address", "Orders"};

        ArrayList<Object[]> rowData = new ArrayList<>();
        for (Customer c : customerManager.getAllCustomers()) {
            rowData.add(new Object[]{
                    c.getName(),
                    c.getPhoneNum(),
                    c.getAddress(),
                    String.join(", ", c.getOrders())
            });
        }

        customerTablePanel = new Table(columnNames, rowData, true, e -> {
            String nameToDelete = e.getActionCommand();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete customer '" + nameToDelete + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                customerManager.deleteCustomer(nameToDelete);
                loadCustomersIntoTable();
            }
        });

        scrollPane = new JScrollPane(customerTablePanel);
        scrollPane.setBounds(50, 180, 700, 350);
        add(scrollPane);
        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New Customers")) {
            System.out.println("Opening new customers dialog");
            CustomerInfo customerInfoDialog = new CustomerInfo(customerManager);
            customerInfoDialog.setVisible(true);
            loadCustomersIntoTable();
            toggleExistingCustomersView(false);

        } else if (e.getActionCommand().equals("Existing Customers")) {
            System.out.println("Displaying existing customers");
            toggleExistingCustomersView(true);
            loadCustomersIntoTable();
            textSearch.setText("Enter Customer Name to Search");

        } else if (e.getSource() == searchButton || e.getSource() == textSearch) {
            String searchTerm = textSearch.getText().trim();
            if (searchTerm.isEmpty() || searchTerm.equals("Enter Customer Name to Search")) {
                JOptionPane.showMessageDialog(this, "Please enter a name to search.", "Search Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int index = customerManager.binarySearchCustomer(searchTerm);
            Customer foundCustomer = customerManager.getCustomerByName(searchTerm);

            if (index != -1 && foundCustomer != null) {
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
