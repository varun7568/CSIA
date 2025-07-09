import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Table extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private CustomerManager customerManager; // needed to trigger delete

    public Table(String[] columnNames, ArrayList<Customer> allCustomers, boolean includeDeleteColumn, CustomerManager customerManager) {
        this.customerManager = customerManager;
        setLayout(new BorderLayout());

        // Add Delete column if needed
        if (includeDeleteColumn) {
            String[] extendedColumns = new String[columnNames.length + 1];
            System.arraycopy(columnNames, 0, extendedColumns, 0, columnNames.length);
            extendedColumns[columnNames.length] = "Delete";
            columnNames = extendedColumns;
        }

        // Convert data
        Object[][] rowData = new Object[allCustomers.size()][columnNames.length];
        for (int i = 0; i < allCustomers.size(); i++) {
            Customer c = allCustomers.get(i);
            rowData[i][0] = c.getName();
            rowData[i][1] = c.getPhoneNum();
            rowData[i][2] = c.getAddress();
            rowData[i][3] = String.join(", ", c.getOrders());
            if (includeDeleteColumn) {
                rowData[i][4] = "Delete";
            }
        }

        model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return includeDeleteColumn && column == getColumnCount() - 1;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        if (includeDeleteColumn) {
            setupDeleteButton(); // Activate delete logic
        }
    }

    private void setupDeleteButton() {
        table.getColumn("Delete").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> new JButton("Delete"));

        table.getColumn("Delete").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final JButton deleteButton = new JButton("Delete");

            {
                deleteButton.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = (String) model.getValueAt(selectedRow, 0); // Column 0 = Name
                        int confirm = JOptionPane.showConfirmDialog(table,
                                "Delete customer '" + name + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            customerManager.deleteCustomer(name);
                            model.removeRow(selectedRow);
                        }
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return deleteButton;
            }
        });
    }

    public Table(String[] columnNames, Object[][] rowData, boolean includeDeleteColumn) {
        setLayout(new BorderLayout());

        // If Delete column is required, add it
        if (includeDeleteColumn) {
            String[] extendedColumns = new String[columnNames.length + 1];
            System.arraycopy(columnNames, 0, extendedColumns, 0, columnNames.length);
            extendedColumns[columnNames.length] = "Delete";
            columnNames = extendedColumns;

            Object[][] newRowData = new Object[rowData.length][columnNames.length];
            for (int i = 0; i < rowData.length; i++) {
                System.arraycopy(rowData[i], 0, newRowData[i], 0, rowData[i].length);
                newRowData[i][columnNames.length - 1] = "Delete";
            }
            rowData = newRowData;
        }

        model = new DefaultTableModel(rowData, columnNames) {
            public boolean isCellEditable(int row, int column) {
                if (includeDeleteColumn && column == getColumnCount() - 1) return true;
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        if (includeDeleteColumn) {
            setupDeleteButton2();
        }
    }

    private void setupDeleteButton2() {
        table.getColumn("Delete").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            return new JButton("Delete");
        });

        table.getColumn("Delete").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final JButton deleteButton = new JButton("Delete");

            {
                deleteButton.addActionListener(e -> {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        model.removeRow(row);
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return deleteButton;
            }
        });
    }
}