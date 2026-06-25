package dao;

import model.Docente;
import model.Studente;
import model.Utente;

public interface UtenteDAO {
    Utente login(String email, String password);
    boolean registraStudente(Studente studente);
    boolean registraDocente(Docente docente);
}