package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentUser;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class HomeController implements ControllerWithModel {

  private DataModel
      model; // objeto que agrega todos os daddos que eu eventualmente precise usar ou setar
  private Stage stage; // representa a janela atual, é necessário para mudar de cena

  // propriedades que correspondem aos elementos da view, são preenchidas automaticamente pelo
  // JavaFX com base no id de cada elemento na view
  @FXML private Label welcomeLabel;
  @FXML private Label roleLabel;
  @FXML private Button ordersButton;
  @FXML private Button restaurantsButton;
  @FXML private TextFlow clientMessageFlow;

  public void initData(CurrentUser currentUser) {
    String name = currentUser.getName();
    String role =
        switch (currentUser.getClass().getSimpleName()) {
          case "CurrentClient" -> "Client";
          case "CurrentDeliveryPerson" -> "Delivery Person";
          case "CurrentAdmin" -> "Admin";
          default -> "UNKNOWN";
        };
    welcomeLabel.setText("Welcome, " + name);
    roleLabel.setText("You're a " + role);

    if ("Client".equals(role)) {
      clientMessageFlow.setVisible(true);
      clientMessageFlow.setManaged(true);
    } else if ("Delivery Person".equals(role)) {
      ordersButton.setVisible(true);
      ordersButton.setManaged(true);
    } else if ("Admin".equals(role)) {
      restaurantsButton.setVisible(true);
      restaurantsButton.setManaged(true);
    }
  }

  @FXML
  void handleSeeOrders(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/deliveries.fxml", "Deliveries", model);
  }

  @FXML
  void handleManageRestaurants(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/restaurant-list.fxml",
        "Restaurant List",
        model);
  }

  @FXML
  void handleMyProfile(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/profile-details.fxml",
        "Profile Details",
        model);
  }

  @FXML
  void handleLogout(ActionEvent event) {
    // limpa os dados do user logado no model
    model.setCurrentUser(null);
    // volta para a cena de login
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  @Override
  public void initModel(Stage stage, DataModel model) {
    // inicializa os dados da view com base no user logado, que está guardado no model
    if (model.getCurrentUser() != null) {
      this.stage = stage;
      this.model = model;
      initData(model.getCurrentUser());
    } else {
      welcomeLabel.setText("No user logged in");
    }
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
