package pt.ul.fc.css.tascaeats.exceptions;

public class UserCannotBeDeletedException extends RuntimeException {
  public UserCannotBeDeletedException(String message) {
    super(message);
  }
}
