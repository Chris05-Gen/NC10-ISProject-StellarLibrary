package dao;



import utils.DBManager;
import model.Utente;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtenteDAO {

    private final DBManager db = DBManager.getInstance();

    public boolean registraUtente(Utente u) throws SQLException {
        String sql = "INSERT INTO utente (Nome, Cognome, Email, Password, Tipo) VALUES (?, ?, ?, ?, 'Cliente')";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, u.getNome());
            stmt.setString(2, u.getCognome());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, hashPassword(u.getPassword()));
            return stmt.executeUpdate() > 0;
        }
    }
    public Utente findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utente WHERE Email = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUtente(rs);
                }
            }
        }
        return null;
    }

    public Utente login(String email, String password) throws SQLException {
        String sql = "SELECT * FROM utente WHERE Email = ? AND Password = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, hashPassword(password));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utente u = new Utente();
                u.setId(rs.getInt("ID"));
                u.setNome(rs.getString("Nome"));
                u.setCognome(rs.getString("Cognome"));
                u.setEmail(rs.getString("Email"));
                u.setTipo(rs.getString("Tipo"));
                return u;
            }
        }
        return null;
    }

    public Utente findById(int id) throws SQLException {
        String sql = "SELECT * FROM utente WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractUtente(rs);
                }
            }
        }
        return null;
    }

    public List<Utente> findAll() throws SQLException {
        List<Utente> list = new ArrayList<>();
        String sql = "SELECT * FROM utente";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractUtente(rs));
            }
        }
        return list;
    }

    public void update(Utente utente) throws SQLException {
        String sql = "UPDATE utente SET Nome=?, Cognome=?, Email=?, Password=?, Tipo=? WHERE ID=?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utente.getNome());
            stmt.setString(2, utente.getCognome());
            stmt.setString(3, utente.getEmail());
            stmt.setString(4, utente.getPassword());
            stmt.setString(5, utente.getTipo());
            stmt.setInt(6, utente.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM utente WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    private Utente extractUtente(ResultSet rs) throws SQLException {
        Utente u = new Utente();
        u.setId(rs.getInt("ID"));
        u.setNome(rs.getString("Nome"));
        u.setCognome(rs.getString("Cognome"));
        u.setEmail(rs.getString("Email"));
        u.setPassword(rs.getString("Password"));
        u.setTipo(rs.getString("Tipo"));
        return u;
    }
    public boolean emailEsistente(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utente WHERE Email = ?";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }



    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Errore hashing password", e);
        }
    }


}
