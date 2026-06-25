package dao;

import model.Lezione;
import java.util.ArrayList;

public interface LezioneDAO {
    boolean inserisciLezione(Lezione lezione);
    ArrayList<Lezione> getTutteLezioni();
    ArrayList<Lezione> getLezioniDelDocente(String emailDocente);
}
