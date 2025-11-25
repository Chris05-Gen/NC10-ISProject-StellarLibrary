package dao;


import utils.DBManager;
import model.Recensione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO {
    private final DBManager db = DBManager.getInstance();

    public void addRecensione(Recensione r) throws SQLException {
        String sql = "INSERT INTO recensione (IDUtente, ISBNCodice, Titolo, Testo, Valutazione, Data) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, r.getIdUtente());
            stmt.setString(2, r.getIsbn());
            stmt.setString(3, r.getTitolo());
            stmt.setString(4, r.getTesto());
            stmt.setInt(5, r.getValutazione());
            stmt.setDate(6, r.getData());
            stmt.executeUpdate();
        }
    }

    public List<Recensione> getByLibro(String isbn) throws SQLException {
        List<Recensione> list = new ArrayList<>();
        String sql = "SELECT * FROM recensione WHERE ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Recensione r = new Recensione(
                        rs.getInt("ID"),
                        rs.getInt("IDUtente"),
                        rs.getString("ISBNCodice"),
                        rs.getString("Titolo"),
                        rs.getString("Testo"),
                        rs.getInt("Valutazione"),
                        rs.getDate("Data")
                );
                list.add(r);
            }
        }
        return list;
    }
    public List<Recensione> getAllRecensioniConNomeUtente() throws SQLException {
        List<Recensione> list = new ArrayList<>();
        String sql = "SELECT r.*, u.Nome, u.Cognome FROM recensione r JOIN utente u ON r.IDUtente = u.ID";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Recensione r = new Recensione(
                        rs.getInt("ID"),
                        rs.getInt("IDUtente"),
                        rs.getString("ISBNCodice"),
                        rs.getString("Titolo"),
                        rs.getString("Testo"),
                        rs.getInt("Valutazione"),
                        rs.getDate("Data")
                );
                r.setNomeUtente(rs.getString("Nome") + " " + rs.getString("Cognome"));
                list.add(r);
            }
        }
        return list;
    }
    public void deleteRecensioneById(int id) throws SQLException {
        String sql = "DELETE FROM recensione WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

}