package dao;
import model.Vincolo;
import java.util.ArrayList;

public interface VincoloDAO {
    ArrayList<Vincolo> getVincoliPerDocente(String email);
    void inserisciVincolo(Vincolo v, String emailDocente);
}
