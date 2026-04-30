package pt.ul.fc.di.css.javafxtascaeats.model;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

// é basicamente o dto do backend, mas com os tipos mais adequados
// para tratar do binding com os controllers
public class CurrentUser {

  private final LongProperty id = new SimpleLongProperty();
  private final StringProperty role = new SimpleStringProperty();
  private final StringProperty username = new SimpleStringProperty();
  private final StringProperty name = new SimpleStringProperty();
  private final StringProperty phoneNumber = new SimpleStringProperty();
  private final StringProperty nif = new SimpleStringProperty();

  // getters de propriedade
  public LongProperty idProperty() {
    return id;
  }

  public StringProperty roleProperty() {
    return role;
  }

  public StringProperty usernameProperty() {
    return username;
  }

  public StringProperty nameProperty() {
    return name;
  }

  public StringProperty phoneNumberProperty() {
    return phoneNumber;
  }

  public StringProperty nifProperty() {
    return nif;
  }

  // getters/setters normais
  public long getId() {
    return id.get();
  }

  public void setId(long v) {
    id.set(v);
  }

  public String getRole() {
    return role.get();
  }

  public void setRole(String v) {
    role.set(v);
  }

  public String getUsername() {
    return username.get();
  }

  public void setUsername(String v) {
    username.set(v);
  }

  public String getName() {
    return name.get();
  }

  public void setName(String v) {
    name.set(v);
  }

  public String getPhoneNumber() {
    return phoneNumber.get();
  }

  public void setPhoneNumber(String v) {
    phoneNumber.set(v);
  }

  public String getNif() {
    return nif.get();
  }

  public void setNif(String v) {
    nif.set(v);
  }
}
