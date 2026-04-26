package pt.ul.fc.css.tascaeats.dto.order;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public class OrderRequest {
    @NotNull
    private Long clientId;
    @NotNull
    private List<Long> productIds;

    public OrderRequest() {}

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }        
    public List<Long> getProductIds() { return productIds; }
    public void setProductIds(List<Long> productIds) { this.productIds = productIds; }  
}
