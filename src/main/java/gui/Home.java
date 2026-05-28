package gui;

import javax.swing.*;
import controller.Controller;
import model.Docente;
import model.Responsabile;
import model.Studente;
import model.Utente;

public class Home extends JFrame {
    private controller.Controller controller;
    private JPanel panelHome;
    private JTextField textField2;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JPanel panelHome0;
    private JTextField accediConLeTueTextField;
    private JButton btnVaiARegistrazione;

    public Home(Controller controller) {
        this.controller = controller;
        this.setContentPane(panelHome);
        this.setTitle("ACCESSO");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setSize(400, 300);

        loginButton.addActionListener(e -> {
            String email = this.textField2.getText();
            String password = String.valueOf(this.passwordField1.getPassword());

            if (controller.effettuaLogin(email, password)) {
                Utente utenteLoggato = controller.getUtenteLoggato();

                if (utenteLoggato instanceof Studente) {
                    STUDENTE schermataStudente = new STUDENTE(controller, (model.Studente) utenteLoggato);
                    schermataStudente.setVisible(true);
                    this.dispose();

                } else if(utenteLoggato instanceof Responsabile){
                    RESPONSABILE schermataRESPONSABILE = new RESPONSABILE(controller);
                    schermataRESPONSABILE.setVisible(true);
                    this.dispose();

                } else if (utenteLoggato instanceof Docente) {
                    DOCENTE schermataDOCENTE = new DOCENTE(controller, (model.Docente) utenteLoggato);
                    schermataDOCENTE.setVisible(true);
                    this.dispose();
                }

            } else {
                JOptionPane.showMessageDialog(this, "Email o password errati!", "Errore Login", JOptionPane.ERROR_MESSAGE);
            }
        });
        btnVaiARegistrazione.addActionListener(e -> {
            gui.REGISTRAZIONE schermataRegistrazione = new gui.REGISTRAZIONE(this.controller);
            schermataRegistrazione.setVisible(true);
            this.dispose();
        });
    }

    public static void main(String[] args) {
        controller.Controller controllerGenerale = new controller.Controller();

        SwingUtilities.invokeLater(() -> {
            gui.Home schermataIniziale = new gui.Home(controllerGenerale);
            schermataIniziale.setVisible(true);
        });
    }
}