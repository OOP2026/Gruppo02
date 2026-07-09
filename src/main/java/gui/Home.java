package gui;

import javax.swing.*;
import controller.Controller;
import model.Docente;
import model.Responsabile;
import model.Studente;
import model.Utente;
/**
 * <h1>Capitolo 6: Manuale d'uso - Schermata di Login</h1>
 * <p>La schermata di accesso è la prima cosa che l'utente vede quando avvia l'applicazione.
 * Serve a proteggere i dati richiedendo l'inserimento di un'Email e di una Password.</p>
 * * <img src="doc-files/login.png" alt="Schermata di Login" style="max-width: 80%; border: 1px solid black;">
 * <p>Quando l'utente clicca sul pulsante per accedere, il Controller controlla nel database
 * il ruolo dell'utente (Studente, Docente o Responsabile) e apre la dashboard corretta.</p>
 */
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
}