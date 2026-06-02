package pt.ul.fc.di.css.javafxtascaeats.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.tascaeats.grpc.DeleteDPAccountResponse;
import pt.ul.fc.css.tascaeats.grpc.UpdateDeliveryPersonResponse;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentDeliveryPerson;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentUser;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class ProfileController implements ControllerWithModel {

  private static final String GENERIC_ERROR = "An error occurred, please try again later.";

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
  @FXML private Button updateButton;
  @FXML private ProgressIndicator loadingIndicator;
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
                  if (model.getCurrentUser() instanceof CurrentDeliveryPerson) {
                    ApiClient.deleteDPAccount(
                        model.getCurrentUser().getUsername(), buildDeleteDPObserver());
                  } else {
                    ApiClient.deleteAccount(model.getCurrentUser().getId());
                    model.setCurrentUser(null);
                    UtilController.switchScene(
                        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
                  }
                } catch (Exception e) {
                  showError(e.getMessage());
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

    if (name.isEmpty() || phone.isEmpty() || nif.isEmpty()) {
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

    if (user instanceof CurrentDeliveryPerson) {
      String status = statusComboBox.getValue();
      ApiClient.updateDeliveryPerson(
          username,
          passwordToSend,
          name,
          phone,
          nif,
          status,
          buildUpdateObserver(username, name, phone, nif, status));
    } else {
      try {
        if (user instanceof CurrentClient) {
          ApiClient.updateClient(user.getId(), username, passwordToSend, name, phone, nif);
        } else {
          ApiClient.updateAdmin(user.getId(), username, passwordToSend, name, phone, nif);
        }
      } catch (Exception e) {
        showError(e.getMessage());
        return;
      }
      applyUpdate(user, username, name, phone, nif);
      showSuccess();
    }
  }

  private StreamObserver<UpdateDeliveryPersonResponse> buildUpdateObserver(
      String username, String name, String phone, String nif, String status) {
    return new StreamObserver<>() {
      @Override
      public void onNext(UpdateDeliveryPersonResponse response) {
        if (response.getPlayloadCase() == UpdateDeliveryPersonResponse.PlayloadCase.ACKNOWLEDGED) {
          Platform.runLater(() -> showLoading());
        } else if (response.getPlayloadCase() == UpdateDeliveryPersonResponse.PlayloadCase.PERSON) {
          Platform.runLater(
              () -> {
                hideLoading();
                CurrentUser user = model.getCurrentUser();
                applyUpdate(user, username, name, phone, nif);
                ((CurrentDeliveryPerson) user).setStatus(status);
                showSuccess();
              });
        }
      }

      @Override
      public void onError(Throwable t) {
        Platform.runLater(
            () -> {
              hideLoading();
              if (t instanceof StatusRuntimeException e) {
                Status.Code code = e.getStatus().getCode();
                switch (code) {
                  case ALREADY_EXISTS -> showError("This username is already taken.");
                  case NOT_FOUND -> showError("User not found.");
                  case PERMISSION_DENIED -> showError(e.getStatus().getDescription());
                  default -> showError(GENERIC_ERROR);
                }
              } else {
                showError(GENERIC_ERROR);
              }
            });
      }

      @Override
      public void onCompleted() {}
    };
  }

  private StreamObserver<DeleteDPAccountResponse> buildDeleteDPObserver() {
    return new StreamObserver<>() {
      @Override
      public void onNext(DeleteDPAccountResponse response) {
        if (response.getPayloadCase() == DeleteDPAccountResponse.PayloadCase.ACKNOWLEDGED) {
          Platform.runLater(() -> showLoading());
        } else if (response.getPayloadCase() == DeleteDPAccountResponse.PayloadCase.DELETED) {
          Platform.runLater(
              () -> {
                hideLoading();
                model.setCurrentUser(null);
                UtilController.switchScene(
                    stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
              });
        }
      }

      @Override
      public void onError(Throwable t) {
        Platform.runLater(
            () -> {
              hideLoading();
              if (t instanceof StatusRuntimeException e) {
                Status.Code code = e.getStatus().getCode();
                if (code == Status.Code.NOT_FOUND) {
                  showError("Account not found.");
                } else if (code == Status.Code.PERMISSION_DENIED) {
                  showError(e.getStatus().getDescription());
                } else {
                  showError("An error occurred while deleting the account. Please try again.");
                }
              } else {
                showError("An error occurred while deleting the account. Please try again.");
              }
            });
      }

      @Override
      public void onCompleted() {}
    };
  }

  private void applyUpdate(
      CurrentUser user, String username, String name, String phone, String nif) {
    user.setUsername(username);
    user.setName(name);
    user.setPhoneNumber(phone);
    user.setNif(nif);
    hideError();
  }

  private void showSuccess() {
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

  private void showLoading() {
    loadingIndicator.setVisible(true);
    loadingIndicator.setManaged(true);
    updateButton.setDisable(true);
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
  }

  private void hideLoading() {
    loadingIndicator.setVisible(false);
    loadingIndicator.setManaged(false);
    updateButton.setDisable(false);
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
