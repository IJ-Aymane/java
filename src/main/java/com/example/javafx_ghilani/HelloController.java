package com.example.javafx_ghilani;

import com.example.javafx_ghilani.controller.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {

    @FXML
    protected void onEtudiantsClick() {
        openEtudiantsWindow();
    }

    @FXML
    protected void onNotesClick() {
        openNotesWindow();
    }

    @FXML
    protected void onConsultationClick() {
        openConsultationWindow();
    }

    @FXML
    protected void onArchivesClick() {
        openArchivesWindow();
    }

    private void openEtudiantsWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("etudiants-view.fxml"));
            Scene scene = new Scene(loader.load(), 1000, 700);
            Stage stage = new Stage();
            stage.setTitle("Gestion des Étudiants");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de gestion des étudiants: " + e.getMessage());
        }
    }

    private void openNotesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("notes-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Gestion des Notes");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de gestion des notes: " + e.getMessage());
        }
    }

    private void openConsultationWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("consultation-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Consultation");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre de consultation: " + e.getMessage());
        }
    }

    private void openArchivesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("archives-view.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);
            Stage stage = new Stage();
            stage.setTitle("Archives");
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre des archives: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}