package com.example.javafx_ghilani.database;

import com.example.javafx_ghilani.model.Eleve;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EleveDAO {
    private final Connection connection;

    public EleveDAO() {
        this.connection = new DatabaseManager().getConnection();
    }

    public List<Eleve> getAllEleves() throws SQLException {
        List<Eleve> eleves = new ArrayList<>();
        String sql = "SELECT * FROM eleve";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Eleve eleve = new Eleve(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getInt("niveau"),
                        rs.getString("code_fil")
                );
                eleves.add(eleve);
            }
        }
        return eleves;
    }

    public Eleve getEleveByCode(String code) throws SQLException {
        String sql = "SELECT * FROM eleve WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, code);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Eleve(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getInt("niveau"),
                            rs.getString("code_fil")
                    );
                }
            }
        }
        return null;
    }

    public void addEleve(Eleve eleve) throws SQLException {
        String sql = "INSERT INTO eleve (code, nom, prenom, niveau, code_fil) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eleve.getCode());
            stmt.setString(2, eleve.getNom());
            stmt.setString(3, eleve.getPrenom());
            stmt.setInt(4, eleve.getNiveau());
            stmt.setString(5, eleve.getCodeFil());
            stmt.executeUpdate();
        }
    }

    public void updateEleve(Eleve eleve) throws SQLException {
        String sql = "UPDATE eleve SET nom = ?, prenom = ?, niveau = ?, code_fil = ? WHERE code = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, eleve.getNom());
            stmt.setString(2, eleve.getPrenom());
            stmt.setInt(3, eleve.getNiveau());
            stmt.setString(4, eleve.getCodeFil());
            stmt.setString(5, eleve.getCode());
            stmt.executeUpdate();
        }
    }

    public void deleteEleve(int id) throws SQLException {
        String sql = "DELETE FROM eleve WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public List<Eleve> searchEleves(String code, String nom, String prenom) throws SQLException {
        List<Eleve> results = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM eleve WHERE 1=1");

        if (code != null && !code.isEmpty()) sql.append(" AND code = ?");
        if (nom != null && !nom.isEmpty()) sql.append(" AND nom LIKE ?");
        if (prenom != null && !prenom.isEmpty()) sql.append(" AND prenom LIKE ?");

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            int index = 1;
            if (code != null && !code.isEmpty()) stmt.setString(index++, code);
            if (nom != null && !nom.isEmpty()) stmt.setString(index++, "%" + nom + "%");
            if (prenom != null && !prenom.isEmpty()) stmt.setString(index++, "%" + prenom + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Eleve(
                            rs.getInt("id"),
                            rs.getString("code"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getInt("niveau"),
                            rs.getString("code_fil")
                    ));
                }
            }
        }
        return results;
    }

    // Méthodes auxiliaires sur les filières, niveaux et matières

    public List<String> getAllFilieres() throws SQLException {
        List<String> filieres = new ArrayList<>();
        String sql = "SELECT code FROM filiere";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                filieres.add(rs.getString("code"));
            }
        }
        return filieres;
    }

    public List<Integer> getNiveauxByFiliere(String filiereCode) throws SQLException {
        List<Integer> niveaux = new ArrayList<>();
        String sql = "SELECT DISTINCT niveau FROM module WHERE code_fil = ? ORDER BY niveau";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, filiereCode);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    niveaux.add(rs.getInt("niveau"));
                }
            }
        }
        return niveaux;
    }

    public List<String> getMatieresByFiliereAndNiveau(String filiereCode, int niveau) throws SQLException {
        List<String> matieres = new ArrayList<>();
        String sql = "SELECT designation FROM module WHERE code_fil = ? AND niveau = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, filiereCode);
            stmt.setInt(2, niveau);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    matieres.add(rs.getString("designation"));
                }
            }
        }
        return matieres;
    }
}
