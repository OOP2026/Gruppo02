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
}
