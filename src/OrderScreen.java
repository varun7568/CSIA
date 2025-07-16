import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OrderScreen extends JFrame implements ActionListener{
    private JLabel labelOutput;
    private JButton viewOrders;
    private JButton addOrders;
    private OrderManager orderManager;
    private Recipes recipes;
    private CustomerManager customerManager;
    private JTextField textName;
    private JTextField textNum;
    private JTextField textOrder;
    private JButton createButton;

    public OrderScreen(OrderManager orderManager){
        this.orderManager = new OrderManager();
        this.customerManager = new CustomerManager();
        this.recipes = new Recipes();
        orderManager.loadOrders(customerManager, recipes);setTitle("Order Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // quit the app when we close the window
        setSize(600, 400);
        setLayout(null);
        labelOutput = new JLabel("Order Screen");
        labelOutput.setBounds(150, 50, 150, 30);

        viewOrders = new JButton("View Orders");
        viewOrders.setBounds(80, 80, 180, 100);
        viewOrders.addActionListener(this);

        addOrders = new JButton("New Orders");
        addOrders.setBounds(80, 200, 180, 100);
        addOrders.addActionListener(this);

        add(labelOutput);
        add(viewOrders);
        add(addOrders);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("View Orders")) {
            openOrders();
        } if(e.getActionCommand().equals("New Orders")){
            viewOrders.setVisible(false);
            addOrders.setVisible(false);
            addOrder();
            System.out.println("Opening New Orders");
        } if(e.getSource() == createButton) {
            String name = textName.getText().trim();
            String phoneNum = textNum.getText().trim();
            String address = textOrder.getText().trim();

            if (name.isEmpty() || phoneNum.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer newCustomer = new Customer(name, phoneNum, address);
            customerManager.addCustomer(newCustomer);

            JOptionPane.showMessageDialog(this, "Customer '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            textName.setText("");
            textNum.setText("");
            textOrder.setText("");
            this.dispose();
        }
    }

    public void openOrders(){
        viewOrders.setVisible(false);
        addOrders.setVisible(false);
        labelOutput.setVisible(false);
        setTitle("Order Screen");
        setSize(600, 400);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        //sort orders by completion date

        String[] columns = {"Order ID", "Customer", "Date", "Status"};
        Object[][] ongoingOrders = {
                {"001", "", "", "Ongoing"},
                {"002", "", "", "Ongoing"}
                ///need to write from orders textfile
        };
        Object[][] previousOrders = {
                {"003", "", "", "Completed"},
                {"004", "", "", "Completed"}
        };
        Object[][] upcomingOrders = {
                {"005", "", "", "Upcoming"},
                {"006", "", "", "Upcoming"}
        };

        tabbedPane.add("Ongoing", new Table(columns, ongoingOrders, false));
        tabbedPane.add("Previous", new Table(columns, previousOrders, false));
        tabbedPane.add("Upcoming", new Table(columns, upcomingOrders, false));

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    public void addOrder(){
        CustomerManager cm = new CustomerManager();

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel nameLabel = new JLabel("Customer Name:");
        nameLabel.setBounds(50, 50, 150, 25);
        add(nameLabel);
        textName = new JTextField();
        textName.setBounds(50, 80, 250, 30);
        add(textName);

        JLabel numLabel = new JLabel("Phone Number:");
        numLabel.setBounds(50, 120, 150, 25);
        add(numLabel);
        textNum = new JTextField();
        textNum.setBounds(50, 150, 250, 30);
        add(textNum);

        JLabel orderLabel = new JLabel("Order:");
        orderLabel.setBounds(50, 190, 150, 25);
        add(orderLabel);
        textOrder = new JTextField();
        textOrder.setBounds(50, 220, 250, 30);
        add(textOrder);

        createButton = new JButton("Add Customer");
        createButton.setBounds(120, 270, 150, 30);
        createButton.addActionListener(this);
        add(createButton);

    }

}