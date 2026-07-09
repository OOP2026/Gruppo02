package gui;

import javax.swing.*;
import controller.Controller;
import javax.swing.table.DefaultTableModel;
/**
 * <h2>Area Studente</h2>
 * <p>Questa interfaccia è dedicata agli studenti iscritti all'università. È strutturata
 * per essere semplice e di immediata consultazione.</p>
 * * * <img src="doc-files/dash_studente.png" alt="Dashboard Studente" style="max-width: 80%; border: 1px solid black;">
 * * <h3>Funzionalità principali:</h3>
 * <ul>
 * <li><b>Visualizzazione Orario Personalizzato:</b> Mostra una tabella dinamica che filtra e visualizza in automatico solo le lezioni relative all'anno di corso dello studente attualmente loggato.</li>
 * <li><b>Logout Sicuro:</b> Pulsante per chiudere la sessione e tornare alla pagina di accesso iniziale.</li>
 * </ul>
 */
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

        logoutButton.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(this, "Vuoi davvero uscire dal tuo account?", "Conferma Logout", JOptionPane.YES_NO_OPTION);
            if (scelta == JOptionPane.YES_OPTION) {
                this.dispose();
                new Home(controller).setVisible(true);
            }
        });
    }

    private void setupTabellaOrario() {
        String[] fasceOrarie = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        String[] colonne = {"Orario", "Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"};
        DefaultTableModel model = new DefaultTableModel(colonne, 0);
        for (String ora : fasceOrarie) {
            model.addRow(new Object[]{ora, "", "", "", "", ""});
        }

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
