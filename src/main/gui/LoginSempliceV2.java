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
    private JPanel cardPanel;
    private JPanel contentWrapper; // Nuovo wrapper per gestire lo shake

    // Palette colori raffinata
    private static final Color BG_PRIMARY = new Color(13, 17, 23); // Più scuro, stile GitHub
    private static final Color BG_CARD = new Color(21, 26, 33);
    private static final Color ACCENT_PRIMARY = new Color(47, 129, 247);
    private static final Color TEXT_PRIMARY = new Color(230, 237, 243);
    private static final Color TEXT_SECONDARY = new Color(139, 148, 158);
    private static final Color BORDER_COLOR = new Color(48, 54, 61);
    private static final Color ERROR_COLOR = new Color(248, 81, 73);
    private static final Color SUCCESS_COLOR = new Color(63, 185, 80);

    public LoginSempliceV2() {
        setTitle("InventoryPro - Accesso");
        setSize(500, 700); // Dimensioni più proporzionate
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principale con gradiente radiale simulato
        JPanel mainPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, BG_PRIMARY, 0, getHeight(), new Color(22, 27, 34));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Card Panel migliorata
        cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(BG_CARD);
        cardPanel.setPreferredSize(new Dimension(380, 520));
        cardPanel.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        setupUI();
        
        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void setupUI() {
        // --- LOGO/TITOLO ---
        JLabel managerLabel = new JLabel("InventoryPro");
        managerLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        managerLabel.setForeground(ACCENT_PRIMARY);
        managerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subTitle = new JLabel("Effettua il login per continuare");
        subTitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subTitle.setForeground(TEXT_SECONDARY);
        subTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- CAMPI INPUT ---
        userField = createStyledTextField("ID Utente o Email");
        passField = createStyledPasswordField();

        // Panel per la password che include il toggle dell'occhio
        JPanel passWrapper = new JPanel(new BorderLayout());
        passWrapper.setBackground(BG_CARD);
        passWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        // Eye icon toggle
        eyeIcon = new JLabel("👁"); 
        eyeIcon.setForeground(TEXT_SECONDARY);
        eyeIcon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeIcon.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        eyeIcon.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { togglePassword(); }
        });

        // --- BOTTONE ---
        loginBtn = createStyledButton("Accedi");
        loginBtn.addActionListener(e -> eseguiLoginConValidazione());

        // Messaggio di stato
        messageLabel = new JLabel(" ", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Composizione finale
        cardPanel.add(Box.createVerticalGlue());
        cardPanel.add(managerLabel);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(subTitle);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        cardPanel.add(alignLeft(new JLabel("ID UTENTE")));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        cardPanel.add(userField);
        
        cardPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        cardPanel.add(alignLeft(new JLabel("PASSWORD")));
        cardPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        
        // Wrapper per il campo password per contenere l'occhio internamente
        JPanel pFieldPanel = new JPanel(new BorderLayout());
        pFieldPanel.setBackground(new Color(13, 17, 23));
        pFieldPanel.setBorder(new LineBorder(BORDER_COLOR, 1, true));
        pFieldPanel.add(passField, BorderLayout.CENTER);
        pFieldPanel.add(eyeIcon, BorderLayout.EAST);
        pFieldPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        cardPanel.add(pFieldPanel);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        cardPanel.add(loginBtn);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        cardPanel.add(messageLabel);
        cardPanel.add(Box.createVerticalGlue());
    }

    // --- HELPER METODS PER ESTETICA ---

    private JTextField createStyledTextField(String placeholder) {
        JTextField tf = new JTextField();
        tf.setBackground(new Color(13, 17, 23));
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(new Color(13, 17, 23));
        pf.setForeground(TEXT_PRIMARY);
        pf.setCaretColor(TEXT_PRIMARY);
        pf.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return pf;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) g2d.setColor(ACCENT_PRIMARY.darker());
                else if (getModel().isRollover()) g2d.setColor(ACCENT_PRIMARY.brighter());
                else g2d.setColor(ACCENT_PRIMARY);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        return btn;
    }

    private JPanel alignLeft(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_SECONDARY);
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        p.setBackground(BG_CARD);
        p.add(label);
        return p;
    }

    private void togglePassword() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            passField.setEchoChar((char) 0);
            eyeIcon.setText("🔒"); // Cambia icona quando visibile
        } else {
            passField.setEchoChar('•');
            eyeIcon.setText("👁");
        }
    }

    // Correzione Animazione Shake (usa i margini invece della posizione assoluta)
    private void shakeCard() {
        final Point loc = cardPanel.getLocation();
        Timer timer = new Timer(40, new ActionListener() {
            int x = 0;
            int step = 0;
            int[] offsets = {10, -10, 8, -8, 6, -6, 4, -4, 0};
            public void actionPerformed(ActionEvent e) {
                cardPanel.setLocation(loc.x + offsets[step], loc.y);
                step++;
                if (step >= offsets.length) {
                    ((Timer)e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    private void eseguiLoginConValidazione() {
        if (userField.getText().isEmpty() || new String(passField.getPassword()).isEmpty()) {
            messageLabel.setText("Inserisci tutte le credenziali");
            messageLabel.setForeground(ERROR_COLOR);
            shakeCard();
            return;
        }
        // Logica di caricamento...
        loginBtn.setText("Verifica...");
        loginBtn.setEnabled(false);
        
        Timer t = new Timer(1000, e -> {
            // Qui invocheresti il controller
            loginBtn.setEnabled(true);
            loginBtn.setText("Accedi");
            // Esempio: LoginController.eseguiLogin(...);
        });
        t.setRepeats(false);
        t.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginSempliceV2().setVisible(true));
    }

    public void mostraMessaggio(String message, Color color) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mostraMessaggio'");
    }

    public void pulisciPassword() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pulisciPassword'");
    }
}