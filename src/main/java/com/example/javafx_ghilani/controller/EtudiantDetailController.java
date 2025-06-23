
package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.model.Eleve;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EtudiantDetailController {
    @FXML private Label codeLabel;
    @FXML private Label nomLabel;
    @FXML private Label prenomLabel;
    @FXML private Label niveauLabel;
    @FXML private Label filiereLabel;

    public void setEleve(Eleve eleve) {
        codeLabel.setText(eleve.getCode());
        nomLabel.setText(eleve.getNom());
        prenomLabel.setText(eleve.getPrenom());
        niveauLabel.setText(String.valueOf(eleve.getNiveau()));
        filiereLabel.setText(eleve.getCodeFil());
    }
}