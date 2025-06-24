package com.example.javafx_ghilani.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NoteDAO {
    private final DatabaseManager dbManager = new DatabaseManager();

    public void addNote(String eleveCode, String matiereCode, double note) throws SQLException {
        String sql = "INSERT INTO Notes (code_eleve, code_mat, note) VALUES (?, ?, ?)";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, eleveCode);
            pstmt.setString(2, matiereCode);
            pstmt.setDouble(3, note);
            pstmt.executeUpdate();
        }
    }

    public double calculateAverage(String eleveCode) throws SQLException {
        String sql = "SELECT AVG(note) FROM Notes WHERE code_eleve = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, eleveCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        }
        return -1; // Error indicator
    }

    public void saveAverage(String eleveCode, double average) throws SQLException {
        String sql = "INSERT INTO Moyennes (code_eleve, code_fil, niveau, moyenne) VALUES (?, (SELECT code_fil FROM Eleve WHERE code = ?), (SELECT niveau FROM Eleve WHERE code = ?), ?) " +
                "ON DUPLICATE KEY UPDATE moyenne = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, eleveCode);
            pstmt.setString(2, eleveCode);
            pstmt.setString(3, eleveCode);
            pstmt.setDouble(4, average);
            pstmt.setDouble(5, average);
            pstmt.executeUpdate();
        }
    }
}