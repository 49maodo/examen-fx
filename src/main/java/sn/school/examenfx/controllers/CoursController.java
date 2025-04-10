package sn.school.examenfx.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.school.examenfx.dao.CoursImpl;
import sn.school.examenfx.dao.SalleImpl;
import sn.school.examenfx.dao.UserImpl;
import sn.school.examenfx.entities.Cours;
import sn.school.examenfx.entities.Salle;
import sn.school.examenfx.entities.User;

import java.net.URL;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class CoursController implements Initializable {
  @FXML
  private Button BtnAdd;

  @FXML
  private Button BtnClear;

  @FXML
  private Button BtnDelete;

  @FXML
  private Button BtnUpdate;

  @FXML
  private Label LblErrorDesc,LblErrorProfesseur;

  @FXML
  private Label LblErrorHeureDebut;

  @FXML
  private Label LblErrorHeureFin;

  @FXML
  private Label LblErrorNom;

  @FXML
  private Label LblErrorSalle;

  @FXML
  private ComboBox<Salle> CmbSalle;
  @FXML
  private ComboBox<User> CmbProfesseur;

  @FXML
  private Spinner<Integer> SpnHeureDebut;

  @FXML
  private Spinner<Integer> SpnHeureFin;

  @FXML
  private TextArea TxtDesc;

  @FXML
  private TextField TxtNom, TxtId;

  @FXML
  private TableView<Cours> TableCours;

  @FXML
  private TableColumn<?, ?> TbHeureDebut;

  @FXML
  private TableColumn<?, ?> TbHeureFin;

  @FXML
  private TableColumn<?, ?> TbId;

  @FXML
  private TableColumn<?, ?> TbNom;

  @FXML
  private TableColumn<?, ?> TbProfesseur;

  @FXML
  private TableColumn<?, ?> TbSalle;

  private final CoursImpl coursIml = new CoursImpl();
  private final SalleImpl salleIml = new SalleImpl();
  private final UserImpl userIml = new UserImpl();
  private Validator validator;
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    LoadSpinnerHeure(SpnHeureDebut);
    LoadSpinnerHeure(SpnHeureFin);
    Loadsalle();
    LoadProfesseur();
    LoadTable();

  }
  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  private void ValidateEmargement(Cours cours){
    clearErrorMessages();
    try {
      Set<ConstraintViolation<Cours>> violations = validator.validate(cours);
      if (!violations.isEmpty()) {
        StringBuilder errorMessage = new StringBuilder("Erreur(s) de validation :\n");
        for (ConstraintViolation<Cours> violation : violations) {
          errorMessage.append("- ").append(violation.getMessage()).append("\n");
          showErrorMessage(violation.getPropertyPath().toString(), violation.getMessage());
        }
        throw new RuntimeException(errorMessage.toString());
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private void showErrorMessage(String field, String message) {
    switch (field) {
      case "nom":
        LblErrorNom.setText(message);
        break;
      case "description":
        LblErrorDesc.setText(message);
        break;
      case "heureDebut":
        LblErrorHeureDebut.setText(message);
        break;
      case "heureFin":
        LblErrorHeureFin.setText(message);
        break;
      case "salle":
        LblErrorSalle.setText(message);
        break;
      case "professeur":
        LblErrorProfesseur.setText(message);
        break;
    }
  }

  private boolean checkForScheduleConflict(Cours newCours, User professeur) {
    // Vérifier les conflits d'horaires pour le professeur
    List<Cours> existingProfesseurCourses = coursIml.getAllByProfesseur(professeur);

    for (Cours existingCours : existingProfesseurCourses) {
      if (existingCours.getId() == newCours.getId()) {
        continue; // Ignorer le cours en cours de modification
      }
      boolean professorConflict = newCours.getHeureDebut().isBefore(existingCours.getHeureFin()) &&
              newCours.getHeureFin().isAfter(existingCours.getHeureDebut());

      if (professorConflict) {
        showAlert("Erreur", "Le professeur a déjà un cours à cet horaire.", Alert.AlertType.ERROR);
        return true;  // Conflit trouvé pour le professeur
      }
    }

    // Vérifier les conflits d'horaires pour la salle
    List<Cours> existingSalleCourses = coursIml.getAllBySalle(newCours.getSalle());

    for (Cours existingCours : existingSalleCourses) {
      if (existingCours.getId() == newCours.getId()) {
        continue; // Ignorer le cours en cours de modification
      }
      boolean salleConflict = newCours.getHeureDebut().isBefore(existingCours.getHeureFin()) &&
              newCours.getHeureFin().isAfter(existingCours.getHeureDebut());

      if (salleConflict) {
        showAlert("Erreur", "Il y a un conflit d'horaire dans la salle.", Alert.AlertType.ERROR);
        return true;  // Conflit trouvé pour la salle
      }
    }

    return false;  // Aucun conflit trouvé
  }

  private void LoadSpinnerHeure(Spinner<Integer> spinner) {
    SpinnerValueFactory<Integer> valueFactoryHeureDebut =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(6, 22, 8);
    spinner.setValueFactory(valueFactoryHeureDebut);
  }
  private void Loadsalle(){
    ObservableList<Salle> salles = FXCollections.observableArrayList(salleIml.getAll());
    CmbSalle.setItems(salles);
  }
  private void LoadProfesseur(){
    ObservableList<User> professeurs = FXCollections.observableArrayList(userIml.getAllProfessors());
    CmbProfesseur.setItems(professeurs);
  }
  private void LoadTable(){
    TableCours.getItems().clear();
    TbId.setCellValueFactory(new PropertyValueFactory<>("id"));
    TbNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
    TbHeureDebut.setCellValueFactory(new PropertyValueFactory<>("heureDebut"));
    TbHeureFin.setCellValueFactory(new PropertyValueFactory<>("heureFin"));
    TbSalle.setCellValueFactory(new PropertyValueFactory<>("salle"));
    TbProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
    TableCours.setItems(coursIml.getAll());
    ClearForm();
  }
//  private void LoadSpinnerMinutes() {
//    SpinnerValueFactory<Integer> valueFactoryMinutes =
//            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 30, 0, 30); // Pas de 30 minutes
//    SpnMinutes.setValueFactory(valueFactoryMinutes);
//  }
  private void ClearForm(){
    TxtId.clear();
    TxtDesc.clear();
    TxtNom.clear();
    CmbSalle.getSelectionModel().clearSelection();
    CmbProfesseur.getSelectionModel().clearSelection();
    SpnHeureDebut.getValueFactory().setValue(0);
    SpnHeureFin.getValueFactory().setValue(0);
    clearErrorMessages();
  }
  private void clearErrorMessages() {
    LblErrorSalle.setText("");
    LblErrorProfesseur.setText("");
    LblErrorNom.setText("");
    LblErrorDesc.setText("");
    LblErrorHeureDebut.setText("");
    LblErrorHeureFin.setText("");
  }

  @FXML
  void Add(ActionEvent event) {
    // Créer un nouvel objet Cours à partir des données de l'interface utilisateur
    Cours newCours = Cours.builder()
            .nom(TxtNom.getText())
            .description(TxtDesc.getText())
            .heureDebut(LocalTime.of(SpnHeureDebut.getValue(), 0))
            .heureFin(LocalTime.of(SpnHeureFin.getValue(), 0))
            .salle(CmbSalle.getValue())
            .professeur(CmbProfesseur.getValue())
            .build();
    try {
      ValidateEmargement(newCours); // Valider les informations du cours
    } catch (RuntimeException e) {
      // Si la validation échoue, ne pas ajouter le cours
      showAlert("Erreur de validation", "Le cours n'a pas pu être ajouté : " + e.getMessage(), Alert.AlertType.ERROR);
      return; // Arrêter l'exécution
    }
    if (checkForScheduleConflict(newCours, newCours.getProfesseur())) {
      showAlert("Conflit d'horaire", "Il y a un conflit d'horaire avec un autre cours.", Alert.AlertType.ERROR);
      return;
    }
    // Ajouter le cours via le DAO (CoursImpl)
    try {
      coursIml.add(newCours); // Ajouter le cours dans la base de données
      showAlert("Succès", "Le cours a été ajouté avec succès.", Alert.AlertType.INFORMATION);
      LoadTable();
    } catch (Exception e) {
      showAlert("Erreur", "Une erreur est survenue lors de l'ajout du cours.", Alert.AlertType.ERROR);
    }
  }

  @FXML
  void Clear(ActionEvent event) {
    ClearForm();
  }

  @FXML
  void Delete(ActionEvent event) {
    Cours cours = TableCours.getSelectionModel().getSelectedItem();
    if (cours != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirmation de suppression");
      alert.setHeaderText("Supprimer ce cours");
      alert.setContentText("Voulez-vous vraiment supprimer cours ?");
      if (alert.showAndWait().get() == ButtonType.OK) {
        try {
          coursIml.delete(cours.getId());
          LoadTable();
        } catch (Exception e){
          e.printStackTrace();
          showAlert("Erreur", "Impossible de supprimer", Alert.AlertType.ERROR);
        }
      }
    } else {
      System.out.println("Aucun cours sélectionné pour la suppression");
      showAlert("Erreur", "Aucun Cours sélectionné pour la suppression", Alert.AlertType.ERROR);
    }
  }

  @FXML
  void Update(ActionEvent event) {
    if (TxtId.getText().isEmpty()) {
      showAlert("Erreur", "Veuillez sélectionner un cours à modifier.", Alert.AlertType.ERROR);
      return;
    }
    Cours newCours = Cours.builder()
            .id(Integer.valueOf(TxtId.getText()))
            .nom(TxtNom.getText())
            .description(TxtDesc.getText())
            .heureDebut(LocalTime.of(SpnHeureDebut.getValue(), 0))
            .heureFin(LocalTime.of(SpnHeureFin.getValue(), 0))
            .salle(CmbSalle.getValue())
            .professeur(CmbProfesseur.getValue())
            .build();
    try {
      ValidateEmargement(newCours); // Valider les informations du cours
    } catch (RuntimeException e) {
      // Si la validation échoue, ne pas ajouter le cours
      showAlert("Erreur de validation", "Le cours n'a pas pu être ajouté : " + e.getMessage(), Alert.AlertType.ERROR);
      return; // Arrêter l'exécution
    }
    if (checkForScheduleConflict(newCours, newCours.getProfesseur())) {
      showAlert("Conflit d'horaire", "Il y a un conflit d'horaire avec un autre cours.", Alert.AlertType.ERROR);
      return;
    }
    try {
      coursIml.update(newCours); // Ajouter le cours dans la base de données
      showAlert("Succès", "Le cours a été ajouté avec succès.", Alert.AlertType.INFORMATION);
      LoadTable();
    } catch (Exception e) {
      showAlert("Erreur", "Une erreur est survenue lors de l'ajout du cours.", Alert.AlertType.ERROR);
    }
  }

  @FXML
  void getData(MouseEvent event) {
    if (event.getClickCount() == 2) {
      Cours cours = TableCours.getSelectionModel().getSelectedItem();
      if (cours != null) {
        TxtId.setText(String.valueOf(cours.getId()));
        TxtNom.setText(cours.getNom());
        TxtDesc.setText(cours.getDescription());
        SpnHeureDebut.getValueFactory().setValue(cours.getHeureDebut().getHour());
        SpnHeureFin.getValueFactory().setValue(cours.getHeureFin().getHour());
        CmbProfesseur.getSelectionModel().select(cours.getProfesseur());
        CmbSalle.getSelectionModel().select(cours.getSalle());
      }
    }
  }
}
