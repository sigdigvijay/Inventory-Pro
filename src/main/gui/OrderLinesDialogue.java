package src.main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import src.main.dao.OrdineDAO;

public class OrderLinesDialogue extends JDialog {

    private JTable table;
    private DefaultTableModel tableModel;
    private String orderId;

    public OrderLinesDialogue(JFrame parent, String orderId) {
        super(parent, "Righe Ordine", true);
        this.orderId = orderId;

        initComponents();
        loadOrderLines();

        setSize(600, 400);
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new String[]{"Line ID", "Product ID", "Quantity", "Unit Price"});

        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton closeButton = new JButton("Chiudi");
        closeButton.addActionListener(e -> dispose());

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(closeButton, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private void loadOrderLines() {
        // Get only lines for this order
        List<String[]> lines = OrdineDAO.getLinesByOrderId(orderId);

        tableModel.setRowCount(0); // clear existing rows
        for (String[] line : lines) {
            tableModel.addRow(new Object[]{
                    line[0], // line ID
                    line[1], // product ID
                    line[2], // quantity
                    line[3]  // unit price
            });
        }
    }
}
