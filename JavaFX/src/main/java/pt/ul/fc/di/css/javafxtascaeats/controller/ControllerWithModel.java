package pt.ul.fc.di.css.javafxtascaeats.controller;

import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

// vai ser comum a todos os controllers que precisam de dados
public interface ControllerWithModel {

  void initModel(Stage stage, DataModel model);

  Stage getStage();

  void setStage(Stage stage);
}
