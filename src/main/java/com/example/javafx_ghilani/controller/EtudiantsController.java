package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.database.*;
import com.example.javafx_ghilani.model.*;
import com.example.javafx_ghilani.util.XMLUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EtudiantsController implements Initializable {

    @FXML private ComboBox<Filiere> filiereComboBox;
    @FXML private ComboBox<Integer> niveauComboBox;
    @FXML private TextField codeField;
    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TableView<Eleve> elevesTable;
    @FXML private TableColumn<Eleve, String> codeColumn;
    @FXML private TableColumn<Eleve, String> nomColumn;
    @FXML private TableColumn<Eleve, String> prenomColumn;
    @FXML private TableColumn<Eleve, Integer> niveauColumn;
    @FXML private TableColumn<Eleve, String> filiereColumn;
    @FXML private CheckBox rechercheCodeCheckBox;
    @FXML private CheckBox rechercheNomCheckBox;
    @FXML private CheckBox recherchePrenomCheckBox;
    @FXML private CheckBox rechercheFiliereCheckBox;
    @FXML private CheckBox rechercheNiveauCheckBox;

    private DatabaseManager dbManager;
    private EleveDAO eleveDAO;
    private FiliereDAO filiereDAO;
    private ObservableList<Eleve> elevesData;
    private Eleve selectedEleve;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = new DatabaseManager();
        eleveDAO = new EleveDAO(dbManager);
        filiereDAO = new FiliereDAO(dbManager);
        elevesData = FXCollections.observableArrayList();

        setupTableColumns();
        setupComboBoxes();
        loadFilieres();
        loadAllEleves();
        setupTableSelection();
        setupCheckBoxListeners();
    }

    private void setupTableColumns() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        niveauColumn.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        filiereColumn.setCellValueFactory(new PropertyValueFactory<>("codeFil"));

        elevesTable.setItems(elevesData);
    }

    private void setupComboBoxes() {
        niveauComboBox.setItems(FXCollections.observableArrayList(1, 2, 3));

        filiereComboBox.setOnAction(e -> {
            Filiere selectedFiliere = filiereComboBox.getSelectionModel().getSelectedItem();
            if (selectedFiliere != null) {
                loadNiveauxForFiliere(selectedFiliere.getCode());
            }
        });
    }

    private void setupTableSelection() {
        elevesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedEleve = newSelection;
                fillFormWithSelectedEleve(newSelection);
            }
        });
    }

    private void setupCheckBoxListeners() {
        rechercheCodeCheckBox.setOnAction(e -> {
            if (rechercheCodeCheckBox.isSelected()) {
                rechercheNomCheckBox.setSelected(false);
                recherchePrenomCheckBox.setSelected(false);
                rechercheFiliereCheckBox.setSelected(false);
                rechercheNiveauCheckBox.setSelected(false);
            }
        });
    }

    private void loadFilieres() {
        try {
            List<Filiere> filieres = filiereDAO.getAllFilieres();
            filiereComboBox.setItems(FXCollections.observableArrayList(filieres));
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les filières: " + e.getMessage());
        }
    }

    private void loadNiveauxForFiliere(String codeFiliere) {
        // For this example, we'll show all levels
        niveauComboBox.setItems(FXCollections.observableArrayList(1, 2, 3));
    }

    private void loadAllEleves() {
        try {
            List<Eleve> eleves = eleveDAO.getAllEleves();
            elevesData.clear();
            elevesData.addAll(eleves);
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de charger les étudiants: " + e.getMessage());
        }
    }

    private void fillFormWithSelectedEleve(Eleve eleve) {
        codeField.setText(eleve.getCode());
        nomField.setText(eleve.getNom());
        prenomField.setText(eleve.getPrenom());
        niveauComboBox.setValue(eleve.getNiveau());

        // Find and select the filiere
        for (Filiere filiere : filiereComboBox.getItems()) {
            if (filiere.getCode().equals(eleve.getCodeFil())) {
                filiereComboBox.setValue(filiere);
                break;
            }
        }
    }

    @FXML
    private void onNouveauClick() {
        clearForm();
        selectedEleve = null;
    }

    @FXML
    private void onAjouterClick() {
        if (!validateForm()) {
            return;
        }

        try {
            Eleve eleve = createEleveFromForm();
            eleveDAO.addEleve(eleve);
            loadAllEleves();
            clearForm();
            showAlert("Succès", "Étudiant ajouté avec succès!");
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible d'ajouter l'étudiant: " + e.getMessage());
        }
    }

    @FXML
    private void onModifierClick() {
        if (selectedEleve == null) {
            showAlert("Erreur", "Veuillez sélectionner un étudiant à modifier.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        try {
            Eleve eleve = createEleveFromForm();
            eleveDAO.updateEleve(eleve);
            loadAllEleves();
            clearForm();
            showAlert("Succès", "Étudiant modifié avec succès!");
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de modifier l'étudiant: " + e.getMessage());
        }
    }

    @FXML
    private void onSupprimerClick() {
        if (selectedEleve == null) {
            showAlert("Erreur", "Veuillez sélectionner un étudiant à supprimer.");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirmation");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet étudiant?");

        Optional<ButtonType> result = confirmAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                XMLUtil.archiveDeletedEleve(selectedEleve);
                eleveDAO.deleteEleve(selectedEleve.getCode());
                loadAllEleves();
                clearForm();
                selectedEleve = null;
                showAlert("Succès", "Étudiant supprimé avec succès!");
            } catch (SQLException e) {
                showAlert("Erreur", "Impossible de supprimer l'étudiant: " + e.getMessage());
            }
        }
    }

    @FXML
    private void onRechercherClick() {
        try {
            String code = rechercheCodeCheckBox.isSelected() ? codeField.getText() : null;
            String nom = rechercheNomCheckBox.isSelected() ? nomField.getText() : null;
            String prenom = recherchePrenomCheckBox.isSelected() ? prenomField.getText() : null;
            String codeFil = rechercheFiliereCheckBox.isSelected() && filiereComboBox.getValue() != null
                    ? filiereComboBox.getValue().getCode()
                    : null;
            Integer niveau = rechercheNiveauCheckBox.isSelected() ? niveauComboBox.getValue() : null;

            List<Eleve> resultats = eleveDAO.rechercherEleves(code, nom, prenom, codeFil, niveau);
            elevesData.setAll(resultats);
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }

    private boolean validateForm() {
        if (codeField.getText().isEmpty() || nomField.getText().isEmpty() || prenomField.getText().isEmpty()
                || niveauComboBox.getValue() == null || filiereComboBox.getValue() == null) {
            showAlert("Validation", "Veuillez remplir tous les champs.");
            return false;
        }
        return true;
    }

    private Eleve createEleveFromForm() {
        String code = codeField.getText();
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        int niveau = niveauComboBox.getValue();
        String codeFil = filiereComboBox.getValue().getCode();
        return new Eleve(code, nom, prenom, niveau, codeFil);
    }

    private void clearForm() {
        codeField.clear();
        nomField.clear();
        prenomField.clear();
        niveauComboBox.setValue(null);
        filiereComboBox.setValue(null);
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onExporterXMLClick() {
        try {
            XMLUtil.exportElevesToXML(elevesData);
            showAlert("Succès", "Exportation XML réussie !");
        } catch (IOException e) {
            showAlert("Erreur", "Erreur lors de l'exportation XML : " + e.getMessage());
        }
    }

    @FXML
    private void onImprimerPDFClick() {
        try {
            com.example.javafx_ghilani.util.PDFUtil.exportElevesToPDF(elevesData);
            showAlert("Succès", "Exportation PDF réussie !");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    @FXML
    private void onAfficherDetailClick() {
        if (selectedEleve == null) {
            showAlert("Information", "Veuillez sélectionner un étudiant pour afficher les détails.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javafx_ghilani/etudiant-detail-view.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Détails de l'étudiant");

            EtudiantDetailController controller = loader.getController();
            controller.setEleve(selectedEleve);
            stage.showAndWait();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible d'afficher les détails : " + e.getMessage());
        }
    }
}
