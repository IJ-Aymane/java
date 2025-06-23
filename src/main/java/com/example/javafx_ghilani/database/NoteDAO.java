// NoteDAO.java
package com.example.javafx_ghilani.database;

import com.example.javafx_ghilani.model.Note;
import com.example.javafx_ghilani.model.Moyenne;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {
    private DatabaseManager dbManager;

    public NoteDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void addNote(Note note) throws SQLException {
        String sql = "INSERT INTO Notes (code_eleve, code_mat, note) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, note.getCodeEleve());
            stmt.setString(2, note.getCodeMat());
            stmt.setDouble(3, note.getNote());
            stmt.executeUpdate();
        }
    }

    public void updateNote(Note note) throws SQLException {
        String sql = "UPDATE Notes SET note = ? WHERE code_eleve = ? AND code_mat = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setDouble(1, note.getNote());
            stmt.setString(2, note.getCodeEleve());
            stmt.setString(3, note.getCodeMat());
            stmt.executeUpdate();
        }
    }

    public List<Note> getNotesByEleve(String codeEleve) throws SQLException {
        List<Note> notes = new ArrayList<>();
        String sql = "SELECT * FROM Notes WHERE code_eleve = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, codeEleve);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Note note = new Note();
                    note.setId(rs.getInt("id"));
                    note.setCodeEleve(rs.getString("code_eleve"));
                    note.setCodeMat(rs.getString("code_mat"));
                    note.setNote(rs.getDouble("note"));
                    notes.add(note);
                }
            }
        }
        return notes;
    }

    public Note getNoteByEleveAndMatiere(String codeEleve, String codeMatiere) throws SQLException {
        String sql = "SELECT * FROM Notes WHERE code_eleve = ? AND code_mat = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, codeEleve);
            stmt.setString(2, codeMatiere);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Note note = new Note();
                    note.setId(rs.getInt("id"));
                    note.setCodeEleve(rs.getString("code_eleve"));
                    note.setCodeMat(rs.getString("code_mat"));
                    note.setNote(rs.getDouble("note"));
                    return note;
                }
            }
        }
        return null;
    }

    public double calculateMoyenne(String codeEleve) throws SQLException {
        String sql = "SELECT AVG(note) as moyenne FROM Notes WHERE code_eleve = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, codeEleve);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("moyenne");
                }
            }
        }
        return 0.0;
    }

    public void saveMoyenne(Moyenne moyenne) throws SQLException {
        String sql = "INSERT INTO Moyennes (code_eleve, code_fil, niveau, moyenne) VALUES (?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE moyenne = VALUES(moyenne)";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, moyenne.getCodeEleve());
            stmt.setString(2, moyenne.getCodeFil());
            stmt.setInt(3, moyenne.getNiveau());
            stmt.setDouble(4, moyenne.getMoyenne());
            stmt.executeUpdate();
        }
    }

    public Moyenne getMoyenneByEleve(String codeEleve) throws SQLException {
        String sql = "SELECT * FROM Moyennes WHERE code_eleve = ?";
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql)) {
            stmt.setString(1, codeEleve);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Moyenne moyenne = new Moyenne();
                    moyenne.setId(rs.getInt("id"));
                    moyenne.setCodeEleve(rs.getString("code_eleve"));
                    moyenne.setCodeFil(rs.getString("code_fil"));
                    moyenne.setNiveau(rs.getInt("niveau"));
                    moyenne.setMoyenne(rs.getDouble("moyenne"));
                    return moyenne;
                }
            }
        }
        return null;
    }
}
