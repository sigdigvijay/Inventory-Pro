package src.main.gui;
import src.main.gui.components.SchermataInventario;
import src.main.gui.FormProdotto;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel titleLabel;

    // Screens
    private SchermataCatalogo schermataCatalogo;
    private SchermataInventario schermataInventario;
    private SchermataOrdini schermataOrdini;
    private SchermataStatistiche schermataStatistiche;

    public MainFrame() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.err.println("Failed to initialize FlatLaf");
        }

        setTitle("InventoryPro - Sistema di Gestione Magazzino");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 900);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Main container
        JPanel container = new JPanel(new BorderLayout(0, 0));
        container.setBackground(new Color(30, 30, 30));

        // Top header bar
        JPanel headerPanel = createHeaderPanel();
        
        // Sidebar menu
        JPanel sidebar = createSidebar();

        // CardLayout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(40, 40, 40));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        schermataCatalogo = new SchermataCatalogo();
        schermataInventario = new SchermataInventario();
        schermataOrdini = new SchermataOrdini();
        schermataStatistiche = new SchermataStatistiche();

        mainPanel.add(schermataCatalogo, "Catalogo");
        mainPanel.add(schermataInventario, "Inventario");
        mainPanel.add(schermataOrdini, "Ordini");
        mainPanel.add(schermataStatistiche, "Statistiche");

        // Layout assembly
        container.add(headerPanel, BorderLayout.NORTH);
        container.add(sidebar, BorderLayout.WEST);
        container.add(mainPanel, BorderLayout.CENTER);

        setContentPane(container);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(25, 25, 25));
        header.setBorder(new EmptyBorder(15, 5, 10, 10));
        header.setPreferredSize(new Dimension(0, 80));

        // Left side - Logo and title
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        leftPanel.setOpaque(false);

        titleLabel = new JLabel("Catalogo Prodotti");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(220, 220, 220));

        leftPanel.add(titleLabel);

        // Right side - Quick action button
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);

        JButton btnAggiungiProdotto = new JButton("+ Aggiungi Prodotto");
        styleActionButton(btnAggiungiProdotto, new Color(46, 125, 50));
        btnAggiungiProdotto.addActionListener(e -> openFormProdotto());

        rightPanel.add(btnAggiungiProdotto);
       
        header.add(leftPanel, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);

        return header;
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(35, 35, 35));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setBorder(new EmptyBorder(20, 10, 20, 10));

        // Menu title
        JLabel menuTitle = new JLabel("MENU PRINCIPALE");
        menuTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        menuTitle.setForeground(new Color(150, 150, 150));
        menuTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuTitle.setBorder(new EmptyBorder(10, 15, 15, 0));

        sidebar.add(menuTitle);
        sidebar.add(Box.createVerticalStrut(10));

        // Menu buttons
        JButton btnCatalogo = createMenuButton("Catalogo Prodotti", "Catalogo");
        JButton btnInventario = createMenuButton("Inventario", "Inventario");
        JButton btnOrdini = createMenuButton("Ordini Fornitori", "Ordini");
        JButton btnStatistiche = createMenuButton("Statistiche", "Statistiche");

        sidebar.add(btnCatalogo);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnInventario);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnOrdini);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(btnStatistiche);

        // Button actions
        btnCatalogo.addActionListener(e -> switchScreen("Catalogo", "Catalogo Prodotti"));
        btnInventario.addActionListener(e -> switchScreen("Inventario", "Gestione Inventario"));
        btnOrdini.addActionListener(e -> switchScreen("Ordini", "Ordini Fornitori"));
        btnStatistiche.addActionListener(e -> {
            schermataStatistiche.loadStatistics();
            switchScreen("Statistiche", "Statistiche & Report");
        });

        sidebar.add(Box.createVerticalGlue());

        return sidebar;
    }

    private JButton createMenuButton(String text, String screenName) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setForeground(new Color(200, 200, 200));
        btn.setBackground(new Color(45, 45, 45));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(true);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMaximumSize(new Dimension(230, 45));
        btn.setPreferredSize(new Dimension(230, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setBorder(new EmptyBorder(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(55, 55, 55));
                btn.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(45, 45, 45));
                btn.setForeground(new Color(200, 200, 200));
            }
        });

        return btn;
    }

    private void styleActionButton(JButton btn, Color bgColor) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bgColor);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(180, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 20, 8, 20));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    private void styleIconButton(JButton btn) {
        btn.setBackground(new Color(50, 50, 50));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(45, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void switchScreen(String screenName, String title) {
        titleLabel.setText(title);
        cardLayout.show(mainPanel, screenName);
    }

    private void openFormProdotto() {
        FormProdotto formProdotto = new FormProdotto(this, null);
        formProdotto.setVisible(true);
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}