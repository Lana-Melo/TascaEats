package pt.ul.fc.di.css.javafxtascaeats.model;

import java.util.List;

public class MenuModel {

  private long id;
  private String name;
  private String cuisine;
  private List<Long> productIds;

  public MenuModel(long id, String name, String cuisine, List<Long> productIds) {
    this.id = id;
    this.name = name;
    this.cuisine = cuisine;
    this.productIds = productIds;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCuisine() {
    return cuisine;
  }

  public List<Long> getProductIds() {
    return productIds;
  }

  @Override
  public String toString() {
    return name;
  }
}
