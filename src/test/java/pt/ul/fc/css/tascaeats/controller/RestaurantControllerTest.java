package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.tascaeats.dto.product.ProductRequest;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponse;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantRequest;
import pt.ul.fc.css.tascaeats.dto.restaurant.RestaurantResponse;
import pt.ul.fc.css.tascaeats.services.RestaurantService;

@WebMvcTest(RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantService restaurantService;

    @Test
    void searchRestaurant_shouldReturn200_whenRestaurantsFound() throws Exception {
        RestaurantResponse restaurant = new RestaurantResponse(1L, "Cantina Velha", "OPEN", "123456789", "Admin", "Rua A", "Lisboa", "1000-001", 1L);
        when(restaurantService.searchByDesignationOrCity("Cantina Velha", "Lisboa")).thenReturn(List.of(restaurant));

        mockMvc.perform(get("/api/restaurants/search")
                .param("designation", "Cantina Velha")
                .param("city", "Lisboa"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].designation").value("Cantina Velha"))
            .andExpect(jsonPath("$[0].city").value("Lisboa"));
    }

    @Test
    void searchRestaurant_shouldReturn404_whenServiceReturnsNull() throws Exception {
        when(restaurantService.searchByDesignationOrCity(any(), any())).thenReturn(null);
        
        mockMvc.perform(get("/api/restaurants/search"))
            .andExpect(status().isNotFound());
    }

    @Test
    void createRestaurant_shouldReturn200_whenSuccessful() throws Exception {
        RestaurantRequest request = new RestaurantRequest();
        request.setDesignation("Cantina Velha");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");
        request.setAdminId(1L);

        RestaurantResponse response = new RestaurantResponse(1L, "Cantina Velha", "OPEN", "123456789", "Admin", "Rua A", "Lisboa", "1000-001", 1L);
        when(restaurantService.createRestaurant(any())).thenReturn(response);

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.designation").value("Cantina Velha"))
            .andExpect(jsonPath("$.city").value("Lisboa"));
    }

    @Test
    void createRestaurant_shouldReturn400_whenServiceFails() throws Exception {
        RestaurantRequest request = new RestaurantRequest();
        request.setDesignation("Cantina Velha");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");
        request.setAdminId(1L);

        when(restaurantService.createRestaurant(any())).thenReturn(null);

        mockMvc.perform(post("/api/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_shouldReturn200_whenSuccessful() throws Exception {
        RestaurantResponse response = new RestaurantResponse(1L, "Cantina Velha", "CLOSED", "123456789", "Admin", "Rua A", "Lisboa", "1000-001", 1L);
        when(restaurantService.changeStatus(eq(1L), eq("CLOSED"))).thenReturn(response);

        mockMvc.perform(patch("/api/restaurants/1/status")
                .param("status", "CLOSED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CLOSED"));
    }

    @Test
    void updateStatus_shouldReturn404_whenNotFound() throws Exception {
        when(restaurantService.changeStatus(eq(99L), eq("OPEN"))).thenReturn(null);

        mockMvc.perform(patch("/api/restaurants/99/status")
                .param("status", "OPEN"))
            .andExpect(status().isNotFound());
    }

    @Test
    void addProduct_shouldReturn200_whenSuccessful() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Pure");
        request.setDescription("Maravilhoso pure de batata da cantina");
        request.setPrice(9.99);
        request.setAvailability("AVAILABLE");

        ProductResponse response = new ProductResponse(1L, 9.99, "Maravilhoso pure de batata da cantina", "Pure", "AVAILABLE");
        when(restaurantService.addProduct(eq(1L), any())).thenReturn(response);

        mockMvc.perform(post("/api/restaurants/1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.designation").value("Pure"))
            .andExpect(jsonPath("$.availability").value("AVAILABLE"));
    }

    @Test
    void addProduct_shouldReturn404_whenRestaurantNotFound() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Agua de batata");
        request.setDescription("Tenntativa pure de batata da cantina");
        request.setPrice(9.99);
        request.setAvailability("AVAILABLE");

        when(restaurantService.addProduct(eq(99L), any())).thenReturn(null);

        mockMvc.perform(post("/api/restaurants/99/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void updateProduct_shouldReturn200_whenSuccessful() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Agua de batata");
        request.setDescription("Tenntativa pure de batata da cantina");
        request.setPrice(12.99);
        request.setAvailability("AVAILABLE");

        ProductResponse response = new ProductResponse(1L, 12.99, "Tenntativa pure de batata da cantina", "Agua de batata", "AVAILABLE");
        when(restaurantService.updateProduct(eq(1L), eq(1L), any())).thenReturn(response);

        mockMvc.perform(put("/api/restaurants/1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.designation").value("Agua de batata"));
    }

    @Test
    void updateProduct_shouldReturn404_whenNotFound() throws Exception {
        ProductRequest request = new ProductRequest();
        request.setDesignation("Agua de batata");
        request.setDescription("Tenntativa pure de batata da cantina");
        request.setPrice(12.99);
        request.setAvailability("AVAILABLE");

        when(restaurantService.updateProduct(eq(1L), eq(99L), any())).thenReturn(null);

        mockMvc.perform(put("/api/restaurants/1/products/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void deleteProduct_shouldReturn204_whenDeleted() throws Exception {
        when(restaurantService.deleteProduct(1L, 1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/1/products/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void deleteProduct_shouldReturn404_whenNotFound() throws Exception {
        when(restaurantService.deleteProduct(1L, 99L)).thenReturn(false);

        mockMvc.perform(delete("/api/restaurants/1/products/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void getProducts_shouldReturn200_whenProductsFound() throws Exception {
        ProductResponse product = new ProductResponse(1L, 9.99, "Maravilhoso pure de batata da cantina", "Pure", "AVAILABLE");
        when(restaurantService.getProducts(1L)).thenReturn(List.of(product));

        mockMvc.perform(get("/api/restaurants/1/products"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].designation").value("Pure"));
    }

    @Test
    void getProducts_shouldReturn404_whenServiceReturnsNull() throws Exception {
        when(restaurantService.getProducts(anyLong())).thenReturn(null);
        
        mockMvc.perform(get("/api/restaurants/1/products"))
            .andExpect(status().isNotFound());
    }
}