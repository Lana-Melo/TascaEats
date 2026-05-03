package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.ProductModel;

public class BackOfficeMenuEditController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private Label menuNameLabel;
  @FXML private Label menuTitleLabel;
  @FXML private Label errorLabel;

  @FXML private TableView<ProductModel> productsTable;
  @FXML private TableColumn<ProductModel, String> colDesignation;
  @FXML private TableColumn<ProductModel, String> colDescription;
  @FXML private TableColumn<ProductModel, Double> colPrice;
  @FXML private TableColumn<ProductModel, Void> colEdit;
  @FXML private TableColumn<ProductModel, Void> colDelete;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    menuNameLabel.setText(model.getCurrentMenu().getName());
    menuTitleLabel.setText(model.getCurrentMenu().getName());

    colDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

    colEdit.setCellFactory(
        col ->
            new TableCell<>() {
              private final Button btn = new Button("✏");

              {
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btn.setOnAction(
                    e -> {
                      ProductModel product = getTableView().getItems().get(getIndex());
                      model.setCurrentProduct(product);
                      UtilController.switchScene(
                          stage,
                          "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-product-form.fxml",
                          "Edit Product",
                          model);
                    });
              }

              @Override
              protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
              }
            });

    colDelete.setCellFactory(
        col ->
            new TableCell<>() {
              private final Button btn = new Button("🗑");

              {
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btn.setOnAction(
                    e -> {
                      ProductModel product = getTableView().getItems().get(getIndex());
                      handleDeleteProduct(product);
                    });
              }

              @Override
              protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
              }
            });

    loadProducts();
  }

  private void loadProducts() {
    try {
      List<ProductModel> products = ApiClient.getMenuProducts(model.getCurrentMenu().getId());
      productsTable.setItems(FXCollections.observableArrayList(products));
    } catch (Exception e) {
      showError("Erro ao carregar produtos: " + e.getMessage());
    }
  }

  private void handleDeleteProduct(ProductModel product) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
    confirm.setTitle("Confirmar");
    confirm.setHeaderText(null);
    confirm.setContentText(
        "Tens a certeza que queres remover o produto \"" + product.getDesignation() + "\"?");

    confirm
        .showAndWait()
        .ifPresent(
            response -> {
              if (response == ButtonType.OK) {
                try {
                  ApiClient.deleteProduct(model.getCurrentMenu().getId(), product.getId());
                  loadProducts();
                } catch (Exception e) {
                  showError("Erro ao remover produto: " + e.getMessage());
                }
              }
            });
  }

  @FXML
  void handleAddProduct(ActionEvent event) {
    model.setCurrentProduct(null);
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-product-form.fxml",
        "Add Product",
        model);
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  @FXML
  void handleGoMenus(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-menus.fxml", "Menus", model);
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
