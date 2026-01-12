package src.main.gui;

import src.main.dao.OrdineDAO;
import src.main.dao.ProductsDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import com.formdev.flatlaf.FlatDarkLaf;

public class SchermataOrdini extends JPanel {

    private JTable tableOrders;
    private DefaultTableModel ordersModel;
    private JLabel lblOrderCount;

    public SchermataOrdini() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(new Color(30, 30, 30));

        initComponents();
        loadOrders();
    }

    private void initComponents() {

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(40, 40, 40));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel titleLabel = new JLabel("Gestione Ordini Fornitori");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(220, 220, 220));

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(new Color(40, 40, 40));

        JButton btnAdd = createStyledButton("Nuovo Ordine", new Color(46, 125, 50));
        JButton btnReceive = createStyledButton("Ricevi Ordine", new Color(0, 120, 215));
        JButton btnRefresh = createStyledButton("Aggiorna", new Color(108, 117, 125));

        btnAdd.addActionListener(this::onAddOrder);
        btnReceive.addActionListener(this::onReceiveOrder);
        btnRefresh.addActionListener(e -> loadOrders());

        buttonsPanel.add(btnAdd);
        buttonsPanel.add(btnReceive);
        buttonsPanel.add(btnRefresh);

        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(buttonsPanel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(new Color(40, 40, 40));
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        JLabel lblTable = new JLabel("Elenco Ordini");
        lblTable.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTable.setForeground(new Color(220, 220, 220));
        lblTable.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        String[] columns = { "ID", "N. Ordine", "Fornitore ID", "Data Ordine", "Data Consegna", "Stato", "Totale",
                "Note" };
        ordersModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableOrders = new JTable(ordersModel);
        styleTable(tableOrders);

        JScrollPane scroll = new JScrollPane(tableOrders);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 1));
        scroll.getViewport().setBackground(new Color(45, 45, 45));

        tablePanel.add(lblTable, BorderLayout.NORTH);
        tablePanel.add(scroll, BorderLayout.CENTER);

        add(tablePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(new Color(40, 40, 40));
        bottomPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)));

        lblOrderCount = new JLabel("Totale ordini: 0");
        lblOrderCount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOrderCount.setForeground(new Color(180, 180, 180));
        bottomPanel.add(lblOrderCount);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 38));

        Color darkerColor = bgColor.darker();
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(darkerColor);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
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

        table.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(35, 35, 35));
        header.setForeground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
    }

    private void loadOrders() {
        ordersModel.setRowCount(0);

        try {
            List<String[]> orders = OrdineDAO.getAllOrders();

            for (String[] o : orders) {
                try {

                    Object[] row = new Object[8];
                    for (int i = 0; i < 8; i++) {
                        row[i] = (o.length > i && o[i] != null) ? o[i] : "";
                    }
                    ordersModel.addRow(row);
                } catch (Exception e) {
                    System.err.println("Errore nel caricamento ordine: " + e.getMessage());
                }
            }

            lblOrderCount.setText("Totale ordini: " + ordersModel.getRowCount());
        } catch (Exception e) {
            System.err.println("Errore nel caricamento ordini: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Errore nel caricamento degli ordini: " + e.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onAddOrder(ActionEvent e) {
        try {
            FormOrdine form = new FormOrdine(null);
            form.setVisible(true);
            if (form.isSaved()) {
                loadOrders();
                JOptionPane.showMessageDialog(this,
                        "Ordine creato con successo!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            System.err.println("Errore nella creazione ordine: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Errore nella creazione dell'ordine: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onReceiveOrder(ActionEvent e) {
        int selectedRow = tableOrders.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Seleziona un ordine da ricevere",
                    "Attenzione",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Object orderIdObj = ordersModel.getValueAt(selectedRow, 0);
            if (orderIdObj == null || orderIdObj.toString().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "ID ordine non valido",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String orderId = orderIdObj.toString();
            Object statusObj = ordersModel.getValueAt(selectedRow, 5);
            String status = statusObj != null ? statusObj.toString() : "";

            if ("Ricevuto".equalsIgnoreCase(status) || "Received".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(this,
                        "Questo ordine è già stato ricevuto",
                        "Attenzione",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Confermi la ricezione dell'ordine #" + orderId + "?",
                    "Conferma Ricezione",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                OrdineDAO.receiveOrder(orderId);
                JOptionPane.showMessageDialog(this,
                        "Ordine ricevuto e giacenze aggiornate!",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE);
                loadOrders();
            }
        } catch (Exception ex) {
            System.err.println("Errore nella ricezione ordine: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Errore nella ricezione dell'ordine: " + ex.getMessage(),
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected && value != null) {
                String status = value.toString().toLowerCase();
                if (status.contains("ricevuto") || status.contains("received")) {
                    c.setBackground(new Color(40, 167, 69));
                    c.setForeground(Color.WHITE);
                } else if (status.contains("pendente") || status.contains("pending")) {
                    c.setBackground(new Color(255, 193, 7));
                    c.setForeground(new Color(33, 37, 41));
                } else if (status.contains("annullato") || status.contains("cancelled")) {
                    c.setBackground(new Color(220, 53, 69));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(45, 45, 45));
                    c.setForeground(new Color(230, 230, 230));
                }
            }

            setHorizontalAlignment(JLabel.CENTER);
            return c;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Ordini Fornitori");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1920, 1080);
            frame.add(new SchermataOrdini());
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
