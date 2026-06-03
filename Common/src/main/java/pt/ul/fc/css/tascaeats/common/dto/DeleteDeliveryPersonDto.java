package pt.ul.fc.css.tascaeats.common.dto;

public class DeleteDeliveryPersonDto {
  private String correlationId; // relacionado ao evento
  private String username;

  public String getCorrelationId() {
    return correlationId;
  }

  public String getUsername() {
    return username;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
