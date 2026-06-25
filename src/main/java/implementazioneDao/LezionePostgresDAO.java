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
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO Lezione (insegnamento_nome, giorno_settimana, ora_inizio, ora_fine, aula_nome) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, lezione.getInsegnamento().getNome());
            ps.setString(2, lezione.getGiornoSettimana());
            ps.setString(3, lezione.getOrainizio());
            ps.setString(4, lezione.getOrafine());
            ps.setString(5, lezione.getAula().getNome());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Errore inserimento lezione: " + e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Lezione> getTutteLezioni() {
        ArrayList<Lezione> listaLezioni = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM Lezione";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Insegnamento ins = new Insegnamento(rs.getString("insegnamento_nome"), 0, 0, null);
                Aula aula = new Aula(rs.getString("aula_nome"));

                Lezione l = new Lezione(ins, rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine"), aula);
                listaLezioni.add(l);
            }
        } catch (SQLException e) {
            System.err.println("Errore recupero lezioni: " + e.getMessage());
        }

        return listaLezioni;
    }

    @Override
    public ArrayList<Lezione> getLezioniDelDocente(String emailDocente) {
        ArrayList<Lezione> listaLezioni = new ArrayList<>();
        Connection conn = DBConnection.getConnection();

        String query = "SELECT l.* FROM Lezione l JOIN Insegnamento i ON l.insegnamento_nome = i.nome WHERE i.docente_email = ?";

        try {
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, emailDocente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Docente profFittizio = new Docente("", "", emailDocente, "");
                Insegnamento ins = new Insegnamento(rs.getString("insegnamento_nome"), 0, 0, profFittizio);
                Aula aula = new Aula(rs.getString("aula_nome"));

                Lezione l = new Lezione(ins, rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine"), aula);
                listaLezioni.add(l);
            }
        } catch (SQLException e) {
            System.err.println("Errore recupero lezioni docente: " + e.getMessage());
        }

        return listaLezioni;
    }
}