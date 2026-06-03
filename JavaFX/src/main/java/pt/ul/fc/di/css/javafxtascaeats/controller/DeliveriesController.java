package pt.ul.fc.di.css.javafxtascaeats.controller;

import io.grpc.stub.StreamObserver;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.ul.fc.css.tascaeats.grpc.DeliveryItem;
import pt.ul.fc.css.tascaeats.grpc.DeliveryResponse;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.CurrentDeliveryPerson;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;

public class DeliveriesController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;
  private List<DeliveryItem> deliveries;

  @FXML private ListView<DeliveryItem> deliveriesListView;
  @FXML private Label errorLabel;

  @Override
  public void initModel(Stage stage, DataModel model) {
    this.stage = stage;
    this.model = model;
    loadDeliveries();
  }

  private void loadDeliveries() {
    try {
      CurrentDeliveryPerson dp = (CurrentDeliveryPerson) model.getCurrentUser();
      deliveries = ApiClient.getMyDeliveries(dp.getUsername());
      deliveriesListView.setItems(FXCollections.observableArrayList(deliveries));
      deliveriesListView.setCellFactory(
          lv ->
              new ListCell<>() {
                @Override
                protected void updateItem(DeliveryItem item, boolean empty) {
                  super.updateItem(item, empty);
                  if (empty || item == null) {
                    setText(null);
                  } else {
                    setText("Order #" + item.getOrderId() + " — " + item.getStatus());
                  }
                }
              });
    } catch (Exception e) {
      showError("Failed to load deliveries.");
    }
  }

  @FXML
  void handleMarkDelivered(ActionEvent event) {
    DeliveryItem selected = deliveriesListView.getSelectionModel().getSelectedItem();
    if (selected == null) {
      showError("Select a delivery first.");
      return;
    }
    ApiClient.markDeliveryAsDelivered(
        selected.getId(),
        new StreamObserver<>() {
          @Override
          public void onNext(DeliveryResponse value) {
            Platform.runLater(
                () -> {
                  // atualiza o item na lista sem ir ao Kafka outra vez
                  int index = deliveriesListView.getItems().indexOf(selected);
                  if (index >= 0) {
                    DeliveryItem updated =
                        DeliveryItem.newBuilder()
                            .setId(value.getId())
                            .setOrderId(value.getOrderId())
                            .setStatus(value.getStatus())
                            .setPickedUpAt(value.getPickedUpAt())
                            .setDeliveredAt(value.getDeliveredAt())
                            .build();
                    deliveriesListView.getItems().set(index, updated);
                  }
                });
          }

          @Override
          public void onError(Throwable t) {
            Platform.runLater(() -> showError("Failed: " + t.getMessage()));
          }

          @Override
          public void onCompleted() {
            Platform.runLater(
                () -> new Alert(Alert.AlertType.INFORMATION, "Marked as delivered!").showAndWait());
          }
        });
  }

  @FXML
  void handleGoHome(ActionEvent event) {
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/home.fxml", "Home", model);
  }

  @Override
  public Stage getStage() {
    return stage;
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }

  private void showError(String msg) {
    errorLabel.setText(msg);
    errorLabel.setVisible(true);
    errorLabel.setManaged(true);
  }
}
