package src.main.logic;

import src.main.gui.LoginSempliceV2; // Importa la classe LoginSempliceV2

import javax.swing.SwingUtilities; // Importa SwingUtilities per l'avvio della GUI

public class Main {
    
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            LoginSempliceV2 frame = new LoginSempliceV2();
            
            frame.setVisible(true);
        });
    }
}