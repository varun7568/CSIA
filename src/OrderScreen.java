import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OrderScreen extends JFrame implements ActionListener {
    private JButton viewOrders;
    private JButton addOrders;
    private OrderManager orderManager;
    private Recipes recipes;
    private CustomerManager customerManager;

    public OrderScreen() {
        this.orderManager = new OrderManager();
        this.customerManager = new CustomerManager();
        this.recipes = new Recipes();
        orderManager.loadOrders(customerManager, recipes);

        setTitle("Order Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
        setLayout(new FlowLayout());

        addOrders = new JButton("New Order");
        addOrders.setPreferredSize(new Dimension(200, 100));
        addOrders.addActionListener(this);

        viewOrders = new JButton("View Orders");
        viewOrders.setPreferredSize(new Dimension(200, 100));
        viewOrders.addActionListener(this);

        add(addOrders);
        add(viewOrders);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addOrders) {
            new OrderDialog(this, orderManager, customerManager, recipes);
        } else if (e.getSource() == viewOrders) {
            openOrdersView();
        }
    }

    public void openOrdersView() {
        // Hide the initial buttons
        addOrders.setVisible(false);
        viewOrders.setVisible(false);

        // Add a title
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(titleLabel, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();

        String[] columns = {"Order ID", "Customer Name", "Completion Date", "Status"};

        // Create lists of orders filtered by status
        ArrayList<Order> allOrders = orderManager.getOrders();
        ArrayList<Order> ongoingOrders = new ArrayList<>();
        ArrayList<Order> upcomingOrders = new ArrayList<>();
        ArrayList<Order> completedOrders = new ArrayList<>();

        for (Order order : allOrders) {
            if ("Ongoing".equalsIgnoreCase(order.getStatus())) {
                ongoingOrders.add(order);
            } else if ("Upcoming".equalsIgnoreCase(order.getStatus())) {
                upcomingOrders.add(order);
            } else if ("Completed".equalsIgnoreCase(order.getStatus())) {
                completedOrders.add(order);
            }
        }

        // Convert the order lists to Object arrays for the Table class
        ArrayList<Object[]> ongoingData = convertOrdersToRows(ongoingOrders);
        ArrayList<Object[]> upcomingData = convertOrdersToRows(upcomingOrders);
        ArrayList<Object[]> completedData = convertOrdersToRows(completedOrders);

        // Create the tables for each status
        tabbedPane.add("Ongoing", new Table(columns, ongoingData, true, e -> {
            // Implement delete logic here if needed
            JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.");
        }));
        tabbedPane.add("Upcoming", new Table(columns, upcomingData, true, e -> {
            JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.");
        }));
        tabbedPane.add("Completed", new Table(columns, completedData, true, e -> {
            JOptionPane.showMessageDialog(this, "Delete functionality to be implemented.");
        }));

        add(tabbedPane, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private ArrayList<Object[]> convertOrdersToRows(ArrayList<Order> orders) {
        ArrayList<Object[]> rows = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Order order : orders) {
            rows.add(new Object[]{
                    order.getOrderID(),
                    order.getCustomer().getName(),
                    sdf.format(order.getCompletionDate()),
                    order.getStatus()
            });
        }
        return rows;
    }
}