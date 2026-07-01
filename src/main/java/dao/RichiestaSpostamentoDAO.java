package dao;

import model.RichiestaSpostamento;
import java.util.ArrayList;

public interface RichiestaSpostamentoDAO {
    ArrayList<RichiestaSpostamento> getTutteLeRichieste();
    void inserisciRichiesta(RichiestaSpostamento r);
    boolean eliminaRichiesta(RichiestaSpostamento r);
}