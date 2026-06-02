package pt.ul.fc.css.tascaeats.common.dto;


public class AssignmentRequestDto {

  private Long deliveryId;
  private Long deliveryPersonId;

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
