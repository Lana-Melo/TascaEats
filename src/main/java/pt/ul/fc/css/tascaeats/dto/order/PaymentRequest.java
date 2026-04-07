package pt.ul.fc.css.tascaeats.dto.order;

public class PaymentRequest {
    private Long orderId;
    private String paymentMethod;
    private double amount;

    public PaymentRequest() {}  

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }            
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

}
