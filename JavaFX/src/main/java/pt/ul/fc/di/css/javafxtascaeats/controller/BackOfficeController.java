package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class BackOfficeController implements ControllerWithModel {

  private DataModel
      model; // objeto que agrega todos os daddos que eu eventualmente precise usar ou setar
  private Stage stage; // representa a janela atual, é necessário para mudar de cena

  @FXML
  void handleUsers(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-users.fxml",
        "Back Office - Users",
        model);
  }

  @FXML
  void handleRestaurants(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-restaurants.fxml",
        "Back Office - Restaurants",
        model);
  }

  @FXML
  void handleMenus(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-menus.fxml",
        "Back Office - Menus",
        model);
  }

  @FXML
  void handleProducts(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-products.fxml",
        "Back Office - Products",
        model);
  }

  @FXML
  void handleOrders(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/back-office-orders.fxml",
        "Back Office - Orders",
        model);
  }

  @FXML
  void handleGoLogin(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;
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
