package pt.ul.fc.css.tascaeats.dto.restaurant;

import jakarta.validation.constraints.NotBlank;

public class ReviewRequestDto {
  @NotBlank private int rating;
  @NotBlank private String comment;
  @NotBlank private Long clientId;

  public ReviewRequestDto() {}

  public int getRating() {
    return rating;
  }

  public void setRating(int rating) {
    this.rating = rating;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public Long getClientId() {
    return clientId;
  }

  public void setClientId(Long clientId) {
    this.clientId = clientId;
  }
}
