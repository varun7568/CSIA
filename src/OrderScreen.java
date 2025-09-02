import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;

public class OrderScreen extends JFrame implements ActionListener {
    private JLabel labelOrders;
    private JButton newOrderButton;
    private JButton existingOrdersButton;
    private JScrollPane scrollPane;
    private OrderManager orderManager;
    private Table orderTablePanel;

    public OrderScreen(OrderManager orderManager) {
        this.orderManager = orderManager;

        setTitle("Order Screen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(null);

        setupUI();
        setVisible(true);
    }

    private void setupUI() {
        labelOrders = new JLabel("Order Screen");
        labelOrders.setBounds(300, 20, 200, 30);
        labelOrders.setHorizontalAlignment(SwingConstants.CENTER);
        labelOrders.setFont(new Font("Arial", Font.BOLD, 20));

        newOrderButton = new JButton("New Order");
        newOrderButton.setBounds(50, 80, 150, 30);
        newOrderButton.addActionListener(this);

        existingOrdersButton = new JButton("Existing Orders");
        existingOrdersButton.setBounds(210, 80, 180, 30);
        existingOrdersButton.addActionListener(this);

        add(labelOrders);
        add(newOrderButton);
        add(existingOrdersButton);

        toggleExistingOrdersView(false);
    }

    private void toggleExistingOrdersView(boolean visible) {
        if (scrollPane != null) scrollPane.setVisible(visible);
        newOrderButton.setVisible(!visible);
        existingOrdersButton.setVisible(!visible);
        labelOrders.setText(visible ? "Order Overview" : "Order Screen");
    }

    private void loadOrdersIntoTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columnNames = {"Order ID", "Customer", "Date", "Status", "Dishes"};
        ArrayList<Order> orders = orderManager.getOrdersByStatus("Upcoming"); // example

        Object[][] data = new Object[orders.size()][5];
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            data[i][0] = o.getOrderID();
            data[i][1] = o.getCustomer().getName();
            data[i][2] = o.getCompletionDate() != null ? o.getCompletionDate().toString() : "N/A";
            data[i][3] = o.getStatus();
            data[i][4] = o.getDishes().toString();
        }

        orderTablePanel = new Table(
                columnNames,
                data,
                true,
                Map.of(
                        "Delete", (id, action) -> {
                            orderManager.deleteOrder(Integer.parseInt(id));
                        },
                        "Edit", (id, action) -> {
                            JOptionPane.showMessageDialog(this,
                                    "Edit feature not implemented yet for Order ID: " + id,
                                    "Edit Order",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                )
        );

        scrollPane = new JScrollPane(orderTablePanel);
        scrollPane.setBounds(50, 130, 700, 400);
        add(scrollPane);

        revalidate();
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newOrderButton) {
            JOptionPane.showMessageDialog(this, "New Order dialog would open here.");
            loadOrdersIntoTable();

        } else if (e.getSource() == existingOrdersButton) {
            toggleExistingOrdersView(true);
            loadOrdersIntoTable();
        }
    }
}