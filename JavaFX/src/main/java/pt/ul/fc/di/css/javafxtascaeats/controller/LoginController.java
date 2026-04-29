package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

  @FXML private TextField emailField;

  @FXML private PasswordField passwordField;

  @FXML
  void handleLogin(ActionEvent event) {
    String email = emailField.getText();
    String password = passwordField.getText();
    System.out.println("Login apertado — email: " + email + ", password: " + password);
  }
}
