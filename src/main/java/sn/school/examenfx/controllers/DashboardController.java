package sn.school.examenfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sn.school.examenfx.App;
import sn.school.examenfx.dao.SessionManager;
import sn.school.examenfx.dao.UserImpl;
import sn.school.examenfx.entities.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

//  User CurrentUser = SessionManager.getInstance().getCurrentUser();
  User CurrentUser ;
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    UserImpl userImp = new UserImpl();
    if(SessionManager.getInstance().getCurrentUser() != null){
      CurrentUser = SessionManager.getInstance().getCurrentUser();
      txtUsername.setText(CurrentUser.getNom() + " " + CurrentUser.getPrenom());
      txtRole.setText(String.valueOf(CurrentUser.getRole()));
    }
  }

  @FXML
  private Button BtnCours;

  @FXML
  private Button BtnEmargement;

  @FXML
  private Button BtnRapport;

  @FXML
  private Button BtnSalle;

  @FXML
  private Button BtnUser;

  @FXML
  private Button BtnLogout;

  @FXML
  private Label txtRole;

  @FXML
  private Label txtUsername;

  @FXML
  private AnchorPane pnDestop;
  // Méthode générique pour charger les pages
  public void loadPage(String pageFXML) throws IOException {
    try {
    URL url = getClass().getResource(pageFXML);
    System.out.println("Chargement du fichier FXML : " + url); // Debug

    if (url == null) {
      throw new IOException("Fichier FXML introuvable : " + pageFXML);
    }
    FXMLLoader loader = new FXMLLoader(url);
    AnchorPane page = loader.load(); // Charger la page FXML
    page.prefWidthProperty().bind(pnDestop.widthProperty());
    page.prefHeightProperty().bind(pnDestop.heightProperty());
    pnDestop.getChildren().setAll(page); // Remplacer le contenu de pnDestop

    } catch (Exception e){
      System.err.println("⚠️ Erreur lors du chargement de la page : " + pageFXML);
      e.printStackTrace();
    }
  }
  private static final String DEFAULT_BUTTON_COLOR = "button-color: var(--subnav-background-color);";
  private static final String ACTIVE_BUTTON_COLOR = "-fx-background-color: #f97316; -fx-text-fill: white;";

  private void DesActiveBtn() {
    BtnEmargement.setStyle(DEFAULT_BUTTON_COLOR);
    BtnRapport.setStyle(DEFAULT_BUTTON_COLOR);
    BtnSalle.setStyle(DEFAULT_BUTTON_COLOR);
    BtnUser.setStyle(DEFAULT_BUTTON_COLOR);
    BtnCours.setStyle(DEFAULT_BUTTON_COLOR);
  }

  private void ActiveBtn(Button btn) {
    btn.setStyle(ACTIVE_BUTTON_COLOR);
  }



  @FXML
  void PgCours(MouseEvent event) throws IOException {
    DesActiveBtn();
    loadPage("/pages/Cours.fxml");
    ActiveBtn(BtnCours);
  }

  @FXML
  void PgEmargement(MouseEvent event) throws IOException {
    DesActiveBtn();
    loadPage("/pages/Emargement.fxml");
    ActiveBtn(BtnEmargement);
  }

  @FXML
  void PgRapport(MouseEvent event) throws IOException {
    DesActiveBtn();
    loadPage("/pages/Rapport.fxml");
    ActiveBtn(BtnRapport);
  }

  @FXML
  void PgSalle(MouseEvent event) throws IOException {
    DesActiveBtn();
    loadPage("/pages/Salle.fxml");
    ActiveBtn(BtnSalle);
  }

  @FXML
  void PgUSER(MouseEvent event){
    try {
      DesActiveBtn();
      loadPage("/pages/User.fxml");
      ActiveBtn(BtnUser);
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  @FXML
  void Logout(ActionEvent event) {
    SessionManager.getInstance().logout();
    App app = new App();
    try {
      BtnLogout.getScene().getWindow().hide();
      app.start(new Stage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
