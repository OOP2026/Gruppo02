package project.arch; // Aggiunto per risolvere il "Missing package statement"

import controller.Controller;
import org.junit.Test; // Modificato per usare JUnit 4
import static org.junit.Assert.*; // Modificato per usare JUnit 4

public class ControllerTest {

    @Test
    public void testMetodiCentralizzatiOrario() {
        Controller controller = new Controller();

        // Facciamo testare al sistema i metodi per alzare la copertura
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
}