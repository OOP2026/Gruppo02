package model;

import org.junit.Test;
import static org.junit.Assert.*;

public class ModelTest {

    @Test
    public void testCreazioneUtenti() {
        Docente d = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        assertNotNull(d);
        Studente s = new Studente("Luca", "Bianchi", "luca@email.com", "pass", "12345", 2);
        assertNotNull(s);
        Responsabile r = new Responsabile("Anna", "Neri", "anna@email.com", "admin");
        assertNotNull(r);
    }

    @Test
    public void testCreazioneDidattica() {
        Aula a = new Aula("N1");
        assertNotNull(a);
        Docente d = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Insegnamento i = new Insegnamento("Analisi", 9, 1, d);
        assertNotNull(i);
        Lezione l = new Lezione(i, "Lunedì", "08:00", "10:00", a);
        assertNotNull(l);
    }

    @Test
    public void testCreazioneLogica() {
        Vincolo v = new Vincolo("Martedì", "09:00", "11:00");
        assertNotNull(v);
        Aula a = new Aula("N1");
        Docente d = new Docente("Mario", "Rossi", "mario@email.com", "pass");
        Insegnamento i = new Insegnamento("Analisi", 9, 1, d);
        Lezione l = new Lezione(i, "Lunedì", "08:00", "10:00", a);
        RichiestaSpostamento req = new RichiestaSpostamento(l, "Mercoledì", "10:00", "12:00", "Motivo X");
        assertNotNull(req);
    }
}
