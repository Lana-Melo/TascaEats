package pt.ul.fc.di.css.javafxtascaeats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.controller.LoginController;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    String prefix = "/pt/ul/fc/di/css/javafxtascaeats/view/";

    BorderPane root = new BorderPane();
    FXMLLoader initLoader = new FXMLLoader(getClass().getResource(prefix + "login.fxml"));
    root.setCenter(initLoader.load());

    DataModel modal = new DataModel();
    LoginController loginController = initLoader.getController();
    loginController.initModel(primaryStage, modal);

    Scene scene = new Scene(root, 280, 320);
    primaryStage.setScene(scene);
    primaryStage.setTitle("TascaEats");
    primaryStage.show();
  }

  public static void main(String[] args) {
    launch(args);
  }
}
