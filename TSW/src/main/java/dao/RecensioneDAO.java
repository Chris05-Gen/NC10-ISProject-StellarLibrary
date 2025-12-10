package dao;


import model.Libro;
import model.Utente;
import utils.DBManager;
import model.Recensione;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RecensioneDAO {
    private final DBManager db = DBManager.getInstance();

    public void addRecensione(Recensione r) throws SQLException {
        String sql = """
        INSERT INTO recensione 
        (IDUtente, ISBNCodice, Titolo, Testo, Valutazione, Data)
        VALUES (?, ?, ?, ?, ?, ?)
    """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, r.getUtente().getId());          // ✅ da Utente
            stmt.setString(2, r.getLibro().getIsbn());     // ✅ da Libro
            stmt.setString(3, r.getTitolo());
            stmt.setString(4, r.getTesto());
            stmt.setInt(5, r.getValutazione());
            stmt.setDate(6, r.getData());

            stmt.executeUpdate();
        }
    }


    public List<Recensione> getByLibro(String isbn) throws SQLException {
        List<Recensione> list = new ArrayList<>();

        String sql = """
        SELECT r.*, u.Nome, u.Cognome
        FROM recensione r
        JOIN utente u ON r.IDUtente = u.ID
        WHERE r.ISBNCodice = ?
    """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {

                Utente u = new Utente();
                u.setId(rs.getInt("IDUtente"));
                u.setNome(rs.getString("Nome"));
                u.setCognome(rs.getString("Cognome"));

                Libro l = new Libro();
                l.setIsbn(rs.getString("ISBNCodice"));

                Recensione r = new Recensione(
                        rs.getInt("ID"),
                        u,
                        l,
                        rs.getString("Titolo"),
                        rs.getString("Testo"),
                        rs.getInt("Valutazione"),
                        rs.getDate("Data")
                );

                r.setNomeUtente(u.getNome() + " " + u.getCognome());
                list.add(r);
            }
        }
        return list;
    }


    public List<Recensione> getAllRecensioniConNomeUtente() throws SQLException {
        List<Recensione> list = new ArrayList<>();

        String sql = """
        SELECT r.*, u.Nome, u.Cognome, l.ISBN, l.Titolo AS TitoloLibro
        FROM recensione r
        JOIN utente u ON r.IDUtente = u.ID
        JOIN libro l ON r.ISBNCodice = l.ISBN
    """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                // --- crea Utente leggero ---
                Utente utente = new Utente();
                utente.setId(rs.getInt("IDUtente"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));

                // --- crea Libro leggero ---
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("ISBN"));
                libro.setTitolo(rs.getString("TitoloLibro"));

                // --- crea Recensione UML-compliant ---
                Recensione r = new Recensione(
                        rs.getInt("ID"),
                        utente,
                        libro,
                        rs.getString("Titolo"), // titolo recensione
                        rs.getString("Testo"),
                        rs.getInt("Valutazione"),
                        rs.getDate("Data")
                );

                r.setNomeUtente(utente.getNome() + " " + utente.getCognome());

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