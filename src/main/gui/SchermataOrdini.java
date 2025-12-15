package src.main.gui;

import src.main.dao.OrdineDAO;
import src.main.dao.ProductsDAO; // ✅


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataOrdini extends JPanel {

    private JTable tableOrders;
    private DefaultTableModel ordersModel;

    public SchermataOrdini() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout());

        initComponents();
        loadOrders();
    }

    private void initComponents() {
        // Orders table
        String[] columns = {"ID", "Order Number", "Supplier ID", "Order Date", "Delivery Date", "Status", "Total", "Notes"};
        ordersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableOrders = new JTable(ordersModel);
        JScrollPane scroll = new JScrollPane(tableOrders);
        scroll.setBorder(BorderFactory.createTitledBorder("Ordini Fornitori"));
        add(scroll, BorderLayout.CENTER);

        // Buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAdd = new JButton("Nuovo Ordine");
        JButton btnReceive = new JButton("Ricevi Ordine");
        JButton btnRefresh = new JButton("Aggiorna");

        btnAdd.addActionListener(this::onAddOrder);
        btnReceive.addActionListener(this::onReceiveOrder);
        btnRefresh.addActionListener(e -> loadOrders());

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnReceive);
        buttonsPanel.add(btnRefresh);
        add(buttonsPanel, BorderLayout.NORTH);
    }

    private void loadOrders() {
        ordersModel.setRowCount(0);
        List<String[]> orders = OrdineDAO.getAllOrders();
        for (String[] o : orders) {
            ordersModel.addRow(o);
        }
    }

    private void onAddOrder(ActionEvent e) {
        FormOrdine form = new FormOrdine(null);
        form.setVisible(true);
        if (form.isSaved()) {
            loadOrders();
        }
    }

    private void onReceiveOrder(ActionEvent e) {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleziona un ordine da ricevere", "Attenzione", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String orderId = ordersModel.getValueAt(selectedRow, 0).toString();
        OrdineDAO.receiveOrder(orderId);
        JOptionPane.showMessageDialog(this, "Ordine ricevuto e giacenze aggiornate!", "Successo", JOptionPane.INFORMATION_MESSAGE);
        loadOrders();
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ordini Fornitori");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 600);
            frame.add(new SchermataOrdini());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
