package implementazioneDao;

import dao.VincoloDAO;
import Database.DBConnection;
import model.Vincolo;
import java.sql.*;
import java.util.ArrayList;

public class VincoloPostgresDAO implements VincoloDAO {

    @Override
    public ArrayList<Vincolo> getVincoliPerDocente(String email) {
        ArrayList<Vincolo> lista = new ArrayList<>();
        String query = "SELECT * FROM Vincolo WHERE docente_email = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(new Vincolo(rs.getString("giorno_settimana"), rs.getString("ora_inizio"), rs.getString("ora_fine")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return lista;
    }

    @Override
    public void inserisciVincolo(Vincolo v, String emailDocente) {
        String query = "INSERT INTO Vincolo (giorno_settimana, ora_inizio, ora_fine, docente_email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, v.getVincoloGiornoSettimana());
            ps.setString(2, v.getVincoloOraInizio());
            ps.setString(3, v.getVincoloOraFine());
            ps.setString(4, emailDocente);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @Override
    public boolean eliminaVincolo(Vincolo v, String emailDocente) {
        String query = "DELETE FROM Vincolo WHERE giorno_settimana = ? AND ora_inizio = ? AND ora_fine = ? AND docente_email = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, v.getVincoloGiornoSettimana());
            ps.setString(2, v.getVincoloOraInizio());
            ps.setString(3, v.getVincoloOraFine());
            ps.setString(4, emailDocente);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Errore eliminazione vincolo: " + e.getMessage());
            return false;
        }
    }
}
