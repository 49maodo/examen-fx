package sn.school.examenfx.controllers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.school.examenfx.dao.EmargementIml;
import sn.school.examenfx.dao.UserImpl;
import sn.school.examenfx.entities.Emargement;
import sn.school.examenfx.entities.Statut;
import sn.school.examenfx.entities.User;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class EmargementController implements Initializable {
  @FXML
  private Button BtnAdd;

  @FXML
  private Button BtnClear;

  @FXML
  private Button BtnUpdate;

  @FXML
  private ComboBox<?> CmbCours;

  @FXML
  private ComboBox<User> CmbProfesseur;

  @FXML
  private ComboBox<Statut> CmbStatut;

  @FXML
  private Label LblErrorCours;

  @FXML
  private Label LblErrorDate;

  @FXML
  private Label LblErrorProfesseur;

  @FXML
  private Label LblErrorStatus;

  @FXML
  private TableView<Emargement> TableEmargement;

  @FXML
  private TableColumn<?, ?> TbCours;

  @FXML
  private TableColumn<?, ?> TbDate;

  @FXML
  private TableColumn<?, ?> TbID;

  @FXML
  private TableColumn<?, ?> TbStatut;

  @FXML
  private TableColumn<?, ?> TbProfesseur;

  @FXML
  private DatePicker TxtDate;

  @FXML
  private TextField TxtId;

  EmargementIml emargementIml = new EmargementIml();
  UserImpl userImpl = new UserImpl();
  private Validator validator;
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    LoadProfesseur();
    LoadStatus();
    LoadTable();
  }
  private void LoadTable(){
    TableEmargement.getItems().clear();
    ObservableList<Emargement> emargementList = FXCollections.observableArrayList(emargementIml.getAll());

    // Définir les valeurs des colonnes
    TbID.setCellValueFactory(new PropertyValueFactory<>("id"));
    TbDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    TbStatut.setCellValueFactory(new PropertyValueFactory<>("statut"));
    TbProfesseur.setCellValueFactory(new PropertyValueFactory<>("professeur"));
    TbCours.setCellValueFactory(new PropertyValueFactory<>("cours"));

    // Ajouter les données au TableView
    TableEmargement.setItems(emargementList);
  }
  private void LoadStatus(){
    ObservableList<Statut> statuts = FXCollections.observableArrayList(Statut.values());
    CmbStatut.setItems(statuts);
  }
  private void LoadProfesseur(){
    ObservableList<User> professeurs = FXCollections.observableArrayList(userImpl.getAllProfessors());
    CmbProfesseur.setItems(professeurs);
  }
  private void LoadCours(){

  }

  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  private void ValidateEmargement(Emargement emg){
    try {
      Set<ConstraintViolation<Emargement>> violations = validator.validate(emg);
      if (!violations.isEmpty()) {
        StringBuilder errorMessage = new StringBuilder("Erreur(s) de validation :\n");
        for (ConstraintViolation<Emargement> violation : violations) {
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
      case "date":
        LblErrorDate.setText(message);
        break;
      case "statut":
        LblErrorStatus.setText(message);
        break;
      case "professeur":
        LblErrorProfesseur.setText(message);
        break;
      case "cours":
        LblErrorCours.setText(message);
        break;
    }
  }

  @FXML
  void Add(ActionEvent event) {

  }

  @FXML
  void Clear(ActionEvent event) {

  }

  @FXML
  void Update(ActionEvent event) {

  }

  @FXML
  void getData(MouseEvent event) {

  }
}
