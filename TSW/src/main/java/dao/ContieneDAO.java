package dao;



import model.Contiene;
import utils.DBManager;

import java.sql.*;
import java.util.*;

public class ContieneDAO {
    private final DBManager db = DBManager.getInstance();

    public void aggiungiLibro(int idCarrello, String isbn, int quantita) throws SQLException {
        String sql = "REPLACE INTO contiene (IDCarrello, ISBNCodice, Quantita) VALUES (?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            stmt.setInt(3, quantita);
            stmt.executeUpdate();
        }
    }

    public List<Contiene> getContenuto(int idCarrello) throws SQLException {
        List<Contiene> list = new ArrayList<>();
        String sql = "SELECT * FROM contiene WHERE IDCarrello = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Contiene(rs.getInt("IDCarrello"), rs.getString("ISBNCodice"), rs.getInt("Quantita")));
            }
        }
        return list;
    }

    public void rimuoviLibro(int idCarrello, String isbn) throws SQLException {
        String sql = "DELETE FROM contiene WHERE IDCarrello = ? AND ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }
    public int getQuantitaLibro(int idCarrello, String isbn) throws SQLException {
        String sql = "SELECT Quantita FROM contiene WHERE IDCarrello = ? AND ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("Quantita");
            } else {
                return 0; // Libro non presente
            }
        }
    }
    public void decrementaQuantitaLibro(int idCarrello, String isbn) throws SQLException {
        String sql = "UPDATE contiene SET Quantita = Quantita - 1 WHERE IDCarrello = ? AND ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }
    public void svuotaCarrello(int idCarrello) throws SQLException {
        String sql = "DELETE FROM contiene WHERE IDCarrello = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCarrello);
            stmt.executeUpdate();
        }
    }

}

