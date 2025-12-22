package src.main.gui;

import src.main.dao.ProductsDAO;
import src.main.util.Validator;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class SchermataCatalogo extends JPanel {

//    private FormProdotto formProdotto;
    private JTable tableProducts;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public SchermataCatalogo() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout());
        initComponents();
        loadProducts();
    }


    private void initComponents() {

        // Top panel: search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = new JLabel("Cerca prodotto:");
        tfSearch = new JTextField(20);
        JButton btnSearch = new JButton("Cerca");
        btnSearch.addActionListener(e -> searchProducts());
//        formProdotto = new FormProdotto();

// daniele
        JButton btnAdd = new JButton("Aggiungi Prodotto");
        btnAdd.addActionListener(e -> new FormProdotto(null, null).setVisible(true));
// daniele
        topPanel.add(btnAdd);
        topPanel.add(lblSearch);
        topPanel.add(tfSearch);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // Table for products
        String[] columns = {"ID", "Codice", "Nome", "Descrizione", "Prezzo Vendita", "Prezzo Acquisto", "Giacenza", "Scorta Minima", "Immagine"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };
        tableProducts = new JTable(tableModel);
        tableProducts.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tableProducts);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadProducts() {
        tableModel.setRowCount(0); // clear table
        List<String[]> products = ProductsDAO.getAllProducts();
        for(String[] p : products) {
            Object[] row = new Object[]{
                    p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[9] // image path
            };
            tableModel.addRow(row);
        }
    }

    private void searchProducts() {
        String query = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        List<String[]> products = ProductsDAO.getAllProducts();
        for(String[] p : products) {
            if(p[1].toLowerCase().contains(query) || p[2].toLowerCase().contains(query)) {
                Object[] row = new Object[]{
                        p[0], p[1], p[2], p[3], p[4], p[5], p[6], p[7], p[9]
                };
                tableModel.addRow(row);
            }
        }
    }

    // Optional: refresh table
    public void refresh() {
        loadProducts();
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Catalogo Prodotti");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.add(new SchermataCatalogo());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}