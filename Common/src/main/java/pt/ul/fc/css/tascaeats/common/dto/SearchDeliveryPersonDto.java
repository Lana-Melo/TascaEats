package pt.ul.fc.css.tascaeats.common.dto;

public class SearchDeliveryPersonDto {

  private String name;
  private String deliveryNumber;

  public SearchDeliveryPersonDto() {}

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDeliveryNumber() {
    return deliveryNumber;
  }

  public void setDeliveryNumber(String deliveryNumber) {
    this.deliveryNumber = deliveryNumber;
  }
}
