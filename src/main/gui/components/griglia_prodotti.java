package src.main.gui.components;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class griglia_prodotti extends JPanel {

    private List<JPanel> productCards;

    public griglia_prodotti() {
        setLayout(new GridLayout(0, 4, 10, 10)); // 4 columns, dynamic rows
        productCards = new ArrayList<>();
    }

    // Add a product card
    public void addProduct(String id, String name, String category, String price, String imagePath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setBackground(new Color(45, 45, 45));

        // Image
        ImageIcon icon;
        try {
            icon = new ImageIcon(imagePath);
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (Exception e) {
            icon = new ImageIcon(new byte[0]); // placeholder
        }
        JLabel imgLabel = new JLabel(icon);
        imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        card.add(imgLabel, BorderLayout.NORTH);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(3, 1));
        infoPanel.setBackground(new Color(45, 45, 45));
        infoPanel.add(new JLabel("Nome: " + name));
        infoPanel.add(new JLabel("Categoria: " + category));
        infoPanel.add(new JLabel("Prezzo: €" + price));
        card.add(infoPanel, BorderLayout.CENTER);

        productCards.add(card);
        add(card);
    }

    // Remove all products
    public void removeAllProducts() {
        removeAll();
        productCards.clear();
    }
}
