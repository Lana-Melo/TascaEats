package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.RestaurantModel;

public class BackOfficeRestaurantsController implements ControllerWithModel {

  private DataModel
      model; // objeto que agrega todos os daddos que eu eventualmente precise usar ou setar
  private Stage stage; // representa a janela atual, é necessário para mudar de cena

  @FXML private CheckBox cbName;
  @FXML private TextField nameField;
  @FXML private CheckBox cbCity;
  @FXML private TextField cityField;
  @FXML private CheckBox cbStreet;
  @FXML private TextField streetField;
  @FXML private CheckBox cbPostalCode;
  @FXML private TextField postalCodeField;
  @FXML private CheckBox cbCuisine;
  @FXML private TextField cuisineField;
  @FXML private CheckBox cbMinOrders;
  @FXML private TextField minOrdersField;
  @FXML private CheckBox cbMinReviews;
  @FXML private TextField minReviewsField;
  @FXML private CheckBox cbActiveAt;
  @FXML private TextField activeAtField;
  @FXML private CheckBox cbMaxAvgPrice;
  @FXML private TextField maxAvgPriceField;

  @FXML private Label errorLabel;

  @FXML private TableView<RestaurantModel> restaurantsTable;
  @FXML private TableColumn<RestaurantModel, String> colDesignation;
  @FXML private TableColumn<RestaurantModel, String> colStatus;
  @FXML private TableColumn<RestaurantModel, String> colCity;
  @FXML private TableColumn<RestaurantModel, String> colStreet;
  @FXML private TableColumn<RestaurantModel, String> colPostalCode;
  @FXML private TableColumn<RestaurantModel, String> colHours;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    bindFilter(cbName, nameField);
    bindFilter(cbCity, cityField);
    bindFilter(cbStreet, streetField);
    bindFilter(cbPostalCode, postalCodeField);
    bindFilter(cbCuisine, cuisineField);
    bindFilter(cbMinOrders, minOrdersField);
    bindFilter(cbMinReviews, minReviewsField);
    bindFilter(cbActiveAt, activeAtField);
    bindFilter(cbMaxAvgPrice, maxAvgPriceField);

    colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
    colStreet.setCellValueFactory(new PropertyValueFactory<>("street"));
    colPostalCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
    colHours.setCellValueFactory(
        cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getOpenHours() + " - " + cellData.getValue().getCloseHours()));
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
    String name = cbName.isSelected() ? nameField.getText() : null;
    String city = cbCity.isSelected() ? cityField.getText() : null;
    String street = cbStreet.isSelected() ? streetField.getText() : null;
    String postalCode = cbPostalCode.isSelected() ? postalCodeField.getText() : null;
    String cuisine = cbCuisine.isSelected() ? cuisineField.getText() : null;
    String minOrders = cbMinOrders.isSelected() ? minOrdersField.getText() : null;
    String minReviews = cbMinReviews.isSelected() ? minReviewsField.getText() : null;
    String activeAt = cbActiveAt.isSelected() ? activeAtField.getText() : null;
    String maxAvgPrice = cbMaxAvgPrice.isSelected() ? maxAvgPriceField.getText() : null;

    try {
      List<RestaurantModel> restaurants =
          ApiClient.searchRestaurants(
              name,
              city,
              street,
              postalCode,
              cuisine,
              minOrders,
              minReviews,
              activeAt,
              maxAvgPrice);
      restaurantsTable.setItems(FXCollections.observableArrayList(restaurants));
      restaurantsTable.setVisible(true);
      restaurantsTable.setManaged(true);
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    } catch (Exception e) {
      errorLabel.setText("Erro: " + e.getMessage());
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
      e.printStackTrace();
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
