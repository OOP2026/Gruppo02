package implementazionedao;

import dao.VincoloDAO;
import database.DBConnection;
import model.Vincolo;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VincoloPostgresDAO implements VincoloDAO {

    private static final Logger LOGGER = Logger.getLogger(VincoloPostgresDAO.class.getName());

    @Override
    public ArrayList<Vincolo> getVincoliPerDocente(String email) {
        ArrayList<Vincolo> lista = new ArrayList<>();
        String query = "SELECT * FROM Vincolo WHERE docente_email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Vincolo(rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine")));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero dei vincoli per il docente", e);
        }
        return lista;
    }

    @Override
    public void inserisciVincolo(Vincolo v, String emailDocente) {
        String query = "INSERT INTO Vincolo (giorno_settimana, ora_inizio, ora_fine, docente_email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, v.getVincoloGiornoSettimana());
            ps.setString(2, v.getVincoloOraInizio());
            ps.setString(3, v.getVincoloOraFine());
            ps.setString(4, emailDocente);
            ps.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inserimento del vincolo", e);
        }
    }

    @Override
    public boolean eliminaVincolo(Vincolo v, String emailDocente) {
        String query = "DELETE FROM Vincolo WHERE giorno_settimana = ? AND ora_inizio = ? AND ora_fine = ? AND docente_email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, v.getVincoloGiornoSettimana());
            ps.setString(2, v.getVincoloOraInizio());
            ps.setString(3, v.getVincoloOraFine());
            ps.setString(4, emailDocente);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'eliminazione del vincolo", e);
            return false;
        }
    }
}
