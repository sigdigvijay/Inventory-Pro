package src.main.gui;

import src.main.dao.ProductsDAO;
import src.main.dao.categories_dao;

import src.main.util.Validator;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataCategorie extends JPanel {

    //    private FormProdotto formProdotto;
    private JTable tableCategorie;
    private DefaultTableModel tableModel;
    private JTextField tfSearch;

    public SchermataCategorie() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout());
        initComponents();
        loadCategorie();
    }


    private void initComponents() {

        // Top panel: search
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblSearch = new JLabel("Cerca Categorie:");
        tfSearch = new JTextField(20);
        JButton btnSearch = new JButton("Cerca");
        btnSearch.addActionListener(e -> searchCategorie());
//        formProdotto = new FormProdotto();
        JButton btnAdd = new JButton("Aggiungi Categoria");
//        btnAdd.addActionListener(e -> {formProdotto})
        topPanel.add(btnAdd);
        topPanel.add(lblSearch);
        topPanel.add(tfSearch);
        topPanel.add(btnSearch);

        add(topPanel, BorderLayout.NORTH);

        // Table for products
        String[] columns = {"ID", "Nome", "Descrizione", "parent_id", "color"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // non-editable
            }
        };
        tableCategorie = new JTable(tableModel);
        tableCategorie.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(tableCategorie);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadCategorie() {
        tableModel.setRowCount(0); // clear table
        List<String[]> categories = categories_dao.get_all_categories();
        for(String[] p : categories) {
            Object[] row = new Object[]{
                    p[0], p[1], p[2], p[3], p[4]
            };
            tableModel.addRow(row);
        }
    }

    private void searchCategorie() {
        String query = tfSearch.getText().trim().toLowerCase();
        tableModel.setRowCount(0);

        List<String[]> categories = categories_dao.search_categories(query);
        for(String[] p : categories) {
            if(p[1].toLowerCase().contains(query) || p[2].toLowerCase().contains(query)) {
                Object[] row = new Object[]{
                        p[0], p[1], p[2], p[3], p[4]
                };
                tableModel.addRow(row);
            }
        }
    }

    // Optional: refresh table
    public void refresh() {
        loadCategorie();
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Catalogo Categorie");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 500);
            frame.add(new SchermataCatalogo());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
