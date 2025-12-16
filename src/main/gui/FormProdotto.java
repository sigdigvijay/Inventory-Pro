package src.main.gui;

import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormProdotto extends JDialog {

    private JTextField tfCode, tfName, tfSalePrice, tfPurchasePrice, tfStock, tfMinStock, tfMaxStock, tfImage;
    private JTextArea taDescription;
    private JButton btnSave, btnCancel;

    private boolean saved = false;
    private String productId = null;

    // Colori personalizzati
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private static final Color CANCEL_COLOR = new Color(158, 158, 158);
    private static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color PANEL_COLOR = new Color(50, 50, 50);

    public FormProdotto(Frame parent, String productId) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.productId = productId;

        setTitle(productId == null ? "➕ Nuovo Prodotto" : "✏️ Modifica Prodotto");
        setSize(900, 700);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);

        initComponents();

        if (productId != null) {
            loadProductData();
        }
    }

    private void initComponents() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(new EmptyBorder(20, 25, 20, 25));

        JLabel titleLabel = new JLabel(productId == null ? "Nuovo Prodotto" : "Modifica Prodotto");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel(productId == null ? 
            "Compila i campi per aggiungere un nuovo prodotto" : 
            "Modifica le informazioni del prodotto");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(230, 230, 230));

        JPanel titleContainer = new JPanel(new BorderLayout(0, 5));
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.NORTH);
        titleContainer.add(subtitleLabel, BorderLayout.CENTER);

        headerPanel.add(titleContainer, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel with Scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(PANEL_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            new EmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Inizializza componenti con stile migliorato
        tfCode = createStyledTextField();
        tfName = createStyledTextField();
        tfSalePrice = createStyledTextField();
        tfPurchasePrice = createStyledTextField();
        tfStock = createStyledTextField();
        tfMinStock = createStyledTextField();
        tfMaxStock = createStyledTextField();
        tfImage = createStyledTextField();
        
        taDescription = new JTextArea(4, 20);
        taDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        JScrollPane spDescription = new JScrollPane(taDescription);
        spDescription.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));

        int row = 0;

        // Sezione Informazioni Base
        addSectionTitle(formPanel, gbc, "📋 Informazioni Base", row++);
        addFormField(formPanel, gbc, "Codice Prodotto *", tfCode, row++);
        addFormField(formPanel, gbc, "Nome Prodotto *", tfName, row++);
        
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblDesc = createStyledLabel("Descrizione");
        formPanel.add(lblDesc, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(spDescription, gbc);
        row++;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Sezione Prezzi
        addSectionTitle(formPanel, gbc, "💰 Prezzi", row++);
        addFormField(formPanel, gbc, "Prezzo Vendita *", tfSalePrice, row++);
        addFormField(formPanel, gbc, "Prezzo Acquisto", tfPurchasePrice, row++);

        // Sezione Magazzino
        addSectionTitle(formPanel, gbc, "📦 Gestione Magazzino", row++);
        addFormField(formPanel, gbc, "Giacenza Attuale", tfStock, row++);
        addFormField(formPanel, gbc, "Scorta Minima", tfMinStock, row++);
        addFormField(formPanel, gbc, "Scorta Massima", tfMaxStock, row++);

        // Sezione Immagine
        addSectionTitle(formPanel, gbc, "🖼️ Media", row++);
        addFormField(formPanel, gbc, "Path Immagine", tfImage, row++);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        // Footer Panel con bottoni
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.setBorder(new EmptyBorder(0, 25, 25, 25));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        btnCancel = createStyledButton("Annulla", CANCEL_COLOR);
        btnSave = createStyledButton("💾 Salva", SUCCESS_COLOR);

        btnCancel.addActionListener(e -> dispose());
        btnSave.addActionListener(this::onSave);

        btnPanel.add(btnCancel);
        btnPanel.add(btnSave);
        
        footerPanel.add(btnPanel, BorderLayout.EAST);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private JTextField createStyledTextField() {
        JTextField tf = new JTextField();
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        return tf;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        label.setForeground(new Color(200, 200, 200));
        return label;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 25, 10, 25));
        
        // Hover effect
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        
        return btn;
    }

    private void addSectionTitle(JPanel panel, GridBagConstraints gbc, String title, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.insets = new Insets(row == 0 ? 0 : 20, 8, 12, 8);
        
        JLabel sectionLabel = new JLabel(title);
        sectionLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        sectionLabel.setForeground(PRIMARY_COLOR);
        
        panel.add(sectionLabel, gbc);
        
        gbc.gridwidth = 1;
        gbc.insets = new Insets(8, 8, 8, 8);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(createStyledLabel(labelText), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
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
            showStyledError("Codice, Nome e Prezzo Vendita sono obbligatori");
            return;
        }

        if (productId == null && ProductsDAO.existsByCode(code)) {
            showStyledError("Codice prodotto già esistente");
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

    private void showStyledError(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "⚠️ Attenzione", 
            JOptionPane.WARNING_MESSAGE);
    }

    public boolean isSaved() {
        return saved;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormProdotto form = new FormProdotto(null, null);
            form.setVisible(true);
        });
    }
}