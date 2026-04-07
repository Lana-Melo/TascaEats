package pt.ul.fc.css.tascaeats.dto.order;

import java.util.List;

public class OrderRequest {
    private Long clientId;
    private Long restaurantId;
    private List<Long> productIds;

    public OrderRequest() {}

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }        
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }  
}
