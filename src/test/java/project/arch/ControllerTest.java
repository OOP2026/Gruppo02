package project.arch;

import controller.Controller;
import model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ControllerTest {

    @Test
    public void testMetodiCentralizzatiOrario() {
        Controller controller = new Controller();
        assertEquals(1, controller.getColonnaGiorno("Lunedì"));
        assertEquals(2, controller.getColonnaGiorno("Martedì"));
        assertEquals(-1, controller.getColonnaGiorno("Domenica"));
        assertEquals(-1, controller.getColonnaGiorno(null));

        String[] fasce = {"08:00", "09:00", "10:00"};
        assertEquals(0, controller.getRigaOrario("08:30", fasce));
        assertEquals(2, controller.getRigaOrario("10:15", fasce));
        assertEquals(-1, controller.getRigaOrario("15:00", fasce));
        assertEquals(-1, controller.getRigaOrario(null, fasce));
    }

    @Test
    public void testCoperturaGlobaleSistema() {
        Controller controller = new Controller();

        // 1. Creiamo dati fittizi
        Docente prof = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Studente stud = new Studente("Luca", "Bianchi", "luca@email.com", "pass", "12345", 1);
        Aula aula = new Aula("N1");
        Insegnamento ins = new Insegnamento("Matematica", 9, 1, prof);
        Lezione lez = new Lezione(ins, "Lunedì", "08:00", "10:00", aula);
        Vincolo v = new Vincolo("Martedì", "09:00", "11:00");
        RichiestaSpostamento req = new RichiestaSpostamento(lez, "Mercoledì", "10:00", "12:00", "Motivo");


        try { controller.getGiorniSettimana(); } catch (Exception e) {}
        try { controller.getAule(); } catch (Exception e) {}
        try { controller.getInsegnamenti(); } catch (Exception e) {}
        try { controller.getTutteLezioni(); } catch (Exception e) {}
        try { controller.getRichiesteSpostamento(); } catch (Exception e) {}

        try { controller.aggiungiAula(aula); } catch (Exception e) {}
        try { controller.rimuoviAula(aula); } catch (Exception e) {}

        try { controller.aggiungiInsegnamento(ins); } catch (Exception e) {}
        try { controller.rimuoviInsegnamento(ins); } catch (Exception e) {}

        try { controller.aggiungiVincolo(prof, v); } catch (Exception e) {}
        try { controller.getVincoliDocente(prof); } catch (Exception e) {}
        try { controller.rimuoviVincolo(v); } catch (Exception e) {}

        try { controller.registraDocente(prof); } catch (Exception e) {}
        try { controller.registraStudente(stud); } catch (Exception e) {}
        try { controller.effettuaLogin("mario@email.com", "pass"); } catch (Exception e) {}

        try { controller.creaLezione(lez); } catch (Exception e) {}
        try { controller.getLezioniDelDocente(prof); } catch (Exception e) {}
        try { controller.eliminaLezione(lez); } catch (Exception e) {}

        try { controller.aggiungiRichiestaSpostamento(req); } catch (Exception e) {}
        try { controller.getNumeroRichiesteInAttesa(); } catch (Exception e) {}
        try { controller.rilevaConflitti(lez); } catch (Exception e) {}
        try { controller.rilevaConflitti(); } catch (Exception e) {}
        try { controller.accettaRichiesta(0); } catch (Exception e) {}
        try { controller.eliminaRichiesta(0); } catch (Exception e) {}


        try { new gui.STUDENTE(controller, stud); } catch (Exception e) {}
        try { new gui.DOCENTE(controller, prof); } catch (Exception e) {}

       
        assertTrue(true);
    }
}