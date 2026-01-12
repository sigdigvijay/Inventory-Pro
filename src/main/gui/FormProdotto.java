package src.main.gui;

import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.formdev.flatlaf.FlatDarkLaf;

public class FormProdotto extends JDialog {

    private JTextField tfCode, tfName, tfSalePrice, tfPurchasePrice, tfStock, tfMinStock, tfMaxStock, tfImage;
    private JTextArea taDescription;
    private JComboBox<String> cbCategory;
    private JButton btnSave, btnCancel, btnSelectImage, btnRemoveImage;
    private JLabel lblImagePreview;

    private boolean saved = false;
    private String productId = null;
    private String selectedImagePath = "";

    // Colori personalizzati
    private static final Color PRIMARY_COLOR = new Color(70, 130, 180);
    private static final Color SUCCESS_COLOR = new Color(36, 175, 50);
    private static final Color CANCEL_COLOR = new Color(220, 20, 50);
    private static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color PANEL_COLOR = new Color(50, 50, 50);

    // Cartella per le immagini dei prodotti
    private static final String IMAGES_FOLDER = "product_images";

    // Categorie predefinite
    private static final String[] CATEGORIES = {
            "Seleziona categoria...",
            "Elettronica",
            "Alimentari",
            "Abbigliamento",
            "Casa e Giardino",
            "Sport e Tempo Libero",
            "Libri e Riviste",
            "Giocattoli",
            "Salute e Bellezza",
            "Automotive",
            "Ufficio e Cancelleria",
            "Altro"
    };

    public FormProdotto(Frame parent, String productId) {
        super(parent, true);
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        this.productId = productId;

        setTitle(productId == null ? "Nuovo Prodotto" : "Modifica Prodotto");
        setSize(900, 900);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Crea la cartella immagini se non esiste
        createImagesFolderIfNotExists();

        initComponents();

        if (productId != null) {
            loadProductData();
        }
    }

    private void createImagesFolderIfNotExists() {
        File imageDir = new File(IMAGES_FOLDER);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
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

        JLabel subtitleLabel = new JLabel(productId == null ? "Compila i campi per aggiungere un nuovo prodotto"
                : "Modifica le informazioni del prodotto");
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
                new EmptyBorder(25, 25, 25, 25)));

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
        tfImage.setEditable(false);

        // ComboBox per le categorie
        cbCategory = new JComboBox<>(CATEGORIES);
        cbCategory.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbCategory.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                new EmptyBorder(5, 8, 5, 8)));
        cbCategory.setBackground(new Color(60, 60, 60));
        cbCategory.setForeground(new Color(200, 200, 200));

        taDescription = new JTextArea(4, 20);
        taDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taDescription.setLineWrap(true);
        taDescription.setWrapStyleWord(true);
        taDescription.setBackground(new Color(60, 60, 60));
        taDescription.setForeground(new Color(200, 200, 200));
        taDescription.setCaretColor(Color.WHITE);
        JScrollPane spDescription = new JScrollPane(taDescription);
        spDescription.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 80), 1));

        int row = 0;

        // Sezione Informazioni Base
        addSectionTitle(formPanel, gbc, "Informazioni Base", row++);
        addFormField(formPanel, gbc, "Codice Prodotto *", tfCode, row++);
        addFormField(formPanel, gbc, "Nome Prodotto *", tfName, row++);
        addFormField(formPanel, gbc, "Categoria *", cbCategory, row++);

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
        addSectionTitle(formPanel, gbc, "Prezzi", row++);
        addFormField(formPanel, gbc, "Prezzo Vendita (€) *", tfSalePrice, row++);
        addFormField(formPanel, gbc, "Prezzo Acquisto (€)", tfPurchasePrice, row++);

        // Sezione Magazzino
        addSectionTitle(formPanel, gbc, "Gestione Magazzino", row++);
        addFormField(formPanel, gbc, "Giacenza Attuale", tfStock, row++);
        addFormField(formPanel, gbc, "Scorta Minima *", tfMinStock, row++);
        addFormField(formPanel, gbc, "Scorta Massima", tfMaxStock, row++);

        // Sezione Immagine con anteprima
        addSectionTitle(formPanel, gbc, "Immagine Prodotto", row++);

        // Panel per selezione immagine
        JPanel imageSelectionPanel = new JPanel(new BorderLayout(10, 0));
        imageSelectionPanel.setOpaque(false);

        btnSelectImage = createStyledButton("Sfoglia...", PRIMARY_COLOR);
        btnSelectImage.setPreferredSize(new Dimension(100, 35));
        btnSelectImage.addActionListener(e -> selectImage());

        btnRemoveImage = createStyledButton("✕", CANCEL_COLOR);
        btnRemoveImage.setPreferredSize(new Dimension(45, 35));
        btnRemoveImage.addActionListener(e -> removeImage());
        btnRemoveImage.setEnabled(false);

        JPanel btnImagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        btnImagePanel.setOpaque(false);
        btnImagePanel.add(btnSelectImage);
        btnImagePanel.add(btnRemoveImage);

        imageSelectionPanel.add(tfImage, BorderLayout.CENTER);
        imageSelectionPanel.add(btnImagePanel, BorderLayout.EAST);

        addFormField(formPanel, gbc, "File Immagine", imageSelectionPanel, row++);

        // Anteprima immagine
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblPreviewLabel = createStyledLabel("Anteprima");
        formPanel.add(lblPreviewLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setOpaque(false);

        lblImagePreview = new JLabel("Nessuna immagine selezionata");
        lblImagePreview.setPreferredSize(new Dimension(200, 200));
        lblImagePreview.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 2),
                new EmptyBorder(10, 10, 10, 10)));
        lblImagePreview.setHorizontalAlignment(JLabel.CENTER);
        lblImagePreview.setVerticalAlignment(JLabel.CENTER);
        lblImagePreview.setForeground(new Color(150, 150, 150));
        lblImagePreview.setBackground(new Color(40, 40, 40));
        lblImagePreview.setOpaque(true);

        previewPanel.add(lblImagePreview, BorderLayout.CENTER);
        formPanel.add(previewPanel, gbc);
        row++;

        gbc.anchor = GridBagConstraints.WEST;

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
        btnSave = createStyledButton("Salva", SUCCESS_COLOR);

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
                new EmptyBorder(8, 10, 8, 10)));
        tf.setBackground(new Color(60, 60, 60));
        tf.setForeground(new Color(200, 200, 200));
        tf.setCaretColor(Color.WHITE);
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

    private void selectImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleziona un'immagine per il prodotto");

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Immagini (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);

        // Imposta la directory corrente come punto di partenza
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();

            // Verifica dimensione file (max 5MB)
            if (selectedFile.length() > 5 * 1024 * 1024) {
                showStyledError("L'immagine è troppo grande. Dimensione massima: 5MB");
                return;
            }

            selectedImagePath = selectedFile.getAbsolutePath();
            tfImage.setText(selectedFile.getName());
            btnRemoveImage.setEnabled(true);

            // Mostra anteprima
            displayImagePreview(selectedImagePath);
        }
    }

    private void removeImage() {
        selectedImagePath = "";
        tfImage.setText("");
        lblImagePreview.setIcon(null);
        lblImagePreview.setText("Nessuna immagine selezionata");
        btnRemoveImage.setEnabled(false);
    }

    private void displayImagePreview(String imagePath) {
        try {
            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();

            // Calcola le dimensioni mantenendo le proporzioni
            int maxWidth = 200;
            int maxHeight = 200;
            int width = img.getWidth(null);
            int height = img.getHeight(null);

            double ratio = Math.min((double) maxWidth / width, (double) maxHeight / height);
            int scaledWidth = (int) (width * ratio);
            int scaledHeight = (int) (height * ratio);

            Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
            lblImagePreview.setIcon(new ImageIcon(scaledImg));
            lblImagePreview.setText("");
        } catch (Exception ex) {
            lblImagePreview.setIcon(null);
            lblImagePreview.setText("Errore caricamento anteprima");
            ex.printStackTrace();
        }
    }

    private String copyImageToProductFolder(File sourceFile) {
        try {
            // Ottieni l'estensione del file
            String fileName = sourceFile.getName();
            String extension = fileName.substring(fileName.lastIndexOf("."));

            // Genera un nome univoco per l'immagine
            String newFileName = "prod_" + System.currentTimeMillis() + extension;
            File destFile = new File(IMAGES_FOLDER, newFileName);

            // Copia il file nella cartella del progetto
            Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return IMAGES_FOLDER + "/" + newFileName;
        } catch (Exception ex) {
            ex.printStackTrace();
            showStyledError("Errore nel copiare l'immagine: " + ex.getMessage());
            return "";
        }
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

                // Carica l'immagine se presente
                if (p.length > 9 && !p[9].isEmpty()) {
                    String imagePath = p[9];
                    tfImage.setText(new File(imagePath).getName());
                    selectedImagePath = imagePath;
                    btnRemoveImage.setEnabled(true);

                    // Verifica se il file esiste e mostra l'anteprima
                    File imageFile = new File(imagePath);
                    if (imageFile.exists()) {
                        displayImagePreview(imagePath);
                    } else {
                        lblImagePreview.setText("Immagine non trovata");
                    }
                }

                // Carica la categoria se presente
                if (p.length > 11 && !p[11].isEmpty()) {
                    cbCategory.setSelectedItem(p[11]);
                }
                break;
            }
        }
    }

    private void onSave(ActionEvent e) {
        String code = tfCode.getText().trim();
        String name = tfName.getText().trim();
        String category = (String) cbCategory.getSelectedItem();
        String description = taDescription.getText().trim();
        String salePrice = tfSalePrice.getText().trim();
        String purchasePrice = tfPurchasePrice.getText().trim();
        String stock = tfStock.getText().trim();
        String minStock = tfMinStock.getText().trim();
        String maxStock = tfMaxStock.getText().trim();

        // Validazione campi obbligatori
        if (code.isEmpty() || name.isEmpty() || salePrice.isEmpty()) {
            showStyledError("Codice, Nome e Prezzo Vendita sono obbligatori");
            return;
        }

        // Validazione categoria
        if (category == null || category.equals("Seleziona categoria...")) {
            showStyledError("Seleziona una categoria per il prodotto");
            return;
        }

        // Validazione codice duplicato (solo per nuovi prodotti)
        if (productId == null && ProductsDAO.existsByCode(code)) {
            showStyledError("Codice prodotto già esistente");
            return;
        }

        // Validazione prezzo (deve essere un numero valido)
        try {
            double salePriceValue = Double.parseDouble(salePrice);
            if (salePriceValue <= 0) {
                showStyledError("Il prezzo di vendita deve essere maggiore di zero");
                return;
            }

            if (!purchasePrice.isEmpty()) {
                double purchasePriceValue = Double.parseDouble(purchasePrice);
                if (purchasePriceValue < 0) {
                    showStyledError("Il prezzo di acquisto non può essere negativo");
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            showStyledError("I prezzi devono essere numeri validi");
            return;
        }

        // Validazione scorte
        try {
            if (!stock.isEmpty()) {
                int stockValue = Integer.parseInt(stock);
                if (stockValue < 0) {
                    showStyledError("La giacenza non può essere negativa");
                    return;
                }
            }
            if (!minStock.isEmpty()) {
                int minStockValue = Integer.parseInt(minStock);
                if (minStockValue < 0) {
                    showStyledError("La scorta minima non può essere negativa");
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            showStyledError("Le scorte devono essere numeri interi validi");
            return;
        }

        // Gestisci l'immagine
        String imagePath = "";
        if (!selectedImagePath.isEmpty()) {
            File sourceFile = new File(selectedImagePath);

            // Se è un nuovo file (non già nella cartella del progetto), copialo
            if (!selectedImagePath.startsWith(IMAGES_FOLDER) && sourceFile.exists()) {
                imagePath = copyImageToProductFolder(sourceFile);
                if (imagePath.isEmpty()) {
                    return; // Errore già mostrato in copyImageToProductFolder
                }
            } else {
                // L'immagine è già nella cartella del progetto o è stata caricata in precedenza
                imagePath = selectedImagePath;
            }
        }

        String id = productId != null ? productId : ProductsDAO.generateId();
        String[] productData = new String[] {
                id,
                code,
                name,
                description,
                salePrice,
                purchasePrice,
                stock.isEmpty() ? "0" : stock,
                minStock.isEmpty() ? "5" : minStock,
                maxStock.isEmpty() ? "" : maxStock,
                imagePath,
                String.valueOf(System.currentTimeMillis()),
                category
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

    // Metodo helper per caricare immagini da usare in altre parti dell'applicazione
    public static ImageIcon loadProductImage(String imagePath, int width, int height) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                return null;
            }

            ImageIcon icon = new ImageIcon(imagePath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImg);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FormProdotto form = new FormProdotto(null, null);
            form.setVisible(true);
        });
    }
}