package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.tascaeats.grpc.UserResponse;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentUser;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class LoginController implements ControllerWithModel {

  private DataModel
      model; // objeto que agrega todos os daddos que eu eventualmente precise usar ou setar
  private Stage stage; // representa a janela atual, é necessário para mudar de cena

  // propriedades que correspondem aos elementos da view, são preenchidas automaticamente pelo
  // JavaFX com base no id de cada elemento na view
  @FXML private TextField emailField;
  @FXML private PasswordField passwordField;
  @FXML private Label errorLabel;

  // este método é chamado pela view ao carregar no botao de login
  @FXML
  void handleLogin(ActionEvent event) {
    // pega os valores preenchidos pelo user
    String email = emailField.getText();
    // String password = passwordField.getText();

    // faz a chamada à API (backend) para validar o login e obter os dados do user
    try {
      UserResponse response = ApiClient.login(email, "mock"); // login é mockado

      CurrentUser currentUser = new CurrentUser();
      currentUser.setId(response.getId());
      currentUser.setName(response.getName());
      currentUser.setPhoneNumber(response.getPhoneNumber());
      currentUser.setNif(response.getNif());
      currentUser.setRole(response.getRole());
      currentUser.setUsername(response.getUsername());

      // guarda os dados do user logado no model, para que possam ser acedidos por outros
      // controllers
      model.setCurrentUser(currentUser);

      // troca de cena passando o model, a scene e o path para o ficheiro da nova view
      UtilController.switchScene(
          stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
    } catch (IllegalArgumentException e) {
      // Trata o caso de credenciais inválidas
      errorLabel.setText("Invalid credentials");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);

    } catch (Exception e) {
      // outros erros, como problemas de conexão
      errorLabel.setText("An error occured, please try again later.");
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    }
  }

  @FXML
  void handleCreateAccount(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/register.fxml", "Register", model);
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
