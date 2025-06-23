package com.example.javafx_ghilani.database;

import com.example.javafx_ghilani.model.Eleve;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {
    private DatabaseManager dbManager;

    public EleveDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addEleve(Eleve eleve) throws SQLException {
        String sql = "CALL AddEleve(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, eleve.getCode());
            stmt.setString(2, eleve.getNom());
            stmt.setString(3, eleve.getPrenom());
            stmt.setInt(4, eleve.getNiveau());
            stmt.setString(5, eleve.getCodeFil());
            stmt.executeUpdate();
        }
    }

    public void updateEleve(Eleve eleve) throws SQLException {
        String sql = "CALL UpdateEleve(?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, eleve.getCode());
            stmt.setString(2, eleve.getNom());
            stmt.setString(3, eleve.getPrenom());
            stmt.setInt(4, eleve.getNiveau());
            stmt.setString(5, eleve.getCodeFil());
            stmt.executeUpdate();
        }
    }

    public void deleteEleve(String code) throws SQLException {
        String sql = "CALL DeleteEleve(?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.executeUpdate();
        }
    }

    public List<Eleve> getAllEleves() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM Eleve";
        try (Statement stmt = dbManager.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Eleve eleve = new Eleve();
                eleve.setId(rs.getInt("id"));
                eleve.setCode(rs.getString("code"));
                eleve.setNom(rs.getString("nom"));
                eleve.setPrenom(rs.getString("prenom"));
                eleve.setNiveau(rs.getInt("niveau"));
                eleve.setCodeFil(rs.getString("code_fil"));
                eleves.add(eleve);
            }
        }
        return eleves;
    }

    public Eleve getEleveByCode(String code) throws SQLException {
        String sql = "SELECT * FROM Eleve WHERE code = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Eleve eleve = new Eleve();
                    eleve.setId(rs.getInt("id"));
                    eleve.setCode(rs.getString("code"));
                    eleve.setNom(rs.getString("nom"));
                    eleve.setPrenom(rs.getString("prenom"));
                    eleve.setNiveau(rs.getInt("niveau"));
                    eleve.setCodeFil(rs.getString("code_fil"));
                    return eleve;
                }
            }
        }
        return null;
    }

    /**
     * Recherche les élèves en fonction des critères fournis.
     * Si un critère est null ou vide, il n'est pas pris en compte dans la recherche.
     *
     * @param code Code de l'élève (exact)
     * @param nom Nom de l'élève (contient)
     * @param prenom Prénom de l'élève (contient)
     * @param filiere Code de la filière (exact)
     * @param niveau Niveau (exact)
     * @return Liste des élèves correspondants aux critères
     * @throws SQLException
     */
    public List<Eleve> searchEleves(String code, String nom, String prenom, String filiere, Integer niveau) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Eleve WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            sql.append(" AND code = ?");
            params.add(code);
        }
        if (nom != null && !nom.isEmpty()) {
            sql.append(" AND nom LIKE ?");
            params.add("%" + nom + "%");
        }
        if (prenom != null && !prenom.isEmpty()) {
            sql.append(" AND prenom LIKE ?");
            params.add("%" + prenom + "%");
        }
        if (filiere != null && !filiere.isEmpty()) {
            sql.append(" AND code_fil = ?");
            params.add(filiere);
        }
        if (niveau != null) {
            sql.append(" AND niveau = ?");
            params.add(niveau);
        }

        List<Eleve> eleves = new ArrayList<>();
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Eleve eleve = new Eleve();
                    eleve.setId(rs.getInt("id"));
                    eleve.setCode(rs.getString("code"));
                    eleve.setNom(rs.getString("nom"));
                    eleve.setPrenom(rs.getString("prenom"));
                    eleve.setNiveau(rs.getInt("niveau"));
                    eleve.setCodeFil(rs.getString("code_fil"));
                    eleves.add(eleve);
                }
            }
        }
        return eleves;
    }
}
