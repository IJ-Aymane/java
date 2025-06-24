package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.model.Eleve;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EtudiantDetailController {

    @FXML
    private Label idLabel;

    @FXML
    private Label codeLabel;

    @FXML
    private Label nomLabel;

    @FXML
    private Label prenomLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label ageLabel;

    @FXML
    private Label niveauLabel;

    @FXML
    private Label codeFilLabel;

    public void setEleve(Eleve eleve) {
        if (eleve != null) {
            idLabel.setText(String.valueOf(eleve.getId()));
            codeLabel.setText(eleve.getCode());
            nomLabel.setText(eleve.getNom());
            prenomLabel.setText(eleve.getPrenom());
            emailLabel.setText(eleve.getEmail());
            ageLabel.setText(String.valueOf(eleve.getAge()));
            niveauLabel.setText(String.valueOf(eleve.getNiveau()));
            codeFilLabel.setText(eleve.getCodeFil());
        }
    }
}