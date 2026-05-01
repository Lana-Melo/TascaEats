package pt.ul.fc.di.css.javafxtascaeats.controller;

import io.grpc.StatusRuntimeException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import pt.ul.fc.css.tascaeats.grpc.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentAdmin;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentDeliveryPerson;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentUser;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class RegisterController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private RadioButton adminRadio;
  @FXML private RadioButton deliveryRadio;
  @FXML private ToggleGroup roleGroup;
  @FXML private TextField usernameField;
  @FXML private PasswordField passwordField;
  @FXML private TextField nameField;
  @FXML private TextField phoneNumberField;
  @FXML private TextField nifField;
  @FXML private Label errorLabel;

  @FXML
  void handleRegister(ActionEvent event) {

    String role = adminRadio.isSelected() ? "ADMIN" : "DELIVERY_PERSON";
    String username = usernameField.getText();
    String password = passwordField.getText();
    String name = nameField.getText();
    String phoneNumber = phoneNumberField.getText();
    String nif = nifField.getText();

    if (username.isBlank()
        || password.isBlank()
        || name.isBlank()
        || phoneNumber.isBlank()
        || nif.isBlank()) {
      errorLabel.setText("Please fill in all fields.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      return;
    }
    try {
      if ("ADMIN".equals(role)) {
        handleAdminRegister(username, password, name, phoneNumber, nif);
      } else {
        handleDeliveryPersonRegister(username, password, name, phoneNumber, nif);
      }
    } catch (IllegalArgumentException e) {
      errorLabel.setText("This username is already taken.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);

    } catch (StatusRuntimeException e) {
      errorLabel.setText("An error occurred while registering. Please try again.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    } catch (Exception e) {
      // outros erros, como problemas de conexão
      errorLabel.setText("An error occured, please try again later.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    }
  }

  private void handleAdminRegister(
      String username, String password, String name, String phoneNumber, String nif) {
    UserResponse response = ApiClient.registerAdmin(username, password, name, phoneNumber, nif);

    CurrentUser currentUser = new CurrentAdmin();
    currentUser.setId(response.getId());
    currentUser.setName(response.getName());
    currentUser.setPhoneNumber(response.getPhoneNumber());
    currentUser.setNif(response.getNif());
    currentUser.setUsername(response.getUsername());

    model.setCurrentUser(currentUser);

    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  private void handleDeliveryPersonRegister(
      String username, String password, String name, String phoneNumber, String nif) {

    DeliveryPersonResponse response =
        ApiClient.registerDeliveryPerson(username, password, name, phoneNumber, nif);

    CurrentUser currentUser = new CurrentDeliveryPerson();
    currentUser.setId(response.getId());
    currentUser.setName(response.getName());
    currentUser.setPhoneNumber(response.getPhoneNumber());
    currentUser.setNif(response.getNif());
    currentUser.setUsername(response.getUsername());
    model.setCurrentUser(currentUser);

    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  @FXML
  void handleBackToLogin(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.model = model;
    this.stage = stage;
  }

  @Override
  public Stage getStage() {
    return stage;
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
