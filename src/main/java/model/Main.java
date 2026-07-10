package gui;

import controller.Controller;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Home(controller).setVisible(true);
            }
        });
    }
}

