package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pt.ul.fc.css.tascaeats.dto.product.ProductItemResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponseDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewRequestDto;
import pt.ul.fc.css.tascaeats.dto.restaurant.ReviewResponseDto;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.services.RestaurantService;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private RestaurantService restaurantService;
  @Autowired private ObjectMapper objectMapper;

  private RestaurantResponseDto restaurantResponse() {
    return new RestaurantResponseDto(1L, "Cantina", "OPEN", "123456789",
        "Admin", "Rua A", "Lisboa", "1000-001", "11:00", "22:00", null, 0.0, 0);
  }
  private RestaurantRequestDto restaurantRequest() {
    RestaurantRequestDto r = new RestaurantRequestDto();
    r.setDesignation("Cantina"); r.setNif("123456789");
    r.setCity("Lisboa"); r.setStreet("Rua A"); r.setPostalCode("1000-001");
    r.setAdminId(1L); r.setOpenHours("11:00"); r.setCloseHours("22:00");
    return r;
  }
  private ProductItemResponseDto productItemResponse() {
    return new ProductItemResponseDto(1L, 1L, 1L, "Pizza", "Mussarela", "AVAILABLE", 9.99);
  }
  private ReviewResponseDto reviewResponse() {
    return new ReviewResponseDto(1L, 4, "Great!", 1L, 1L);
  }

  // ─── GET /api/restaurants/search ─────────────────────────────────────────────

  @Test
  void searchRestaurant_shouldReturn200_withResults() throws Exception {
    when(restaurantService.searchBy(any(), any(), any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(List.of(restaurantResponse()));

    mockMvc.perform(get("/api/restaurants/search").param("designation", "Cantina"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].designation").value("Cantina"));
  }

  @Test
  void searchRestaurant_shouldReturn200_withEmptyList() throws Exception {
    when(restaurantService.searchBy(any(), any(), any(), any(), any(), any(), any(), any(), any()))
        .thenReturn(List.of());

    mockMvc.perform(get("/api/restaurants/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  // ─── GET /api/restaurants/{id}/products/search ───────────────────────────────

  @Test
  void searchProduct_shouldReturn200_withResults() throws Exception {
    when(restaurantService.searchProductBy(any(), any(), any(), any(), any(), any()))
        .thenReturn(List.of(productItemResponse()));

    mockMvc.perform(get("/api/restaurants/1/products/search").param("designation", "Pizza"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].designation").value("Pizza"));
  }

  @Test
  void searchProduct_shouldReturn200_withEmptyList() throws Exception {
    when(restaurantService.searchProductBy(any(), any(), any(), any(), any(), any()))
        .thenReturn(List.of());

    mockMvc.perform(get("/api/restaurants/1/products/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }

  // ─── POST /api/restaurants ───────────────────────────────────────────────────

  @Test
  void createRestaurant_shouldReturn200_whenSuccessful() throws Exception {
    when(restaurantService.createRestaurant(any())).thenReturn(restaurantResponse());

    mockMvc.perform(post("/api/restaurants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(restaurantRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.designation").value("Cantina"))
        .andExpect(jsonPath("$.status").value("OPEN"));
  }

  @Test
  void createRestaurant_shouldReturn400_whenBodyInvalid() throws Exception {
    mockMvc.perform(post("/api/restaurants")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{}"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createRestaurant_shouldReturn404_whenAdminNotFound() throws Exception {
    when(restaurantService.createRestaurant(any()))
        .thenThrow(new ResourceNotFoundException("User not found"));

    mockMvc.perform(post("/api/restaurants")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(restaurantRequest())))
        .andExpect(status().isNotFound());
  }

  // ─── PATCH /api/restaurants/{id}/status ──────────────────────────────────────

  @Test
  void updateStatus_shouldReturn200_whenSuccessful() throws Exception {
    RestaurantResponseDto closed = new RestaurantResponseDto(1L, "Cantina", "CLOSED", "123456789",
        "Admin", "Rua A", "Lisboa", "1000-001", "11:00", "22:00", null, 0.0, 0);
    when(restaurantService.changeStatus(eq(1L), any())).thenReturn(closed);

    mockMvc.perform(patch("/api/restaurants/1/status").param("status", "CLOSED"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("CLOSED"));
  }

  @Test
  void updateStatus_shouldReturn404_whenRestaurantNotFound() throws Exception {
    when(restaurantService.changeStatus(eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Restaurant not found"));

    mockMvc.perform(patch("/api/restaurants/99/status").param("status", "CLOSED"))
        .andExpect(status().isNotFound());
  }

  // ─── PUT /api/restaurants/{id}/products/{productId} ──────────────────────────

  @Test
  void updateProductStatus_shouldReturn200_whenSuccessful() throws Exception {
    ProductItemResponseDto soldOut = new ProductItemResponseDto(1L, 1L, 1L, "Pizza", "Mussarela", "SOLD_OUT", 9.99);
    when(restaurantService.updateProductStatus(eq(1L), eq(1L), eq(ProductStatus.SOLD_OUT)))
        .thenReturn(soldOut);

    mockMvc.perform(put("/api/restaurants/1/products/1").param("status", "SOLD_OUT"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.availability").value("SOLD_OUT"));
  }

  @Test
  void updateProductStatus_shouldReturn404_whenProductNotFound() throws Exception {
    when(restaurantService.updateProductStatus(eq(1L), eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Product not found"));

    mockMvc.perform(put("/api/restaurants/1/products/99").param("status", "SOLD_OUT"))
        .andExpect(status().isNotFound());
  }

  // ─── DELETE /api/restaurants/{id}/products/{productId} ───────────────────────

  @Test
  void deleteProductItem_shouldReturn204_whenDeleted() throws Exception {
    when(restaurantService.deleteProductItem(1L, 1L)).thenReturn(true);
    mockMvc.perform(delete("/api/restaurants/1/products/1")).andExpect(status().isNoContent());
  }

  @Test
  void deleteProductItem_shouldReturn404_whenNotFound() throws Exception {
    when(restaurantService.deleteProductItem(1L, 99L)).thenReturn(false);
    mockMvc.perform(delete("/api/restaurants/1/products/99")).andExpect(status().isNotFound());
  }

  // ─── POST /api/restaurants/{id}/reviews ──────────────────────────────────────

  @Test
  void addReview_shouldReturn200_whenSuccessful() throws Exception {
    ReviewRequestDto req = new ReviewRequestDto();
    req.setRating(4); req.setComment("Great!"); req.setClientId(1L);
    when(restaurantService.addReview(eq(1L), any())).thenReturn(reviewResponse());

    mockMvc.perform(post("/api/restaurants/1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.rating").value(4))
        .andExpect(jsonPath("$.comment").value("Great!"));
  }

  @Test
  void addReview_shouldReturn404_whenRestaurantNotFound() throws Exception {
    ReviewRequestDto req = new ReviewRequestDto();
    req.setRating(4); req.setComment("Good"); req.setClientId(1L);
    when(restaurantService.addReview(eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Restaurant not found"));

    mockMvc.perform(post("/api/restaurants/99/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isNotFound());
  }

  @Test
  void addReview_shouldReturn400_whenClientNeverOrdered() throws Exception {
    ReviewRequestDto req = new ReviewRequestDto();
    req.setRating(4); req.setComment("Good"); req.setClientId(1L);
    when(restaurantService.addReview(eq(1L), any()))
        .thenThrow(new RequirementsNotMetException("Cannot review"));

    mockMvc.perform(post("/api/restaurants/1/reviews")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(req)))
        .andExpect(status().isBadRequest());
  }

  // ─── GET /api/restaurants/{id}/reviews ───────────────────────────────────────

  @Test
  void getReviews_shouldReturn200_withResults() throws Exception {
    when(restaurantService.getReviews(1L)).thenReturn(List.of(reviewResponse()));

    mockMvc.perform(get("/api/restaurants/1/reviews"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].rating").value(4));
  }

  @Test
  void getReviews_shouldReturn404_whenRestaurantNotFound() throws Exception {
    when(restaurantService.getReviews(99L)).thenThrow(new ResourceNotFoundException("Not found"));

    mockMvc.perform(get("/api/restaurants/99/reviews")).andExpect(status().isNotFound());
  }

  // ─── PATCH /api/restaurants/{id}/menu/{menuId} ───────────────────────────────

  @Test
  void assignMenu_shouldReturn200_whenSuccessful() throws Exception {
    RestaurantResponseDto withMenu = new RestaurantResponseDto(1L, "Cantina", "OPEN", "123456789",
        "Admin", "Rua A", "Lisboa", "1000-001", "11:00", "22:00", 1L, 0.0, 0);
    when(restaurantService.assignMenu(1L, 1L)).thenReturn(withMenu);

    mockMvc.perform(patch("/api/restaurants/1/menu/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.menuId").value(1));
  }

  @Test
  void assignMenu_shouldReturn404_whenRestaurantOrMenuNotFound() throws Exception {
    when(restaurantService.assignMenu(1L, 99L))
        .thenThrow(new ResourceNotFoundException("Menu not found"));

    mockMvc.perform(patch("/api/restaurants/1/menu/99"))
        .andExpect(status().isNotFound());
  }
}