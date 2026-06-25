package implementazioneDao;

import dao.InsegnamentoDAO;
import Database.DBConnection;
import model.Docente;
import model.Insegnamento;
import java.sql.*;
import java.util.ArrayList;

public class InsegnamentoPostgresDAO implements InsegnamentoDAO {

    @Override
    public ArrayList<Insegnamento> getTuttiInsegnamenti() {
        ArrayList<Insegnamento> lista = new ArrayList<>();
        // Uniamo la tabella Insegnamento con Utente per recuperare i dati del docente
        String query = "SELECT i.*, u.nome, u.cognome, u.password FROM Insegnamento i " +
                "JOIN Utente u ON i.docente_email = u.email";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Ricreiamo il Docente
                Docente d = new Docente(
                        rs.getString("nome"),
                        rs.getString("cognome"),
                        rs.getString("docente_email"),
                        rs.getString("password")
                );

                // Ricreiamo l'Insegnamento
                Insegnamento ins = new Insegnamento(
                        rs.getString("nome"),
                        rs.getInt("cfu"),
                        rs.getInt("anno_corso"),
                        d
                );
                lista.add(ins);
            }
        } catch (SQLException e) {
            System.err.println("Errore nel recupero insegnamenti: " + e.getMessage());
        }
        return lista;
    }

    @Override
    public boolean inserisciInsegnamento(Insegnamento i) {
        String query = "INSERT INTO Insegnamento (nome, cfu, anno_corso, docente_email) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, i.getNome());
            ps.setInt(2, i.getCFU());
            ps.setInt(3, i.getAnnoCorso());
            ps.setString(4, i.getDocente().getEmail());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Errore inserimento insegnamento: " + e.getMessage());
            return false;
        }
    }
}
