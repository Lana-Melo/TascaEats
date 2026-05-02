package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.RestaurantModel;

public class RestaurantListController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private CheckBox nameCheckBox;
  @FXML private Label nameLabel;
  @FXML private TextField nameField;

  @FXML private CheckBox cityCheckBox;
  @FXML private Label cityLabel;
  @FXML private TextField cityField;

  @FXML private CheckBox streetCheckBox;
  @FXML private Label streetLabel;
  @FXML private TextField streetField;

  @FXML private CheckBox postalCodeCheckBox;
  @FXML private Label postalCodeLabel;
  @FXML private TextField postalCodeField;

  @FXML private CheckBox cuisineCheckBox;
  @FXML private Label cuisineLabel;
  @FXML private TextField cuisineField;

  @FXML private CheckBox minOrdersCheckBox;
  @FXML private Label minOrdersLabel;
  @FXML private TextField minOrdersField;

  @FXML private CheckBox minReviewsCheckBox;
  @FXML private Label minReviewsLabel;
  @FXML private TextField minReviewsField;

  @FXML private CheckBox activeAtCheckBox;
  @FXML private Label activeAtLabel;
  @FXML private TextField activeAtField;

  @FXML private CheckBox maxAvgPriceCheckBox;
  @FXML private Label maxAvgPriceLabel;
  @FXML private TextField maxAvgPriceField;

  @FXML private ListView<RestaurantModel> restaurantListView;

  @FXML private Button searchButton;
  @FXML private Button editButton;
  @FXML private Button createNewButton;

  @FXML private Label errorLabel;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;

    bindField(cityCheckBox, cityLabel, cityField);
    bindField(streetCheckBox, streetLabel, streetField);
    bindField(postalCodeCheckBox, postalCodeLabel, postalCodeField);
    bindField(cuisineCheckBox, cuisineLabel, cuisineField);
    bindField(minOrdersCheckBox, minOrdersLabel, minOrdersField);
    bindField(minReviewsCheckBox, minReviewsLabel, minReviewsField);
    bindField(activeAtCheckBox, activeAtLabel, activeAtField);
    bindField(maxAvgPriceCheckBox, maxAvgPriceLabel, maxAvgPriceField);
  }

  private void bindField(CheckBox checkBox, Label label, TextField field) {
    label.visibleProperty().bind(checkBox.selectedProperty());
    label.managedProperty().bind(checkBox.selectedProperty());

    field.visibleProperty().bind(checkBox.selectedProperty());
    field.managedProperty().bind(checkBox.selectedProperty());
  }

  @FXML
  private void handleLogout(ActionEvent event) {
    // limpa os dados do user logado no model
    model.setCurrentUser(null);
    // volta para a cena de login
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @FXML
  private void handleGoHome(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  @FXML
  private void handleSearch(ActionEvent event) {
    String city = cityCheckBox.isSelected() ? cityField.getText() : null;
    String street = streetCheckBox.isSelected() ? streetField.getText() : null;
    String postalCode = postalCodeCheckBox.isSelected() ? postalCodeField.getText() : null;
    String cuisine = cuisineCheckBox.isSelected() ? cuisineField.getText() : null;
    String minOrders = minOrdersCheckBox.isSelected() ? minOrdersField.getText() : null;
    String minReviews = minReviewsCheckBox.isSelected() ? minReviewsField.getText() : null;
    String activeAt = activeAtCheckBox.isSelected() ? activeAtField.getText() : null;
    String maxAvg = maxAvgPriceCheckBox.isSelected() ? maxAvgPriceField.getText() : null;

    java.util.List<RestaurantModel> results =
        ApiClient.searchAdminRestaurants(
            model.getCurrentUser().getId(),
            nameField.getText(),
            city,
            street,
            postalCode,
            cuisine,
            minOrders,
            minReviews,
            activeAt,
            maxAvg);

    model.setRestaurants(results);
    restaurantListView.setItems(FXCollections.observableArrayList(results));
  }

  @FXML
  private void handleEdit(ActionEvent event) {
    RestaurantModel selected = restaurantListView.getSelectionModel().getSelectedItem();
    if (selected == null) {
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      return;
    }
    errorLabel.setVisible(false);
    errorLabel.setManaged(false);
    model.setCurrentRestaurant(selected);
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-details.fxml",
        "Restaurant Details",
        model);
  }

  @FXML
  private void handleCreateNew(ActionEvent event) {
    System.out.println("Create New clicado");
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
