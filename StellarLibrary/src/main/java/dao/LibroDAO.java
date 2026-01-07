package dao;

import utils.DBManager;
import model.Genere;
import model.Libro;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;

public class LibroDAO {

    private final DBManager db = DBManager.getInstance();

    public void create(Libro libro) throws SQLException {
        String sql = "INSERT INTO libro (ISBN, Titolo, Autore, CasaEditrice, Pagine, Copertina, AnnoPubblicazione, Prezzo, IDGenere) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getIsbn());
            stmt.setString(2, libro.getTitolo());
            stmt.setString(3, libro.getAutore());
            stmt.setString(4, libro.getCasaEditrice());
            stmt.setInt(5, libro.getPagine());
            stmt.setString(6, libro.getCopertina());
            stmt.setInt(7, libro.getAnnoPubblicazione());
            stmt.setBigDecimal(8, libro.getPrezzo());
            stmt.setInt(9, libro.getIdGenere());
            stmt.executeUpdate();
        }
    }

    public Libro findByISBN(String isbn) throws SQLException {
        String sql = "SELECT * FROM libro WHERE ISBN = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Libro libro = extractLibro(rs);
                    int idGenere = rs.getInt("IDGenere");
                    GenereDAO gdao = new GenereDAO();
                    Genere genere = gdao.findById(idGenere);
                    libro.setGenere(genere);
                    return libro;
                }
            }
        }
        return null;
    }

    public List<Libro> findAll() throws SQLException {
        List<Libro> list = new ArrayList<>();
        String sql = "SELECT * FROM libro";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(extractLibro(rs));
            }
        }
        return list;
    }

    public void update(Libro libro) throws SQLException {
        String sql = "UPDATE libro SET Titolo=?, Autore=?, CasaEditrice=?, Pagine=?, Copertina=?, AnnoPubblicazione=?, Prezzo=?, IDGenere=? WHERE ISBN=?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, libro.getTitolo());
            stmt.setString(2, libro.getAutore());
            stmt.setString(3, libro.getCasaEditrice());
            stmt.setInt(4, libro.getPagine());
            stmt.setString(5, libro.getCopertina());
            stmt.setInt(6, libro.getAnnoPubblicazione());
            stmt.setBigDecimal(7, libro.getPrezzo());
            stmt.setInt(8, libro.getIdGenere());
            stmt.setString(9, libro.getIsbn());

            stmt.executeUpdate();
        }
    }

    public void delete(String isbn) throws SQLException {
        String sql = "DELETE FROM libro WHERE ISBN = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, isbn);
            stmt.executeUpdate();
        }
    }

    public List<Libro> findUltimi(int quanti) throws SQLException {
        List<Libro> list = new ArrayList<>();
        String sql = "SELECT * FROM libro ORDER BY AnnoPubblicazione DESC LIMIT ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quanti);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(extractLibro(rs));
            }
        }
        return list;
    }

    public List<Libro> cercaPerTitolo(String keyword) throws SQLException {
        List<Libro> risultati = new ArrayList<>();
        String sql = "SELECT ISBN, Titolo FROM libro WHERE Titolo LIKE ? LIMIT 10";
        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Libro libro = new Libro();
                libro.setIsbn(rs.getString("ISBN"));
                libro.setTitolo(rs.getString("Titolo"));
                risultati.add(libro);
            }
        }
        return risultati;
    }

    public List<Libro> cercaConFiltri(Integer idGenere, Integer anno, BigDecimal prezzoMin, BigDecimal prezzoMax, Integer minPagine) throws SQLException {
        List<Libro> libri = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM libro WHERE 1=1");

        if (idGenere != null) sql.append(" AND IDGenere = ?");
        if (anno != null) sql.append(" AND AnnoPubblicazione = ?");
        if (prezzoMin != null) sql.append(" AND Prezzo >= ?");
        if (prezzoMax != null) sql.append(" AND Prezzo <= ?");
        if (minPagine != null) sql.append(" AND Pagine >= ?");

        try (Connection conn = DBManager.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int i = 1;
            if (idGenere != null) ps.setInt(i++, idGenere);
            if (anno != null) ps.setInt(i++, anno);
            if (prezzoMin != null) ps.setBigDecimal(i++, prezzoMin);
            if (prezzoMax != null) ps.setBigDecimal(i++, prezzoMax);
            if (minPagine != null) ps.setInt(i++, minPagine);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                libri.add(extractLibro(rs));
            }
        }
        return libri;
    }

    public BigDecimal getPrezzoByIsbn(String isbn) throws SQLException {
        String sql = "SELECT Prezzo FROM libro WHERE ISBN = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal("Prezzo");
            } else {
                throw new SQLException("Libro non trovato per ISBN: " + isbn);
            }
        }
    }

    private Libro extractLibro(ResultSet rs) throws SQLException {
        Libro l = new Libro();
        l.setIsbn(rs.getString("ISBN"));
        l.setTitolo(rs.getString("Titolo"));
        l.setAutore(rs.getString("Autore"));
        l.setCasaEditrice(rs.getString("CasaEditrice"));
        l.setPagine(rs.getInt("Pagine"));
        l.setCopertina(rs.getString("Copertina"));
        l.setAnnoPubblicazione(rs.getInt("AnnoPubblicazione"));
        l.setPrezzo(rs.getBigDecimal("Prezzo"));
        l.setIdGenere(rs.getInt("IDGenere"));
        return l;
    }
}