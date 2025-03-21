package sn.school.examenfx.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import sn.school.examenfx.dao.SalleImpl;
import sn.school.examenfx.entities.Salle;

import java.net.URL;
import java.util.ResourceBundle;

public class SalleController implements Initializable {

  @FXML
  private Button BtnAdd;

  @FXML
  private Button BtnClear;

  @FXML
  private Button BtnUpdate;

  @FXML
  private Button BtnDelete;

  @FXML
  private TableView<Salle> TableSalle;

  @FXML
  private TableColumn<?, ?> TbId;

  @FXML
  private TableColumn<?, ?> TbLibelle;

  @FXML
  private TextField TxtId;

  @FXML
  private TextField TxtLibelle;

  @FXML
  private Label LbtErrorLibelle;

  private SalleImpl salleImpl = new SalleImpl();

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    LoadTable();
  }

  private void LoadTable() {
    TableSalle.getItems().clear();
    TbId.setCellValueFactory(new PropertyValueFactory<>("Id"));
    TbLibelle.setCellValueFactory(new PropertyValueFactory<>("Libelle"));
    TableSalle.setItems(salleImpl.getAll());
    clearForm();
  }

  @FXML
  void Add(ActionEvent event) {
    clearErrorMessages();
    String libelle = TxtLibelle.getText();

    // Création de l'objet Salle avec les données de l'utilisateur
    Salle salle = Salle.builder().libelle(libelle).build();

    try {
      // Vérification si le libellé est déjà utilisé
      salleImpl.add(salle);  // Si ce libellé est déjà utilisé, une exception sera lancée
      LoadTable();
      showAlert("Succès", "Salle ajoutée avec succès !", Alert.AlertType.INFORMATION);
    } catch (RuntimeException e) {
      LbtErrorLibelle.setText(e.getMessage());  // Afficher l'erreur dans le label
    }
  }

  @FXML
  void Clear(ActionEvent event) {
    clearForm();
    clearErrorMessages();
  }

  @FXML
  void Update(ActionEvent event) {
    clearErrorMessages();
    String idText = TxtId.getText();
    String libelle = TxtLibelle.getText();

    Salle salle = Salle.builder()
            .id(Integer.parseInt(idText))
            .libelle(libelle)
            .build();
    try {
      // Vérification si le libellé est déjà utilisé
      salleImpl.update(salle);  // Si ce libellé est déjà utilisé, une exception sera lancée
      LoadTable();
      showAlert("Succès", "Salle mise à jour avec succès !", Alert.AlertType.INFORMATION);
    } catch (RuntimeException e) {
      LbtErrorLibelle.setText(e.getMessage());  // Afficher l'erreur dans le label
    }
  }
  private void clearErrorMessages() {
    LbtErrorLibelle.setText("");  // Réinitialiser le message d'erreur
  }

  private void clearForm() {
    TxtId.clear();
    TxtLibelle.clear();
    clearErrorMessages();
  }
  private void showAlert(String title, String message, Alert.AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }
  @FXML
  void Delete(ActionEvent event) {
    Salle salle = TableSalle.getSelectionModel().getSelectedItem();
    if (salle != null) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Confirmation de suppression");
      alert.setHeaderText("Supprimer cette salle");
      alert.setContentText("Voulez-vous vraiment supprimer cette salle ?");
      if (alert.showAndWait().get() == ButtonType.OK) {
        try {
          salleImpl.delete(salle.getId());
          LoadTable();
        } catch (Exception e){
          e.printStackTrace();
          showAlert("Erreur", "Impossible de supprimer la salle", Alert.AlertType.ERROR);
        }
      }
    } else {
      System.out.println("Aucune sale sélectionné pour la suppression");
      showAlert("Erreur", "Aucune salle sélectionné pour la suppression", Alert.AlertType.ERROR);
    }
  }

  @FXML
  void getData(MouseEvent event) {
    if(event.getClickCount() == 2){
      Salle salle = TableSalle.getSelectionModel().getSelectedItem();
      if(salle != null){
        TxtId.setText(salle.getId().toString());
        TxtLibelle.setText(salle.getLibelle());
      }else {
        System.out.println("Aucune salle sélectionnée");
      }
    }
  }

}
