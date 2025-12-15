package src.main.gui.components;

import src.main.dao.MovementsDAO;
import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataInventario extends JPanel {

    private JTable tableInventory;
    private DefaultTableModel tableModel;
    private JTable tableMovements;
    private DefaultTableModel movementsModel;

    public SchermataInventario() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch(Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout());

        initComponents();
        loadInventory();
        loadMovements();
    }

    private void initComponents() {
        // Top panel: Inventory table
        String[] columns = {"ID", "Codice", "Nome", "Giacenza", "Scorta Minima", "Scorta Massima"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableInventory = new JTable(tableModel);
        JScrollPane inventoryScroll = new JScrollPane(tableInventory);
        inventoryScroll.setBorder(BorderFactory.createTitledBorder("Inventario Prodotti"));

        // Bottom panel: Movements table
        String[] moveColumns = {"ID", "Prodotto", "Tipo", "Quantità", "Causale", "Data"};
        movementsModel = new DefaultTableModel(moveColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMovements = new JTable(movementsModel);
        JScrollPane movementsScroll = new JScrollPane(tableMovements);
        movementsScroll.setBorder(BorderFactory.createTitledBorder("Storico Movimenti"));

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inventoryScroll, movementsScroll);
        splitPane.setResizeWeight(0.5);
        add(splitPane, BorderLayout.CENTER);
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
        List<String[]> products = ProductsDAO.getAllProducts();
        for(String[] p : products) {
            Object[] row = {
                    p[0], p[1], p[2], p[6], p[7], p[8]
            };
            tableModel.addRow(row);

            // Highlight low stock
            int stock = Integer.parseInt(p[6]);
            int minStock = Integer.parseInt(p[7]);
            if(stock < minStock) {
                tableInventory.setRowSelectionInterval(tableModel.getRowCount()-1, tableModel.getRowCount()-1);
            }
        }
    }

    private void loadMovements() {
        movementsModel.setRowCount(0);
        List<String[]> movements = MovementsDAO.getAllMovements();
        for(String[] m : movements) {
            Object[] row = {
                    m[0], m[1], m[2], m[3], m[4], m[5]
            };
            movementsModel.addRow(row);
        }
    }

    public void refresh() {
        loadInventory();
        loadMovements();
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventario e Movimenti");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900,600);
            frame.add(new SchermataInventario());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
