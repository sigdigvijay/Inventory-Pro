package src.main.gui;

import src.main.dao.ProductsDAO;
import src.main.util.Validator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataCatalogo extends JPanel {

    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public SchermataCatalogo() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(30, 30, 30));
        initComponents();
        loadProducts();
    }


    private void initComponents() {
        // Top panel: search with modern styling
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        topPanel.setBackground(new Color(40, 40, 40));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblSearch = new JLabel("Cerca prodotto:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSearch.setForeground(new Color(220, 220, 220));

        tfSearch = new JTextField(25);
        tfSearch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tfSearch.setBackground(new Color(50, 50, 50));
        tfSearch.setForeground(Color.WHITE);
        tfSearch.setCaretColor(Color.WHITE);
        tfSearch.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JButton btnSearch = new JButton("Cerca");
        btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSearch.setBackground(new Color(0, 120, 215));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setFocusPainted(false);
        btnSearch.setBorderPainted(false);
        btnSearch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSearch.setPreferredSize(new Dimension(100, 38));
        btnSearch.addActionListener(e -> searchProducts());
        
        // Hover effect
        btnSearch.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSearch.setBackground(new Color(0, 100, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSearch.setBackground(new Color(0, 120, 215));
            }
        });

        JButton btnRefresh = new JButton("Aggiorna");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setBackground(new Color(60, 60, 60));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setPreferredSize(new Dimension(110, 38));
        btnRefresh.addActionListener(e -> refresh());
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(80, 80, 80));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(60, 60, 60));
            }
        });

        topPanel.add(lblSearch);
        topPanel.add(tfSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);

        add(topPanel, BorderLayout.NORTH);

        // Center panel with table and action buttons
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(30, 30, 30));

        // Table for products with modern styling
        String[] columns = {"ID", "Codice", "Nome", "Descrizione", "Prezzo Vendita", "Prezzo Acquisto", "Giacenza", "Scorta Minima", "Immagine"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableProducts = new JTable(tableModel);
        tableProducts.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tableProducts.setRowHeight(35);
        tableProducts.setFillsViewportHeight(true);
        tableProducts.setBackground(new Color(45, 45, 45));
        tableProducts.setForeground(new Color(230, 230, 230));
        tableProducts.setGridColor(new Color(60, 60, 60));
        tableProducts.setSelectionBackground(new Color(0, 120, 215));
        tableProducts.setSelectionForeground(Color.WHITE);
        tableProducts.setShowVerticalLines(true);
        tableProducts.setShowHorizontalLines(true);
        tableProducts.setIntercellSpacing(new Dimension(1, 1));

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tableProducts.getColumnCount(); i++) {
            tableProducts.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style header
        JTableHeader header = tableProducts.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        JScrollPane scrollPane = new JScrollPane(tableProducts);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));

        centerPanel.add(scrollPane, BorderLayout.CENTER);

        // Action buttons panel (right side)
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBackground(new Color(40, 40, 40));
        actionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Aggiungi button
        JButton btnAdd = createActionButton("Aggiungi", new Color(34, 139, 34), new Color(28, 115, 28));
        btnAdd.addActionListener(e -> aggiungiProdotto());

        // Modifica button
        JButton btnEdit = createActionButton("Modifica", new Color(255, 140, 0), new Color(220, 120, 0));
        btnEdit.addActionListener(e -> modificaProdotto());

        // Elimina button
        JButton btnDelete = createActionButton("Elimina", new Color(220, 53, 69), new Color(180, 40, 55));
        btnDelete.addActionListener(e -> eliminaProdotto());

        actionPanel.add(btnAdd);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnEdit);
        actionPanel.add(Box.createVerticalStrut(10));
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalGlue());

        centerPanel.add(actionPanel, BorderLayout.EAST);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom info panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 70, 70)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblInfo = new JLabel("Totale prodotti: 0");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInfo.setForeground(new Color(180, 180, 180));
        bottomPanel.add(lblInfo);

        add(bottomPanel, BorderLayout.SOUTH);

        // Update count on load
        tableModel.addTableModelListener(e -> {
            lblInfo.setText("Totale prodotti: " + tableModel.getRowCount());
        });
    }

    private JButton createActionButton(String text, Color bgColor, Color hoverColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(150, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
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

    private void aggiungiProdotto() {
        // TODO: Implementare la logica per aggiungere un prodotto
        JOptionPane.showMessageDialog(this, 
            "Funzionalità 'Aggiungi Prodotto' da implementare", 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
        // Qui puoi aprire un dialog per inserire i dati del nuovo prodotto
        // e poi chiamare ProductsDAO per salvarlo
    }

    private void modificaProdotto() {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleziona un prodotto da modificare", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // TODO: Implementare la logica per modificare un prodotto
        String productId = tableModel.getValueAt(selectedRow, 0).toString();
        JOptionPane.showMessageDialog(this, 
            "Funzionalità 'Modifica Prodotto' da implementare\nID Prodotto: " + productId, 
            "Info", 
            JOptionPane.INFORMATION_MESSAGE);
        // Qui puoi aprire un dialog precompilato con i dati del prodotto
        // e poi chiamare ProductsDAO per aggiornarlo
    }

    private void eliminaProdotto() {
        int selectedRow = tableProducts.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Seleziona un prodotto da eliminare", 
                "Attenzione", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String productId = tableModel.getValueAt(selectedRow, 0).toString();
        String productName = tableModel.getValueAt(selectedRow, 2).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Sei sicuro di voler eliminare il prodotto:\n" + productName + " (ID: " + productId + ")?", 
            "Conferma Eliminazione", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            // TODO: Implementare la logica per eliminare un prodotto
            // ProductsDAO.deleteProduct(productId);
            JOptionPane.showMessageDialog(this, 
                "Funzionalità 'Elimina Prodotto' da implementare", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            // refresh();
        }
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        List<String[]> products = ProductsDAO.getAllProducts();
        for(String[] p : products) {
            try {
                Object[] row = new Object[]{
                        p.length > 0 ? p[0] : "",
                        p.length > 1 ? p[1] : "",
                        p.length > 2 ? p[2] : "",
                        p.length > 3 ? p[3] : "",
                        p.length > 4 ? p[4] : "",
                        p.length > 5 ? p[5] : "",
                        p.length > 6 ? p[6] : "",
                        p.length > 7 ? p[7] : "",
                        p.length > 9 ? p[9] : ""
                };
                tableModel.addRow(row);
            } catch (Exception e) {
                System.err.println("Errore nel caricamento prodotto: " + e.getMessage());
            }
        }
    }

    private void searchProducts() {
        String query = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        List<String[]> products = ProductsDAO.getAllProducts();
        for(String[] p : products) {
            try {
                String codice = p.length > 1 ? p[1].toLowerCase() : "";
                String nome = p.length > 2 ? p[2].toLowerCase() : "";
                
                if(codice.contains(query) || nome.contains(query)) {
                    Object[] row = new Object[]{
                            p.length > 0 ? p[0] : "",
                            p.length > 1 ? p[1] : "",
                            p.length > 2 ? p[2] : "",
                            p.length > 3 ? p[3] : "",
                            p.length > 4 ? p[4] : "",
                            p.length > 5 ? p[5] : "",
                            p.length > 6 ? p[6] : "",
                            p.length > 7 ? p[7] : "",
                            p.length > 9 ? p[9] : ""
                    };
                    tableModel.addRow(row);
                }
            } catch (Exception e) {
                System.err.println("Errore nella ricerca prodotto: " + e.getMessage());
            }
        }
    }

    public void refresh() {
        loadProducts();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Catalogo Prodotti");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 600);
            frame.add(new SchermataCatalogo());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}