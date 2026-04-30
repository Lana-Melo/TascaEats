package pt.ul.fc.di.css.javafxtascaeats.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CurrentDeliveryPerson extends CurrentUser {
  private final StringProperty status = new SimpleStringProperty();

  public CurrentDeliveryPerson() {
    super();
  }
}
