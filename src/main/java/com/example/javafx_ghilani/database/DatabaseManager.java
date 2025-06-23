package com.example.javafx_ghilani.database;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/gestion_notes";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            createTables();
            createStoredProcedures();
            createTriggers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    private void createTables() throws SQLException {
        String[] tables = {
                """
            CREATE TABLE IF NOT EXISTS Filiere (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code VARCHAR(10) UNIQUE NOT NULL,
                designation VARCHAR(100) NOT NULL
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS Module (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code VARCHAR(10) UNIQUE NOT NULL,
                designation VARCHAR(100) NOT NULL,
                niveau INT NOT NULL,
                semestre INT NOT NULL,
                code_fil VARCHAR(10),
                FOREIGN KEY (code_fil) REFERENCES Filiere(code)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS Matiere (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code VARCHAR(10) UNIQUE NOT NULL,
                designation VARCHAR(100) NOT NULL,
                VH INT NOT NULL,
                code_module VARCHAR(10),
                FOREIGN KEY (code_module) REFERENCES Module(code)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS Eleve (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code VARCHAR(10) UNIQUE NOT NULL,
                nom VARCHAR(50) NOT NULL,
                prenom VARCHAR(50) NOT NULL,
                niveau INT NOT NULL,
                code_fil VARCHAR(10),
                FOREIGN KEY (code_fil) REFERENCES Filiere(code)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS Notes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code_eleve VARCHAR(10),
                code_mat VARCHAR(10),
                note DECIMAL(4,2),
                FOREIGN KEY (code_eleve) REFERENCES Eleve(code),
                FOREIGN KEY (code_mat) REFERENCES Matiere(code)
            )
            """,
                """
            CREATE TABLE IF NOT EXISTS Moyennes (
                id INT AUTO_INCREMENT PRIMARY KEY,
                code_eleve VARCHAR(10),
                code_fil VARCHAR(10),
                niveau INT,
                moyenne DECIMAL(4,2),
                FOREIGN KEY (code_eleve) REFERENCES Eleve(code),
                FOREIGN KEY (code_fil) REFERENCES Filiere(code)
            )
            """
        };

        for (String table : tables) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(table);
            }
        }

        // Insert initial data
        insertInitialData();
    }

    private void insertInitialData() throws SQLException {
        // Insert Filieres
        String insertFiliere = "INSERT IGNORE INTO Filiere (code, designation) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertFiliere)) {
            stmt.setString(1, "GI");
            stmt.setString(2, "Génie Informatique");
            stmt.executeUpdate();

            stmt.setString(1, "GE");
            stmt.setString(2, "Génie Électrique");
            stmt.executeUpdate();
        }

        // Insert Modules
        String insertModule = "INSERT IGNORE INTO Module (code, designation, niveau, semestre, code_fil) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertModule)) {
            stmt.setString(1, "M1");
            stmt.setString(2, "Programmation");
            stmt.setInt(3, 1);
            stmt.setInt(4, 1);
            stmt.setString(5, "GI");
            stmt.executeUpdate();

            stmt.setString(1, "M2");
            stmt.setString(2, "Base de Données");
            stmt.setInt(3, 2);
            stmt.setInt(4, 1);
            stmt.setString(5, "GI");
            stmt.executeUpdate();
        }

        // Insert Matieres
        String insertMatiere = "INSERT IGNORE INTO Matiere (code, designation, VH, code_module) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(insertMatiere)) {
            stmt.setString(1, "MAT1");
            stmt.setString(2, "Java");
            stmt.setInt(3, 40);
            stmt.setString(4, "M1");
            stmt.executeUpdate();

            stmt.setString(1, "MAT2");
            stmt.setString(2, "C++");
            stmt.setInt(3, 30);
            stmt.setString(4, "M1");
            stmt.executeUpdate();

            stmt.setString(1, "MAT3");
            stmt.setString(2, "SQL");
            stmt.setInt(3, 35);
            stmt.setString(4, "M2");
            stmt.executeUpdate();
        }
    }

    private void createStoredProcedures() throws SQLException {
        String[] procedures = {
                """
            CREATE PROCEDURE IF NOT EXISTS AddEleve(
                IN p_code VARCHAR(10),
                IN p_nom VARCHAR(50),
                IN p_prenom VARCHAR(50),
                IN p_niveau INT,
                IN p_code_fil VARCHAR(10)
            )
            BEGIN
                INSERT INTO Eleve (code, nom, prenom, niveau, code_fil)
                VALUES (p_code, p_nom, p_prenom, p_niveau, p_code_fil);
            END
            """,
                """
            CREATE PROCEDURE IF NOT EXISTS UpdateEleve(
                IN p_code VARCHAR(10),
                IN p_nom VARCHAR(50),
                IN p_prenom VARCHAR(50),
                IN p_niveau INT,
                IN p_code_fil VARCHAR(10)
            )
            BEGIN
                UPDATE Eleve 
                SET nom = p_nom, prenom = p_prenom, niveau = p_niveau, code_fil = p_code_fil
                WHERE code = p_code;
            END
            """,
                """
            CREATE PROCEDURE IF NOT EXISTS DeleteEleve(IN p_code VARCHAR(10))
            BEGIN
                DELETE FROM Notes WHERE code_eleve = p_code;
                DELETE FROM Moyennes WHERE code_eleve = p_code;
                DELETE FROM Eleve WHERE code = p_code;
            END
            """
        };

        for (String procedure : procedures) {
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("DROP PROCEDURE IF EXISTS " + procedure.split(" ")[3]);
                stmt.execute(procedure.replace("IF NOT EXISTS", ""));
            }
        }
    }

    private void createTriggers() throws SQLException {
        String trigger = """
            CREATE TRIGGER IF NOT EXISTS after_moyenne_insert
            AFTER INSERT ON Moyennes
            FOR EACH ROW
            BEGIN
                IF NEW.moyenne >= 10 THEN
                    UPDATE Eleve 
                    SET niveau = niveau + 1 
                    WHERE code = NEW.code_eleve AND niveau < 3;
                END IF;
            END
            """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TRIGGER IF EXISTS after_moyenne_insert");
            stmt.execute(trigger.replace("IF NOT EXISTS", ""));
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}