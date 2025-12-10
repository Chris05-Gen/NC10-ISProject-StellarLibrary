package dao;



import model.Utente;
import utils.DBManager;
import model.Indirizzo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndirizzoDAO {
    private final DBManager db = DBManager.getInstance();

    public void addIndirizzo(Indirizzo indirizzo) throws SQLException {
        String sql = "INSERT INTO indirizzo (IDUtente, Via, Citta, CAP, Provincia, Nazione, Telefono) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, indirizzo.getUtente().getId());
            stmt.setString(2, indirizzo.getVia());
            stmt.setString(3, indirizzo.getCitta());
            stmt.setString(4, indirizzo.getCap());
            stmt.setString(5, indirizzo.getProvincia());
            stmt.setString(6, indirizzo.getNazione());
            stmt.setString(7, indirizzo.getTelefono());
            stmt.executeUpdate();
        }
    }

    public List<Indirizzo> getIndirizziByUtente(int idUtente) throws SQLException {
        List<Indirizzo> list = new ArrayList<>();
        String sql = "SELECT * FROM indirizzo WHERE IDUtente = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUtente);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Utente u = new Utente();
                u.setId(rs.getInt("IDUtente"));

                Indirizzo i = new Indirizzo();
                i.setId(rs.getInt("ID"));
                i.setUtente(u);
                i.setVia(rs.getString("Via"));
                i.setCitta(rs.getString("Citta"));
                i.setCap(rs.getString("CAP"));
                i.setProvincia(rs.getString("Provincia"));
                i.setNazione(rs.getString("Nazione"));
                i.setTelefono(rs.getString("Telefono"));
                list.add(i);
            }
        }
        return list;
    }
    public Indirizzo getIndirizzoById(int id) throws SQLException {
        String sql = "SELECT * FROM indirizzo WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Utente u = new Utente();
                u.setId(rs.getInt("IDUtente"));

                return new Indirizzo(
                        rs.getInt("ID"),
                        u,
                        rs.getString("Via"),
                        rs.getString("Citta"),
                        rs.getString("CAP"),
                        rs.getString("Provincia"),
                        rs.getString("Nazione"),
                        rs.getString("Telefono")
                );

            }
        }
        return null;
    }
    public boolean AppartieneA(int indirizzoId, int userId) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement(
                    "SELECT 1 FROM indirizzo WHERE ID = ? AND IDUtente = ?");
            ps.setInt(1, indirizzoId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


}

