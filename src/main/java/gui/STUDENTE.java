package gui;

import javax.swing.*;
import controller.Controller;
import javax.swing.table.DefaultTableModel;

public class STUDENTE extends JFrame {
    private Controller controller;
    private model.Studente studenteLoggato;
    private JTable tabellaOrario;
    private JScrollPane scrollPane1;
    private JButton logoutButton;
    private JPanel panel1;
    private JPanel panelStudente;

    public STUDENTE(Controller controller, model.Studente studenteLoggato) {
        this.controller = controller;
        this.studenteLoggato = studenteLoggato;

        setContentPane(panelStudente);
        setTitle("Orario Studente - " + studenteLoggato.getAnnoCorso() + "° Anno");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setupTabellaOrario();

        logoutButton.addActionListener(e -> controller.gestisciLogout(this));
    }

    private void setupTabellaOrario() {
        DefaultTableModel model = controller.creaModelloOrarioVuoto();
        String[] fasceOrarie = controller.getFasceOrarie();

        if (controller.getTutteLezioni() != null && studenteLoggato != null) {
            for (model.Lezione lezione : controller.getTutteLezioni()) {
                if (lezione.getInsegnamento() != null && lezione.getInsegnamento().getAnnoCorso() == studenteLoggato.getAnnoCorso()) {

                    int colonna = controller.getColonnaGiorno(lezione.getGiornoSettimana());
                    int riga = controller.getRigaOrario(lezione.getOrainizio(), fasceOrarie);

                    if (colonna != -1 && riga != -1) {
                        String cella = lezione.getInsegnamento().getNome() + " (" + lezione.getAula().getNome() + ")";
                        model.setValueAt(cella, riga, colonna);
                    }
                }
            }
        }
        tabellaOrario.setModel(model);
        tabellaOrario.setRowHeight(40);
    }

    private void createUIComponents() {
        tabellaOrario = new JTable();
        scrollPane1 = new JScrollPane();
    }
}