package pt.ul.fc.di.css.javafxtascaeats.controller;

import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.ul.fc.di.css.javafxtascaeats.api.ApiClient;
import pt.ul.fc.di.css.javafxtascaeats.model.DataModel;
import pt.ul.fc.di.css.javafxtascaeats.model.OrderModel;
import pt.ul.fc.di.css.javafxtascaeats.model.OrderProductItemModel;

public class BackOfficeOrdersController implements ControllerWithModel {

  private DataModel model;
  private Stage stage;

  // ─── List View ────────────────────────────────────────────────────
  @FXML private ComboBox<String> statusFilterComboBox;
  @FXML private TableView<OrderModel> ordersTable;
  @FXML private TableColumn<OrderModel, String> clientNameCol;
  @FXML private TableColumn<OrderModel, String> createdAtCol;
  @FXML private TableColumn<OrderModel, String> statusCol;
  @FXML private TableColumn<OrderModel, Void> editCol;
  @FXML private Label errorLabel;

  // ─── Detail View ──────────────────────────────────────────────────
  @FXML private Label createdAtLabel;
  @FXML private Label currentStatusLabel;
  @FXML private Label totalValueLabel;
  @FXML private Label addressLabel;
  @FXML private Label clientNameLabel;
  @FXML private Label clientNifLabel;
  @FXML private Label deliveryPersonNameLabel;
  @FXML private Label deliveryPersonNifLabel;
  @FXML private TableView<OrderProductItemModel> productsTable;
  @FXML private TableColumn<OrderProductItemModel, String> productDesignationCol;
  @FXML private TableColumn<OrderProductItemModel, Double> productPriceCol;
  @FXML private ComboBox<String> newStatusComboBox;
  @FXML private Label statusErrorLabel;
  @FXML private VBox paymentInfoBox;
  @FXML private Label paymentAmountLabel;
  @FXML private Label paymentAtLabel;
  @FXML private HBox paymentChangeBox;
  @FXML private Label paymentChangeLabel;
  @FXML private VBox paymentFormBox;
  @FXML private ComboBox<String> paymentMethodComboBox;
  @FXML private TextField paymentAmountField;
  @FXML private Label paymentInfoLabel;
  @FXML private TextField paymentInfoField;
  @FXML private Label paymentErrorLabel;

  @Override
  public void initModel(Stage stage, DataModel model) {
    if (this.model != null) {
      throw new IllegalStateException("Model can only be initialized once");
    }
    this.stage = stage;
    this.model = model;
    if (statusFilterComboBox != null) {
      initListView();
    }
    if (createdAtLabel != null) {
      initDetailView();
    }
  }

  // ─── List View Init ───────────────────────────────────────────────

  private void initListView() {
    statusFilterComboBox.getItems().clear();
    statusFilterComboBox.getItems().add("All");
    statusFilterComboBox
        .getItems()
        .addAll("CREATED", "PAID", "PREPARING", "READY", "CANCELLED", "DELIVERED");
    statusFilterComboBox.setValue("All");

    clientNameCol.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getClientName()));
    createdAtCol.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getCreatedAt().replace("T", " ")));
    statusCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

    editCol.setCellFactory(
        col ->
            new TableCell<>() {
              private final Button btn = new Button("Edit");

              {
                btn.setOnAction(
                    e -> {
                      OrderModel order = getTableView().getItems().get(getIndex());
                      model.setCurrentOrder(order);
                      UtilController.switchScene(
                          stage,
                          "/pt/ul/fc/di/css/javafxtascaeats/view/backoffice-order-details.fxml",
                          "Back Office - Order Details",
                          model);
                    });
              }

              @Override
              protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
              }
            });

    loadOrders(null);
  }

  private void loadOrders(String status) {
    try {
      List<OrderModel> orders = ApiClient.searchOrders("All".equals(status) ? null : status);
      ordersTable.setItems(FXCollections.observableArrayList(orders));
    } catch (Exception e) {
      showError(errorLabel, "Failed to load orders: " + e.getMessage());
    }
  }

  // ─── Detail View Init ─────────────────────────────────────────────

  private void initDetailView() {
    OrderModel order = model.getCurrentOrder();
    if (order == null) return;

    createdAtLabel.setText(order.getCreatedAt().replace("T", " "));
    currentStatusLabel.setText(order.getStatus());
    totalValueLabel.setText(String.format("%.2f €", order.getTotalValue()));
    addressLabel.setText(order.getStreet() + ", " + order.getCity() + " " + order.getPostalCode());
    clientNameLabel.setText(order.getClientName());
    clientNifLabel.setText(order.getClientNif());
    deliveryPersonNameLabel.setText(
        order.getDeliveryPersonName() != null && !order.getDeliveryPersonName().isEmpty()
            ? order.getDeliveryPersonName()
            : "N/A");
    deliveryPersonNifLabel.setText(
        order.getDeliveryPersonNif() != null && !order.getDeliveryPersonNif().isEmpty()
            ? order.getDeliveryPersonNif()
            : "N/A");

    productDesignationCol.setCellValueFactory(
        data -> new SimpleStringProperty(data.getValue().getDesignation()));
    productPriceCol.setCellValueFactory(
        data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
    productsTable.setItems(FXCollections.observableArrayList(order.getProducts()));

    newStatusComboBox.getItems().clear();
    newStatusComboBox.getItems().addAll("PREPARING", "READY", "CANCELLED");

    paymentMethodComboBox.getItems().clear();
    paymentMethodComboBox.getItems().addAll("Cash", "MBWay", "Card");
    paymentMethodComboBox.setOnAction(
        e -> {
          String method = paymentMethodComboBox.getValue();
          boolean needsInfo = "MBWay".equals(method) || "Card".equals(method);
          paymentInfoLabel.setVisible(needsInfo);
          paymentInfoLabel.setManaged(needsInfo);
          paymentInfoField.setVisible(needsInfo);
          paymentInfoField.setManaged(needsInfo);
        });

    boolean alreadyPaid = order.getPaymentAt() != null && !order.getPaymentAt().isEmpty();
    if (alreadyPaid) {
      showPaymentInfo(order);
    }
  }

  private void showPaymentInfo(OrderModel order) {
    paymentAmountLabel.setText(String.format("%.2f €", order.getPaymentAmount()));
    paymentAtLabel.setText(order.getPaymentAt().replace("T", " "));
    if (order.getPaymentChange() > 0) {
      paymentChangeLabel.setText(String.format("%.2f €", order.getPaymentChange()));
      paymentChangeBox.setVisible(true);
      paymentChangeBox.setManaged(true);
    } else {
      paymentChangeBox.setVisible(false);
      paymentChangeBox.setManaged(false);
    }
    paymentInfoBox.setVisible(true);
    paymentInfoBox.setManaged(true);
    paymentFormBox.setVisible(false);
    paymentFormBox.setManaged(false);
  }

  // ─── List View Handlers ───────────────────────────────────────────

  @FXML
  void handleSearch(ActionEvent event) {
    loadOrders(statusFilterComboBox.getValue());
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

  @FXML
  void handleLogout(ActionEvent event) {
    model.setCurrentUser(null);
    UtilController.switchScene(
        stage, "/pt/ul/fc/di/css/javafxtascaeats/view/login.fxml", "Login", model);
  }

  // ─── Detail View Handlers ─────────────────────────────────────────

  @FXML
  void handleGoOrders(ActionEvent event) {
    UtilController.switchScene(
        stage,
        "/pt/ul/fc/di/css/javafxtascaeats/view/back-office-orders.fxml",
        "Back Office - Orders",
        model);
  }

  @FXML
  void handleUpdateStatus(ActionEvent event) {
    String status = newStatusComboBox.getValue();
    if (status == null) {
      showError(statusErrorLabel, "Please select a status.");
      return;
    }
    OrderModel order = model.getCurrentOrder();
    try {
      OrderModel updated = ApiClient.updateOrderStatus(order.getId(), status);
      model.setCurrentOrder(updated);
      currentStatusLabel.setText(updated.getStatus());
      deliveryPersonNameLabel.setText(
          updated.getDeliveryPersonName() != null && !updated.getDeliveryPersonName().isEmpty()
              ? updated.getDeliveryPersonName()
              : "N/A");
      deliveryPersonNifLabel.setText(
          updated.getDeliveryPersonNif() != null && !updated.getDeliveryPersonNif().isEmpty()
              ? updated.getDeliveryPersonNif()
              : "N/A");
      hideError(statusErrorLabel);
      showAlert("Success", "Order status updated to " + status + ".");
    } catch (IllegalArgumentException e) {
      showError(statusErrorLabel, e.getMessage());
    } catch (Exception e) {
      showError(statusErrorLabel, "Failed to update status: " + e.getMessage());
    }
  }

  @FXML
  void handleRegisterPayment(ActionEvent event) {
    String method = paymentMethodComboBox.getValue();
    String amountStr = paymentAmountField.getText();
    String paymentInfo = paymentInfoField.getText();

    if (method == null) {
      showError(paymentErrorLabel, "Please select a payment method.");
      return;
    }
    if (amountStr == null || amountStr.isBlank()) {
      showError(paymentErrorLabel, "Please enter an amount.");
      return;
    }
    double amount;
    try {
      amount = Double.parseDouble(amountStr);
    } catch (NumberFormatException e) {
      showError(paymentErrorLabel, "Invalid amount — must be a number.");
      return;
    }
    if (("MBWay".equals(method) || "Card".equals(method))
        && (paymentInfo == null || paymentInfo.isBlank())) {
      showError(paymentErrorLabel, "Payment info is required for " + method + ".");
      return;
    }

    OrderModel order = model.getCurrentOrder();
    try {
      OrderModel updated =
          ApiClient.processPayment(
              order.getId(), method, amount, paymentInfo.isBlank() ? null : paymentInfo);
      model.setCurrentOrder(updated);
      showPaymentInfo(updated);
      currentStatusLabel.setText(updated.getStatus());
      hideError(paymentErrorLabel);
      showAlert("Success", "Payment registered successfully.");
    } catch (IllegalArgumentException e) {
      showError(paymentErrorLabel, e.getMessage());
    } catch (Exception e) {
      showError(paymentErrorLabel, "Failed to register payment: " + e.getMessage());
    }
  }

  private void showError(Label label, String message) {
    if (label == null) return;
    label.setText(message);
    label.setVisible(true);
    label.setManaged(true);
  }

  private void hideError(Label label) {
    if (label == null) return;
    label.setVisible(false);
    label.setManaged(false);
  }

  private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  @Override
  public Stage getStage() {
    return stage;
  }

  @Override
  public void setStage(Stage stage) {
    this.stage = stage;
  }
}
