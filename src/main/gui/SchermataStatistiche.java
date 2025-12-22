package src.main.gui;

import src.main.dao.ProductsDAO;
import src.main.dao.VenditaDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.*;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataStatistiche extends JPanel {

    private JTable tableTopProducts;
    private DefaultTableModel tableModel;
    private JLabel lblTotalRevenue, lblInventoryValue;

    public SchermataStatistiche() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(30, 30, 30));
        
        initComponents();
        loadStatistics();
    }

    private void initComponents() {
        // Top panel with title and refresh button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(40, 40, 40));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Statistiche e KPI");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(220, 220, 220));

        JButton btnRefresh = new JButton("Aggiorna");
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefresh.setBackground(new Color(0, 120, 215));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorderPainted(false);
        btnRefresh.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRefresh.setPreferredSize(new Dimension(120, 38));
        btnRefresh.addActionListener(e -> loadStatistics());
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(0, 100, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(0, 120, 215));
            }
        });

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(btnRefresh, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Stats cards panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        statsPanel.setBackground(new Color(30, 30, 30));

        // Revenue card
        JPanel revenueCard = createStatCard("", "Fatturato Totale", "€0.00");
        lblTotalRevenue = (JLabel) ((JPanel) revenueCard.getComponent(1)).getComponent(1);
        statsPanel.add(revenueCard);

        // Inventory value card
        JPanel inventoryCard = createStatCard("", "Valore Inventario", "€0.00");
        lblInventoryValue = (JLabel) ((JPanel) inventoryCard.getComponent(1)).getComponent(1);
        statsPanel.add(inventoryCard);

        // Center panel for stats
        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setBackground(new Color(30, 30, 30));
        centerPanel.add(statsPanel, BorderLayout.NORTH);

        // Top products section
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(40, 40, 40));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTopProducts = new JLabel("Top 10 Prodotti Più Venduti");
        lblTopProducts.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTopProducts.setForeground(new Color(220, 220, 220));
        lblTopProducts.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[] columns = {"Product ID", "Nome", "Quantità Venduta", "Margine Totale"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        tableTopProducts = new JTable(tableModel);
        styleTable(tableTopProducts);

        JScrollPane scrollPane = new JScrollPane(tableTopProducts);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scrollPane.getViewport().setBackground(new Color(45, 45, 45));

        tablePanel.add(lblTopProducts, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String icon, String title, String value) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 36));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(new Color(40, 40, 40));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        titleLabel.setForeground(new Color(180, 180, 180));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(new Color(0, 200, 100));

        textPanel.add(titleLabel);
        textPanel.add(valueLabel);

        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);

        return card;
    }

    private void styleTable(JTable table) {
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(45, 45, 45));
        table.setForeground(new Color(230, 230, 230));
        table.setGridColor(new Color(60, 60, 60));
        table.setSelectionBackground(new Color(0, 120, 215));
        table.setSelectionForeground(Color.WHITE);
        table.setShowVerticalLines(true);
        table.setShowHorizontalLines(true);
        table.setIntercellSpacing(new Dimension(1, 1));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
    }

    public void loadStatistics() {
        try {
            List<String[]> sales = VenditaDAO.getAllSales();
            List<String[]> products = ProductsDAO.getAllProducts();

            Map<String, Integer> productQtyMap = new HashMap<>();
            Map<String, Double> productMarginMap = new HashMap<>();
            double totalRevenue = 0;

            for (String[] sale : sales) {
                try {
                    if (sale.length < 4) continue;
                    
                    String productId = sale.length > 1 ? sale[1] : "";
                    if (productId.isEmpty()) continue;
                    
                    int qty = sale.length > 2 && !sale[2].isEmpty() ? Integer.parseInt(sale[2]) : 0;
                    double salePrice = sale.length > 3 && !sale[3].isEmpty() ? Double.parseDouble(sale[3]) : 0.0;
                    totalRevenue += salePrice * qty;

                    String[] product = ProductsDAO.getProductById(productId);
                    if (product == null || product.length < 6) continue;
                    
                    double purchasePrice = (product.length > 5 && !product[5].isEmpty()) ? 
                        Double.parseDouble(product[5]) : 0.0;
                    double margin = (salePrice - purchasePrice) * qty;

                    productQtyMap.put(productId, productQtyMap.getOrDefault(productId, 0) + qty);
                    productMarginMap.put(productId, productMarginMap.getOrDefault(productId, 0.0) + margin);
                } catch (NumberFormatException e) {
                    System.err.println("Errore nel parsing vendita: " + e.getMessage());
                }
            }

            lblTotalRevenue.setText(String.format("€%.2f", totalRevenue));

            // Inventory value
            double inventoryValue = 0;
            for (String[] product : products) {
                try {
                    if (product.length < 7) continue;
                    
                    double price = (product.length > 4 && !product[4].isEmpty()) ? 
                        Double.parseDouble(product[4]) : 0.0;
                    int stock = (product.length > 6 && !product[6].isEmpty()) ? 
                        Integer.parseInt(product[6]) : 0;
                    inventoryValue += price * stock;
                } catch (NumberFormatException e) {
                    System.err.println("Errore nel calcolo valore inventario: " + e.getMessage());
                }
            }
            lblInventoryValue.setText(String.format("€%.2f", inventoryValue));

            // Load top 10 products by quantity sold
            List<String[]> topProducts = new ArrayList<>();
            for (String productId : productQtyMap.keySet()) {
                try {
                    String[] product = ProductsDAO.getProductById(productId);
                    if (product == null || product.length < 3) continue;
                    
                    int qtySold = productQtyMap.get(productId);
                    double margin = productMarginMap.get(productId);
                    String productName = product.length > 2 ? product[2] : "N/A";
                    
                    topProducts.add(new String[]{
                        productId, 
                        productName, 
                        String.valueOf(qtySold), 
                        String.format("€%.2f", margin)
                    });
                } catch (Exception e) {
                    System.err.println("Errore nel caricamento top product: " + e.getMessage());
                }
            }

            topProducts.sort((a, b) -> {
                try {
                    return Integer.parseInt(b[2]) - Integer.parseInt(a[2]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            });
            
            tableModel.setRowCount(0);
            for (int i = 0; i < Math.min(10, topProducts.size()); i++) {
                tableModel.addRow(topProducts.get(i));
            }
        } catch (Exception e) {
            System.err.println("Errore nel caricamento statistiche: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Statistiche");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.add(new SchermataStatistiche());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}