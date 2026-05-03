package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.MenuModel;

public class BackOfficeMenusController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  @FXML private CheckBox cbName;
  @FXML private TextField nameField;
  @FXML private CheckBox cbProductNumber;
  @FXML private TextField productNumberField;
  @FXML private CheckBox cbAverage;
  @FXML private TextField averageField;

  @FXML private Label errorLabel;

  @FXML private TableView<MenuModel> menusTable;
  @FXML private TableColumn<MenuModel, String> colName;
  @FXML private TableColumn<MenuModel, String> colCuisine;
  @FXML private TableColumn<MenuModel, Void> colEdit;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;

    bindFilter(cbName, nameField);
    bindFilter(cbProductNumber, productNumberField);
    bindFilter(cbAverage, averageField);

    colName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colCuisine.setCellValueFactory(new PropertyValueFactory<>("cuisine"));
    colEdit.setCellFactory(
        col ->
            new TableCell<>() {
              private final Button btn = new Button("✏");

              {
                btn.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
                btn.setOnAction(
                    e -> {
                      MenuModel menu = getTableView().getItems().get(getIndex());
                      model.setCurrentMenu(menu);
                      UtilController.switchScene(
                          stage,
                          "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-menu-edit.fxml",
                          "Edit Menu",
                          model);
                    });
              }

              @Override
              protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
                setAlignment(Pos.CENTER);
              }
            });
  }

  private void bindFilter(CheckBox cb, TextField field) {
    cb.selectedProperty()
        .addListener(
            (obs, wasSelected, isSelected) -> {
              field.setVisible(isSelected);
              field.setManaged(isSelected);
              if (!isSelected) field.clear();
            });
  }

  @FXML
  void handleSearch(ActionEvent event) {
    String name = cbName.isSelected() ? nameField.getText() : null;
    String productNumber = cbProductNumber.isSelected() ? productNumberField.getText() : null;
    String average = cbAverage.isSelected() ? averageField.getText() : null;

    try {
      List<MenuModel> menus = ApiClient.searchMenus(name, productNumber, average);
      menusTable.setItems(FXCollections.observableArrayList(menus));
      menusTable.setVisible(true);
      menusTable.setManaged(true);
      errorLabel.setVisible(false);
      errorLabel.setManaged(false);
    } catch (Exception e) {
      errorLabel.setText("Erro: " + e.getMessage());
      errorLabel.setVisible(true);
      errorLabel.setManaged(true);
    }
  }

  @FXML
  void handleGoBackOffice(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice.fxml", "Back Office", model);
  }

  @FXML
  void handleGoLogin(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
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
