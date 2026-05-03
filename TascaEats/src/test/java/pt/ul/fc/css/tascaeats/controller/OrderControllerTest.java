package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.services.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private OrderService orderService;
  @Autowired private ObjectMapper objectMapper;

  private OrderResponseDto orderResponse() {
    return new OrderResponseDto(
        1L, OrderStatus.CREATED, LocalDateTime.now(), 9.99,
        1L, "Client", "123456789", List.of(1L), List.of(1L), List.of(),
        null, null, null, "Lisboa", "Rua A", "1000-001",
        null, null, null);
  }

  private OrderRequestDto orderRequest() {
    OrderRequestDto r = new OrderRequestDto();
    r.setClientId(1L); r.setProductIds(List.of(1L));
    r.setCity("Lisboa"); r.setStreet("Rua A"); r.setPostalCode("1000-001");
    return r;
  }

  // ─── POST /api/orders ────────────────────────────────────────────────────────

  @Test
  void createOrder_shouldReturn200_whenSuccessful() throws Exception {
    when(orderService.createOrder(any())).thenReturn(orderResponse());

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.status").value("CREATED"));
  }

  @Test
  void createOrder_shouldReturn400_whenBodyInvalid() throws Exception {
    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createOrder_shouldReturn404_whenClientNotFound() throws Exception {
    when(orderService.createOrder(any()))
        .thenThrow(new ResourceNotFoundException("User not found"));

    mockMvc.perform(post("/api/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(orderRequest())))
        .andExpect(status().isNotFound());
  }

  // ─── POST /api/orders/payment ────────────────────────────────────────────────

  @Test
  void processPayment_shouldReturn200_whenSuccessful() throws Exception {
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L); req.setPaymentMethod("Cash"); req.setAmount(10.0);
    OrderResponseDto paid = new OrderResponseDto(
        1L, OrderStatus.PAID, LocalDateTime.now(), 9.99,
        1L, "Client", "123456789", List.of(), List.of(), List.of(),
        null, null, null, "Lisboa", "Rua A", "1000-001", 10.0, null, 0.01);
    when(orderService.processPayment(any())).thenReturn(paid);

    mockMvc.perform(post("/api/orders/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PAID"));
  }

  @Test
  void processPayment_shouldReturn400_whenRequirementsNotMet() throws Exception {
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(1L); req.setPaymentMethod("Cash"); req.setAmount(0.01);
    when(orderService.processPayment(any()))
        .thenThrow(new RequirementsNotMetException("Amount insufficient"));

    mockMvc.perform(post("/api/orders/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void processPayment_shouldReturn404_whenOrderNotFound() throws Exception {
    PaymentRequestDto req = new PaymentRequestDto();
    req.setOrderId(99L); req.setPaymentMethod("Cash"); req.setAmount(10.0);
    when(orderService.processPayment(any()))
        .thenThrow(new ResourceNotFoundException("Order not found"));

    mockMvc.perform(post("/api/orders/payment")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());
  }

  // ─── PATCH /api/orders/{id}/status ───────────────────────────────────────────

  @Test
  void updateOrderStatus_shouldReturn200_whenSuccessful() throws Exception {
    OrderResponseDto paid = new OrderResponseDto(
        1L, OrderStatus.PAID, LocalDateTime.now(), 9.99,
        1L, "Client", "123456789", List.of(), List.of(), List.of(),
        null, null, null, "Lisboa", "Rua A", "1000-001", null, null, null);
    when(orderService.updateOrderStatus(eq(1L), any())).thenReturn(paid);

    mockMvc.perform(patch("/api/orders/1/status").param("status", "PAID"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("PAID"));
  }

  @Test
  void updateOrderStatus_shouldReturn404_whenOrderNotFound() throws Exception {
    when(orderService.updateOrderStatus(eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Order not found"));

    mockMvc.perform(patch("/api/orders/99/status").param("status", "PAID"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateOrderStatus_shouldReturn400_whenRequirementsNotMet() throws Exception {
    when(orderService.updateOrderStatus(eq(1L), any()))
        .thenThrow(new RequirementsNotMetException("Cannot transition"));

    mockMvc.perform(patch("/api/orders/1/status").param("status", "DELIVERED"))
        .andExpect(status().isBadRequest());
  }

  // ─── PATCH /api/orders/{id}/deliveryPerson/{deliveryPersonId} ─────────────────

  @Test
  void assignDeliveryPerson_shouldReturn200_whenSuccessful() throws Exception {
    when(orderService.assignDeliveryPerson(1L, 2L)).thenReturn(orderResponse());

    mockMvc.perform(patch("/api/orders/1/deliveryPerson/2"))
        .andExpect(status().isOk());
  }

  @Test
  void assignDeliveryPerson_shouldReturn404_whenOrderOrPersonNotFound() throws Exception {
    when(orderService.assignDeliveryPerson(1L, 99L))
        .thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(patch("/api/orders/1/deliveryPerson/99"))
        .andExpect(status().isNotFound());
  }

  @Test
  void assignDeliveryPerson_shouldReturn400_whenRequirementsNotMet() throws Exception {
    when(orderService.assignDeliveryPerson(1L, 2L))
        .thenThrow(new RequirementsNotMetException("Delivery person busy"));

    mockMvc.perform(patch("/api/orders/1/deliveryPerson/2"))
        .andExpect(status().isBadRequest());
  }

  // ─── DELETE /api/orders/{id} ─────────────────────────────────────────────────

  @Test
  void cancelOrder_shouldReturn204_whenCancelled() throws Exception {
    when(orderService.cancelOrder(1L)).thenReturn(true);
    mockMvc.perform(delete("/api/orders/1")).andExpect(status().isNoContent());
  }

  @Test
  void cancelOrder_shouldReturn404_whenNotFound() throws Exception {
    when(orderService.cancelOrder(99L)).thenReturn(false);
    mockMvc.perform(delete("/api/orders/99")).andExpect(status().isNotFound());
  }

  // ─── GET /api/orders ─────────────────────────────────────────────────────────

  @Test
  void getAllOrders_shouldReturn200_withList() throws Exception {
    when(orderService.getAllOrders()).thenReturn(List.of(orderResponse()));

    mockMvc.perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(1));
  }

  @Test
  void getAllOrders_shouldReturn200_withEmptyList() throws Exception {
    when(orderService.getAllOrders()).thenReturn(List.of());

    mockMvc.perform(get("/api/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
}