package gui;

import javax.swing.*;
import controller.Controller;
import javax.swing.table.DefaultTableModel;

public class STUDENTE extends JFrame{
    private Controller controller;
    private model.Studente studenteLoggato;
    private JTable tabellaOrario;
    private JScrollPane scrollPane1;
    private JButton logoutButton;
    private JPanel panel1;
    private JPanel panelStudente;

    public STUDENTE(Controller controller, model.Studente studenteLoggato) {
        this.controller = controller;
        setContentPane(panelStudente);
        setTitle("Orario Studente - "+studenteLoggato.getAnnoCorso()+"° Anno");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupTabellaOrario();
        logoutButton.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(this, "Sei sicuro di voler uscire dal tuo account?", "Conferma Logout", JOptionPane.YES_NO_OPTION);
            if (scelta == JOptionPane.YES_OPTION) {
                this.dispose();
                gui.Home schermataLogin = new gui.Home(this.controller);
                schermataLogin.setVisible(true);
            }
        });
    }
    private void setupTabellaOrario() {
        String[] colonne = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        String[] fasceOrarie = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00","19:00", "20:00"};
        for (String ora : fasceOrarie) {
            model.addRow(new Object[]{ora, "", "", "", "", ""});
        }
        for (model.Lezione lezione : controller.getTutteLezioni()) {
            if (lezione.getInsegnamento().getAnnoCorso() == studenteLoggato.getAnnoCorso()) {

                int colonna = getColonnaGiorno(lezione.getGiornoSettimana());
                int riga = getRigaOrario(lezione.getOrainizio(), fasceOrarie);

                if (colonna != -1 && riga != -1) {
                    String cella = lezione.getInsegnamento().getNome() + " (" + lezione.getAula().getNome() + ")";
                    model.setValueAt(cella, riga, colonna);
                }
            }
        }
        tabellaOrario.setModel(model);
        tabellaOrario.setRowHeight(40);
    }
    private int getColonnaGiorno(String giorno) {
        switch (giorno.toLowerCase()) {
            case "lunedì": case "lunedi": return 1;
            case "martedì": case "martedi": return 2;
            case "mercoledì": case "mercoledi": return 3;
            case "giovedì": case "giovedi": return 4;
            case "venerdì": case "venerdi": return 5;
            default: return -1;
        }
    }
    private int getRigaOrario(String oraInizio, String[] fasceOrarie) {
        for (int i = 0; i < fasceOrarie.length; i++) {
            if (oraInizio.startsWith(fasceOrarie[i].substring(0, 2))) {
                return i;
            }
        }
        return -1;
    }
    private void createUIComponents() {
        tabellaOrario = new JTable();
        scrollPane1 = new JScrollPane();
    }

    public static void main(String[] args) {
        Controller fintoController = new Controller();
        model.Studente fintoStudente = new model.Studente("Mario", "Rossi", "mario@email.it", "password123", "MAT001", 2);

        SwingUtilities.invokeLater(() -> {
            STUDENTE finestraTest = new STUDENTE(fintoController, fintoStudente);
            finestraTest.setVisible(true);
        });
    }
}



