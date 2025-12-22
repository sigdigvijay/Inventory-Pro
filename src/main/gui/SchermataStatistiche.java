package src.main.gui;

import src.main.dao.ProductsDAO;
import src.main.dao.VenditaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class SchermataStatistiche extends JPanel {

    private JTable tableTopProducts;
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenue, lblInventoryValue;

    public SchermataStatistiche() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout(10,10));
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        JPanel topPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        lblTotalRevenue = new JLabel("Fatturato Totale: €0.00");
        lblInventoryValue = new JLabel("Valore Inventario: €0.00");
        topPanel.add(lblTotalRevenue);
        topPanel.add(lblInventoryValue);
        add(topPanel, BorderLayout.NORTH);

        // Top products table
        String[] columns = {"Product ID", "Nome", "Quantità Venduta", "Margine Totale"};
        tableModel = new DefaultTableModel(columns, 0);
        tableTopProducts = new JTable(tableModel);
        add(new JScrollPane(tableTopProducts), BorderLayout.CENTER);
    }

    public void loadStatistics() {
        List<String[]> sales = VenditaDAO.getAllSales();
        List<String[]> products = ProductsDAO.getAllProducts();

        Map<String, Integer> productQtyMap = new HashMap<>();
        Map<String, Double> productMarginMap = new HashMap<>();
        double totalRevenue = 0;

        for (String[] sale : sales) {
            String productId = sale[1];
            int qty = Integer.parseInt(sale[2]);
            double salePrice = Double.parseDouble(sale[3]);
            totalRevenue += salePrice * qty;

            String[] product = ProductsDAO.getProductById(productId);
            if (product == null) continue;
            double purchasePrice = product[5].isEmpty() ? 0 : Double.parseDouble(product[5]);
            double margin = (salePrice - purchasePrice) * qty;

            productQtyMap.put(productId, productQtyMap.getOrDefault(productId,0)+qty);
            productMarginMap.put(productId, productMarginMap.getOrDefault(productId,0.0)+margin);
        }

        lblTotalRevenue.setText(String.format("Fatturato Totale: €%.2f", totalRevenue));

        // Inventory value
        double inventoryValue = 0;
        for (String[] product : products) {
            double price = product[4].isEmpty() ? 0 : Double.parseDouble(product[4]);
            int stock = product[6].isEmpty() ? 0 : Integer.parseInt(product[6]);
            inventoryValue += price * stock;
        }
        lblInventoryValue.setText(String.format("Valore Inventario: €%.2f", inventoryValue));

        // Load top 10 products by quantity sold
        List<String[]> topProducts = new ArrayList<>();
        for (String productId : productQtyMap.keySet()) {
            String[] product = ProductsDAO.getProductById(productId);
            int qtySold = productQtyMap.get(productId);
            double margin = productMarginMap.get(productId);
            topProducts.add(new String[]{productId, product[2], String.valueOf(qtySold), String.format("%.2f", margin)});
        }

        topProducts.sort((a,b) -> Integer.parseInt(b[2]) - Integer.parseInt(a[2])); // descending
        tableModel.setRowCount(0);
        for (int i = 0; i < Math.min(10, topProducts.size()); i++) {
            tableModel.addRow(topProducts.get(i));
        }
    }

    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Statistiche");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(600, 400);
            frame.add(new SchermataStatistiche());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
