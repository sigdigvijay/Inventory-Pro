package src.main.gui;

import src.main.dao.ProductsDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import com.formdev.flatlaf.FlatLightLaf;

public class FormProdotto extends JDialog {

    private JTextField tfCode, tfName, tfSalePrice, tfPurchasePrice, tfStock, tfMinStock, tfMaxStock, tfImage;
    private JTextArea taDescription;
    private JButton btnSave, btnCancel;

    private boolean saved = false;
    private String productId = null;

    public FormProdotto(Frame parent, String productId) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.productId = productId;

        setTitle(productId == null ? "Nuovo Prodotto" : "Modifica Prodotto");
        setSize(500, 700); 
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        initComponents();

        if (productId != null) {
            loadProductData();
        }
    }

    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        // Border con 0 sopra per ridurre lo spazio iniziale
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; 
        gbc.weightx = 1.0;

        tfCode = new JTextField();
        tfName = new JTextField();
        tfSalePrice = new JTextField();
        tfPurchasePrice = new JTextField();
        tfStock = new JTextField();
        tfMinStock = new JTextField();
        tfMaxStock = new JTextField();
        tfImage = new JTextField();
        taDescription = new JTextArea(3, 20);
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane spDescription = new JScrollPane(taDescription);

        int row = 0;

        // Struttura: Label a riga N, Campo a riga N+1
        panel.add(new JLabel("Codice Prodotto:"), gbcAt(gbc, row++)); 
        panel.add(tfCode, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Nome:"), gbcAt(gbc, row++)); 
        panel.add(tfName, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Descrizione:"), gbcAt(gbc, row++)); 
        panel.add(spDescription, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Prezzo Vendita:"), gbcAt(gbc, row++)); 
        panel.add(tfSalePrice, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Prezzo Acquisto:"), gbcAt(gbc, row++)); 
        panel.add(tfPurchasePrice, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Giacenza:"), gbcAt(gbc, row++)); 
        panel.add(tfStock, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Scorta Minima:"), gbcAt(gbc, row++)); 
        panel.add(tfMinStock, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Scorta Massima:"), gbcAt(gbc, row++)); 
        panel.add(tfMaxStock, gbcAt(gbc, row++));
        
        panel.add(new JLabel("Immagine (path):"), gbcAt(gbc, row++)); 
        panel.add(tfImage, gbcAt(gbc, row++));

        add(new JScrollPane(panel), BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSave = new JButton("Salva");
        btnCancel = new JButton("Annulla");

        btnSave.addActionListener(this::onSave);
        btnCancel.addActionListener(e -> dispose());

        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);
    }

    private GridBagConstraints gbcAt(GridBagConstraints gbc, int row) {
        GridBagConstraints copy = (GridBagConstraints) gbc.clone();
        copy.gridy = row;
        if (row % 2 != 0) {
            copy.insets = new Insets(0, 5, 10, 5);
        }
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
        String salePrice = tfSalePrice.getText().trim();

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
                id, code, name, taDescription.getText().trim(), salePrice, tfPurchasePrice.getText().trim(),
                tfStock.getText().trim().isEmpty() ? "0" : tfStock.getText().trim(),
                tfMinStock.getText().trim().isEmpty() ? "5" : tfMinStock.getText().trim(),
                tfMaxStock.getText().trim(), tfImage.getText().trim(),
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
}