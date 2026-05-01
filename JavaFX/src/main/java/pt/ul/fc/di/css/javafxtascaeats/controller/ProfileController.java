package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentDeliveryPerson;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentUser;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class ProfileController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private TextField usernameField;
  @FXML private PasswordField currentPasswordField;
  @FXML private PasswordField newPasswordField;
  @FXML private TextField nameField;
  @FXML private TextField phoneNumberField;
  @FXML private TextField nifField;
  @FXML private Label statusLabel;
  @FXML private ComboBox<String> statusComboBox;
  @FXML private Button myAddressesButton;
  @FXML private Label errorLabel;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;

    CurrentUser user = model.getCurrentUser();
    usernameField.setText(user.getUsername());
    nameField.setText(user.getName());
    phoneNumberField.setText(user.getPhoneNumber());
    nifField.setText(user.getNif());

    if (user instanceof CurrentClient) {
      myAddressesButton.setVisible(true);
      myAddressesButton.setManaged(true);
    } else if (user instanceof CurrentDeliveryPerson dp) {
      statusLabel.setVisible(true);
      statusLabel.setManaged(true);
      statusComboBox.setVisible(true);
      statusComboBox.setManaged(true);
      statusComboBox.setItems(FXCollections.observableArrayList("AVAILABLE", "BUSY"));
      statusComboBox.setValue(dp.getStatus() != null ? dp.getStatus() : "AVAILABLE");
    }
  }

  @FXML
  void handleGoHome(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  @FXML
  void handleLogout(ActionEvent event) {
    model.setCurrentUser(null);
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @FXML
  void handleMyAddresses(ActionEvent event) {
    System.out.println("My addresses clicked");
  }

  @FXML
  void handleDeleteAccount(ActionEvent event) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Delete Account");
    confirm.setHeaderText("Are you sure you want to delete your account?");
    confirm.setContentText("This action cannot be undone.");

    confirm
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                try {
                  ApiClient.deleteAccount(model.getCurrentUser().getId());
                  model.setCurrentUser(null);
                  UtilController.switchScene(
                      stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
                } catch (Exception e) {
                  showError("An error occurred, please try again later.");
                }
              }
            });
  }

  @FXML
  void handleUpdate(ActionEvent event) {
    String username = usernameField.getText().trim();
    String currentPassword = currentPasswordField.getText().trim();
    String newPassword = newPasswordField.getText().trim();
    String name = nameField.getText().trim();
    String phone = phoneNumberField.getText().trim();
    String nif = nifField.getText().trim();

    if (username.isEmpty() || name.isEmpty() || phone.isEmpty() || nif.isEmpty()) {
      showError("All fields are required.");
      return;
    }

    boolean changingPassword = !currentPassword.isEmpty() || !newPassword.isEmpty();
    if (changingPassword && (currentPassword.isEmpty() || newPassword.isEmpty())) {
      showError("Fill both current and new password to change it.");
      return;
    }

    CurrentUser user = model.getCurrentUser();
    if (user == null) return;
    String passwordToSend = changingPassword ? newPassword : "";

    try {
      if (user instanceof CurrentDeliveryPerson dp) { // pegar o status caso currUser seja delivery
        String status = statusComboBox.getValue();
        ApiClient.updateDeliveryPerson(
            user.getId(), username, passwordToSend, name, phone, nif, status);
        dp.setStatus(status);
      } else if (user instanceof CurrentClient) {
        ApiClient.updateClient(user.getId(), username, passwordToSend, name, phone, nif);
      } else {
        ApiClient.updateAdmin(user.getId(), username, passwordToSend, name, phone, nif);
      }
    } catch (Exception e) {
      showError("An error occurred, please try again later.");
      return;
    }

    // atualiza model que vai ser passado para outros controllers
    user.setUsername(username);
    user.setName(name);
    user.setPhoneNumber(phone);
    user.setNif(nif);
    hideError();
    Alert success = new Alert(Alert.AlertType.INFORMATION);
    success.setTitle("Profile Updated");
    success.setHeaderText(null);
    success.setContentText("Profile updated!");
    success.showAndWait();
  }

  @Override
  public Stage getStage() {
    return this.stage;
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  private void hideError() {
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }
}
