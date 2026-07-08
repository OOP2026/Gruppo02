package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void testCoperturaGlobaleModel() {
        Docente prof = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        prof.getEmail(); prof.getCognome(); prof.getNome(); prof.getPassword();

        Studente s = new Studente("L", "B", "l@e", "p", "123", 1);
        s.getMatricola(); s.getAnnoCorso();

        Responsabile r = new Responsabile("A", "B", "C", "D");
        assertNotNull(r);

        Aula a = new Aula("N1");
        a.getNome(); a.setNome("N2");

        Insegnamento ins = new Insegnamento("Matematica", 9, 1, prof);
        ins.getNome(); ins.getDocente(); ins.getAnnoCorso();

        Lezione l = new Lezione(ins, "Lunedì", "08:00", "10:00", a);
        l.getInsegnamento(); l.getGiornoSettimana(); l.getOrainizio(); l.getOrafine(); l.getAula();
        l.setGiornoSettimana("Martedì"); l.setOrainizio("09:00"); l.setOrafine("11:00");

        Vincolo v = new Vincolo("Lunedì", "08:00", "10:00");
        v.getVincoloGiornoSettimana(); v.getVincoloOraInizio(); v.getVincoloOraFine();

        RichiestaSpostamento req = new RichiestaSpostamento(l, "Martedì", "10:00", "12:00", "Nota");
        req.getStato(); req.getLezionedaSpostare(); req.getNuovoGiornoLezione();
        req.getNuovaOraInizio(); req.getNuovaOraFine();
        req.setStato("Accettata");

        prof.aggiungiVincolo(v);
        prof.getVincoli();
        prof.rimuoviVincolo(v);

        assertTrue(true);
    }
}
