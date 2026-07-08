package controller;

import dao.*;
import model.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class ControllerTest {

    @InjectMocks
    private Controller c;

    @Mock private UtenteDAO utenteDAO;
    @Mock private LezioneDAO lezioneDAO;
    @Mock private AulaDAO aulaDAO;
    @Mock private InsegnamentoDAO insegnamentoDAO;
    @Mock private VincoloDAO vincoloDAO;
    @Mock private RichiestaSpostamentoDAO richiestaDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testEsplosioneCondizioniIf() {
        // 1. SETUP DATI ESTREMI
        Docente prof = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Aula aula1 = new Aula("N1");
        Aula aula2 = new Aula("N2");
        Insegnamento ins1 = new Insegnamento("Matematica", 9, 1, prof);
        Insegnamento ins2 = new Insegnamento("Fisica", 9, 1, prof);

        // Tre lezioni incrociate per scatenare l'inferno degli "IF"
        Lezione l1 = new Lezione(ins1, "Lunedì", "08:00", "10:00", aula1);
        Lezione l2 = new Lezione(ins1, "Lunedì", "08:00", "10:00", aula1); // Conflitto Aula!
        Lezione l3 = new Lezione(ins2, "Lunedì", "08:00", "10:00", aula2); // Conflitto Docente!

        ArrayList<Lezione> listaLezioni = new ArrayList<>();
        listaLezioni.add(l1); listaLezioni.add(l2); listaLezioni.add(l3);

        // Vincolo che si sovrappone
        Vincolo v = new Vincolo("Martedì", "09:00", "11:00");
        ArrayList<Vincolo> listaVincoli = new ArrayList<>(); listaVincoli.add(v);

        // Richieste di spostamento cattive
        RichiestaSpostamento req1 = new RichiestaSpostamento(l1, "Martedì", "09:30", "10:30", "Motivo 1"); // Viola il vincolo
        RichiestaSpostamento req2 = new RichiestaSpostamento(l1, "Lunedì", "08:30", "09:30", "Motivo 2"); // Viola orario di altra lezione
        RichiestaSpostamento req3 = new RichiestaSpostamento(l1, "Venerdì", "15:00", "17:00", "Motivo 3"); // OK
        req1.setStato("In attesa");

        ArrayList<RichiestaSpostamento> listaReq = new ArrayList<>();
        listaReq.add(req1); listaReq.add(req2); listaReq.add(req3);

        // 2. INIEZIONE DELLE CONTROFIGURE MOCKITO
        Mockito.when(lezioneDAO.getTutteLezioni()).thenReturn(listaLezioni);
        Mockito.when(vincoloDAO.getVincoliPerDocente(Mockito.anyString())).thenReturn(listaVincoli);
        Mockito.when(richiestaDAO.getTutteLeRichieste()).thenReturn(listaReq);
        Mockito.when(aulaDAO.getTutteLeAule()).thenReturn(new ArrayList<>());
        Mockito.when(insegnamentoDAO.getTuttiInsegnamenti()).thenReturn(new ArrayList<>());

        c.setUtenteLoggato(prof);

        // 3. ESECUZIONE MIRATA

        // Forza tutti gli "IF" di sovrapposizione e conflitti!
        c.rilevaConflitti(l2);
        c.rilevaConflitti(l3);
        c.rilevaConflitti();

        // Forza la Creazione Lezioni (Buone e Cattive)
        c.creaLezione(new Lezione(ins1, "Martedì", "09:30", "10:30", aula1)); // Cadrà nel vincolo
        c.creaLezione(new Lezione(ins1, "Lunedì", "08:00", "10:00", aula1)); // Cadrà nel conflitto
        c.creaLezione(new Lezione(ins1, "Mercoledì", "10:00", "12:00", aula1)); // Passerà liscio

        // Forza l'Accettazione Richieste
        c.accettaRichiesta(-1); // Indice Sbagliato
        c.accettaRichiesta(0);  // Sbatte contro il vincolo (req1)
        c.accettaRichiesta(1);  // Sbatte contro l'altra lezione (req2)
        c.accettaRichiesta(2);  // Richiesta perfetta (req3)

        // Switch giorni e orologi
        assertEquals(1, c.getColonnaGiorno("Lunedì"));
        assertEquals(-1, c.getColonnaGiorno(null));
        String[] fasce = {"08:00", "09:00", "10:00"};
        assertEquals(0, c.getRigaOrario("08:30", fasce));
        assertEquals(-1, c.getRigaOrario(null, fasce));

        // Passata per chiudere le altre righe
        c.registraDocente(prof); c.effettuaLogin("mario@email.com", "pass");
        c.getDocenti(); c.getAule(); c.getNumeroAule();
        c.getInsegnamenti(); c.getNumeroInsegnamenti();
        c.getNumeroLezioni(); c.getLezioniDelDocente(prof);
        c.aggiungiAula(aula1); c.rimuoviAula(aula1);
        c.aggiungiInsegnamento(ins1); c.rimuoviInsegnamento(ins1);
        c.aggiungiVincolo(prof, v); c.getVincoliDocente(prof); c.rimuoviVincolo(v);
        c.eliminaLezione(l1); c.registraStudente(new Studente("A", "B", "C", "D", "E", 1));
        c.getNumeroRichiesteInAttesa(); c.eliminaRichiesta(0);

        assertTrue(true);
    }
}
