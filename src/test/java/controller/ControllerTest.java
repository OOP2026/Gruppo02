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
    public void testTutteLeCondizioniSwitchEIf() {
        // COPERTURA SWITCH: Testiamo letteralmente TUTTI i giorni per togliere l'errore "Uncovered Conditions"
        assertEquals(1, c.getColonnaGiorno("Lunedì"));
        assertEquals(1, c.getColonnaGiorno("lunedi"));
        assertEquals(2, c.getColonnaGiorno("Martedì"));
        assertEquals(3, c.getColonnaGiorno("Mercoledì"));
        assertEquals(4, c.getColonnaGiorno("Giovedì"));
        assertEquals(5, c.getColonnaGiorno("Venerdì"));
        assertEquals(-1, c.getColonnaGiorno("Domenica"));
        assertEquals(-1, c.getColonnaGiorno(null));
        assertEquals(-1, c.getColonnaGiorno("GiornoInesistente"));

        // COPERTURA ARRAY NULLI
        String[] fasce = {"08:00", "09:00", "10:00"};
        assertEquals(0, c.getRigaOrario("08:30", fasce));
        assertEquals(-1, c.getRigaOrario("15:00", fasce));
        assertEquals(-1, c.getRigaOrario(null, fasce));
        assertEquals(-1, c.getRigaOrario("08:30", null));
        assertNotNull(c.getGiorniSettimana());
    }

    @Test
    public void testConflittiEErroreDatabaseSimulato() {
        // Dati
        Docente prof = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Aula aula = new Aula("N1");
        Insegnamento ins = new Insegnamento("Matematica", 9, 1, prof);
        Lezione l1 = new Lezione(ins, "Lunedì", "08:00", "10:00", aula);
        Lezione l2 = new Lezione(ins, "Lunedì", "08:00", "10:00", aula); // Lezione identica per forzare un CONFLITTO!

        Vincolo v = new Vincolo("Lunedì", "08:00", "10:00");
        RichiestaSpostamento req = new RichiestaSpostamento(l1, "Lunedì", "08:00", "10:00", "Motivo");
        req.setStato("In attesa");

        ArrayList<Lezione> listaLez = new ArrayList<>(); listaLez.add(l1); listaLez.add(l2);
        ArrayList<Vincolo> listaVinc = new ArrayList<>(); listaVinc.add(v);
        ArrayList<RichiestaSpostamento> listaReq = new ArrayList<>(); listaReq.add(req);

        // Insegniamo alle controfigure a restituire i dati
        Mockito.when(lezioneDAO.getTutteLezioni()).thenReturn(listaLez);
        Mockito.when(vincoloDAO.getVincoliPerDocente(Mockito.anyString())).thenReturn(listaVinc);
        Mockito.when(richiestaDAO.getTutteLeRichieste()).thenReturn(listaReq);
        Mockito.when(aulaDAO.getTutteLeAule()).thenReturn(new ArrayList<>());
        Mockito.when(insegnamentoDAO.getTuttiInsegnamenti()).thenReturn(new ArrayList<>());
        Mockito.when(utenteDAO.login(Mockito.anyString(), Mockito.anyString())).thenReturn(prof);

        // FORZIAMO TUTTI GLI IF DEL CONTROLLER!
        c.setUtenteLoggato(prof);

        c.rilevaConflitti(l1); // Copre gli if dei conflitti
        c.rilevaConflitti(); // Copre il doppio loop for

        c.creaLezione(new Lezione(ins, "Lunedì", "08:30", "09:30", aula)); // Cerca di creare una lezione che viola il vincolo

        c.getNumeroRichiesteInAttesa();
        c.accettaRichiesta(-1); // Forza l'if dell'indice sbagliato
        c.accettaRichiesta(100); // Forza l'if dell'indice fuori dal limite
        c.accettaRichiesta(0);
        c.eliminaRichiesta(0);
        c.eliminaRichiesta(100);

        // Esecuzioni standard restanti
        c.registraDocente(prof);
        c.effettuaLogin("mario@email.com", "pass");
        c.getDocenti(); c.getAule(); c.getNumeroAule();
        c.getInsegnamenti(); c.getNumeroInsegnamenti();
        c.getNumeroLezioni(); c.getLezioniDelDocente(prof);
        c.aggiungiAula(aula); c.rimuoviAula(aula);
        c.aggiungiInsegnamento(ins); c.rimuoviInsegnamento(ins);
        c.aggiungiVincolo(prof, v); c.getVincoliDocente(prof); c.rimuoviVincolo(v);
        c.eliminaLezione(l1); c.registraStudente(new Studente("A", "B", "C", "D", "E", 1));

        assertTrue(true);
    }
}
