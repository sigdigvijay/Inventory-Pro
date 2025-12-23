package src.main.logic;

import src.main.util.CSVHelper; 
import src.main.gui.LoginSempliceV2;
import src.main.gui.MainFrame;
import javax.swing.*;
import java.awt.Color;
import java.util.List;

public class LoginController {
    private static final String CREDENTIALS_FILE = "data/credenziali.csv"; 
    
    public static void eseguiLogin(String username, String password, Object loginWindow) {
        if (username == null || username.trim().isEmpty() || password == null || password.isEmpty()) {
            mostraMessaggio(loginWindow, "Compilare tutti i campi.", Color.RED);
            return;
        }
        
        String ruolo = verificaCredenziali(username, password);

        if (ruolo != null) {
            if (loginWindow instanceof JFrame) {
                ((JFrame) loginWindow).dispose();
            }
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
        } else {
            mostraMessaggio(loginWindow, "Credenziali errate.", Color.RED);
            pulisciPassword(loginWindow);
        }
    }
    
    private static String verificaCredenziali(String username, String password) {
        List<String[]> rows = CSVHelper.readCSV(CREDENTIALS_FILE);
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length >= 3) { 
                if (row[0].trim().equalsIgnoreCase(username) && row[1].equals(password)) {
                    return row[2]; 
                }
            }
        }
        return null;
    }
    
    private static void mostraMessaggio(Object loginWindow, String message, Color color) {
        if (loginWindow instanceof LoginSempliceV2) {
            ((LoginSempliceV2) loginWindow).mostraMessaggio(message, color);
        }
    }
    
    private static void pulisciPassword(Object loginWindow) {
        if (loginWindow instanceof LoginSempliceV2) {
            ((LoginSempliceV2) loginWindow).pulisciPassword();
        }
    }
}