import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.function.BiConsumer;

public class Table extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    /**
     * @param columnNames    Table column headers
     * @param rowData        Table data
     * @param includeActions Whether to add an "Actions" column
     * @param actions        Map of button label -> callback (rowKey, actionLabel)
     */
    public Table(String[] columnNames, Object[][] rowData,
                 boolean includeActions,
                 Map<String, BiConsumer<String, String>> actions) {

        setLayout(new BorderLayout());

        if (includeActions && actions != null) {
            // Add "Actions" column
            String[] extendedColumns = Arrays.copyOf(columnNames, columnNames.length + 1);
            extendedColumns[columnNames.length] = "Actions";

            Object[][] newRowData = new Object[rowData.length][extendedColumns.length];
            for (int i = 0; i < rowData.length; i++) {
                System.arraycopy(rowData[i], 0, newRowData[i], 0, rowData[i].length);
                newRowData[i][extendedColumns.length - 1] = "Actions";
            }

            columnNames = extendedColumns;
            rowData = newRowData;
        }

        model = new DefaultTableModel(rowData, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return includeActions && column == getColumnCount() - 1;
            }
        };

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        if (includeActions && actions != null) {
            setupActionButtons(actions);
        }
    }

    private void setupActionButtons(Map<String, BiConsumer<String, String>> actions) {
        table.getColumn("Actions").setCellRenderer((tbl, value, isSelected, hasFocus, row, col) -> new JButton("Actions"));

        table.getColumn("Actions").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            private final JButton actionButton = new JButton("Actions");

            {
                actionButton.addActionListener(e -> {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        Object rowKeyObj = model.getValueAt(selectedRow, 0); // first col as identifier
                        String rowKey = String.valueOf(rowKeyObj); // safe conversion

                        // Show action choices
                        Object[] options = actions.keySet().toArray();
                        String choice = (String) JOptionPane.showInputDialog(
                                table,
                                "Choose an action for " + rowKey,
                                "Actions",
                                JOptionPane.PLAIN_MESSAGE,
                                null,
                                options,
                                options[0]
                        );

                        if (choice != null && actions.containsKey(choice)) {
                            actions.get(choice).accept(rowKey, choice);

                            // If delete was chosen, remove row from UI
                            if (choice.equalsIgnoreCase("Delete")) {
                                model.removeRow(selectedRow);
                            }
                        }
                    }
                });
            }

            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                return actionButton;
            }
        });
    }
}
