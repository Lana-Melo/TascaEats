package pt.ul.fc.css.tascaeats.delivery.dto.request;

import jakarta.validation.constraints.NotNull;

public class AssignmentRequestDto {

  @NotNull private Long deliveryId;
  @NotNull private Long deliveryPersonId;

  public AssignmentRequestDto() {}

  public Long getDeliveryId() {
    return deliveryId;
  }

  public void setDeliveryId(Long deliveryId) {
    this.deliveryId = deliveryId;
  }

  public Long getDeliveryPersonId() {
    return deliveryPersonId;
  }

  public void setDeliveryPersonId(Long deliveryPersonId) {
    this.deliveryPersonId = deliveryPersonId;
  }
}
