package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

  @FXML private TextField emailField;

  @FXML private PasswordField passwordField;

  @FXML
  void handleLogin(ActionEvent event) {
    String email = emailField.getText();
    String password = passwordField.getText();
    System.out.println("Login apertado — email: " + email + ", password: " + password);

    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/home.fxml"));

    // chamada a ApiClient

    Parent root = loader.load();
    HomeController homeController = loader.getController();
    homeController.initData(response.getName(), response.getRole());

    Stage stage = (Stage) emailField.getScene().getWindow();
    stage.setScene(new Scene(root, 640, 400));
  }
}
