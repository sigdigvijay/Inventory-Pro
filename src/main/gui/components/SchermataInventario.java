package src.main.gui.components;

import src.main.dao.MovementsDAO;
import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
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

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(30, 30, 30));

        initComponents();
        loadInventory();
        loadMovements();
    }

    private void initComponents() {
        // Top panel with refresh button
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 40));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel("Gestione Inventario");
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
        btnRefresh.addActionListener(e -> refresh());
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(0, 100, 180));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(0, 120, 215));
            }
        });

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnRefresh, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Inventory table
        String[] columns = {"ID", "Codice", "Nome", "Giacenza", "Scorta Minima", "Scorta Massima"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableInventory = new JTable(tableModel);
        styleTable(tableInventory);

        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryPanel.setBackground(new Color(40, 40, 40));
        inventoryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblInventory = new JLabel("Inventario Prodotti");
        lblInventory.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblInventory.setForeground(new Color(220, 220, 220));
        lblInventory.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JScrollPane inventoryScroll = new JScrollPane(tableInventory);
        inventoryScroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        inventoryScroll.getViewport().setBackground(new Color(45, 45, 45));

        inventoryPanel.add(lblInventory, BorderLayout.NORTH);
        inventoryPanel.add(inventoryScroll, BorderLayout.CENTER);

        // Movements table
        String[] moveColumns = {"ID", "Prodotto", "Tipo", "Quantità", "Causale", "Data"};
        movementsModel = new DefaultTableModel(moveColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tableMovements = new JTable(movementsModel);
        styleTable(tableMovements);

        JPanel movementsPanel = new JPanel(new BorderLayout());
        movementsPanel.setBackground(new Color(40, 40, 40));
        movementsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblMovements = new JLabel("Storico Movimenti");
        lblMovements.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblMovements.setForeground(new Color(220, 220, 220));
        lblMovements.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        JScrollPane movementsScroll = new JScrollPane(tableMovements);
        movementsScroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        movementsScroll.getViewport().setBackground(new Color(45, 45, 45));

        movementsPanel.add(lblMovements, BorderLayout.NORTH);
        movementsPanel.add(movementsScroll, BorderLayout.CENTER);

        // Split pane with modern styling
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, inventoryPanel, movementsPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(8);
        splitPane.setBackground(new Color(30, 30, 30));
        splitPane.setBorder(null);
        
        add(splitPane, BorderLayout.CENTER);
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

        // Center align cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
    }

    private void loadInventory() {
        tableModel.setRowCount(0);
        List<String[]> products = ProductsDAO.getAllProducts();
        
        for(String[] p : products) {
            try {
                Object[] row = {
                        p.length > 0 ? p[0] : "",
                        p.length > 1 ? p[1] : "",
                        p.length > 2 ? p[2] : "",
                        p.length > 6 ? p[6] : "0",
                        p.length > 7 ? p[7] : "0",
                        p.length > 8 ? p[8] : "0"
                };
                tableModel.addRow(row);

                // Highlight low stock with custom renderer
                if(p.length > 6 && p.length > 7) {
                    try {
                        int stock = Integer.parseInt(p[6]);
                        int minStock = Integer.parseInt(p[7]);
                        
                        if(stock < minStock) {
                            int rowIndex = tableModel.getRowCount() - 1;
                            // Apply red background for low stock items
                            tableInventory.getColumnModel().getColumn(3).setCellRenderer(
                                new LowStockCellRenderer()
                            );
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Errore nel parsing dello stock: " + e.getMessage());
                    }
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricamento prodotto inventario: " + e.getMessage());
            }
        }
    }

    private void loadMovements() {
        movementsModel.setRowCount(0);
        List<String[]> movements = MovementsDAO.getAllMovements();
        
        for(String[] m : movements) {
            try {
                Object[] row = {
                        m.length > 0 ? m[0] : "",
                        m.length > 1 ? m[1] : "",
                        m.length > 2 ? m[2] : "",
                        m.length > 3 ? m[3] : "",
                        m.length > 4 ? m[4] : "",
                        m.length > 5 ? m[5] : ""
                };
                movementsModel.addRow(row);
            } catch (Exception e) {
                System.err.println("Errore nel caricamento movimento: " + e.getMessage());
            }
        }
    }

    public void refresh() {
        loadInventory();
        loadMovements();
    }

    // Custom cell renderer for low stock warning
    private class LowStockCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            try {
                if (!isSelected && column == 3) { // Giacenza column
                    int stock = Integer.parseInt(value.toString());
                    int minStock = Integer.parseInt(table.getValueAt(row, 4).toString());
                    
                    if (stock < minStock) {
                        c.setBackground(new Color(139, 0, 0)); // Dark red
                        c.setForeground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(45, 45, 45));
                        c.setForeground(new Color(230, 230, 230));
                    }
                }
                setHorizontalAlignment(JLabel.CENTER);
            } catch (Exception e) {
                c.setBackground(new Color(45, 45, 45));
                c.setForeground(new Color(230, 230, 230));
            }
            
            return c;
        }
    }

    // Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Inventario e Movimenti");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 700);
            frame.add(new SchermataInventario());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}