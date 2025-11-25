package dao;



import utils.DBManager;
import model.Ordine;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdineDAO {
    private final DBManager db = DBManager.getInstance();

    public int creaOrdine(Ordine ordine) throws SQLException {
        String sql = "INSERT INTO ordine (IDUtente, IDIndirizzo, IDMetodoPagamento, Totale) VALUES (?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ordine.getIdUtente());
            stmt.setInt(2, ordine.getIdIndirizzo());
            stmt.setInt(3, ordine.getIdMetodoPagamento());
            stmt.setBigDecimal(4, ordine.getTotale());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
        }
        return -1;
    }

    public List<Ordine> getOrdiniByUtente(int idUtente) throws SQLException {
        List<Ordine> list = new ArrayList<>();
        String sql = "SELECT * FROM ordine WHERE IDUtente = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new Ordine(
                        rs.getInt("ID"),
                        rs.getInt("IDUtente"),
                        rs.getInt("IDIndirizzo"),
                        rs.getInt("IDMetodoPagamento"),
                        rs.getTimestamp("DataOrdine"),
                        rs.getBigDecimal("Totale")
                ));
            }
        }
        return list;
    }
    public List<Map<String, Object>> getOrdiniConUtente() throws SQLException {
        List<Map<String, Object>> ordini = new ArrayList<>();
        String sql = """
        SELECT o.ID, o.IDUtente, o.IDIndirizzo, o.IDMetodoPagamento, o.DataOrdine, o.Totale,
               u.Nome, u.Cognome
        FROM ordine o
        JOIN utente u ON o.IDUtente = u.ID
        ORDER BY o.DataOrdine DESC
        """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> riga = new HashMap<>();
                riga.put("id", rs.getInt("ID"));
                riga.put("idUtente", rs.getInt("IDUtente"));
                riga.put("idIndirizzo", rs.getInt("IDIndirizzo"));
                riga.put("idMetodoPagamento", rs.getInt("IDMetodoPagamento"));
                riga.put("dataOrdine", rs.getTimestamp("DataOrdine"));
                riga.put("totale", rs.getBigDecimal("Totale"));
                riga.put("nome", rs.getString("Nome"));
                riga.put("cognome", rs.getString("Cognome"));

                ordini.add(riga);
            }

        }

        return ordini;
    }


}

