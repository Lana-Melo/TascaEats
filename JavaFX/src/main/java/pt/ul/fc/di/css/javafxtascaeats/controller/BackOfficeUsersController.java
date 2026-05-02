package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class BackOfficeUsersController implements ControllerWithModel {

  private DataModel
      model; // objeto que agrega todos os daddos que eu eventualmente precise usar ou setar
  private Stage stage; // representa a janela atual, é necessário para mudar de cena

  @FXML private CheckBox cbUsername;
  @FXML private TextField usernameField;
  @FXML private CheckBox cbUserType;
  @FXML private ComboBox<String> userTypeCombo;
  @FXML private CheckBox cbUserOrders;
  @FXML private TextField userOrdersField;
  @FXML private CheckBox cbUserDeliveries;
  @FXML private TextField userDeliveriesField;

  @FXML private Label errorLabel;

  @FXML private TableView<UserResponse> usersTable;
  @FXML private TableColumn<UserResponse, String> colName;
  @FXML private TableColumn<UserResponse, String> colUsername;
  @FXML private TableColumn<UserResponse, String> colRole;
  @FXML private TableColumn<UserResponse, String> colNif;
  @FXML private TableColumn<UserResponse, String> colPhone;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    bindFilter(cbUsername, usernameField);
    bindFilter(cbUserOrders, userOrdersField);
    bindFilter(cbUserDeliveries, userDeliveriesField);

    userTypeCombo.setItems(FXCollections.observableArrayList("Client", "Delivery Person", "Admin"));
    cbUserType
        .selectedProperty()
        .addListener(
            (obs, wasSelected, isSelected) -> {
              userTypeCombo.setVisible(isSelected);
              userTypeCombo.setManaged(isSelected);
              if (!isSelected) userTypeCombo.setValue(null);
            });

    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
    colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
    colNif.setCellValueFactory(new PropertyValueFactory<>("nif"));
    colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
  }

  private void bindFilter(CheckBox cb, TextField field) {
    cb.selectedProperty()
        .addListener(
            (obs, wasSelected, isSelected) -> {
              field.setVisible(isSelected);
              field.setManaged(isSelected);
              if (!isSelected) field.clear();
            });
  }

  @FXML
  void handleSearch(ActionEvent event) {
    String name = cbUsername.isSelected() ? usernameField.getText() : null;
    String userType = cbUserType.isSelected() ? userTypeCombo.getValue() : null;
    String orderNumber = cbUserOrders.isSelected() ? userOrdersField.getText() : null;
    String deliveryNumber = cbUserDeliveries.isSelected() ? userDeliveriesField.getText() : null;

    try {
      List<UserResponse> users = ApiClient.searchUsers(name, userType, orderNumber, deliveryNumber);
      usersTable.setItems(FXCollections.observableArrayList(users));
      usersTable.setVisible(true);
      usersTable.setManaged(true);
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    } catch (Exception e) {
      errorLabel.setText("An error occurred, please try again later.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    }
  }

  @FXML
  void handleGoBackOffice(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice.fxml", "Back Office", model);
  }

  @FXML
  void handleGoLogin(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @Override
  public Stage getStage() {
    return this.stage;
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
