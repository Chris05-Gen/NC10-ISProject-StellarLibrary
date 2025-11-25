package dao;


import utils.DBManager;
import model.MetodoPagamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MetodoPagamentoDAO {
    private final DBManager db = DBManager.getInstance();

    public List<MetodoPagamento> getAll() throws SQLException {
        List<MetodoPagamento> list = new ArrayList<>();
        String sql = "SELECT * FROM metodopagamento";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MetodoPagamento m = new MetodoPagamento(
                        rs.getInt("ID"),
                        rs.getString("Tipo"),
                        rs.getString("Circuito"),
                        rs.getString("Descrizione")
                );
                list.add(m);
            }
        }
        return list;
    }
    public MetodoPagamento getMetodoById(int id) throws SQLException {
        String sql = "SELECT * FROM metodopagamento WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new MetodoPagamento(
                        rs.getInt("ID"),
                        rs.getString("Tipo"),
                        rs.getString("Circuito"),
                        rs.getString("Descrizione")
                );
            }
        }
        return null; // Se non trovato
    }
    public boolean Esiste(int id) throws SQLException {
        try (Connection con = db.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT 1 FROM metodopagamento WHERE ID = ?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
    }


}
