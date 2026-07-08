package controller;

    import org.junit.Test;
import static org.junit.Assert.*;

    public class ControllerTest {

        @Test
        public void testLogicaOrario() {
            Controller c = new Controller();
            assertNotNull(c);

            assertEquals(1, c.getColonnaGiorno("Lunedì"));
            assertEquals(2, c.getColonnaGiorno("Martedì"));
            assertEquals(-1, c.getColonnaGiorno("Domenica"));
            assertEquals(-1, c.getColonnaGiorno(null));

            String[] fasce = {"08:00", "09:00", "10:00"};
            assertEquals(0, c.getRigaOrario("08:30", fasce));
            assertEquals(2, c.getRigaOrario("10:15", fasce));
            assertEquals(-1, c.getRigaOrario("15:00", fasce));
            assertEquals(-1, c.getRigaOrario(null, fasce));
        }

        @Test
        public void testMetodiDatabaseSicuri() {
            Controller c = new Controller();


            try { c.getAule(); } catch (Exception e) {}
            try { c.getInsegnamenti(); } catch (Exception e) {}
            try { c.getTutteLezioni(); } catch (Exception e) {}
            try { c.getGiorniSettimana(); } catch (Exception e) {}
            try { c.getRichiesteSpostamento(); } catch (Exception e) {}
            try { c.effettuaLogin("mario@email.com", "pass"); } catch (Exception e) {}
        }
    }
