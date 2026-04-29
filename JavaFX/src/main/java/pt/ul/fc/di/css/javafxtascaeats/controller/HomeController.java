package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;

public class HomeController {

  @FXML private Label welcomeLabel;
  @FXML private Label roleLabel;
  @FXML private Label clientMessageLabel;
  @FXML private Hyperlink clientLink;
  @FXML private Button ordersButton;
  @FXML private Button restaurantsButton;

  public void initData(String name, String role) {
    welcomeLabel.setText("Welcome, " + name);
    roleLabel.setText("You're a " + role);

    if ("CLIENT".equals(role)) {
      clientMessageLabel.setVisible(true);
      clientMessageLabel.setManaged(true);
      clientLink.setVisible(true);
      clientLink.setManaged(true);
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
}
