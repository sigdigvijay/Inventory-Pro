package src.main.gui;
import src.main.gui.components.SchermataInventario ;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

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
        setSize(1000, 700);
        setLocationRelativeTo(null);

        initComponents();
    }

    private void initComponents() {
        // Sidebar menu
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(0,1,5,5));
        JButton btnCatalogo = new JButton("Catalogo Prodotti");
        JButton btnInventario = new JButton("Inventario");
        JButton btnOrdini = new JButton("Ordini Fornitori");
        JButton btnStatistiche = new JButton("Statistiche");

        sidebar.add(btnCatalogo);
        sidebar.add(btnInventario);
        sidebar.add(btnOrdini);
        sidebar.add(btnStatistiche);

        // CardLayout panel
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        schermataCatalogo = new SchermataCatalogo();
        schermataInventario = new SchermataInventario();
        schermataOrdini = new SchermataOrdini();
        schermataStatistiche = new SchermataStatistiche();

        mainPanel.add(schermataCatalogo, "Catalogo");
        mainPanel.add(schermataInventario, "Inventario");
        mainPanel.add(schermataOrdini, "Ordini");
        mainPanel.add(schermataStatistiche, "Statistiche");

        // Button actions
        btnCatalogo.addActionListener(e -> cardLayout.show(mainPanel, "Catalogo"));
        btnInventario.addActionListener(e -> cardLayout.show(mainPanel, "Inventario"));
        btnOrdini.addActionListener(e -> cardLayout.show(mainPanel, "Ordini"));
        btnStatistiche.addActionListener(e -> {
            schermataStatistiche.loadStatistics();
            cardLayout.show(mainPanel, "Statistiche");
        });

        // Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(sidebar, BorderLayout.WEST);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
