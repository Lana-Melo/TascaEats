package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
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

  public void initData(String name, String role) {
    welcomeLabel.setText("Welcome, " + name);
    roleLabel.setText("You're a " + role);

    if ("CLIENT".equals(role)) {
      clientMessageFlow.setVisible(true);
      clientMessageFlow.setManaged(true);
    } else if ("DELIVERY_PERSON".equals(role)) {
      ordersButton.setVisible(true);
      ordersButton.setManaged(true);
    } else if ("ADMIN".equals(role)) {
      restaurantsButton.setVisible(true);
      restaurantsButton.setManaged(true);
    }
  }

  @FXML
  void handleSeeOrders(ActionEvent event) {
    System.out.println("See my orders clicado");
  }

  @FXML
  void handleManageRestaurants(ActionEvent event) {
    System.out.println("Manage restaurants clicado");
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
      initData(model.getCurrentUser().getName(), model.getCurrentUser().getRole());
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
