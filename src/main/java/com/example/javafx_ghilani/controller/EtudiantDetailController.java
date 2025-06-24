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

    // Suppression des labels emailLabel et ageLabel car Eleve ne possède pas ces champs
    // Si tu veux les garder dans l'interface, il faut les ajouter dans ta classe Eleve

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
            niveauLabel.setText(String.valueOf(eleve.getNiveau()));
            codeFilLabel.setText(eleve.getCodeFil());
        } else {
            // Si eleve est null, on peut nettoyer les labels (optionnel)
            idLabel.setText("");
            codeLabel.setText("");
            nomLabel.setText("");
            prenomLabel.setText("");
            niveauLabel.setText("");
            codeFilLabel.setText("");
        }
    }
}
