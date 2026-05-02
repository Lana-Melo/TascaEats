package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.RestaurantModel;

public class RestaurantDetailsController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private Label designationLabel;
  @FXML private Label addressLabel;
  @FXML private Label statusLabel;
  @FXML private Label openingHoursLabel;

  @FXML private Button manageRestaurantButton;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;

    RestaurantModel restaurant = model.getCurrentRestaurant();
    designationLabel.setText(restaurant.getDesignation());
    addressLabel.setText(
        restaurant.getStreet() + ", " + restaurant.getCity() + " " + restaurant.getPostalCode());
    statusLabel.setText(restaurant.getStatus());
    openingHoursLabel.setText(restaurant.getOpenHours() + " - " + restaurant.getCloseHours());
  }

  @FXML
  private void handleManageRestaurant(ActionEvent event) {
    System.out.println("Manage Restaurant clicked");
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
  private void handleGoBack(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-list.fxml", "Restaurants", model);
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
