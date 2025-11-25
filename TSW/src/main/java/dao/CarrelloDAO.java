package dao;



import model.Carrello;
import utils.DBManager;

import java.sql.*;

public class CarrelloDAO {
    private final DBManager db = DBManager.getInstance();

    public Carrello findByUtenteId(int userId) throws SQLException {
        String sql = "SELECT * FROM carrello WHERE IDUtente = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Carrello(rs.getInt("ID"), rs.getInt("IDUtente"));
            }
        }
        return null;
    }

    public Carrello createCarrello(Integer userId) throws SQLException {
        String sql = "INSERT INTO carrello (IDUtente) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (userId != null) {
                stmt.setInt(1, userId);
            } else {
                stmt.setNull(1, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return new Carrello(rs.getInt(1), userId != null ? userId : -1);
            }
        }
        return null;
    }

    public void assegnaCarrelloAUtente(int carrelloId, int userId) throws SQLException {
        String sql = "UPDATE carrello SET IDUtente = ? WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, carrelloId);
            stmt.executeUpdate();
        }
    }
    public void eliminaCarrelloGuest(int carrelloId) throws SQLException {
        String sql = "DELETE FROM carrello WHERE ID = ? AND IDUtente IS NULL";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, carrelloId);
            stmt.executeUpdate();
        }
    }


}

