package dao;

import utils.DBManager;
import model.Genere;

import java.sql.*;
import java.util.*;
public class GenereDAO {
    private final DBManager db = DBManager.getInstance();
    public List<Genere> findAll() throws SQLException {
        List<Genere> list = new ArrayList<>();
        String sql = "SELECT ID, Nome FROM genere ORDER BY Nome";
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Genere g = new Genere();
                g.setId(rs.getInt("ID"));
                g.setNome(rs.getString("Nome"));
                list.add(g);
            }
        }
        return list;
    }
    public Genere findById(int id) throws SQLException {
        String sql = "SELECT * FROM genere WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Genere(rs.getInt("ID"), rs.getString("Nome"));
                }
            }
        }
        return null;
    }
}
