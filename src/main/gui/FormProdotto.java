package src.main.gui;

import src.main.dao.ProductsDAO; // ✅


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormProdotto extends JDialog {

    private JTextField tfCode, tfName, tfSalePrice, tfPurchasePrice, tfStock, tfMinStock, tfMaxStock, tfImage;
    private JTextArea taDescription;
    private JButton btnSave, btnCancel;

    private boolean saved = false;
    private String productId = null;

    public FormProdotto(Frame parent, String productId) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.productId = productId;

        setTitle(productId == null ? "Nuovo Prodotto" : "Modifica Prodotto");
        setSize(500, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();

        if (productId != null) {
            loadProductData();
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        tfCode = new JTextField();
        tfName = new JTextField();
        tfSalePrice = new JTextField();
        tfPurchasePrice = new JTextField();
        tfStock = new JTextField();
        tfMinStock = new JTextField();
        tfMaxStock = new JTextField();
        tfImage = new JTextField();
        taDescription = new JTextArea(5, 20);
        JScrollPane spDescription = new JScrollPane(taDescription);

        int row = 0;
        panel.add(new JLabel("Codice Prodotto:"), gbcWithRow(gbc, row)); panel.add(tfCode, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Nome:"), gbcWithRow(gbc, row)); panel.add(tfName, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Descrizione:"), gbcWithRow(gbc, row)); panel.add(spDescription, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Prezzo Vendita:"), gbcWithRow(gbc, row)); panel.add(tfSalePrice, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Prezzo Acquisto:"), gbcWithRow(gbc, row)); panel.add(tfPurchasePrice, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Giacenza:"), gbcWithRow(gbc, row)); panel.add(tfStock, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Scorta Minima:"), gbcWithRow(gbc, row)); panel.add(tfMinStock, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Scorta Massima:"), gbcWithRow(gbc, row)); panel.add(tfMaxStock, gbcWithRow(gbc, row++));
        panel.add(new JLabel("Immagine (path):"), gbcWithRow(gbc, row)); panel.add(tfImage, gbcWithRow(gbc, row++));

        add(panel, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Salva");
        btnCancel = new JButton("Annulla");

        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private GridBagConstraints gbcWithRow(GridBagConstraints gbc, int row) {
        GridBagConstraints copy = (GridBagConstraints) gbc.clone();
        copy.gridy = row;
        copy.gridx = 0;
        copy.weightx = 0.3;
        return copy;
    }

    private void loadProductData() {
        for (String[] p : ProductsDAO.getAllProducts()) {
            if (p[0].equals(productId)) {
                tfCode.setText(p[1]);
                tfName.setText(p[2]);
                taDescription.setText(p[3]);
                tfSalePrice.setText(p[4]);
                tfPurchasePrice.setText(p[5]);
                tfStock.setText(p[6]);
                tfMinStock.setText(p[7]);
                tfMaxStock.setText(p[8]);
                tfImage.setText(p[9]);
                break;
            }
        }
    }

    private void onSave(ActionEvent e) {
        String code = tfCode.getText().trim();
        String name = tfName.getText().trim();
        String description = taDescription.getText().trim();
        String salePrice = tfSalePrice.getText().trim();
        String purchasePrice = tfPurchasePrice.getText().trim();
        String stock = tfStock.getText().trim();
        String minStock = tfMinStock.getText().trim();
        String maxStock = tfMaxStock.getText().trim();
        String image = tfImage.getText().trim();

        if (code.isEmpty() || name.isEmpty() || salePrice.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Codice, Nome e Prezzo Vendita sono obbligatori", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (productId == null && ProductsDAO.existsByCode(code)) {
            JOptionPane.showMessageDialog(this, "Codice prodotto già esistente", "Errore", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String id = productId != null ? productId : ProductsDAO.generateId();
        String[] productData = new String[]{
                id, code, name, description, salePrice, purchasePrice, stock.isEmpty() ? "0" : stock,
                minStock.isEmpty() ? "5" : minStock,
                maxStock.isEmpty() ? "" : maxStock,
                image.isEmpty() ? "" : image,
                String.valueOf(System.currentTimeMillis())
        };

        if (productId != null) {
            ProductsDAO.updateProduct(id, productData);
        } else {
            ProductsDAO.addProduct(productData);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormProdotto form = new FormProdotto(null, null);
            form.setVisible(true);
        });
    }
}
