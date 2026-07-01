package dao;

import model.Aula;
import java.util.ArrayList;

public interface AulaDAO {
    ArrayList<Aula> getTutteLeAule();


    boolean inserisciAula(Aula aula);
    boolean eliminaAula(Aula aula);
}