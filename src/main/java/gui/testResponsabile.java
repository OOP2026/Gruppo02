package gui;

import controller.Controller;
import model.Docente;
import model.Responsabile;
import gui.RESPONSABILE;

import javax.swing.*;

public class testResponsabile {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Controller controller = new Controller();
            Responsabile admin = new Responsabile("Mario", "Rossi", "admin@uni.it", "password123");
            controller.setUtenteLoggato(admin);
            Docente prof1 = new Docente("Alan", "Turing", "turing@uni.it", "pass");
            Docente prof2 = new Docente("Margherita", "Hack", "hack@uni.it", "pass");
            controller.getDocenti().add(prof1);
            controller.getDocenti().add(prof2);
            model.Aula aulaBase = new model.Aula("Aula Magna");
            controller.aggiungiAula(aulaBase);
            RESPONSABILE frameResponsabile = new RESPONSABILE(controller);
            frameResponsabile.setVisible(true);

        });
    }
}
