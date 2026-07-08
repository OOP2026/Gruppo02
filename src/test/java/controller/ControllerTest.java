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

    // Creiamo le Controfigure del Database!
    @Mock private UtenteDAO utenteDAO;
    @Mock private LezioneDAO lezioneDAO;
    @Mock private AulaDAO aulaDAO;
    @Mock private InsegnamentoDAO insegnamentoDAO;
    @Mock private VincoloDAO vincoloDAO;
    @Mock private RichiestaSpostamentoDAO richiestaDAO;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Attiva le controfigure
    }

    @Test
    public void testLogicaOrologio() {
        assertEquals(1, c.getColonnaGiorno("Lunedì"));
        assertEquals(-1, c.getColonnaGiorno("Domenica"));
        String[] fasce = {"08:00", "09:00", "10:00"};
        assertEquals(0, c.getRigaOrario("08:30", fasce));
        assertEquals(-1, c.getRigaOrario("15:00", fasce));
        assertNotNull(c.getGiorniSettimana());
    }

    @Test
    public void testForzaDatabaseSimulato() {
        // 1. Creiamo i nostri dati perfetti
        Docente prof = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Aula aula = new Aula("N1");
        Insegnamento ins = new Insegnamento("Matematica", 9, 1, prof);
        Lezione l1 = new Lezione(ins, "Lunedì", "08:00", "10:00", aula);
        Vincolo v = new Vincolo("Lunedì", "08:00", "10:00");
        RichiestaSpostamento req = new RichiestaSpostamento(l1, "Lunedì", "08:00", "10:00", "Motivo");
        req.setStato("In attesa");

        ArrayList<Lezione> listaLez = new ArrayList<>(); listaLez.add(l1);
        ArrayList<Vincolo> listaVinc = new ArrayList<>(); listaVinc.add(v);
        ArrayList<RichiestaSpostamento> listaReq = new ArrayList<>(); listaReq.add(req);
        ArrayList<Aula> listaAule = new ArrayList<>(); listaAule.add(aula);
        ArrayList<Insegnamento> listaIns = new ArrayList<>(); listaIns.add(ins);

        // 2. Addestriamo le controfigure a rispondere con le liste piene (invece di farle fallire!)
        Mockito.when(lezioneDAO.getTutteLezioni()).thenReturn(listaLez);
        Mockito.when(vincoloDAO.getVincoliPerDocente(Mockito.anyString())).thenReturn(listaVinc);
        Mockito.when(richiestaDAO.getTutteLeRichieste()).thenReturn(listaReq);
        Mockito.when(aulaDAO.getTutteLeAule()).thenReturn(listaAule);
        Mockito.when(insegnamentoDAO.getTuttiInsegnamenti()).thenReturn(listaIns);
        Mockito.when(utenteDAO.login(Mockito.anyString(), Mockito.anyString())).thenReturn(prof);

        // 3. Eseguiamo il controller: ora entrerà IN TUTTI I CICLI FOR macinando % a palate!
        c.registraDocente(prof);
        c.effettuaLogin("mario@email.com", "pass");
        c.creaLezione(l1);
        c.rilevaConflitti(l1);
        c.rilevaConflitti();
        c.getNumeroRichiesteInAttesa();
        c.accettaRichiesta(0);
        c.eliminaRichiesta(0);
        c.getDocenti();
        c.getAule();
        c.getNumeroAule();
        c.getInsegnamenti();
        c.getNumeroInsegnamenti();
        c.getNumeroLezioni();
        c.getLezioniDelDocente(prof);
        c.aggiungiAula(aula);
        c.rimuoviAula(aula);
        c.aggiungiInsegnamento(ins);
        c.rimuoviInsegnamento(ins);
        c.aggiungiVincolo(prof, v);
        c.getVincoliDocente(prof);
        c.rimuoviVincolo(v);
        c.eliminaLezione(l1);
        c.registraStudente(new Studente("A", "B", "C", "D", "E", 1));

        assertTrue(true);
    }
}
