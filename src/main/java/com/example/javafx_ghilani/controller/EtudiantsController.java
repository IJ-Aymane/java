package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.database.EleveDAO;
import com.example.javafx_ghilani.model.Eleve;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class EtudiantsController {

    @FXML private ComboBox<String> filiereComboBox;
    @FXML private ComboBox<Integer> niveauComboBox;
    @FXML private TextField codeTextField;
    @FXML private TextField nomTextField;
    @FXML private TextField prenomTextField;
    @FXML private Button nouveauButton;
    @FXML private Button ajouterButton;
    @FXML private Button modifierButton;
    @FXML private Button supprimerButton;
    @FXML private Button rechercherButton;
    @FXML private Button gestionNotesButton;
    @FXML private CheckBox codeCheckBox;
    @FXML private CheckBox nomCheckBox;
    @FXML private CheckBox prenomCheckBox;
    @FXML private TableView<Eleve> tableView;
    @FXML private TableColumn<Eleve, String> colCode;
    @FXML private TableColumn<Eleve, String> colNom;
    @FXML private TableColumn<Eleve, String> colPrenom;
    @FXML private TableColumn<Eleve, String> colFiliere;
    @FXML private TableColumn<Eleve, Integer> colNiveau;

    private final EleveDAO eleveDAO = new EleveDAO();
    private ObservableList<Eleve> eleveList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        try {
            loadFilieres();
            filiereComboBox.valueProperty().addListener((obs, oldVal, newVal) -> loadNiveaux(newVal));
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de chargement des données: " + e.getMessage());
        }
        initializeTable();
        nouveauButton.setOnAction(e -> resetForm());
        ajouterButton.setOnAction(e -> addEleve());
        modifierButton.setOnAction(e -> modifyEleve());
        supprimerButton.setOnAction(e -> deleteEleve());
        rechercherButton.setOnAction(e -> searchEleves());
        gestionNotesButton.setOnAction(e -> openNoteManagement());
    }

    private void initializeTable() {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colFiliere.setCellValueFactory(new PropertyValueFactory<>("filiere"));
        colNiveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        try {
            eleveList.setAll(eleveDAO.getAllEleves());
            tableView.setItems(eleveList);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les étudiants: " + e.getMessage());
        }
    }

    private void loadFilieres() throws SQLException {
        ObservableList<String> filieres = FXCollections.observableArrayList(eleveDAO.getAllFilieres());
        filiereComboBox.setItems(filieres);
    }

    private void loadNiveaux(String filiereCode) {
        if (filiereCode != null) {
            try {
                ObservableList<Integer> niveaux = FXCollections.observableArrayList(eleveDAO.getNiveauxByFiliere(filiereCode));
                niveauComboBox.setItems(niveaux);
                niveauComboBox.setValue(null);
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de chargement des niveaux: " + e.getMessage());
            }
        }
    }

    private void resetForm() {
        codeTextField.clear();
        nomTextField.clear();
        prenomTextField.clear();
        filiereComboBox.setValue(null);
        niveauComboBox.setValue(null);
    }

    private void addEleve() {
        String code = codeTextField.getText();
        String nom = nomTextField.getText();
        String prenom = prenomTextField.getText();
        String filiere = filiereComboBox.getValue();
        Integer niveau = niveauComboBox.getValue();

        if (code.isEmpty() || nom.isEmpty() || prenom.isEmpty() || filiere == null || niveau == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return;
        }
        try {
            if (eleveDAO.getEleveByCode(code) != null) {
                showAlert("Erreur", "Le code doit être unique.");
                return;
            }
            Eleve eleve = new Eleve(0, code, nom, prenom, "", 0, niveau, filiere);
            eleveDAO.addEleve(eleve);
            eleveList.add(eleve);
            resetForm();
            showAlert("Succès", "Étudiant ajouté avec succès.");
        } catch (SQLException e) {
            showAlert("Erreur", "Échec d'ajout: " + e.getMessage());
        }
    }

    private void modifyEleve() {
        String code = codeTextField.getText();
        try {
            if (code.isEmpty() || eleveDAO.getEleveByCode(code) == null) {
                showAlert("Erreur", "Code invalide ou inexistant.");
                return;
            }
            Eleve eleve = new Eleve(0, code, nomTextField.getText(), prenomTextField.getText(), "", 0,
                    niveauComboBox.getValue(), filiereComboBox.getValue());
            eleveDAO.updateEleve(eleve);
            showAlert("Succès", "Étudiant modifié.");
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de modification: " + e.getMessage());
        }
    }

    private void deleteEleve() {
        String code = codeTextField.getText();
        try {
            if (code.isEmpty() || eleveDAO.getEleveByCode(code) == null) {
                showAlert("Erreur", "Code invalide ou inexistant.");
                return;
            }
            if (showConfirmation("Confirmation", "Confirmer la suppression ?")) {
                Eleve eleve = eleveDAO.getEleveByCode(code);
                eleveDAO.deleteEleve(eleve.getId());
                archiveDeletedEleve(eleve);
                eleveList.remove(eleve);
                resetForm();
                showAlert("Succès", "Étudiant supprimé et archivé.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de suppression: " + e.getMessage());
        }
    }

    private void searchEleves() {
        String code = codeCheckBox.isSelected() ? codeTextField.getText() : null;
        String nom = nomCheckBox.isSelected() ? nomTextField.getText() : null;
        String prenom = prenomCheckBox.isSelected() ? prenomTextField.getText() : null;

        if (code != null) {
            nomCheckBox.setSelected(false);
            prenomCheckBox.setSelected(false);
        }
        try {
            List<Eleve> results = eleveDAO.searchEleves(code, nom, prenom);
            eleveList.setAll(results);
        } catch (SQLException e) {
            showAlert("Erreur", "Échec de recherche: " + e.getMessage());
        }
    }

    private void openNoteManagement() {
        String code = codeTextField.getText();
        try {
            if (code.isEmpty() || eleveDAO.getEleveByCode(code) == null) {
                showAlert("Erreur", "Code étudiant invalide ou inexistant.");
                return;
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_ghilani/note_management.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Gestion des Notes");
            NoteManagementController controller = loader.getController();
            controller.setEleveCode(code);
            stage.showAndWait();
        } catch (IOException | SQLException e) {
            showAlert("Erreur", "Erreur de chargement: " + e.getMessage());
        }
    }

    private void archiveDeletedEleve(Eleve eleve) {
        System.out.println("Archivé: " + eleve.getCode() + " à " + LocalDateTime.now());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}