// FiliereDAO.java
package com.example.javafx_ghilani.database;

import com.example.javafx_ghilani.model.Filiere;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FiliereDAO {
    private DatabaseManager dbManager;

    public FiliereDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public List<Filiere> getAllFilieres() throws SQLException {
        List<Filiere> filieres = new ArrayList<>();
        String sql = "SELECT * FROM Filiere";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Filiere filiere = new Filiere();
                filiere.setId(rs.getInt("id"));
                filiere.setCode(rs.getString("code"));
                filiere.setDesignation(rs.getString("designation"));
                filieres.add(filiere);
            }
        }
        return filieres;
    }
}

