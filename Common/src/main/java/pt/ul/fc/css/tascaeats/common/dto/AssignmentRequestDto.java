package pt.ul.fc.css.tascaeats.common.dto;

public class AssignmentRequestDto {

  private Long deliveryId;
  private String deliveryPersonUsername;

  public AssignmentRequestDto() {}

  public Long getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

  public String getDeliveryPersonUsername() {
    return deliveryPersonUsername;
  }

  public void setDeliveryPersonUsername(String deliveryPersonUsername) {
    this.deliveryPersonUsername = deliveryPersonUsername;
  }
}
