package dao;



import model.Indirizzo;
import model.MetodoPagamento;
import model.Utente;
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

        String sql = """
        INSERT INTO ordine (IDUtente, IDIndirizzo, IDMetodoPagamento, Totale)
        VALUES (?, ?, ?, ?)
    """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, ordine.getUtente().getId());
            stmt.setInt(2, ordine.getIndirizzo().getId());
            stmt.setInt(3, ordine.getMetodoPagamento().getId());
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

                Utente u = new Utente();
                u.setId(rs.getInt("IDUtente"));

                Indirizzo i = new Indirizzo();
                i.setId(rs.getInt("IDIndirizzo"));

                MetodoPagamento m = new MetodoPagamento();
                m.setId(rs.getInt("IDMetodoPagamento"));

                Ordine o = new Ordine(
                        rs.getInt("ID"),
                        u,
                        i,
                        m,
                        rs.getTimestamp("DataOrdine"),
                        rs.getBigDecimal("Totale")
                );

                list.add(o);
            }
        }

        return list;
    }

    public List<Ordine> getOrdiniConUtente() throws SQLException {

        List<Ordine> ordini = new ArrayList<>();

        String sql = """
        SELECT o.*, 
               u.ID AS uid, u.Nome, u.Cognome,
               i.ID AS iid, i.Via, i.CAP, i.Citta, i.Provincia, i.Nazione, i.Telefono,
               m.ID AS mid, m.Tipo, m.Circuito, m.Descrizione
        FROM ordine o
        JOIN utente u ON o.IDUtente = u.ID
        JOIN indirizzo i ON o.IDIndirizzo = i.ID
        JOIN metodopagamento m ON o.IDMetodoPagamento = m.ID
        ORDER BY o.DataOrdine DESC
    """;

        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                // ✅ UTENTE
                Utente utente = new Utente();
                utente.setId(rs.getInt("uid"));
                utente.setNome(rs.getString("Nome"));
                utente.setCognome(rs.getString("Cognome"));

                // ✅ INDIRIZZO
                Indirizzo indirizzo = new Indirizzo();
                indirizzo.setId(rs.getInt("iid"));
                indirizzo.setVia(rs.getString("Via"));
                indirizzo.setCap(rs.getString("CAP"));
                indirizzo.setCitta(rs.getString("Citta"));
                indirizzo.setProvincia(rs.getString("Provincia"));
                indirizzo.setNazione(rs.getString("Nazione"));
                indirizzo.setTelefono(rs.getString("Telefono"));
                indirizzo.setUtente(utente);

                // ✅ METODO PAGAMENTO
                MetodoPagamento metodo = new MetodoPagamento();
                metodo.setId(rs.getInt("mid"));
                metodo.setTipo(rs.getString("Tipo"));
                metodo.setCircuito(rs.getString("Circuito"));
                metodo.setDescrizione(rs.getString("Descrizione"));

                // ✅ ORDINE UML COMPLETO
                Ordine ordine = new Ordine(
                        rs.getInt("ID"),
                        utente,
                        indirizzo,
                        metodo,
                        rs.getTimestamp("DataOrdine"),
                        rs.getBigDecimal("Totale")
                );

                ordini.add(ordine);
            }
        }

        return ordini;
    }



}

