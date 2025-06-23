package com.example.javafx_ghilani.controller;

import com.example.javafx_ghilani.database.*;
import com.example.javafx_ghilani.model.*;
import com.example.javafx_ghilani.util.XMLUtil;
import com.example.javafx_ghilani.util.PDFUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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

    // قائمة للاحتفاظ بالطلاب المحذوفين
    private List<Eleve> deletedEleves = new ArrayList<>();

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
        // إضافة مستمعات لجميع checkboxes لضمان عدم تداخل البحث
        rechercheCodeCheckBox.setOnAction(e -> updateSearchCheckBoxes());
        rechercheNomCheckBox.setOnAction(e -> updateSearchCheckBoxes());
        recherchePrenomCheckBox.setOnAction(e -> updateSearchCheckBoxes());
        rechercheFiliereCheckBox.setOnAction(e -> updateSearchCheckBoxes());
        rechercheNiveauCheckBox.setOnAction(e -> updateSearchCheckBoxes());
    }

    private void updateSearchCheckBoxes() {
        // يمكن تحديد أكثر من خيار بحث في نفس الوقت
        // هذا منطق أفضل من تحديد خيار واحد فقط
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
        // يمكن تحسين هذا لتحميل المستويات المتاحة للفرع المحدد
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

        // البحث عن الفرع وتحديده
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
            selectedEleve = null;
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
                // إضافة الطالب المحذوف إلى القائمة
                deletedEleves.add(selectedEleve);

                // أرشفة في XML
                XMLUtil.archiveDeletedEleve(selectedEleve);

                // حذف من قاعدة البيانات
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
            String code = rechercheCodeCheckBox.isSelected() && !codeField.getText().isEmpty()
                    ? codeField.getText() : null;
            String nom = rechercheNomCheckBox.isSelected() && !nomField.getText().isEmpty()
                    ? nomField.getText() : null;
            String prenom = recherchePrenomCheckBox.isSelected() && !prenomField.getText().isEmpty()
                    ? prenomField.getText() : null;
            String codeFil = rechercheFiliereCheckBox.isSelected() && filiereComboBox.getValue() != null
                    ? filiereComboBox.getValue().getCode() : null;
            Integer niveau = rechercheNiveauCheckBox.isSelected() ? niveauComboBox.getValue() : null;

            // إذا لم يتم تحديد أي معايير بحث، أظهر جميع الطلاب
            if (code == null && nom == null && prenom == null && codeFil == null && niveau == null) {
                loadAllEleves();
                return;
            }

            List<Eleve> resultats = eleveDAO.searchEleves(code, nom, prenom, codeFil, niveau);
            elevesData.setAll(resultats);

            if (resultats.isEmpty()) {
                showAlert("Information", "Aucun résultat trouvé pour les critères de recherche spécifiés.");
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Erreur lors de la recherche : " + e.getMessage());
        }
    }

    @FXML
    private void onAfficherTousClick() {
        // إعادة تحميل جميع الطلاب
        loadAllEleves();
        clearForm();
        clearSearchCheckBoxes();
    }

    private void clearSearchCheckBoxes() {
        rechercheCodeCheckBox.setSelected(false);
        rechercheNomCheckBox.setSelected(false);
        recherchePrenomCheckBox.setSelected(false);
        rechercheFiliereCheckBox.setSelected(false);
        rechercheNiveauCheckBox.setSelected(false);
    }

    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();

        if (codeField.getText().trim().isEmpty()) {
            errors.append("- Le code est obligatoire\n");
        }
        if (nomField.getText().trim().isEmpty()) {
            errors.append("- Le nom est obligatoire\n");
        }
        if (prenomField.getText().trim().isEmpty()) {
            errors.append("- Le prénom est obligatoire\n");
        }
        if (niveauComboBox.getValue() == null) {
            errors.append("- Le niveau est obligatoire\n");
        }
        if (filiereComboBox.getValue() == null) {
            errors.append("- La filière est obligatoire\n");
        }

        if (errors.length() > 0) {
            showAlert("Validation", "Veuillez corriger les erreurs suivantes :\n" + errors.toString());
            return false;
        }
        return true;
    }

    private Eleve createEleveFromForm() {
        String code = codeField.getText().trim();
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
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
        // إنشاء مربع حوار لاختيار نوع التقرير
        Alert choiceAlert = new Alert(Alert.AlertType.CONFIRMATION);
        choiceAlert.setTitle("Choix du rapport");
        choiceAlert.setHeaderText("Quel type de rapport voulez-vous générer ?");

        ButtonType btnTousEtudiants = new ButtonType("Tous les étudiants");
        ButtonType btnEtudiantsSupprimés = new ButtonType("Étudiants supprimés");
        ButtonType btnAnnuler = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

        choiceAlert.getButtonTypes().setAll(btnTousEtudiants, btnEtudiantsSupprimés, btnAnnuler);

        Optional<ButtonType> choice = choiceAlert.showAndWait();

        if (choice.isPresent()) {
            if (choice.get() == btnTousEtudiants) {
                generateAllStudentsPDF();
            } else if (choice.get() == btnEtudiantsSupprimés) {
                generateDeletedStudentsPDF();
            }
        }
    }

    private void generateAllStudentsPDF() {
        try {
            // اختيار مكان حفظ الملف
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le rapport PDF");
            fileChooser.setInitialFileName("rapport_tous_etudiants.pdf");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
            );

            Stage stage = (Stage) elevesTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                List<Eleve> currentEleves = new ArrayList<>(elevesData);
                PDFUtil.generateStudentsReport(currentEleves, file.getAbsolutePath(),
                        "Rapport de Tous les Étudiants");
                showAlert("Succès", "Rapport PDF généré avec succès !\nFichier : " + file.getAbsolutePath());
            }
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la génération du PDF : " + e.getMessage());
        }
    }

    private void generateDeletedStudentsPDF() {
        try {
            if (deletedEleves.isEmpty()) {
                showAlert("Information", "Aucun étudiant supprimé à inclure dans le rapport.");
                return;
            }

            // اختيار مكان حفظ الملف
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sauvegarder le rapport des étudiants supprimés");
            fileChooser.setInitialFileName("rapport_etudiants_supprimes.pdf");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Fichiers PDF", "*.pdf")
            );

            Stage stage = (Stage) elevesTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file != null) {
                PDFUtil.generateDeletedStudentsReport(deletedEleves, file.getAbsolutePath());
                showAlert("Succès", "Rapport des étudiants supprimés généré avec succès !\nFichier : " + file.getAbsolutePath());
            }
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

    // دالة للحصول على قائمة الطلاب المحذوفين (يمكن استخدamها في مكان آخر)
    public List<Eleve> getDeletedEleves() {
        return new ArrayList<>(deletedEleves);
    }

    // دالة لمسح قائمة الطلاب المحذوفين
    public void clearDeletedEleves() {
        deletedEleves.clear();
    }
    /**
     * Recherche des étudiants avec des critères optionnels.
     */
    public List<Eleve> rechercherEleves(String code, String nom, String prenom, String codeFil, Integer niveau) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT * FROM Eleve WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (code != null && !code.isEmpty()) {
            sql.append(" AND code LIKE ?");
            params.add("%" + code + "%");
        }
        if (nom != null && !nom.isEmpty()) {
            sql.append(" AND nom LIKE ?");
            params.add("%" + nom + "%");
        }
        if (prenom != null && !prenom.isEmpty()) {
            sql.append(" AND prenom LIKE ?");
            params.add("%" + prenom + "%");
        }
        if (codeFil != null && !codeFil.isEmpty()) {
            sql.append(" AND code_fil = ?");
            params.add(codeFil);
        }
        if (niveau != null) {
            sql.append(" AND niveau = ?");
            params.add(niveau);
        }

        List<Eleve> eleves = new ArrayList<>();
        try (PreparedStatement stmt = dbManager.getConnection().prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Eleve eleve = new Eleve();
                    eleve.setId(rs.getInt("id"));
                    eleve.setCode(rs.getString("code"));
                    eleve.setNom(rs.getString("nom"));
                    eleve.setPrenom(rs.getString("prenom"));
                    eleve.setNiveau(rs.getInt("niveau"));
                    eleve.setCodeFil(rs.getString("code_fil"));
                    eleves.add(eleve);
                }
            }
        }
        return eleves;
    }
}