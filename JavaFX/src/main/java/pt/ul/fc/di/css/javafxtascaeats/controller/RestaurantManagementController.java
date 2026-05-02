package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.MenuModel;
import pt.ul.fc.di.css.javafxtascaeats.model.RestaurantModel;

public class RestaurantManagementController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private Label titleLabel;
  @FXML private ComboBox<String> statusComboBox;
  @FXML private ComboBox<MenuModel> menuComboBox;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;

    RestaurantModel restaurant = model.getCurrentRestaurant();
    titleLabel.setText("Edit restaurant " + restaurant.getDesignation());

    statusComboBox.setItems(FXCollections.observableArrayList("OPEN", "CLOSED"));
    statusComboBox.setValue(restaurant.getStatus());

    model.setMenus(ApiClient.getAllMenus());
    menuComboBox.setItems(FXCollections.observableArrayList(model.getMenus()));
  }

  @FXML
  private void handleUpdate(ActionEvent event) {
    String selectedStatus = statusComboBox.getValue();
    MenuModel selectedMenu = menuComboBox.getValue();

    ApiClient.changeRestaurantStatus(model.getCurrentRestaurant().getId(), selectedStatus);

    if (selectedMenu != null) {
      ApiClient.assignMenu(model.getCurrentRestaurant().getId(), selectedMenu.getId());
    }

    RestaurantModel current = model.getCurrentRestaurant();
    model.setCurrentRestaurant(
        new RestaurantModel(
            current.getId(),
            current.getDesignation(),
            selectedStatus,
            current.getNif(),
            current.getStreet(),
            current.getCity(),
            current.getPostalCode(),
            current.getOpenHours(),
            current.getCloseHours()));

    Alert success = new Alert(Alert.AlertType.INFORMATION);
    success.setTitle("Restaurant Updated");
    success.setHeaderText(null);
    success.setContentText("Restaurant updated!");
    success.showAndWait();

    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-details.fxml",
        "Restaurant Details",
        model);
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

  @FXML
  private void handleGoDetails(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-details.fxml",
        "Restaurant Details",
        model);
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
