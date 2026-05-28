package gui;

import controller.Controller;
import javax.swing.SwingUtilities;
import javax.swing.JFrame;

public class testRegistrazione {

    public static void main(String[] args) {

        // 1. Creiamo un Controller "fittizio" e vuoto per fare i test
        Controller controllerTest = new Controller();

        // 2. Avviamo la grafica nel modo corretto in Java
        SwingUtilities.invokeLater(() -> {

            // 3. Creiamo la nostra schermata passandole il controller
            REGISTRAZIONE schermata = new REGISTRAZIONE(controllerTest);

            // 4. Impostazioni della finestra
            schermata.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Chiude il programma se premiamo la X
            schermata.setSize(500, 700); // Imposta larghezza e altezza della finestra
            schermata.setLocationRelativeTo(null); // Fa apparire la finestra perfettamente al centro dello schermo!

            // 5. Mostriamola!
            schermata.setVisible(true);
        });
    }
}
