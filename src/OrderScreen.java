import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class OrderScreen extends JFrame implements ActionListener {
    private JLabel labelOrders;
    private JButton newOrderButton;
    private JButton existingOrdersButton;
    private JScrollPane scrollPane;
    private OrderManager orderManager;
    private Table orderTablePanel;
    private boolean ordersViewVisible = false;
    private DishManager dishManager;
    private JButton backButton;

    public OrderScreen(OrderManager orderManager, DishManager dishManager) {
        this.orderManager = orderManager;
        this.dishManager = dishManager;

        setTitle("Order Screen");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
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

        backButton = new JButton("Back to Home");
        backButton.setBounds(600, 80, 150, 30);
        backButton.addActionListener(this);
        add(backButton);

        toggleExistingOrdersView(false);
    }

    private void toggleExistingOrdersView(boolean visible) {
        ordersViewVisible = visible;
        if (scrollPane != null) {
            scrollPane.setVisible(visible);
        }
        newOrderButton.setVisible(!visible);
        existingOrdersButton.setVisible(!visible);
        labelOrders.setText(visible ? "Order Overview" : "Order Screen");
    }

    private void loadOrdersIntoTable() {
        if (scrollPane != null) {
            remove(scrollPane);
        }

        String[] columnNames = {"Order ID", "Customer", "Date", "Status", "Dishes"};
        ArrayList<Order> orders = orderManager.getAllOrders(); // This should work now

        Object[][] data = new Object[orders.size()][5];
        for (int i = 0; i < orders.size(); i++) {
            Order o = orders.get(i);
            data[i][0] = o.getOrderID();
            data[i][1] = o.getCustomer().getName();
            data[i][2] = o.getCompletionDate() != null ? new SimpleDateFormat("dd/MM/yyyy").format(o.getCompletionDate()) : "N/A";
            data[i][3] = o.getStatus();
            data[i][4] = getDishNames(o.getDishes());
        }

        orderTablePanel = new Table(
                columnNames,
                data,
                true,
                Map.of(
                        "Delete", (id, action) -> {
                            orderManager.deleteOrder(Integer.parseInt(id));
                            loadOrdersIntoTable();
                        },
                        "Complete", (id, action) -> {
                            orderManager.completeOrder(Integer.parseInt(id));
                            loadOrdersIntoTable();
                        }
                )
        );

        scrollPane = new JScrollPane(orderTablePanel);
        scrollPane.setBounds(50, 130, 700, 400);
        scrollPane.setVisible(ordersViewVisible);
        add(scrollPane);

        revalidate();
        repaint();
    }

    private String getDishNames(ArrayList<Dish> dishes) {
        StringBuilder sb = new StringBuilder();
        for (Dish dish : dishes) {
            sb.append(dish.getName()).append(", ");
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 2) : "";
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newOrderButton) {
            NewOrderDialog dialog = new NewOrderDialog(this, new CustomerManager(), dishManager, orderManager);
            dialog.setVisible(true);
        } else if (e.getSource() == existingOrdersButton) {
            toggleExistingOrdersView(true);
            loadOrdersIntoTable();
        }
        if (e.getSource() == backButton) {
            this.dispose();
        }
    }
}