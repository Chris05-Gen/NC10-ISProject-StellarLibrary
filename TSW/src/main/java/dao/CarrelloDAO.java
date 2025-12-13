// dao/CarrelloDAO.java
package dao;

import model.Carrello;
import model.Utente;
import utils.DBManager;

import java.sql.*;

public class CarrelloDAO {
    private final DBManager db = DBManager.getInstance();

    public Carrello findByUtente(Utente utente) throws SQLException {
        String sql = "SELECT * FROM carrello WHERE IDUtente = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utente.getId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Carrello c = new Carrello();
                c.setId(rs.getInt("ID"));
                c.setUtente(utente); // già noto
                return c;
            }
        }
        return null;
    }

    // overload se hai solo l'id
    public Carrello findByUtenteId(int userId) throws SQLException {
        Utente u = new Utente();
        u.setId(userId);
        return findByUtente(u);
    }

    public Carrello createCarrello(Utente utente) throws SQLException {
        String sql = "INSERT INTO carrello (IDUtente) VALUES (?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            if (utente != null) {
                stmt.setInt(1, utente.getId());
            } else {
                stmt.setNull(1, Types.INTEGER); // carrello guest
            }
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new Carrello(id, utente);
                }
            }
        }
        return null;
    }

    public void assegnaCarrelloAUtente(Carrello carrello, Utente utente) throws SQLException {
        String sql = "UPDATE carrello SET IDUtente = ? WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, utente.getId());
            stmt.setInt(2, carrello.getId());
            stmt.executeUpdate();
        }
        carrello.setUtente(utente); // aggiorna anche l’oggetto in memoria
    }

    public void eliminaCarrelloGuest(Carrello carrello) throws SQLException {
        String sql = "DELETE FROM carrello WHERE ID = ? AND IDUtente IS NULL";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, carrello.getId());
            stmt.executeUpdate();
        }
    }
    public Carrello findRawByUtente(Utente utente) throws SQLException {
        return findByUtente(utente); // ma NON crea carrelli
    }

}
