import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerInfo extends JDialog implements ActionListener {
    private JTextField textName;
    private JTextField textNum;
    private JTextField textAddress;
    private JButton createButton;

    private CustomerManager customerManager;

    public CustomerInfo(CustomerManager cm){
        super((JFrame)null, "Enter Customer Info", true);
        this.customerManager = cm;

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

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(50, 190, 150, 25);
        add(addressLabel);
        textAddress = new JTextField();
        textAddress.setBounds(50, 220, 250, 30);
        add(textAddress);

        createButton = new JButton("Add Customer");
        createButton.setBounds(120, 270, 150, 30);
        createButton.addActionListener(this);
        add(createButton);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == createButton) {
            String name = textName.getText().trim();
            String phoneNum = textNum.getText().trim();
            String address = textAddress.getText().trim();

            if (name.isEmpty() || phoneNum.isEmpty() || address.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer newCustomer = new Customer(name, phoneNum, address);
            customerManager.addCustomer(newCustomer);

            JOptionPane.showMessageDialog(this, "Customer '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            textName.setText("");
            textNum.setText("");
            textAddress.setText("");
            this.dispose();
        }
    }
}