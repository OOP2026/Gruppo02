package implementazioneDao;

import dao.AulaDAO;
import database.DBConnection;
import model.Aula;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AulaPostgresDAO implements AulaDAO {

    private static final Logger LOGGER = Logger.getLogger(AulaPostgresDAO.class.getName());

    @Override
    public ArrayList<Aula> getTutteLeAule() {
        ArrayList<Aula> aule = new ArrayList<>();
        String query = "SELECT * FROM Aula";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                aule.add(new Aula(rs.getString("nome")));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante il recupero di tutte le aule", e);
        }
        return aule;
    }

    @Override
    public boolean inserisciAula(Aula aula) {
        String query = "INSERT INTO Aula (nome) VALUES (?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, aula.getNome());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'inserimento dell'aula", e);
            return false;
        }
    }

    @Override
    public boolean eliminaAula(Aula aula) {
        String query = "DELETE FROM Aula WHERE nome = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, aula.getNome());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Errore durante l'eliminazione dell'aula", e);
            return false;
        }
    }
}
