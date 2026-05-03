package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class CreateRestaurantController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private TextField designationField;
  @FXML private TextField nifField;
  @FXML private TextField cityField;
  @FXML private TextField postalCodeField;
  @FXML private TextField streetField;
  @FXML private TextField openHoursField;
  @FXML private TextField closeHoursField;
  @FXML private Label errorLabel;
  @FXML private Button createButton;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;
  }

  @FXML
  private void handleCreate(ActionEvent event) {
    String designation = designationField.getText().trim();
    String nif = nifField.getText().trim();
    String city = cityField.getText().trim();
    String postalCode = postalCodeField.getText().trim();
    String street = streetField.getText().trim();
    String openHours = openHoursField.getText().trim();
    String closeHours = closeHoursField.getText().trim();

    if (designation.isEmpty()
        || nif.isEmpty()
        || city.isEmpty()
        || postalCode.isEmpty()
        || street.isEmpty()
        || openHours.isEmpty()
        || closeHours.isEmpty()) {
      showError("All fields are required.");
      return;
    }

    try {
      ApiClient.createRestaurant(
          model.getCurrentUser().getId(),
          designation,
          nif,
          city,
          postalCode,
          street,
          openHours,
          closeHours);

      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Success");
      alert.setHeaderText(null);
      alert.setContentText("Restaurant created successfully!");
      alert.showAndWait();

      UtilController.switchScene(
          stage,
          "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-list.fxml",
          "Restaurants",
          model);
    } catch (Exception e) {
      showError("Failed to create restaurant: " + e.getMessage());
    }
  }

  @FXML
  private void handleLogout(ActionEvent event) {
    model.setCurrentUser(null);
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @FXML
  private void handleGoHome(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  @FXML
  private void handleGoRestaurants(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-list.fxml", "Restaurants", model);
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
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
