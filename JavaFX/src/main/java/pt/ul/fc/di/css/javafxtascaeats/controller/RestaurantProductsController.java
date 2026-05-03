package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.ProductModel;

public class RestaurantProductsController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  // ── products list view fields ──────────────────────────────────────
  @FXML private Hyperlink restaurantBreadcrumb;

  @FXML private CheckBox designationCheckBox;
  @FXML private Label designationLabel;
  @FXML private TextField designationField;

  @FXML private CheckBox priceCheckBox;
  @FXML private Label priceLabel;
  @FXML private TextField priceField;

  @FXML private CheckBox descriptionCheckBox;
  @FXML private Label descriptionLabel;
  @FXML private TextField descriptionField;

  @FXML private CheckBox availabilityCheckBox;
  @FXML private Label availabilityLabel;
  @FXML private ComboBox<String> availabilityField;

  @FXML private CheckBox timesOrderedCheckBox;
  @FXML private Label timesOrderedLabel;
  @FXML private TextField timesOrderedField;

  @FXML private TableView<ProductModel> productsTable;
  @FXML private TableColumn<ProductModel, String> nameColumn;
  @FXML private TableColumn<ProductModel, Double> priceColumn;
  @FXML private TableColumn<ProductModel, String> descriptionColumn;
  @FXML private TableColumn<ProductModel, String> statusColumn;
  @FXML private TableColumn<ProductModel, Void> editColumn;

  @FXML private Label productNameLabel;
  @FXML private ComboBox<String> statusComboBox;

  @FXML private Label errorLabel;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;

    if (productNameLabel != null) {
      initUpdateStatusView();
    } else {
      initProductsListView();
    }
  }

  private void initProductsListView() {
    if (restaurantBreadcrumb != null) {
      restaurantBreadcrumb.setText(model.getCurrentRestaurant().getDesignation());
    }

    bindField(priceCheckBox, priceLabel, priceField);
    bindField(descriptionCheckBox, descriptionLabel, descriptionField);
    availabilityField.setItems(FXCollections.observableArrayList("AVAILABLE", "SOLD_OUT"));
    bindField(availabilityCheckBox, availabilityLabel, availabilityField);
    bindField(timesOrderedCheckBox, timesOrderedLabel, timesOrderedField);

    nameColumn.setCellValueFactory(new PropertyValueFactory<>("designation"));
    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

    editColumn.setCellFactory(
        col ->
            new TableCell<>() {
              private final Button btn = new Button("✏");

              {
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btn.setOnAction(
                    e -> {
                      ProductModel product = getTableView().getItems().get(getIndex());
                      handleEditProduct(product);
                    });
              }

              @Override
              protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
              }
            });

    loadProducts("", null, null, null, null);
  }

  private void initUpdateStatusView() {
    ProductModel product = model.getCurrentProduct();
    if (restaurantBreadcrumb != null) {
      restaurantBreadcrumb.setText(model.getCurrentRestaurant().getDesignation());
    }
    productNameLabel.setText(product.getDesignation());
    statusComboBox.setItems(FXCollections.observableArrayList("AVAILABLE", "SOLD_OUT"));
    statusComboBox.setValue(product.getStatus());
  }

  private void bindField(CheckBox checkBox, Label label, TextField field) {
    if (label != null) {
      label.visibleProperty().bind(checkBox.selectedProperty());
      label.managedProperty().bind(checkBox.selectedProperty());
    }
    if (field != null) {
      field.visibleProperty().bind(checkBox.selectedProperty());
      field.managedProperty().bind(checkBox.selectedProperty());
    }
  }

  private void bindField(CheckBox checkBox, Label label, ComboBox<String> field) {
    if (label != null) {
      label.visibleProperty().bind(checkBox.selectedProperty());
      label.managedProperty().bind(checkBox.selectedProperty());
    }
    if (field != null) {
      field.visibleProperty().bind(checkBox.selectedProperty());
      field.managedProperty().bind(checkBox.selectedProperty());
    }
  }

  private void loadProducts(
      String designation,
      String price,
      String description,
      String availability,
      String timesOrdered) {
    try {
      List<ProductModel> products =
          ApiClient.getRestaurantProducts(
              model.getCurrentRestaurant().getId(),
              price,
              designation,
              description,
              availability,
              timesOrdered);
      productsTable.setItems(FXCollections.observableArrayList(products));
    } catch (Exception e) {
      showError("Failed to load products: " + e.getMessage());
    }
  }

  @FXML
  private void handleSearch(ActionEvent event) {
    String designation = designationField.getText().trim();
    String price = priceCheckBox.isSelected() ? priceField.getText().trim() : null;
    String description =
        descriptionCheckBox.isSelected() ? descriptionField.getText().trim() : null;
    String availability = availabilityCheckBox.isSelected() ? availabilityField.getValue() : null;
    String timesOrdered =
        timesOrderedCheckBox.isSelected() ? timesOrderedField.getText().trim() : null;
    loadProducts(designation, price, description, availability, timesOrdered);
  }

  private void handleEditProduct(ProductModel product) {
    model.setCurrentProduct(product);
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/update-product-status.fxml",
        "Update Product Status",
        model);
  }

  @FXML
  private void handleUpdate(ActionEvent event) {
    String newStatus = statusComboBox.getValue();
    if (newStatus == null) {
      showError("Please select a status.");
      return;
    }
    try {
      ApiClient.updateProductStatus(
          model.getCurrentRestaurant().getId(), model.getCurrentProduct().getId(), newStatus);

      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Success");
      alert.setHeaderText(null);
      alert.setContentText("Product status updated successfully!");
      alert.showAndWait();

      UtilController.switchScene(
          stage,
          "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-products.fxml",
          "Products",
          model);
    } catch (Exception e) {
      showError("Failed to update status: " + e.getMessage());
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

  @FXML
  private void handleGoRestaurantDetails(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-details.fxml",
        "Restaurant Details",
        model);
  }

  @FXML
  private void handleGoProducts(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-products.fxml", "Products", model);
  }

  private void showError(String message) {
    if (errorLabel != null) {
      errorLabel.setText(message);
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    }
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
