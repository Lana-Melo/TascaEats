package pt.ul.fc.di.css.javafxtascaeats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    String prefix = "/pt/ul/fc/di/css/javafxtascaeats/view/";

    BorderPane root = new BorderPane();
    FXMLLoader initLoader = new FXMLLoader(getClass().getResource(prefix + "init.fxml"));
    root.setCenter(initLoader.load());

    // O controller já é instanciado pelo FXMLLoader via fx:controller no FXML
    // LoginController loginController = initLoader.getController();

    Scene scene = new Scene(root, 280, 320);
    primaryStage.setScene(scene);
    primaryStage.setTitle("TascaEats");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
