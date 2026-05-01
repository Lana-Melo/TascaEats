package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class UtilController {

  public static void switchScene(Stage stage, String fxmlPath, String title, DataModel model) {
    try {
      FXMLLoader loader = new FXMLLoader(UtilController.class.getResource(fxmlPath));
      Scene newScene = new Scene(loader.load());

      ControllerWithModel controller = loader.getController();
      controller.initModel(stage, model);

      stage.setTitle(title);
      stage.setScene(newScene);
      stage.show();

    } catch (IOException | ClassCastException e) {
      e.printStackTrace();
    }
  }
}
