package src.main.logic;

// Import della classe di utilità (era mancante nel tuo errore)
import src.main.util.CSVHelper; 
// Import della classe View (essenziale per i metodi mostraMessaggio, pulisciPassword)
import src.main.gui.LoginSempliceV2; 
import src.main.gui.MainFrame;

import javax.swing.*;
import java.awt.Color;
import java.util.List;

public class LoginController {
    // Ho cambiato il percorso in "data/" per riflettere il tuo albero di directory
    private static final String CREDENTIALS_FILE = "data/credenziali.csv"; 
    private static final int USERNAME_INDEX = 0;
    private static final int PASSWORD_INDEX = 1;
    private static final int RUOLO_INDEX = 2;
    
    // Inizializzazione statica del file
    static {
        inizializzaFile();
    }
    
    /**
     * FUNZIONE PRINCIPALE - Chiamata quando l'utente preme il bottone Accedi
     * Controlla le credenziali e gestisce il risultato
     * @param username Username inserito dall'utente
     * @param password Password inserita dall'utente
     * @param loginWindow Riferimento alla finestra di login per mostrare messaggi
     */
    public static void eseguiLogin(String username, String password, Object loginWindow) {
        // Controllo campi vuoti
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            mostraErrore(loginWindow, "Compilare tutti i campi.");
            return;
        }
        
        // Verifica credenziali
        String ruolo = verificaCredenziali(username, password);

        if (ruolo != null) {
            // Login Riuscito
            mostraSuccesso(loginWindow, "Accesso eseguito come " + ruolo + ".");
            
            // Logica per aprire la finestra principale e chiudere quella di login
            if (loginWindow instanceof JFrame) {
                ((JFrame) loginWindow).dispose();
            }
            // Codice per aprire la MainFrameV2
            SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
            
        } else {
            // Login Fallito
            mostraErrore(loginWindow, "Credenziali non valide.");
            pulisciPassword(loginWindow);
        }
    }
    
    /**
     * Controlla se le credenziali esistono nel file CSV
     * Ritorna il ruolo dell'utente se il login ha successo, altrimenti null
     */
    private static String verificaCredenziali(String username, String password) {
        // Legge tutti i dati dal file
        List<String[]> rows = CSVHelper.readCSV(CREDENTIALS_FILE);
        
        // Inizia da 1 per saltare l'header
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            // Assicurati che la riga abbia abbastanza colonne prima di accedere
            if (row.length > RUOLO_INDEX) { 
                if (row[USERNAME_INDEX].trim().equals(username) &&
                    row[PASSWORD_INDEX].equals(password)) {
                    return row[RUOLO_INDEX]; // Login riuscito, ritorna il ruolo
                }
            }
        }
        return null; // Login fallito
    }
    
    /**
     * Se il file credenziali non esiste, lo crea con il solo header.
     * RIMOSSA la logica di inserimento dell'utente 'admin' hardcoded.
     */
    private static void inizializzaFile() {
        List<String[]> rows = CSVHelper.readCSV(CREDENTIALS_FILE);
        
        if (rows.isEmpty()) {
            // Crea solo l'header
            String[] header = {"username", "password", "ruolo"};
            CSVHelper.appendToCSV(CREDENTIALS_FILE, header);
            
            System.out.println("File credenziali.csv creato con solo l'header.");
            System.out.println("Assicurati di inserire i dati degli utenti nel file.");
        }
    }
    
    // Metodi di utilità per interagire con la View
    
    private static void mostraErrore(Object loginWindow, String message) {
        if (loginWindow instanceof LoginSempliceV2) {
            ((LoginSempliceV2) loginWindow).mostraMessaggio(message, Color.RED);
        }
    }
    
    private static void mostraSuccesso(Object loginWindow, String message) {
        if (loginWindow instanceof LoginSempliceV2) {
            ((LoginSempliceV2) loginWindow).mostraMessaggio(message, new Color(34, 139, 34)); // Verde
        }
    }
    
    private static void pulisciPassword(Object loginWindow) {
        if (loginWindow instanceof LoginSempliceV2) {
            ((LoginSempliceV2) loginWindow).pulisciPassword();
        }
    }
    
}