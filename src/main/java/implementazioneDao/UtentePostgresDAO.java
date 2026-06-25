package implementazioneDao;

import dao.UtenteDAO;
import Database.DBConnection;
import model.Docente;
import model.Responsabile;
import model.Studente;
import model.Utente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtentePostgresDAO implements UtenteDAO {

    @Override
    public Utente login(String email, String password) {
        Connection conn = DBConnection.getConnection();
        Utente utenteLoggato = null;

        String queryUtente = "SELECT * FROM Utente WHERE email = ? AND password = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(queryUtente);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String ruolo = rs.getString("ruolo");

                if (ruolo.equals("STUDENTE")) {
                    String queryStudente = "SELECT * FROM Studente WHERE email = ?";
                    PreparedStatement psStud = conn.prepareStatement(queryStudente);
                    psStud.setString(1, email);
                    ResultSet rsStud = psStud.executeQuery();

                    if (rsStud.next()) {
                        String matricola = rsStud.getString("matricola");
                        int annoCorso = rsStud.getInt("anno_corso");
                        utenteLoggato = new Studente(nome, cognome, email, password, matricola, annoCorso);
                    }
                } else if (ruolo.equals("DOCENTE")) {
                    utenteLoggato = new Docente(nome, cognome, email, password);
                } else if (ruolo.equals("RESPONSABILE")) {
                    utenteLoggato = new Responsabile(nome, cognome, email, password);
                }
            }
        } catch (SQLException e) {
            System.err.println("Errore durante il login: " + e.getMessage());
        }

        return utenteLoggato;
    }

    @Override
    public boolean registraStudente(Studente studente) {
        Connection conn = DBConnection.getConnection();
        try {
            conn.setAutoCommit(false);

            String queryUtente = "INSERT INTO Utente (email, nome, cognome, password, ruolo) VALUES (?, ?, ?, ?, 'STUDENTE')";
            PreparedStatement ps1 = conn.prepareStatement(queryUtente);
            ps1.setString(1, studente.getEmail());
            ps1.setString(2, studente.getNome());
            ps1.setString(3, studente.getCognome());
            ps1.setString(4, studente.getPassword());
            ps1.executeUpdate();

            String queryStudente = "INSERT INTO Studente (matricola, email, anno_corso) VALUES (?, ?, ?)";
            PreparedStatement ps2 = conn.prepareStatement(queryStudente);
            ps2.setString(1, studente.getMatricola());
            ps2.setString(2, studente.getEmail());
            ps2.setInt(3, studente.getAnnoCorso());
            ps2.executeUpdate();

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Errore registrazione studente: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean registraDocente(Docente docente) {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO Utente (email, nome, cognome, password, ruolo) VALUES (?, ?, ?, ?, 'DOCENTE')";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, docente.getEmail());
            ps.setString(2, docente.getNome());
            ps.setString(3, docente.getCognome());
            ps.setString(4, docente.getPassword());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Errore registrazione docente: " + e.getMessage());
            return false;
        }
    }
}