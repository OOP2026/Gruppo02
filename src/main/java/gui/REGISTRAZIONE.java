package gui;

import controller.Controller;

import javax.swing.*;
public class REGISTRAZIONE extends javax.swing.JFrame {
    private controller.Controller controller;
    private JPanel panelRegistrazione;
    private JTextField txtNome;
    private JTextField txtCognome;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JRadioButton radioStudente;
    private JRadioButton radioDocente;
    private JTextField txtMatricola;
    private JTextField txtMateria;
    private JButton btnRegistrati;
    private JComboBox comboAnno;
    private JLabel lblMatricola;
    private JLabel lblMateria;
    private JLabel lblAnno;

    public REGISTRAZIONE(Controller controller) {
        this.setContentPane(panelRegistrazione);
        this.setTitle("Registrazione Utente");
        this.controller = controller;
        radioStudente.setSelected(true);
        txtMateria.setVisible(false);
        radioStudente.addActionListener(e -> {
            txtMatricola.setVisible(true);
            lblMatricola.setVisible(true);
            lblAnno.setVisible(true);
            comboAnno.setVisible(true);
            txtMateria.setVisible(false);
            lblMateria.setVisible(false);
        });
        radioDocente.addActionListener(e -> {
            txtMatricola.setVisible(false);
            lblMatricola.setVisible(false);
            comboAnno.setVisible(false);
            lblAnno.setVisible(false);
            txtMateria.setVisible(true);
            lblMateria.setVisible(true);
        });
        btnRegistrati.addActionListener(e -> {
            String nome = txtNome.getText().trim();
            String cognome = txtCognome.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());

            if (nome.isEmpty() || cognome.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Compila tutti i campi base (Nome, Cognome, Email e Password)!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (radioStudente.isSelected()) {
                String matricola = txtMatricola.getText().trim();
                if (matricola.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci la tua matricola!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                String annoStringa = comboAnno.getSelectedItem().toString();
                int anno = Integer.parseInt(annoStringa);
                model.Studente nuovoStudente = new model.Studente(nome, cognome, email, password, matricola, anno);
                controller.registraStudente(nuovoStudente);
                JOptionPane.showMessageDialog(null, "Studente registrato con successo!\nMatricola: " + matricola, "Benvenuto", JOptionPane.INFORMATION_MESSAGE);
            } else if (radioDocente.isSelected()) {
                String materia = txtMateria.getText().trim();

                if (materia.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Inserisci la materia che insegni!", "Attenzione", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                model.Docente nuovoDocente = new model.Docente(nome, cognome, email, password);
                controller.registraDocente(nuovoDocente);
                JOptionPane.showMessageDialog(null, "Docente registrato con successo!\nMateria: " + materia, "Benvenuto", JOptionPane.INFORMATION_MESSAGE);
            }
            this.dispose();
            gui.Home schermataLogin = new gui.Home(this.controller);
            schermataLogin.setVisible(true);
        });
    }

}

