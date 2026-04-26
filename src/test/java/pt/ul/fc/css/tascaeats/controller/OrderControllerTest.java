package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.services.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void createOrder_shouldReturn200_whenSuccessful() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setClientId(1L);
        request.setProductIds(List.of(1L, 2L));

        OrderResponse response = new OrderResponse(1L, OrderStatus.CREATED, LocalDateTime.now(), 25.99, 1L, "Client Name", List.of(1L), List.of(1L, 2L), null, null);
        when(orderService.createOrder(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CREATED"))
            .andExpect(jsonPath("$.totalValue").value(25.99));
    }

    @Test
    void processPayment_shouldReturn400_whenMissingRequiredField() throws Exception {
        String jsonWithMissingField = "{\"paymentMethod\": \"CARD\", \"amount\": 25.99}"; // Missing orderId

        mockMvc.perform(post("/api/orders/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithMissingField))
            .andExpect(status().isBadRequest());
    }

    @Test
    void createOrder_shouldReturn400_whenMissingRequiredField() throws Exception {
        String jsonWithMissingField = "{\"productIds\": [1,2]}"; // Missing clientId

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonWithMissingField))
            .andExpect(status().isBadRequest());
    }

    @Test
    void processPayment_shouldReturn200_whenSuccessful() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CARD");
        request.setAmount(25.99);

        OrderResponse response = new OrderResponse(1L, OrderStatus.PAID, LocalDateTime.now(), 25.99, 1L, "Client Name", List.of(1L), List.of(1L, 2L), null, null);
        when(orderService.processPayment(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    void processPayment_shouldReturn200_withCashPayment() throws Exception {
        PaymentRequest request = new PaymentRequest();
        request.setOrderId(1L);
        request.setPaymentMethod("CASH");
        request.setAmount(15.50);

        OrderResponse response = new OrderResponse(1L, OrderStatus.PAID, LocalDateTime.now(), 15.50, 1L, "Client Name", List.of(1L), List.of(1L), null, null);
        when(orderService.processPayment(any())).thenReturn(response);

        mockMvc.perform(post("/api/orders/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PAID"))
            .andExpect(jsonPath("$.totalValue").value(15.50));
    }

    @Test
    void updateOrderStatus_shouldReturn200_whenSuccessful() throws Exception {
        OrderResponse response = new OrderResponse(1L, OrderStatus.READY, LocalDateTime.now(), 25.99, 1L, "Client Name", List.of(1L), List.of(1L, 2L), null, null);
        when(orderService.updateOrderStatus(eq(1L), eq("READY"))).thenReturn(response);

        mockMvc.perform(patch("/api/orders/1/status")
                .param("status", "READY"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("READY"));
    }

    @Test
    void updateOrderStatus_shouldReturn200_whenUpdatingToPreparing() throws Exception {
        OrderResponse response = new OrderResponse(1L, OrderStatus.PREPARING, LocalDateTime.now(), 25.99, 1L, "Client Name", List.of(1L), List.of(1L, 2L), null, null);
        when(orderService.updateOrderStatus(eq(1L), eq("PREPARING"))).thenReturn(response);

        mockMvc.perform(patch("/api/orders/1/status")
                .param("status", "PREPARING"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("PREPARING"));
    }

    @Test
    void updateOrderStatus_shouldReturn400_whenServiceFails() throws Exception {
        when(orderService.updateOrderStatus(eq(1L), eq("READY"))).thenReturn(null);

        mockMvc.perform(patch("/api/orders/1/status")
                .param("status", "READY"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateOrderStatus_shouldReturn400_whenInvalidStatus() throws Exception {
        mockMvc.perform(patch("/api/orders/1/status")
                .param("status", "INVALID_STATUS"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void assignDeliveryPerson_shouldReturn200_whenSuccessful() throws Exception {
        OrderResponse response = new OrderResponse(1L, OrderStatus.DELIVERED, LocalDateTime.now(), 25.99, 1L, "Client Name", List.of(1L), List.of(1L, 2L), 1L, "Delivery Person");
        when(orderService.assignDeliveryPerson(eq(1L), eq(1L))).thenReturn(response);

        mockMvc.perform(patch("/api/orders/1/deliveryPerson/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("DELIVERED"))
            .andExpect(jsonPath("$.deliveryPersonId").value(1));
    }

    @Test
    void assignDeliveryPerson_shouldReturn400_whenServiceFails() throws Exception {
        when(orderService.assignDeliveryPerson(eq(1L), eq(1L))).thenReturn(null);

        mockMvc.perform(patch("/api/orders/1/deliveryPerson/1"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void cancelOrder_shouldReturn204_whenCancelled() throws Exception {
        when(orderService.cancelOrder(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void cancelOrder_shouldReturn404_whenNotFound() throws Exception {
        when(orderService.cancelOrder(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/1"))
            .andExpect(status().isNotFound());
    }
}