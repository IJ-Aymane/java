// MatiereDAO.java
package com.example.javafx_ghilani.database;

import com.example.javafx_ghilani.model.Matiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MatiereDAO {
    private DatabaseManager dbManager;

    public MatiereDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Matiere> getMatieresByFiliereAndNiveau(String codeFiliere, int niveau) throws SQLException {
        List<Matiere> matieres = new ArrayList<>();
        String sql = """
            SELECT m.* FROM Matiere m
            JOIN Module mod ON m.code_module = mod.code
            WHERE mod.code_fil = ? AND mod.niveau = ?
        """;
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, codeFiliere);
            stmt.setInt(2, niveau);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Matiere matiere = new Matiere();
                    matiere.setId(rs.getInt("id"));
                    matiere.setCode(rs.getString("code"));
                    matiere.setDesignation(rs.getString("designation"));
                    matiere.setVh(rs.getInt("VH"));
                    matiere.setCodeModule(rs.getString("code_module"));
                    matieres.add(matiere);
                }
            }
        }
        return matieres;
    }

    public List<Matiere> getAllMatieres() throws SQLException {
        List<Matiere> matieres = new ArrayList<>();
        String sql = "SELECT * FROM Matiere";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Matiere matiere = new Matiere();
                matiere.setId(rs.getInt("id"));
                matiere.setCode(rs.getString("code"));
                matiere.setDesignation(rs.getString("designation"));
                matiere.setVh(rs.getInt("VH"));
                matiere.setCodeModule(rs.getString("code_module"));
                matieres.add(matiere);
            }
        }
        return matieres;
    }
}