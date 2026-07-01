package implementazioneDao;

import dao.AulaDAO;
import Database.DBConnection;
import model.Aula;
import java.sql.*;
import java.util.ArrayList;

public class AulaPostgresDAO implements AulaDAO {

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
            e.printStackTrace();
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
            System.err.println("Errore inserimento aula: " + e.getMessage());
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
            System.err.println("Errore eliminazione aula: " + e.getMessage());
            return false;
        }
    }
}
