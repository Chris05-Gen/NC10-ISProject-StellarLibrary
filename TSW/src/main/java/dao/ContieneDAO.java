package dao;

import model.Contiene;
import model.Libro;
import utils.DBManager;

import java.sql.*;
import java.util.*;

public class ContieneDAO {
    private final DBManager db = DBManager.getInstance();

    // ✅ AGGIUNGE O AGGIORNA (REPLACE)
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

    // ✅ RECUPERO CONTENUTO COMPLETO (con Libro)
    public List<Contiene> getContenuto(int idCarrello) throws SQLException {

        String sql = """
            SELECT c.IDCarrello, c.ISBNCodice, c.Quantita,
                   l.ISBN, l.titolo, l.prezzo, l.Copertina
            FROM contiene c
            JOIN libro l ON c.ISBNCodice = l.ISBN
            WHERE c.IDCarrello = ?
        """;

        List<Contiene> risultati = new ArrayList<>();

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrello);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    Libro libro = new Libro();
                    libro.setIsbn(rs.getString("ISBN"));
                    libro.setTitolo(rs.getString("titolo"));
                    libro.setPrezzo(rs.getBigDecimal("prezzo"));
                    libro.setCopertina(rs.getString("Copertina"));

                    Contiene contiene = new Contiene();
                    contiene.setLibro(libro);
                    contiene.setQuantita(rs.getInt("Quantita"));

                    risultati.add(contiene);
                }
            }
        }
        return risultati;
    }

    // ✅ METODO CHE TI SERVE ORA (ED ERA GIÀ GIUSTO NEL TUO)
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
                return 0;   // libro non presente
            }
        }
    }

    // ✅ DECREMENTO
    public void decrementaQuantitaLibro(int idCarrello, String isbn) throws SQLException {
        String sql = "UPDATE contiene SET Quantita = Quantita - 1 WHERE IDCarrello = ? AND ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    // ✅ RIMOZIONE TOTALE DEL LIBRO
    public void rimuoviLibro(int idCarrello, String isbn) throws SQLException {
        String sql = "DELETE FROM contiene WHERE IDCarrello = ? AND ISBNCodice = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrello);
            stmt.setString(2, isbn);
            stmt.executeUpdate();
        }
    }

    // ✅ SVUOTAMENTO COMPLETO
    public void svuotaCarrello(int idCarrello) throws SQLException {
        String sql = "DELETE FROM contiene WHERE IDCarrello = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idCarrello);
            stmt.executeUpdate();
        }
    }
}
