package dao;

import model.Insegnamento;
import java.util.ArrayList;

public interface InsegnamentoDAO {
    ArrayList<Insegnamento> getTuttiInsegnamenti();
    boolean inserisciInsegnamento(Insegnamento i);
    boolean eliminaInsegnamento(Insegnamento i);
}