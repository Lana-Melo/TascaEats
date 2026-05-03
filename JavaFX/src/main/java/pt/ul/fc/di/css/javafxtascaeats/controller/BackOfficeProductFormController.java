package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.ProductModel;

public class BackOfficeProductFormController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private Label formTitleLabel;
  @FXML private Label formTitleBreadcrumb;
  @FXML private Hyperlink menuBreadcrumb;
  @FXML private Label errorLabel;

  @FXML private TextField designationField;
  @FXML private TextField descriptionField;
  @FXML private TextField priceField;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    menuBreadcrumb.setText(model.getCurrentMenu().getName());

    boolean isEdit = model.getCurrentProduct() != null;

    if (isEdit) {
      formTitleLabel.setText("Edit Product");
      formTitleBreadcrumb.setText("Edit Product");
      ProductModel product = model.getCurrentProduct();
      designationField.setText(product.getDesignation());
      descriptionField.setText(product.getDescription());
      priceField.setText(String.valueOf(product.getPrice()));
    } else {
      formTitleLabel.setText("Add Product");
      formTitleBreadcrumb.setText("Add Product");
    }
  }

  @FXML
  void handleSave(ActionEvent event) {
    String designation = designationField.getText().trim();
    String description = descriptionField.getText().trim();
    String priceText = priceField.getText().trim();

    if (designation.isEmpty() || description.isEmpty() || priceText.isEmpty()) {
      showError("Preenche todos os campos.");
      return;
    }

    double price;
    try {
      price = Double.parseDouble(priceText);
    } catch (NumberFormatException e) {
      showError("Preço inválido.");
      return;
    }

    try {
      if (model.getCurrentProduct() != null) {
        ApiClient.updateProduct(
            model.getCurrentMenu().getId(),
            model.getCurrentProduct().getId(),
            designation,
            description,
            price);
      } else {
        ApiClient.addProduct(model.getCurrentMenu().getId(), designation, description, price);
      }

      // voltar para a edição do menu
      UtilController.switchScene(
          stage,
          "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-menu-edit.fxml",
          "Edit Menu",
          model);

    } catch (Exception e) {
      showError("Erro: " + e.getMessage());
    }
  }

  private void showError(String message) {
    errorLabel.setText(message);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }

  @FXML
  void handleGoMenuEdit(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-menu-edit.fxml",
        "Edit Menu",
        model);
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
