package pt.ul.fc.css.tascaeats.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.services.MenuService;
 
@WebMvcTest(MenuController.class)
class MenuControllerTest {
 
  @Autowired private MockMvc mockMvc;
  @MockBean private MenuService menuService;
  @Autowired private ObjectMapper objectMapper;
 
  // ─── helpers ────────────────────────────────────────────────────────────────
 
  private MenuRequestDto menuRequest() {
    MenuRequestDto r = new MenuRequestDto();
    r.setName("Menu Italiano");
    r.setCuisine("Italian");
    r.setDescription("Melhor massa");
    return r;
  }
 
  private MenuResponseDto menuResponse() {
    return new MenuResponseDto(1L, "Menu Italiano", "Italian", "Melhor massa", List.of());
  }
 
  private ProductRequestDto productRequest() {
    ProductRequestDto r = new ProductRequestDto();
    r.setDesignation("Pizza");
    r.setDescription("Mussarela");
    r.setPrice(9.99);
    return r;
  }
 
  private ProductResponseDto productResponse() {
    return new ProductResponseDto(1L, 9.99, "Mussarela", "Pizza");
  }
 
  // ─── POST /api/menus ─────────────────────────────────────────────────────────
 
  @Test
  void createMenu_shouldReturn200_whenServiceSucceeds() throws Exception {
    when(menuService.createMenu(any())).thenReturn(menuResponse());
 
    mockMvc.perform(post("/api/menus")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Menu Italiano"))
        .andExpect(jsonPath("$.cuisine").value("Italian"));
  }
 
  // ─── PUT /api/menus/{id} ──────────────────────────────────────────────────────
 
  @Test
  void updateMenu_shouldReturn200_whenFound() throws Exception {
    when(menuService.updateMenu(eq(1L), any())).thenReturn(menuResponse());
 
    mockMvc.perform(put("/api/menus/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Menu Italiano"));
  }
 
  @Test
  void updateMenu_shouldReturn404_whenNotFound() throws Exception {
    when(menuService.updateMenu(eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Menu not found"));
 
    mockMvc.perform(put("/api/menus/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(menuRequest())))
        .andExpect(status().isNotFound());
  }
 
  // ─── GET /api/menus/{id} ──────────────────────────────────────────────────────
 
  @Test
  void getMenu_shouldReturn200_whenFound() throws Exception {
    when(menuService.getMenu(1L)).thenReturn(menuResponse());
 
    mockMvc.perform(get("/api/menus/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));
  }
 
  @Test
  void getMenu_shouldReturn404_whenNotFound() throws Exception {
    when(menuService.getMenu(99L)).thenThrow(new ResourceNotFoundException("Menu not found"));
 
    mockMvc.perform(get("/api/menus/99")).andExpect(status().isNotFound());
  }
 
  // ─── GET /api/menus/search ────────────────────────────────────────────────────
 
  @Test
  void searchMenu_shouldReturn200_withResults() throws Exception {
    when(menuService.searchBy(any(), any(), any())).thenReturn(List.of(menuResponse()));
 
    mockMvc.perform(get("/api/menus/search").param("name", "Italiano"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name").value("Menu Italiano"));
  }
 
  @Test
  void searchMenu_shouldReturn200_withEmptyList() throws Exception {
    when(menuService.searchBy(any(), any(), any())).thenReturn(List.of());
 
    mockMvc.perform(get("/api/menus/search")).andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
 
  // ─── DELETE /api/menus/{id} ───────────────────────────────────────────────────
 
  @Test
  void deleteMenu_shouldReturn204_whenDeleted() throws Exception {
    when(menuService.deleteMenu(1L)).thenReturn(true);
 
    mockMvc.perform(delete("/api/menus/1")).andExpect(status().isNoContent());
  }
 
  @Test
  void deleteMenu_shouldReturn404_whenNotFound() throws Exception {
    when(menuService.deleteMenu(99L)).thenReturn(false);
 
    mockMvc.perform(delete("/api/menus/99")).andExpect(status().isNotFound());
  }
 
  // ─── POST /api/menus/{id}/products ───────────────────────────────────────────
 
  @Test
  void addProduct_shouldReturn200_whenMenuFound() throws Exception {
    when(menuService.addProduct(eq(1L), any())).thenReturn(productResponse());
 
    mockMvc.perform(post("/api/menus/1/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.designation").value("Pizza"));
  }
 
  @Test
  void addProduct_shouldReturn404_whenMenuNotFound() throws Exception {
    when(menuService.addProduct(eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Menu not found"));
 
    mockMvc.perform(post("/api/menus/99/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest())))
        .andExpect(status().isNotFound());
  }
 
  // ─── PUT /api/menus/{id}/products/{productId} ────────────────────────────────
 
  @Test
  void updateProduct_shouldReturn200_whenFound() throws Exception {
    when(menuService.updateProduct(eq(1L), eq(1L), any())).thenReturn(productResponse());
 
    mockMvc.perform(put("/api/menus/1/products/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.designation").value("Pizza"));
  }
 
  @Test
  void updateProduct_shouldReturn404_whenNotFound() throws Exception {
    when(menuService.updateProduct(eq(1L), eq(99L), any()))
        .thenThrow(new ResourceNotFoundException("Product not found"));
 
    mockMvc.perform(put("/api/menus/1/products/99")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productRequest())))
        .andExpect(status().isNotFound());
  }
 
  // ─── DELETE /api/menus/{id}/products/{productId} ─────────────────────────────
 
  @Test
  void deleteProduct_shouldReturn204_whenDeleted() throws Exception {
    when(menuService.deleteProduct(1L, 1L)).thenReturn(true);
 
    mockMvc.perform(delete("/api/menus/1/products/1")).andExpect(status().isNoContent());
  }
 
  @Test
  void deleteProduct_shouldReturn404_whenNotFound() throws Exception {
    when(menuService.deleteProduct(1L, 99L)).thenReturn(false);
 
    mockMvc.perform(delete("/api/menus/1/products/99")).andExpect(status().isNotFound());
  }
 
  // ─── GET /api/menus/products/search ──────────────────────────────────────────
 
  @Test
  void searchProduct_shouldReturn200_withResults() throws Exception {
    when(menuService.searchProductBy(any(), any(), any())).thenReturn(List.of(productResponse()));
 
    mockMvc.perform(get("/api/menus/products/search").param("designation", "Pizza"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].designation").value("Pizza"));
  }
 
  @Test
  void searchProduct_shouldReturn200_withEmptyList() throws Exception {
    when(menuService.searchProductBy(any(), any(), any())).thenReturn(List.of());
 
    mockMvc.perform(get("/api/menus/products/search"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isEmpty());
  }
}
