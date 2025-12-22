package src.main.gui;
import src.main.gui.components.SchermataInventario ;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JButton[] navButtons;
    private int activeTab = 0;

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

        setTitle("InventoryPro - Gestione Magazzino e Ordini");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Header with gradient
        JPanel header = createHeader();

        // Sidebar menu with modern styling
        JPanel sidebar = createStyledSidebar();

        // CardLayout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(30, 41, 59));

        schermataCatalogo = new SchermataCatalogo();
        schermataInventario = new SchermataInventario();
        schermataOrdini = new SchermataOrdini();
        schermataStatistiche = new SchermataStatistiche();

        mainPanel.add(schermataCatalogo, "Catalogo");
        mainPanel.add(schermataInventario, "Inventario");
        mainPanel.add(schermataOrdini, "Ordini");
        mainPanel.add(schermataStatistiche, "Statistiche");

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(30, 41, 59));
        getContentPane().add(header, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(30, 41, 59));
        centerPanel.add(sidebar, BorderLayout.WEST);
        centerPanel.add(mainPanel, BorderLayout.CENTER);

        getContentPane().add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 65, 85),
                        getWidth(), 0, new Color(52, 65, 85));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        header.setPreferredSize(new Dimension(0, 120));
        header.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));

        JLabel titleLabel = new JLabel("InventoryPro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        titleLabel.setForeground(Color.WHITE);

        JLabel subtitleLabel = new JLabel("Gestione Magazzino e Ordini");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        subtitleLabel.setForeground(new Color(255, 255, 255));

        JPanel headerContent = new JPanel();
        headerContent.setOpaque(false);
        headerContent.setLayout(new BoxLayout(headerContent, BoxLayout.Y_AXIS));
        headerContent.add(titleLabel);
        headerContent.add(subtitleLabel);

        header.add(headerContent);
        return header;
    }

    private JPanel createStyledSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(51, 65, 85));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1,
                new Color(71, 85, 105)));

        String[] navLabels = {"Catalogo Prodotti", "Inventario", "Ordini Fornitori", "Statistiche"};
        navButtons = new JButton[navLabels.length];

        for (int i = 0; i < navLabels.length; i++) {
            final int index = i;
            navButtons[i] = createNavButton(navLabels[i], i);

            navButtons[i].addActionListener(e -> {
                selectNavTab(index);
                if (index == 3) {
                    schermataStatistiche.loadStatistics();
                }
                cardLayout.show(mainPanel, navLabels[index].split(" ")[0]);
            });

            sidebar.add(navButtons[i]);
            sidebar.add(Box.createVerticalStrut(5));
        }

        sidebar.add(Box.createVerticalGlue());
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return sidebar;
    }

    private JButton createNavButton(String text, int index) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (activeTab == index) {
                    GradientPaint gp = new GradientPaint(0, 0, new Color(37, 99, 235),
                            getWidth(), 0, new Color(29, 78, 216));
                    g2d.setPaint(gp);
                    g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                } else if (getModel().isArmed() || getModel().isPressed()) {
                    g2d.setColor(new Color(71, 85, 105));
                    g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                } else if (isRolloverEnabled() && getModel().isRollover()) {
                    g2d.setColor(new Color(71, 85, 105));
                    g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                }

                super.paintComponent(g);
            }
        };

        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(new Color(203, 213, 225));
        btn.setBackground(new Color(51, 65, 85));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setPreferredSize(new Dimension(200, 66));
        btn.setMaximumSize(new Dimension(200, 66));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMargin(new Insets(10, 15, 10, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return btn;
    }

    private void selectNavTab(int tabIndex) {
        activeTab = tabIndex;
        for (int i = 0; i < navButtons.length; i++) {
            navButtons[i].repaint();
        }
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}