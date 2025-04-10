package sn.school.examenfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sn.school.examenfx.App;
import sn.school.examenfx.dao.SessionManager;
import sn.school.examenfx.dao.UserImpl;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
  @FXML
  private Button BtnLogin;

  @FXML
  private CheckBox ViewPassword;

  @FXML
  private TextField txtEmail;

  @FXML
  private PasswordField txtPassword;

  @FXML
  private Label txtPasswordVisible;

  private UserImpl userImpl ;


  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    userImpl = new UserImpl();
  }

  @FXML
  void Login(ActionEvent event) {
      BtnLogin.setDisable(true);
      BtnLogin.setText("Connexion en cours...");
    try {
      String email = txtEmail.getText();
      String password = txtPassword.getText();

      if (email.isEmpty() || password.isEmpty()) {
          throw new RuntimeException("Veuillez remplir tous les champs");
      }

      userImpl.login(email, password);
      // Rediriger vers la page d'accueil
      BtnLogin.getScene().getWindow().hide();
//      run App
      App app = new App();
      app.start(new Stage());



      } catch (RuntimeException e) {
        showAlert("Erreur", e.getMessage(), Alert.AlertType.ERROR);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
          BtnLogin.setDisable(false);
          BtnLogin.setText("Se connecter");
      }
  }

  @FXML
  void onViewPassword(ActionEvent event) {
    if (ViewPassword.isSelected()) {
      txtPasswordVisible.setText(txtPassword.getText());
      txtPasswordVisible.setVisible(true);
    } else {
      txtPasswordVisible.setText("");
      txtPasswordVisible.setVisible(false);
    }
  }

  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }



}
