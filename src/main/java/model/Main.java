package gui;

import controller.Controller;

public class Main {
    public static void main(String[] args) {
        // 1. Creiamo il "cervello" del programma
        Controller controller = new Controller();

        // 2. Lanciamo l'interfaccia grafica passando il controller
        // Assicurati che il costruttore della tua Home accetti il Controller
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Home(controller).setVisible(true);
            }
        });
    }
}

