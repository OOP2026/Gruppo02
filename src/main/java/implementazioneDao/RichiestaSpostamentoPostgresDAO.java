package implementazioneDao;

import dao.RichiestaSpostamentoDAO;
import Database.DBConnection;
import model.*;
import java.sql.*;
import java.util.ArrayList;

public class RichiestaSpostamentoPostgresDAO implements RichiestaSpostamentoDAO {

    @Override
    public ArrayList<RichiestaSpostamento> getTutteLeRichieste() {
        ArrayList<RichiestaSpostamento> lista = new ArrayList<>();

        String query = "SELECT r.*, l.ora_inizio, l.ora_fine, l.aula_nome, " +
                "i.cfu, i.anno_corso, u.email, u.nome AS prof_nome, u.cognome AS prof_cognome, u.password " +
                "FROM RichiestaSpostamento r " +
                "JOIN Lezione l ON r.insegnamento_nome = l.insegnamento_nome AND r.vecchio_giorno = l.giorno_settimana " +
                "JOIN Insegnamento i ON l.insegnamento_nome = i.nome " +
                "JOIN Utente u ON i.docente_email = u.email";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Docente docente = new Docente(
                        rs.getString("prof_nome"), rs.getString("prof_cognome"),
                        rs.getString("email"), rs.getString("password")
                );


                Insegnamento ins = new Insegnamento(rs.getString("insegnamento_nome"), rs.getInt("cfu"), rs.getInt("anno_corso"), docente);


                Aula aula = new Aula(rs.getString("aula_nome"));
                Lezione l = new Lezione(ins, rs.getString("vecchio_giorno"), rs.getString("ora_inizio"), rs.getString("ora_fine"), aula);


                RichiestaSpostamento r = new RichiestaSpostamento(
                        l, rs.getString("nuovo_giorno"), rs.getString("nuova_ora_inizio"),
                        rs.getString("nuova_ora_fine"), rs.getString("motivazione")
                );
                r.setStato(rs.getString("stato"));
                lista.add(r);
            }
        } catch (SQLException e) {
            System.err.println("Errore caricamento richieste: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public void inserisciRichiesta(RichiestaSpostamento r) {
        String query = "INSERT INTO RichiestaSpostamento (insegnamento_nome, vecchio_giorno, nuovo_giorno, " +
                "nuova_ora_inizio, nuova_ora_fine, motivazione, stato) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getLezionedaSpostare().getInsegnamento().getNome());
            ps.setString(2, r.getLezionedaSpostare().getGiornoSettimana());
            ps.setString(3, r.getNuovoGiornoLezione());
            ps.setString(4, r.getNuovaOraInizio());
            ps.setString(5, r.getNuovaOraFine());
            ps.setString(6, r.getMotivazione());
            ps.setString(7, r.getStato());
            ps.executeUpdate();
        } catch (SQLException e) { System.err.println("Errore inserimento richiesta: " + e.getMessage()); }
    }

    @Override
    public boolean eliminaRichiesta(RichiestaSpostamento r) {
        String query = "DELETE FROM RichiestaSpostamento WHERE insegnamento_nome = ? AND vecchio_giorno = ? AND nuovo_giorno = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, r.getLezionedaSpostare().getInsegnamento().getNome());
            ps.setString(2, r.getLezionedaSpostare().getGiornoSettimana());
            ps.setString(3, r.getNuovoGiornoLezione());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { return false; }
    }
}