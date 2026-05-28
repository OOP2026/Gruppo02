package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import controller.Controller;
import model.Docente;
import model.Vincolo;

public class DOCENTE extends JFrame {
    private Controller controller;
    private model.Docente docenteLoggato;
    private Docente prof;
    private JPanel panelDocente;
    private JButton richiediSpostamentoLezioniButton;
    private JTable OrarioDocenti;
    private JButton aggiungiVincoloButton;
    private JList<String> listavincoli;
    private DefaultListModel<String> modelloVincoli;
    private JButton rimuoviVincoloButton;
    private JButton btnLogout;

    public DOCENTE(Controller controller, model.Docente docenteLoggato) {
        this.controller = controller;
        this.docenteLoggato = docenteLoggato;

        if (controller.getUtenteLoggato() instanceof Docente) {
            this.prof = (Docente) controller.getUtenteLoggato();
        } else {
            this.prof = docenteLoggato;
        }

        setContentPane(panelDocente);

        if (prof != null) {
            setTitle("Area Docente - Prof. " + prof.getCognome());
        } else {
            setTitle("Area Docente");
        }

        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setupTabellaOrario();
        setupListaVincoli();

        aggiungiVincoloButton.addActionListener((ActionEvent e) -> {
            apriPopupAggiungiVincolo();
        });

        richiediSpostamentoLezioniButton.addActionListener((ActionEvent e) -> {
            apriPopupRichiestaSpostamento();
        });

        rimuoviVincoloButton.addActionListener((ActionEvent e) -> {
            int indiceSelezionato = listavincoli.getSelectedIndex();
            if (indiceSelezionato != -1) {
                model.Vincolo vDaRimuovere = prof.getVincoli().get(indiceSelezionato);
                prof.rimuoviVincolo(vDaRimuovere);
                modelloVincoli.remove(indiceSelezionato);
            } else {
                JOptionPane.showMessageDialog(this, "Seleziona prima un vincolo dalla lista per rimuoverlo.", "Attenzione", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnLogout.addActionListener(e -> {
            int scelta = JOptionPane.showConfirmDialog(this, "Vuoi davvero uscire?", "Conferma Logout", JOptionPane.YES_NO_OPTION);
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
        String[] fasceOrarie = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};

        for (String ora : fasceOrarie) {
            model.addRow(new Object[]{ora, "", "", "", "", ""});
        }

        if (controller.getTutteLezioni() != null) {
            for (model.Lezione lezione : controller.getTutteLezioni()) {
                model.Docente docenteDellaLezione = lezione.getInsegnamento().getDocente();
                if (docenteDellaLezione != null && docenteDellaLezione.getEmail().equals(docenteLoggato.getEmail())) {
                    int colonna = getColonnaGiorno(lezione.getGiornoSettimana());
                    int riga = getRigaOrario(lezione.getOrainizio(), fasceOrarie);

                    if (colonna != -1 && riga != -1) {
                        String cella = lezione.getInsegnamento().getNome() + " (" + lezione.getAula().getNome() + ")";
                        model.setValueAt(cella, riga, colonna);
                    }
                }
            }
        }
        OrarioDocenti.setModel(model);
        OrarioDocenti.setRowHeight(40);
    }

    private int getColonnaGiorno(String giorno) {
        switch (giorno.toLowerCase()) {
            case "lunedì":
            case "lunedi":
                return 1;
            case "martedì":
            case "martedi":
                return 2;
            case "mercoledì":
            case "mercoledi":
                return 3;
            case "giovedì":
            case "giovedi":
                return 4;
            case "venerdì":
            case "venerdi":
                return 5;
            default:
                return -1;
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

    private void setupListaVincoli() {
        modelloVincoli = new DefaultListModel<>();
        listavincoli.setModel(modelloVincoli);
        if (prof != null && prof.getVincoli() != null) {
            for (Vincolo v : prof.getVincoli()) {
                modelloVincoli.addElement(v.getVincoloGiornoSettimana() + " " + v.getVincoloOraInizio() + "-" + v.getVincoloOraFine());
            }
        }
    }

    private void apriPopupAggiungiVincolo() {
        JComboBox<String> comboGiorno = new JComboBox<>(new String[]{"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"});
        String[] orariSingoli = {"08:00", "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        JComboBox<String> comboOraInizio = new JComboBox<>(orariSingoli);
        JComboBox<String> comboOraFine = new JComboBox<>(orariSingoli);

        JPanel myPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        myPanel.add(new JLabel("Giorno:"));
        myPanel.add(comboGiorno);
        myPanel.add(new JLabel("Ora Inizio:"));
        myPanel.add(comboOraInizio);
        myPanel.add(new JLabel("Ora Fine:"));
        myPanel.add(comboOraFine);

        int result = JOptionPane.showConfirmDialog(this, myPanel, "Nuovo Vincolo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String giorno = (String) comboGiorno.getSelectedItem();
            String inizio = (String) comboOraInizio.getSelectedItem();
            String fine = (String) comboOraFine.getSelectedItem();
            Vincolo nuovoVincolo = new Vincolo(giorno, inizio, fine);

            if (comboOraInizio.getSelectedIndex() >= comboOraFine.getSelectedIndex()) {
                JOptionPane.showMessageDialog(this, "Errore: L'ora di fine deve essere successiva all'ora di inizio", "ERRORE ORARIO", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (prof.aggiungiVincolo(nuovoVincolo)) {
                modelloVincoli.addElement(giorno + " " + inizio + " - " + fine);
            } else {
                JOptionPane.showMessageDialog(this, "Hai già inserito il numero massimo di 3 vincoli!", "Limite Raggiunto", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void apriPopupRichiestaSpostamento() {
        JComboBox<String> comboLezione = new JComboBox<>();
        java.util.ArrayList<model.Lezione> lezioniDelProf = controller.getLezioniDelDocente(prof);

        if (lezioniDelProf != null && !lezioniDelProf.isEmpty()) {
            for (model.Lezione l : lezioniDelProf) {
                String infoLezione = l.getInsegnamento().getNome() + " - " + l.getGiornoSettimana() + " " + l.getOrainizio() + "-" + l.getOrafine() + " (" + l.getAula().getNome() + ")";
                comboLezione.addItem(infoLezione);
            }
        } else {
            comboLezione.addItem("Nessuna lezione programmata nell'orario");
            comboLezione.setEnabled(false);
        }

        JComboBox<String> comboGiorno = new JComboBox<>(new String[]{"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì"});
        JComboBox<String> comboInizio = new JComboBox<>(new String[]{"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00", "16:00","17:00","18:00","19:00","20:00"});
        JComboBox<String> comboFine = new JComboBox<>(new String[]{"08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00", "16:00","17:00","18:00","19:00","20:00"});

        JTextArea txtNota = new JTextArea(4, 30);
        txtNota.setLineWrap(true);
        txtNota.setWrapStyleWord(true);
        JScrollPane scrollNota = new JScrollPane(txtNota);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(new JLabel("Seleziona la lezione da spostare"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(comboLezione);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(new JLabel("Nuovo giorno proposto"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(comboGiorno);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(new JLabel("Nuovo orario proposto"));
        mainPanel.add(Box.createVerticalStrut(5));

        JPanel timePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        timePanel.add(new JLabel("Inizio"));
        timePanel.add(comboInizio);
        timePanel.add(new JLabel("Fine"));
        timePanel.add(comboFine);
        timePanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        mainPanel.add(timePanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(new JLabel("Nota per il responsabile (facoltativa)"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(scrollNota);

        for (Component c : mainPanel.getComponents()) {
            if (c instanceof JComponent) {
                ((JComponent) c).setAlignmentX(Component.LEFT_ALIGNMENT);
            }
        }

        Object[] bottoni = {"Invia richiesta", "Annulla"};
        int result = JOptionPane.showOptionDialog(this, mainPanel, "11. RICHIESTA SPOSTAMENTO (DOCENTE)", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, bottoni, bottoni[0]);

        if (result == 0) {
            if (lezioniDelProf == null || lezioniDelProf.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nessuna lezione disponibile per lo spostamento.", "Attenzione", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int index = comboLezione.getSelectedIndex();
            model.Lezione lezioneSelezionata = lezioniDelProf.get(index);
            String giorno = (String) comboGiorno.getSelectedItem();
            String inizio = (String) comboInizio.getSelectedItem();
            String fine = (String) comboFine.getSelectedItem();
            String nota = txtNota.getText();

            model.RichiestaSpostamento nuovaRichiesta = new model.RichiestaSpostamento(lezioneSelezionata, giorno, inizio, fine, nota);
            controller.aggiungiRichiestaSpostamento(nuovaRichiesta);
            JOptionPane.showMessageDialog(this, "Richiesta inviata con successo al Responsabile!", "Operazione completata", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        Controller controllerTest = new Controller();
        Docente profTest = new Docente("Alan", "Turing", "alan.turing@uni.it", "password");
        profTest.aggiungiVincolo(new Vincolo("Lunedì", "08:00", "10:00"));
        profTest.aggiungiVincolo(new Vincolo("Mercoledì", "14:00", "16:00"));
        controllerTest.setUtenteLoggato(profTest);

        java.awt.EventQueue.invokeLater(() -> {
            DOCENTE frame = new DOCENTE(controllerTest, profTest);
            frame.setVisible(true);
        });
    }
}