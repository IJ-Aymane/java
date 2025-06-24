package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.model.Eleve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class EtudiantsController {

    @FXML private TextField txtCode;
    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtNiveau;
    @FXML private TextField txtCodeFil;

    @FXML private TableView<Eleve> tableEleves;
    @FXML private TableColumn<Eleve, Integer> colId;
    @FXML private TableColumn<Eleve, String> colCode;
    @FXML private TableColumn<Eleve, String> colNom;
    @FXML private TableColumn<Eleve, String> colPrenom;
    @FXML private TableColumn<Eleve, Integer> colNiveau;
    @FXML private TableColumn<Eleve, String> colCodeFil;

    private final ObservableList<Eleve> listeEleves = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialisation des colonnes avec les noms des propriétés dans la classe Eleve
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        colCodeFil.setCellValueFactory(new PropertyValueFactory<>("codeFil"));

        tableEleves.setItems(listeEleves);

        // Écouteur sur la sélection dans la table pour afficher les détails dans les champs
        tableEleves.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSelection, newSelection) -> afficherDetails(newSelection));
    }

    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            String code = txtCode.getText().trim();
            String nom = txtNom.getText().trim();
            String prenom = txtPrenom.getText().trim();
            int niveau = Integer.parseInt(txtNiveau.getText().trim());
            String codeFil = txtCodeFil.getText().trim();

            if(code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || codeFil.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs.");
                return;
            }

            int id = listeEleves.size() + 1; // Id généré simplement ici

            Eleve eleve = new Eleve(id, code, nom, prenom, niveau, codeFil);
            listeEleves.add(eleve);
            clearTextFields();
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le niveau doit être un nombre entier.");
        }
    }

    @FXML
    private void handleModifier(ActionEvent event) {
        Eleve selected = tableEleves.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String code = txtCode.getText().trim();
                String nom = txtNom.getText().trim();
                String prenom = txtPrenom.getText().trim();
                int niveau = Integer.parseInt(txtNiveau.getText().trim());
                String codeFil = txtCodeFil.getText().trim();

                if(code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || codeFil.isEmpty()) {
                    showAlert("Erreur", "Veuillez remplir tous les champs.");
                    return;
                }

                selected.setCode(code);
                selected.setNom(nom);
                selected.setPrenom(prenom);
                selected.setNiveau(niveau);
                selected.setCodeFil(codeFil);

                tableEleves.refresh();
                clearTextFields();
            } catch (NumberFormatException e) {
                showAlert("Erreur", "Le niveau doit être un nombre entier.");
            }
        } else {
            showAlert("Attention", "Veuillez sélectionner un élève à modifier.");
        }
    }

    @FXML
    private void handleSupprimer(ActionEvent event) {
        Eleve selected = tableEleves.getSelectionModel().getSelectedItem();
        if (selected != null) {
            listeEleves.remove(selected);
            clearTextFields();
        } else {
            showAlert("Attention", "Veuillez sélectionner un élève à supprimer.");
        }
    }

    private void afficherDetails(Eleve eleve) {
        if (eleve != null) {
            txtCode.setText(eleve.getCode());
            txtNom.setText(eleve.getNom());
            txtPrenom.setText(eleve.getPrenom());
            txtNiveau.setText(String.valueOf(eleve.getNiveau()));
            txtCodeFil.setText(eleve.getCodeFil());
        } else {
            clearTextFields();
        }
    }

    private void clearTextFields() {
        txtCode.clear();
        txtNom.clear();
        txtPrenom.clear();
        txtNiveau.clear();
        txtCodeFil.clear();
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
