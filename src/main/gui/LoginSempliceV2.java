package src.main.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import src.main.logic.LoginController;
import com.formdev.flatlaf.FlatLightLaf;

public class LoginSempliceV2 extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JLabel messageLabel;

    public LoginSempliceV2() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("InventoryPro - Login");
        setSize(1100, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Cambia l'icona della finestra
    try {
        ImageIcon icon = new ImageIcon("src/main/resources/logo.png");
        setIconImage(icon.getImage());
    } catch (Exception e) {
        System.err.println("Impossibile caricare l'icona: " + e.getMessage());
    }

        // Layout split 50/50 perfetto
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.add(createFormPanel());
        mainPanel.add(createImagePanel());
        add(mainPanel);
    }

    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(60, 80, 60, 80));

        // Font di sistema puliti e grandi
        Font fontTitolo = new Font("Segoe UI", Font.BOLD, 42); 
        Font fontSottotitolo = new Font("Segoe UI", Font.PLAIN, 18);
        Font fontEtichetta = new Font("Segoe UI Semibold", Font.PLAIN, 15);
        Font fontInput = new Font("Segoe UI", Font.PLAIN, 16);

        JLabel titleLabel = new JLabel("<html>Login to<br>InventoryPro</html>");
        titleLabel.setFont(fontTitolo);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Inserisci le tue credenziali");
        subtitleLabel.setFont(fontSottotitolo);
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(fontEtichetta);
        userLabel.setBorder(new EmptyBorder(40, 0, 5, 0));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        userField = new JTextField();
        userField.setFont(fontInput);
        userField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        userField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Focus listener per bordo blu neon
        userField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                userField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 149, 255), 2), new EmptyBorder(5, 10, 5, 10)));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                userField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
            }
        });

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(fontEtichetta);
        passLabel.setBorder(new EmptyBorder(20, 0, 5, 0));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passField = new JPasswordField();
        passField.setFont(fontInput);
        passField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Focus listener per bordo blu neon
        passField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0, 149, 255), 2), new EmptyBorder(5, 10, 5, 10)));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passField.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
            }
        });

        // Bottone Login
        loginBtn = new JButton("ACCEDI");
        loginBtn.setBackground(new Color(41, 128, 185));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.addActionListener(e -> LoginController.eseguiLogin(userField.getText(), new String(passField.getPassword()), this));
        
        // Hover effect per il bottone
        loginBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(52, 152, 219));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginBtn.setBackground(new Color(41, 128, 185));
            }
        });

        // Label Messaggi (Prenotiamo lo spazio con un'altezza fissa)
        messageLabel = new JLabel(" "); 
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        messageLabel.setPreferredSize(new Dimension(100, 40));
        messageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(subtitleLabel);
        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);
        panel.add(Box.createRigidArea(new Dimension(0, 35)));
        panel.add(loginBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(messageLabel);

        return panel;
    }

    private JPanel createImagePanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon icon = new ImageIcon("src/main/resources/background.jpg");
                Image img = icon.getImage();
                if (img != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    int pW = getWidth(), pH = getHeight();
                    double ratio = Math.max((double) pW / img.getWidth(null), (double) pH / img.getHeight(null));
                    int w = (int) (img.getWidth(null) * ratio), h = (int) (img.getHeight(null) * ratio);
                    g2d.drawImage(img, (pW - w) / 2, (pH - h) / 2, w, h, null);
                }
            }
        };
    }

    public void mostraMessaggio(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }

    public void pulisciPassword() { passField.setText(""); }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginSempliceV2().setVisible(true));
    }
}