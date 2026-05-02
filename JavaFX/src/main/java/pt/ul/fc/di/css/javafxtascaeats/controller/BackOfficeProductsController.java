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
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.ProductModel;

public class BackOfficeProductsController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private CheckBox cbDesignation;
  @FXML private TextField designationField;
  @FXML private CheckBox cbPrice;
  @FXML private TextField priceField;
  @FXML private CheckBox cbDescription;
  @FXML private TextField descriptionField;
  @FXML private CheckBox cbAvailability;
  @FXML private ComboBox<String> availabilityCombo;
  @FXML private CheckBox cbTimesOrdered;
  @FXML private TextField timesOrderedField;

  @FXML private Label errorLabel;

  @FXML private TableView<ProductModel> productsTable;
  @FXML private TableColumn<ProductModel, String> colDesignation;
  @FXML private TableColumn<ProductModel, Double> colPrice;
  @FXML private TableColumn<ProductModel, String> colDescription;
  @FXML private TableColumn<ProductModel, String> colAvailability;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    bindFilter(cbDesignation, designationField);
    bindFilter(cbPrice, priceField);
    bindFilter(cbDescription, descriptionField);
    availabilityCombo.getItems().addAll("AVAILABLE", "SOLD_OUT");
    cbAvailability
        .selectedProperty()
        .addListener(
            (obs, wasSelected, isSelected) -> {
              availabilityCombo.setVisible(isSelected);
              availabilityCombo.setManaged(isSelected);
              if (!isSelected) availabilityCombo.setValue(null);
            });
    bindFilter(cbTimesOrdered, timesOrderedField);

    colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    colAvailability.setCellValueFactory(new PropertyValueFactory<>("status"));
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
    String designation = cbDesignation.isSelected() ? designationField.getText() : null;
    String price = cbPrice.isSelected() ? priceField.getText() : null;
    String description = cbDescription.isSelected() ? descriptionField.getText() : null;
    String availability = cbAvailability.isSelected() ? availabilityCombo.getValue() : null;
    String timesOrdered = cbTimesOrdered.isSelected() ? timesOrderedField.getText() : null;

    try {
      List<ProductModel> products =
          ApiClient.searchProducts(designation, price, description, availability, timesOrdered);
      productsTable.setItems(FXCollections.observableArrayList(products));
      productsTable.setVisible(true);
      productsTable.setManaged(true);
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    } catch (Exception e) {
      errorLabel.setText("Erro: " + e.getMessage());
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
