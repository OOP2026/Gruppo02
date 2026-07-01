package implementazioneDao;

import dao.LezioneDAO;
import Database.DBConnection;
import model.Aula;
import model.Docente;
import model.Insegnamento;
import model.Lezione;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LezionePostgresDAO implements LezioneDAO {

    @Override
    public boolean inserisciLezione(Lezione lezione) {
        String query = "INSERT INTO Lezione (insegnamento_nome, giorno_settimana, ora_inizio, ora_fine, aula_nome) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, lezione.getInsegnamento().getNome());
            ps.setString(2, lezione.getGiornoSettimana());
            ps.setString(3, lezione.getOrainizio());
            ps.setString(4, lezione.getOrafine());
            ps.setString(5, lezione.getAula().getNome());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override
    public ArrayList<Lezione> getTutteLezioni() {
        ArrayList<Lezione> listaLezioni = new ArrayList<>();
        String query = "SELECT l.*, i.cfu, i.anno_corso, u.email, u.nome AS prof_nome, u.cognome AS prof_cognome, u.password " +
                "FROM Lezione l " +
                "JOIN Insegnamento i ON l.insegnamento_nome = i.nome " +
                "JOIN Utente u ON i.docente_email = u.email";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Docente docente = new Docente(rs.getString("prof_nome"), rs.getString("prof_cognome"), rs.getString("email"), rs.getString("password"));
                Insegnamento ins = new Insegnamento(rs.getString("insegnamento_nome"), rs.getInt("cfu"), rs.getInt("anno_corso"), docente);
                Aula aula = new Aula(rs.getString("aula_nome"));
                Lezione l = new Lezione(ins, rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine"), aula);
                listaLezioni.add(l);
            }
        } catch (SQLException e) { System.err.println("Errore recupero lezioni: " + e.getMessage()); }
        return listaLezioni;
    }

    @Override
    public ArrayList<Lezione> getLezioniDelDocente(String emailDocente) {
        ArrayList<Lezione> listaLezioni = new ArrayList<>();
        String query = "SELECT l.*, i.cfu, i.anno_corso, u.email, u.nome AS prof_nome, u.cognome AS prof_cognome, u.password " +
                "FROM Lezione l JOIN Insegnamento i ON l.insegnamento_nome = i.nome JOIN Utente u ON i.docente_email = u.email " +
                "WHERE i.docente_email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, emailDocente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Docente docente = new Docente(rs.getString("prof_nome"), rs.getString("prof_cognome"), rs.getString("email"), rs.getString("password"));
                    Insegnamento ins = new Insegnamento(rs.getString("insegnamento_nome"), rs.getInt("cfu"), rs.getInt("anno_corso"), docente);
                    Aula aula = new Aula(rs.getString("aula_nome"));
                    Lezione l = new Lezione(ins, rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine"), aula);
                    listaLezioni.add(l);
                }
            }
        } catch (SQLException e) { System.err.println("Errore recupero lezioni docente: " + e.getMessage()); }
        return listaLezioni;
    }

    @Override
    public boolean eliminaLezione(Lezione lezione) {
        String query = "DELETE FROM Lezione WHERE insegnamento_nome = ? AND giorno_settimana = ? AND ora_inizio = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, lezione.getInsegnamento().getNome());
            ps.setString(2, lezione.getGiornoSettimana());
            ps.setString(3, lezione.getOrainizio());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }

    @Override
    public boolean aggiornaLezione(Lezione lezione) {
        // NON USATO - L'aggiornamento viene gestito nel controller tramite Elimina + Inserisci
        return false;
    }
}