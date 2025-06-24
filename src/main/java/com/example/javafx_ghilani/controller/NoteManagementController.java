package com.example.javafx_ghilani.controller;
import com.example.javafx_ghilani.model.Eleve;
import com.example.javafx_ghilani.database.EleveDAO;
import com.example.javafx_ghilani.database.NoteDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.collections.FXCollections;
import java.sql.SQLException;


public class NoteManagementController {

    @FXML private Label eleveCodeLabel;
    @FXML private ComboBox<String> matiereComboBox;
    @FXML private TextField noteTextField;

    private String eleveCode;
    private final NoteDAO noteDAO = new NoteDAO();
    private final EleveDAO eleveDAO = new EleveDAO();

    public void setEleveCode(String code) {
        this.eleveCode = code;
        eleveCodeLabel.setText(code);
        loadMatieres();
    }

    @FXML
    private void initialize() {
        // Additional initialization if needed
    }

    private void loadMatieres() {
        try {
            Eleve eleve = eleveDAO.getEleveByCode(eleveCode);
            if (eleve != null) {
                matiereComboBox.setItems(FXCollections.observableArrayList(
                        eleveDAO.getMatieresByFiliereAndNiveau(eleve.getCodeFil(), eleve.getNiveau())));
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to load matieres: " + e.getMessage());
        }
    }

    @FXML
    private void addNote() {
        String matiere = matiereComboBox.getValue();
        String noteText = noteTextField.getText();
        if (matiere == null || noteText.isEmpty()) {
            showAlert("Error", "Matiere and note are required.");
            return;
        }
        try {
            double note = Double.parseDouble(noteText);
            if (note < 0 || note > 20) {
                showAlert("Error", "Note must be between 0 and 20.");
                return;
            }
            noteDAO.addNote(eleveCode, matiere, note);
            noteTextField.clear();
            showAlert("Success", "Note added.");
            calculateAverageIfComplete();
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid note format.");
        } catch (SQLException e) {
            showAlert("Error", "Failed to add note: " + e.getMessage());
        }
    }

    @FXML
    private void calculateAverage() {
        try {
            double average = noteDAO.calculateAverage(eleveCode);
            if (average >= 0) {
                noteDAO.saveAverage(eleveCode, average);
                showAlert("Success", "Average calculated and saved: " + average);
            } else {
                showAlert("Error", "No notes to calculate average.");
            }
        } catch (SQLException e) {
            showAlert("Error", "Failed to calculate average: " + e.getMessage());
        }
    }

    private void calculateAverageIfComplete() {
        // Placeholder for checking all notes (implement based on project logic)
        calculateAverage();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}