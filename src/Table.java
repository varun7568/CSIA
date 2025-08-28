import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Vector;

public class Table extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public Table(String[] columnNames, ArrayList<Object[]> rowData, boolean includeDeleteColumn, ActionListener deleteListener) {
        setLayout(new BorderLayout());

        Vector<String> columnVector = new Vector<>();
        for (String name : columnNames) {
            columnVector.add(name);
        }

        Vector<Vector<Object>> dataVector = new Vector<>();
        for (Object[] row : rowData) {
            Vector<Object> rowVector = new Vector<>();
            for (Object item : row) {
                rowVector.add(item);
            }
            dataVector.add(rowVector);
        }

        if (includeDeleteColumn) {
            columnVector.add("Delete");
            for (Vector<Object> row : dataVector) {
                row.add("Delete"); // Placeholder value for the button
            }
        }

        model = new DefaultTableModel(dataVector, columnVector) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return includeDeleteColumn && column == getColumnCount() - 1;
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        if (includeDeleteColumn) {
            setupDeleteButton(deleteListener);
        }
    }

    public JTable getTable() {
        return table;
    }

    private void setupDeleteButton(ActionListener deleteListener) {
        table.getColumn("Delete").setCellRenderer((table, value, isSelected, hasFocus, row, column) -> new JButton("Delete"));

        table.getColumn("Delete").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final JButton deleteButton = new JButton("Delete");
            private String nameToDelete;
            private int rowToDelete;

            {
                deleteButton.addActionListener(e -> {
                    fireEditingStopped(); // Stop editing the cell

                    rowToDelete = table.convertRowIndexToModel(table.getEditingRow());
                    nameToDelete = (String) model.getValueAt(rowToDelete, 0);

                    ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, nameToDelete);
                    deleteListener.actionPerformed(event);
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return deleteButton;
            }
        });
    }

    public void refreshData(ArrayList<Object[]> rowData) {
        model.setRowCount(0);

        for (Object[] row : rowData) {
            model.addRow(row);
        }
    }
}