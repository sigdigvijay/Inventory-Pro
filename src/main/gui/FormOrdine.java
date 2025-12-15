package src.main.gui;

import src.main.dao.OrdineDAO;
import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormOrdine extends JDialog {

    private JTextField tfOrderNumber, tfSupplierId, tfDeliveryDate;
    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private JButton btnAddProduct, btnRemoveProduct, btnSave, btnCancel;

    private boolean saved = false;

    public FormOrdine(Frame parent) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("Nuovo Ordine");
        setSize(700, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new GridLayout(1,3,10,10));
        tfOrderNumber = new JTextField();
        tfOrderNumber.setText("ORD-" + System.currentTimeMillis());
        tfSupplierId = new JTextField();
        tfDeliveryDate = new JTextField("YYYY-MM-DD");

        topPanel.add(labeledPanel("Numero Ordine:", tfOrderNumber));
        topPanel.add(labeledPanel("ID Fornitore:", tfSupplierId));
        topPanel.add(labeledPanel("Data Consegna:", tfDeliveryDate));
        add(topPanel, BorderLayout.NORTH);

        // Products table
        String[] columns = {"Product ID", "Product Name", "Quantity", "Unit Price"};
        tableModel = new DefaultTableModel(columns, 0);
        tableProducts = new JTable(tableModel);
        add(new JScrollPane(tableProducts), BorderLayout.CENTER);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAddProduct = new JButton("Aggiungi Prodotto");
        btnRemoveProduct = new JButton("Rimuovi Prodotto");
        btnSave = new JButton("Salva Ordine");
        btnCancel = new JButton("Annulla");

        btnAddProduct.addActionListener(this::onAddProduct);
        btnRemoveProduct.addActionListener(this::onRemoveProduct);
        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> dispose());

        buttonsPanel.add(btnAddProduct);
        buttonsPanel.add(btnRemoveProduct);
        buttonsPanel.add(btnSave);
        buttonsPanel.add(btnCancel);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    private JPanel labeledPanel(String label, JComponent comp) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(label), BorderLayout.NORTH);
        panel.add(comp, BorderLayout.CENTER);
        return panel;
    }

    private void onAddProduct(ActionEvent e) {
        String productId = JOptionPane.showInputDialog(this, "Inserisci Product ID:");
        if (productId == null || productId.isEmpty()) return;

        String[] product = ProductsDAO.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(this, "Prodotto non trovato!", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Quantità:");
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Quantità non valida", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String priceStr = JOptionPane.showInputDialog(this, "Prezzo unitario:");
        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Prezzo non valido", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{productId, product[2], qty, price});
    }

    private void onRemoveProduct(ActionEvent e) {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow != -1) {
            tableModel.removeRow(selectedRow);
        }
    }

    private void onSave(ActionEvent e) {
        String orderNumber = tfOrderNumber.getText().trim();
        String supplierId = tfSupplierId.getText().trim();
        String deliveryDate = tfDeliveryDate.getText().trim();

        if (orderNumber.isEmpty() || supplierId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Numero ordine e ID fornitore sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String orderId = OrdineDAO.generateId();
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            double price = Double.parseDouble(tableModel.getValueAt(i, 3).toString());
            total += qty * price;
        }

        String[] orderData = new String[]{
                orderId, orderNumber, supplierId, String.valueOf(System.currentTimeMillis()),
                deliveryDate, "in_preparazione", String.valueOf(total), ""
        };
        OrdineDAO.addOrder(orderData);

        // Add order lines
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String lineId = OrdineDAO.generateLineId();
            String productId = tableModel.getValueAt(i, 0).toString();
            int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            double price = Double.parseDouble(tableModel.getValueAt(i, 3).toString());

            String[] lineData = new String[]{lineId, orderId, productId, String.valueOf(qty), String.valueOf(price)};
            OrdineDAO.addOrderLine(lineData);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormOrdine form = new FormOrdine(null);
            form.setVisible(true);
        });
    }
}
