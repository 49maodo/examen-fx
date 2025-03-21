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
import sn.school.examenfx.dao.UserImpl;
import sn.school.examenfx.entities.Role;
import sn.school.examenfx.entities.User;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class UserController implements Initializable {
  @FXML
  private Button BtnAdd;

  @FXML
  private Button BtnClear;

  @FXML
  private Button BtnDelete;

  @FXML
  private Button BtnUpdate;

  @FXML
  private ComboBox<Role> CmbRole;

  @FXML
  private TableColumn<?, ?> TbEmail;

  @FXML
  private TableColumn<?, ?> TbId;

  @FXML
  private TableColumn<?, ?> TbNom;

  @FXML
  private TableColumn<?, ?> TbPrenom;

  @FXML
  private TableColumn<?, ?> TbRole;

  @FXML
  private TableView<User> TableUser;

  @FXML
  private TextField TxtEmail;

  @FXML
  private TextField TxtId;

  @FXML
  private TextField TxtNom;

  @FXML
  private TextField TxtPassword;

  @FXML
  private TextField TxtPrenom;

  @FXML
  private Label LblErrorEmail;

  @FXML
  private Label LblErrorNom;

  @FXML
  private Label LblErrorPassword;

  @FXML
  private Label LblErrorPrenom;

  @FXML
  private Label LblErrorRole;

  private Validator validator;
  UserImpl userImp = new UserImpl();
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    LoadRole();
    LoadTable();
    updateButtonLabel();
  }

  private void LoadRole(){
    ObservableList<Role> roles = FXCollections.observableArrayList(Role.values());
    CmbRole.setItems(roles);
  }
  private void LoadTable(){
    TableUser.getItems().clear();
    TbId.setCellValueFactory(new PropertyValueFactory<>("id"));
    TbNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
    TbPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    TbRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    TbEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
    TableUser.setItems(userImp.getAll());
    ClearForm();
  }
  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  private void updateButtonLabel() {
//    if (TxtId.getText().isEmpty()) {
//      BtnAdd.setText("Ajouter");
//    } else {
//      BtnAdd.setText("Modifier");
//    }
  }


  private void ClearForm(){
    // Réinitialiser les champs du formulaire
    TxtEmail.clear();
    TxtId.clear();
    TxtNom.clear();
    TxtPrenom.clear();
    TxtPassword.clear();
    CmbRole.getSelectionModel().clearSelection();
    updateButtonLabel();
  }

  private void ValidateUser(User user){
    try {
      Set<ConstraintViolation<User>> violations = validator.validate(user);
      if (!violations.isEmpty()) {
        StringBuilder errorMessage = new StringBuilder("Erreur(s) de validation :\n");
        for (ConstraintViolation<User> violation : violations) {
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

  @FXML
  private void Add(ActionEvent event) {
    clearErrorMessages();
    String idText = TxtId.getText();
    String email = TxtEmail.getText();
    String nom = TxtNom.getText();
    String prenom = TxtPrenom.getText();
    String password = TxtPassword.getText();
    Role role = CmbRole.getValue();

    User user = User.builder()
            .email(email)
            .nom(nom)
            .prenom(prenom)
            .password(password)
            .role(role)
            .build();
    try {
      ValidateUser(user);
      userImp.add(user);
      LoadTable();
      showAlert("Succès", "Utilisateur ajouté !", Alert.AlertType.INFORMATION);
    } catch (RuntimeException e) {
      showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
    }
  }

  private void showErrorMessage(String field, String message) {
    switch (field) {
      case "nom":
        LblErrorNom.setText(message);
        break;
      case "prenom":
        LblErrorPrenom.setText(message);
        break;
      case "email":
        LblErrorEmail.setText(message);
        break;
      case "password":
        LblErrorPassword.setText(message);
        break;
      case "role":
        LblErrorRole.setText(message);
        break;
    }
  }

  private void clearErrorMessages() {
    LblErrorNom.setText("");
    LblErrorPrenom.setText("");
    LblErrorEmail.setText("");
    LblErrorPassword.setText("");
    LblErrorRole.setText("");
  }

  @FXML
  void Clear(ActionEvent event) {
    ClearForm();
  }

  @FXML
  void getData(MouseEvent event) {
    if (event.getClickCount() == 2) { // Vérifie si c'est un double-clic
      User selectedUser = TableUser.getSelectionModel().getSelectedItem(); // Récupère l'utilisateur sélectionné
      if (selectedUser != null) {
        TxtId.setText(String.valueOf(selectedUser.getId()));
        TxtNom.setText(selectedUser.getNom());
        TxtPrenom.setText(selectedUser.getPrenom());
        TxtEmail.setText(selectedUser.getEmail());
        TxtPassword.setText(""); // On ne récupère pas le mot de passe pour des raisons de sécurité
        CmbRole.setValue(selectedUser.getRole());
      updateButtonLabel();
      } else {
        System.out.println("Aucun utilisateur sélectionné");
      }
    }
  }

  @FXML
  void Delete(ActionEvent event) {
    User selectedUser = TableUser.getSelectionModel().getSelectedItem();

    if (selectedUser != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirmation de suppression");
      alert.setHeaderText("Supprimer l'utilisateur");
      alert.setContentText("Voulez-vous vraiment supprimer cet utilisateur ?");

      if (alert.showAndWait().get() == ButtonType.OK) {
        try {
          userImp.delete(selectedUser.getId()); // Appelle la méthode de suppression
          LoadTable(); // Recharge la TableView après suppression
        } catch (Exception e){
          e.printStackTrace();
          showAlert("Erreur", "Erreur impossible de supprimer", Alert.AlertType.ERROR);
        }
      }
    } else {
      System.out.println("Aucun utilisateur sélectionné pour la suppression");
      showAlert("Erreur", "Aucun utilisateur sélectionné pour la suppression", Alert.AlertType.ERROR);
    }
  }

  @FXML
  void Update(ActionEvent event) {
    clearErrorMessages();
    String idText = TxtId.getText();
    String email = TxtEmail.getText();
    String nom = TxtNom.getText();
    String prenom = TxtPrenom.getText();
    String password = TxtPassword.getText();
    Role role = CmbRole.getValue();
    try {
    User user = User.builder()
            .id(Integer.valueOf(idText))
            .email(email)
            .nom(nom)
            .prenom(prenom)
            .password(password)
            .role(role)
            .build();
      ValidateUser(user);
      userImp.update(user);
      LoadTable();
      showAlert("Succès", "Utilisateur modifié !", Alert.AlertType.INFORMATION);
    } catch (RuntimeException e) {
      showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
    }
  }

}
