package src.main.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import src.main.logic.LoginController;

public class LoginSempliceV2 extends JFrame {
    private JTextField userField;
    private JPasswordField passField;
    private JButton loginBtn;
    private JLabel messageLabel;
    private JCheckBox rememberCheck;
    private JLabel eyeIcon;
    private boolean passwordVisible = false;
    private Timer shakeTimer;
    private int shakeOffset = 0;
    private JPanel cardPanel;
    
    // Palette colori tema scuro
    private static final Color BG_PRIMARY = new Color(25, 28, 35);
    private static final Color BG_SECONDARY = new Color(35, 39, 47);
    private static final Color BG_CARD = new Color(42, 46, 56);
    private static final Color ACCENT_PRIMARY = new Color(88, 166, 255);
    private static final Color ACCENT_HOVER = new Color(108, 186, 255);
    private static final Color ACCENT_PRESSED = new Color(68, 146, 235);
    private static final Color TEXT_PRIMARY = new Color(230, 237, 243);
    private static final Color TEXT_SECONDARY = new Color(156, 163, 175);
    private static final Color BORDER_COLOR = new Color(60, 65, 78);
    private static final Color ERROR_COLOR = new Color(239, 68, 68);
    private static final Color SUCCESS_COLOR = new Color(34, 197, 94);
    private static final Color WARNING_COLOR = new Color(251, 146, 60);

    public LoginSempliceV2() {
        // Configurazione finestra
        setTitle("InventoryPro - Sistema di Autenticazione");
        setSize(600, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Panel principale con gradiente
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, BG_PRIMARY, 0, getHeight(), BG_SECONDARY);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 60, 50, 60));
        
        // Panel contenitore card con ombra
        cardPanel = new JPanel(new BorderLayout(0, 0));
        cardPanel.setBackground(BG_CARD);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(45, 50, 45, 50)
        ));
        
        // Panel titolo con icona
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(BG_CARD);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));
        
        // Icona decorativa
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int size = 60;
                int x = (getWidth() - size) / 2;
                int y = 10;
                
                // Cerchio esterno
                g2d.setColor(new Color(88, 166, 255, 30));
                g2d.fillOval(x - 10, y - 10, size + 20, size + 20);
                
                // Cerchio principale
                GradientPaint gp = new GradientPaint(x, y, ACCENT_PRIMARY, x + size, y + size, ACCENT_HOVER);
                g2d.setPaint(gp);
                g2d.fillOval(x, y, size, size);
                
                // Icona lucchetto
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawRoundRect(x + 20, y + 30, 20, 18, 4, 4);
                g2d.drawArc(x + 22, y + 18, 16, 20, 0, 180);
            }
        };
        iconPanel.setPreferredSize(new Dimension(100, 90));
        iconPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        iconPanel.setBackground(BG_CARD);
        iconPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(iconPanel);
        
        titlePanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JLabel managerLabel = new JLabel("InventoryPro");
        managerLabel.setFont(new Font("Segoe UI", Font.BOLD, 40));
        managerLabel.setForeground(ACCENT_PRIMARY);
        managerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(managerLabel);
        
        titlePanel.add(Box.createRigidArea(new Dimension(0, 12)));
        
        JLabel titleLabel = new JLabel("Accedi al tuo account");
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titlePanel.add(titleLabel);
        
        cardPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel centrale 
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BG_CARD);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 30, 10));
        
        // Label Username
        JLabel userLabel = new JLabel("ID Utente");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(TEXT_PRIMARY);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(userLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Panel per campo username con icona
        JPanel userPanel = new JPanel(new BorderLayout(10, 0));
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        userPanel.setBackground(BG_SECONDARY);
        userPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Campo Username
        userField = new JTextField();
        userField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        userField.setBackground(BG_SECONDARY);
        userField.setForeground(TEXT_PRIMARY);
        userField.setCaretColor(TEXT_PRIMARY);
        userField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        userPanel.add(userField, BorderLayout.CENTER);
        
        // Focus listener per effetto hover
        userField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                userPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT_PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(7, 14, 7, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                userPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        centerPanel.add(userPanel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 25)));
        
        // Label Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(TEXT_PRIMARY);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        centerPanel.add(passLabel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Panel per campo password con icona
        JPanel passPanel = new JPanel(new BorderLayout(10, 0));
        passPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        passPanel.setBackground(BG_SECONDARY);
        passPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        // Icona lucchetto

        // Campo Password
        passField = new JPasswordField();
        passField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passField.setBackground(BG_SECONDARY);
        passField.setForeground(TEXT_PRIMARY);
        passField.setCaretColor(TEXT_PRIMARY);
        passField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        passPanel.add(passField, BorderLayout.CENTER);
        
        // Icona occhio per mostrare/nascondere password
        eyeIcon = new JLabel("👁");
        eyeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                passwordVisible = !passwordVisible;
                if (passwordVisible) {
                    passField.setEchoChar((char) 0);
                    eyeIcon.setText("👁");
                } else {
                    passField.setEchoChar('•');
                    eyeIcon.setText("👁");
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                eyeIcon.setForeground(ACCENT_PRIMARY);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                eyeIcon.setForeground(TEXT_SECONDARY);
            }
        });
        passPanel.add(eyeIcon, BorderLayout.EAST);
        
        // Focus listener per effetto hover
        passField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                passPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(ACCENT_PRIMARY, 2, true),
                    BorderFactory.createEmptyBorder(7, 14, 7, 14)
                ));
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                passPanel.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(BORDER_COLOR, 1, true),
                    BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        centerPanel.add(passPanel);
        
        centerPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Panel opzioni (ricordami + password dimenticata)
        JPanel optionsPanel = new JPanel(new BorderLayout());
        optionsPanel.setBackground(BG_CARD);
        optionsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        // Checkbox Ricordami
        rememberCheck = new JCheckBox("Ricordami");
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberCheck.setForeground(TEXT_SECONDARY);
        rememberCheck.setBackground(BG_CARD);
        rememberCheck.setFocusPainted(false);
        rememberCheck.setCursor(new Cursor(Cursor.HAND_CURSOR));
        optionsPanel.add(rememberCheck, BorderLayout.WEST);
        
        // Link Password dimenticata
        JLabel forgotLabel = new JLabel("<html><u>Password dimenticata?</u></html>");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        forgotLabel.setForeground(ACCENT_PRIMARY);
        forgotLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(LoginSempliceV2.this,
                    "Contatta l'amministratore di sistema per recuperare le credenziali.",
                    "Recupero Password",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
            @Override
            public void mouseEntered(MouseEvent e) {
                forgotLabel.setForeground(ACCENT_HOVER);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                forgotLabel.setForeground(ACCENT_PRIMARY);
            }
        });
        optionsPanel.add(forgotLabel, BorderLayout.EAST);
        
        centerPanel.add(optionsPanel);
        
        cardPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel in basso 
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.setBackground(BG_CARD);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Bottone Login con gradiente
        loginBtn = new JButton("Accedi") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                if (getModel().isPressed()) {
                    g2d.setColor(ACCENT_PRESSED);
                } else if (getModel().isRollover()) {
                    GradientPaint gp = new GradientPaint(0, 0, ACCENT_HOVER, 0, getHeight(), ACCENT_PRIMARY);
                    g2d.setPaint(gp);
                } else {
                    GradientPaint gp = new GradientPaint(0, 0, ACCENT_PRIMARY, 0, getHeight(), new Color(68, 146, 235));
                    g2d.setPaint(gp);
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                
                // Testo
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(getText(), x, y);
            }
        };
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        loginBtn.setPreferredSize(new Dimension(200, 50));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.setBorderPainted(false);
        loginBtn.setContentAreaFilled(false);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        loginBtn.addActionListener(e -> eseguiLoginConValidazione());
        
        bottomPanel.add(loginBtn);
        
        bottomPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Label messaggi
        messageLabel = new JLabel(" ");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(TEXT_SECONDARY);
        bottomPanel.add(messageLabel);
        
        cardPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        mainPanel.add(cardPanel, BorderLayout.CENTER);
        add(mainPanel);
        
        // Premere Enter per login
        passField.addActionListener(e -> eseguiLoginConValidazione());
    }
    
    // Metodo per eseguire login con validazione completa
    private void eseguiLoginConValidazione() {
        String username = userField.getText().trim();
        String password = new String(passField.getPassword());
        
        // Validazione lato client
        if (username.isEmpty()) {
            mostraErrore("Il campo ID Utente è obbligatorio", "Campo vuoto");
            userField.requestFocus();
            shakeCard();
            return;
        }
        
        if (password.isEmpty()) {
            mostraErrore("Il campo Password è obbligatorio", "Campo vuoto");
            passField.requestFocus();
            shakeCard();
            return;
        }
        
        if (username.length() < 3) {
            mostraErrore("L'ID Utente deve contenere almeno 3 caratteri", "ID non valido");
            userField.requestFocus();
            shakeCard();
            return;
        }
        
        if (password.length() < 4) {
            mostraErrore("La Password deve contenere almeno 4 caratteri", "Password non valida");
            passField.requestFocus();
            shakeCard();
            return;
        }
        
        // Mostra stato di caricamento
        loginBtn.setEnabled(false);
        loginBtn.setText("Verifica in corso...");
        mostraMessaggio("Autenticazione in corso...", WARNING_COLOR);
        
        // Simula un piccolo delay per l'autenticazione
        Timer loadingTimer = new Timer(500, e -> {
            loginBtn.setEnabled(true);
            loginBtn.setText("Accedi");
            
            // Chiama il controller
            LoginController.eseguiLogin(username, password, this);
        });
        loadingTimer.setRepeats(false);
        loadingTimer.start();
    }
    
    // Metodo per mostrare errore con dialog
    private void mostraErrore(String messaggio, String titolo) {
        JOptionPane.showMessageDialog(
            this,
            messaggio,
            titolo,
            JOptionPane.ERROR_MESSAGE
        );
        mostraMessaggio(messaggio, ERROR_COLOR);
    }
    
    // Metodo per animazione shake della card
    private void shakeCard() {
        if (shakeTimer != null && shakeTimer.isRunning()) {
            return;
        }
        
        final Point originalLocation = cardPanel.getLocation();
        shakeOffset = 0;
        
        shakeTimer = new Timer(50, new ActionListener() {
            int count = 0;
            int[] offsets = {-10, 10, -8, 8, -5, 5, -2, 2, 0};
            
            @Override
            public void actionPerformed(ActionEvent e) {
                if (count < offsets.length) {
                    cardPanel.setLocation(originalLocation.x + offsets[count], originalLocation.y);
                    count++;
                } else {
                    cardPanel.setLocation(originalLocation);
                    shakeTimer.stop();
                }
            }
        });
        shakeTimer.start();
    }
    
    // Metodo pubblico per mostrare messaggi
    public void mostraMessaggio(String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setForeground(color);
    }
    
    // Metodo pubblico per pulire il campo password
    public void pulisciPassword() {
        passField.setText("");
    }
    
    // Metodo per mostrare successo del login
    public void loginRiuscito() {
        mostraMessaggio("Accesso eseguito con successo!", SUCCESS_COLOR);
        loginBtn.setText("Accesso riuscito");
        loginBtn.setEnabled(false);
        
        // Timer per chiudere o aprire la dashboard
        Timer successTimer = new Timer(1500, e -> {
            // Qui puoi aprire la finestra principale dell'applicazione
            dispose();
        });
        successTimer.setRepeats(false);
        successTimer.start();
    }
    
    // Metodo per mostrare fallimento del login
    public void loginFallito(String motivo) {
        mostraErrore("Credenziali non valide. " + motivo, "Accesso negato");
        shakeCard();
        pulisciPassword();
        userField.requestFocus();
    }
    
    public static void main(String[] args) {
        // Imposta look and feel di sistema
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            LoginSempliceV2 frame = new LoginSempliceV2();
            frame.setVisible(true);
        });
    }
}