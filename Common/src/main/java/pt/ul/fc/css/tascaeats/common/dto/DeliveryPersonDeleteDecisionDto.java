package pt.ul.fc.css.tascaeats.common.dto;

public class DeliveryPersonDeleteDecisionDto {

  private String correlationId;
  private String mode; // "HARD" or "SOFT"

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public String getMode() {
    return mode;
  }

  public void setMode(String mode) {
    this.mode = mode;
  }
}
