package src.main.gui;

import javax.swing.*;
import java.awt.*;
import src.main.logic.LoginController; // Assicurato che l'import sia corretto

public class LoginSempliceV2 extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JLabel messageLabel;

    public LoginSempliceV2() {
        // Configurazione finestra
        setTitle("InventoryPro");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principale
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        // Panel titolo 
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(new Color(240, 240, 240));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel managerLabel = new JLabel("InventoryPro");
        managerLabel.setFont(new Font("Arial", Font.BOLD, 32));
        managerLabel.setForeground(new Color(70, 130, 180));
        managerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(managerLabel);
        
        titlePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel centrale 
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(new Color(240, 240, 240));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        // Label Username
        JLabel userLabel = new JLabel("ID:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(userLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Campo Username
        userField = new JTextField();
        userField.setFont(new Font("Arial", Font.PLAIN, 14));
        userField.setPreferredSize(new Dimension(300, 30));
        userField.setMaximumSize(new Dimension(300, 30));
        userField.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(userField);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Label Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(passLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Campo Password
        passField = new JPasswordField();
        passField.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setPreferredSize(new Dimension(300, 30));
        passField.setMaximumSize(new Dimension(300, 30));
        passField.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(passField);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel in basso 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(new Color(240, 240, 240));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Bottone Login - CHIAMA LA FUNZIONE DALL'ALTRO FILE
        loginBtn = new JButton("Accedi");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 14));
        loginBtn.setPreferredSize(new Dimension(200, 40));
        loginBtn.setMaximumSize(new Dimension(200, 40));
        loginBtn.setBackground(new Color(70, 130, 180));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginBtn.addActionListener(e -> {
            // Prendi i valori dai campi
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            
            // CHIAMA LA FUNZIONE DI CONTROLLO DALL'ALTRO FILE
            LoginController.eseguiLogin(username, password, this);
        });
        bottomPanel.add(loginBtn);
        
        // Spazio tra bottone e messaggio
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Label messaggi
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bottomPanel.add(messageLabel);
        
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Premere Enter per login
        passField.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            LoginController.eseguiLogin(username, password, this);
        });
    }
    
    // Metodo pubblico per mostrare messaggi (chiamato dal controller)
    public void mostraMessaggio(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
    
    // Metodo pubblico per pulire il campo password (chiamato dal controller)
    public void pulisciPassword() {
        passField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginSempliceV2 frame = new LoginSempliceV2();
            frame.setVisible(true);
        });
    }
}