package src.main.gui;

import src.main.dao.OrdineDAO;
import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormOrdine extends JDialog {

    private JTextField tfOrderNumber, tfSupplierId, tfDeliveryDate;
    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private JButton btnAddProduct, btnRemoveProduct, btnSave, btnCancel;
    private JLabel lblTotal;

    private boolean saved = false;

    public FormOrdine(Frame parent) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("Nuovo Ordine Fornitore");
        setSize(900, 650);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(30, 30, 30));

        initComponents();
    }

    private void initComponents() {
        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout(15, 15));
        mainContainer.setBackground(new Color(30, 30, 30));
        mainContainer.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 40, 40));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Dettagli Ordine");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(220, 220, 220));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.NORTH);

        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        fieldsPanel.setOpaque(false);

        tfOrderNumber = createStyledTextField();
        tfOrderNumber.setText("ORD-" + System.currentTimeMillis());
        tfOrderNumber.setEditable(false);
        tfOrderNumber.setBackground(new Color(45, 45, 45));

        tfSupplierId = createStyledTextField();
        tfDeliveryDate = createStyledTextField();
        tfDeliveryDate.setText("YYYY-MM-DD");
        tfDeliveryDate.setForeground(new Color(150, 150, 150));
        tfDeliveryDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (tfDeliveryDate.getText().equals("YYYY-MM-DD")) {
                    tfDeliveryDate.setText("");
                    tfDeliveryDate.setForeground(Color.WHITE);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (tfDeliveryDate.getText().isEmpty()) {
                    tfDeliveryDate.setText("YYYY-MM-DD");
                    tfDeliveryDate.setForeground(new Color(150, 150, 150));
                }
            }
        });

        fieldsPanel.add(createLabeledPanel("Numero Ordine", tfOrderNumber));
        fieldsPanel.add(createLabeledPanel("ID Fornitore", tfSupplierId));
        fieldsPanel.add(createLabeledPanel("Data Consegna", tfDeliveryDate));

        headerPanel.add(fieldsPanel, BorderLayout.CENTER);
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Products section
        JPanel productsPanel = new JPanel(new BorderLayout(0, 10));
        productsPanel.setBackground(new Color(30, 30, 30));

        JLabel productsLabel = new JLabel("Prodotti nell'ordine");
        productsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        productsLabel.setForeground(new Color(220, 220, 220));
        productsPanel.add(productsLabel, BorderLayout.NORTH);

        // Products table
        String[] columns = {"ID Prodotto", "Nome Prodotto", "Quantità", "Prezzo Unitario", "Subtotale"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProducts = new JTable(tableModel);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableProducts.setRowHeight(40);
        tableProducts.setFillsViewportHeight(true);
        tableProducts.setBackground(new Color(45, 45, 45));
        tableProducts.setForeground(new Color(230, 230, 230));
        tableProducts.setGridColor(new Color(60, 60, 60));
        tableProducts.setSelectionBackground(new Color(0, 120, 215));
        tableProducts.setSelectionForeground(Color.WHITE);
        tableProducts.setShowVerticalLines(true);
        tableProducts.setShowHorizontalLines(true);

        // Center alignment for all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableProducts.getColumnCount(); i++) {
            tableProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Column widths
        tableProducts.getColumnModel().getColumn(0).setPreferredWidth(100);
        tableProducts.getColumnModel().getColumn(1).setPreferredWidth(250);
        tableProducts.getColumnModel().getColumn(2).setPreferredWidth(80);
        tableProducts.getColumnModel().getColumn(3).setPreferredWidth(120);
        tableProducts.getColumnModel().getColumn(4).setPreferredWidth(120);

        JTableHeader header = tableProducts.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(tableProducts);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));

        productsPanel.add(scrollPane, BorderLayout.CENTER);

        // Table action buttons
        JPanel tableButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tableButtonsPanel.setBackground(new Color(30, 30, 30));

        btnAddProduct = createStyledButton("Aggiungi Prodotto", new Color(34, 139, 34), new Color(28, 115, 28));
        btnRemoveProduct = createStyledButton("Rimuovi Prodotto", new Color(220, 53, 69), new Color(180, 40, 55));

        btnAddProduct.addActionListener(this::onAddProduct);
        btnRemoveProduct.addActionListener(this::onRemoveProduct);

        tableButtonsPanel.add(btnAddProduct);
        tableButtonsPanel.add(btnRemoveProduct);

        productsPanel.add(tableButtonsPanel, BorderLayout.SOUTH);

        mainContainer.add(productsPanel, BorderLayout.CENTER);

        // Bottom panel with total and action buttons
        JPanel bottomPanel = new JPanel(new BorderLayout(15, 0));
        bottomPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 70, 70)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Total panel
        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        totalPanel.setOpaque(false);

        JLabel lblTotalLabel = new JLabel("Totale Ordine:");
        lblTotalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalLabel.setForeground(new Color(220, 220, 220));

        lblTotal = new JLabel("0.00 €");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTotal.setForeground(new Color(0, 200, 100));

        totalPanel.add(lblTotalLabel);
        totalPanel.add(lblTotal);

        // Action buttons panel
        JPanel actionButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionButtonsPanel.setOpaque(false);

        btnSave = createStyledButton("Salva Ordine", new Color(0, 120, 215), new Color(0, 100, 180));
        btnCancel = createStyledButton("Annulla", new Color(100, 100, 100), new Color(80, 80, 80));

        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> dispose());

        actionButtonsPanel.add(btnCancel);
        actionButtonsPanel.add(btnSave);

        bottomPanel.add(totalPanel, BorderLayout.WEST);
        bottomPanel.add(actionButtonsPanel, BorderLayout.EAST);

        mainContainer.add(bottomPanel, BorderLayout.SOUTH);

        add(mainContainer);
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textField.setBackground(new Color(50, 50, 50));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        return textField;
    }

    private JPanel createLabeledPanel(String labelText, JComponent component) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(new Color(200, 200, 200));

        panel.add(label, BorderLayout.NORTH);
        panel.add(component, BorderLayout.CENTER);

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void onAddProduct(ActionEvent e) {
        String productId = JOptionPane.showInputDialog(this, "Inserisci Product ID:");
        if (productId == null || productId.isEmpty()) return;

        String[] product = ProductsDAO.getProductById(productId);
        if (product == null) {
            JOptionPane.showMessageDialog(this, 
                "❌ Prodotto non trovato!", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Quantità:");
        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Quantità non valida", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String priceStr = JOptionPane.showInputDialog(this, "Prezzo unitario:");
        double price;
        try {
            price = Double.parseDouble(priceStr);
            if (price < 0) throw new NumberFormatException();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Prezzo non valido", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        double subtotal = qty * price;
        tableModel.addRow(new Object[]{
            productId, 
            product[2], 
            qty, 
            String.format("%.2f €", price),
            String.format("%.2f €", subtotal)
        });

        updateTotal();
    }

    private void onRemoveProduct(ActionEvent e) {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Seleziona un prodotto da rimuovere", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        tableModel.removeRow(selectedRow);
        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String subtotalStr = tableModel.getValueAt(i, 4).toString()
                .replace("€", "").replace(",", ".").trim();
            try {
                total += Double.parseDouble(subtotalStr);
            } catch (Exception ex) {
                System.err.println("Errore calcolo totale: " + ex.getMessage());
            }
        }
        lblTotal.setText(String.format("%.2f €", total));
    }

    private void onSave(ActionEvent e) {
        String orderNumber = tfOrderNumber.getText().trim();
        String supplierId = tfSupplierId.getText().trim();
        String deliveryDate = tfDeliveryDate.getText().trim();

        if (orderNumber.isEmpty() || supplierId.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Numero ordine e ID fornitore sono obbligatori", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (deliveryDate.equals("YYYY-MM-DD") || deliveryDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Inserisci una data di consegna valida", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Aggiungi almeno un prodotto all'ordine", 
                "Errore", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String orderId = OrdineDAO.generateId();
        double total = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            int qty = Integer.parseInt(tableModel.getValueAt(i, 2).toString());
            String priceStr = tableModel.getValueAt(i, 3).toString()
                .replace("€", "").replace(",", ".").trim();
            double price = Double.parseDouble(priceStr);
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
            String priceStr = tableModel.getValueAt(i, 3).toString()
                .replace("€", "").replace(",", ".").trim();
            double price = Double.parseDouble(priceStr);

            String[] lineData = new String[]{lineId, orderId, productId, String.valueOf(qty), String.valueOf(price)};
            OrdineDAO.addOrderLine(lineData);
        }

        saved = true;
        JOptionPane.showMessageDialog(this, 
            "✅ Ordine salvato con successo!", 
            "Successo", 
            JOptionPane.INFORMATION_MESSAGE);
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