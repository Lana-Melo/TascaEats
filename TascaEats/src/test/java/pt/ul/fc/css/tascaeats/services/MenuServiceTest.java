package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
 
  @Mock private MenuRepository menuRepository;
  @Mock private RestaurantRepository restaurantRepository;
  @Mock private ProductRepository productRepository;
  @Mock private ProductItemRepository productItemRepository;
  @Mock private OrderRepository orderRepository;
 
  @InjectMocks private MenuService menuService;
 
  // ─── helpers ────────────────────────────────────────────────────────────────
 
  private MenuRequestDto menuRequest() {
    MenuRequestDto r = new MenuRequestDto();
    r.setName("Menu Italiano");
    r.setCuisine("Italian");
    r.setDescription("Melhor massa da cidade");
    return r;
  }
 
  private Menu menu() {
    return new Menu("Menu Italiano", "Italian", "Melhor massa da cidade");
  }
 
  private ProductRequestDto productRequest() {
    ProductRequestDto r = new ProductRequestDto();
    r.setDesignation("Pizza");
    r.setDescription("Mussarela");
    r.setPrice(9.99);
    return r;
  }
 
  // ─── createMenu ─────────────────────────────────────────────────────────────
 
  @Test
  void createMenu_shouldReturnMenuResponse() {
    Menu saved = menu();
    when(menuRepository.save(any(Menu.class))).thenReturn(saved);
 
    MenuResponseDto result = menuService.createMenu(menuRequest());
 
    assertNotNull(result);
    assertEquals("Menu Italiano", result.name());
    assertEquals("Italian", result.cuisine());
    assertEquals("Melhor massa da cidade", result.description());
    verify(menuRepository).save(any(Menu.class));
  }
 
  // ─── updateMenu ─────────────────────────────────────────────────────────────
 
  @Test
  void updateMenu_shouldReturnUpdatedMenu_whenFound() {
    Menu existing = menu();
    when(menuRepository.findById(1L)).thenReturn(Optional.of(existing));
    when(menuRepository.save(any(Menu.class))).thenAnswer(inv -> inv.getArgument(0));
 
    MenuRequestDto update = new MenuRequestDto();
    update.setName("Novo Nome");
    update.setCuisine("French");
    update.setDescription("Nova desc");
 
    MenuResponseDto result = menuService.updateMenu(1L, update);
 
    assertNotNull(result);
    assertEquals("Novo Nome", result.name());
    assertEquals("French", result.cuisine());
  }
 
  @Test
  void updateMenu_shouldThrow_whenNotFound() {
    when(menuRepository.findById(99L)).thenReturn(Optional.empty());
 
    assertThrows(ResourceNotFoundException.class, () -> menuService.updateMenu(99L, menuRequest()));
  }
 
  // ─── getMenu ────────────────────────────────────────────────────────────────
 
  @Test
  void getMenu_shouldReturnMenu_whenFound() {
    when(menuRepository.findById(1L)).thenReturn(Optional.of(menu()));
 
    MenuResponseDto result = menuService.getMenu(1L);
 
    assertNotNull(result);
    assertEquals("Menu Italiano", result.name());
  }
 
  @Test
  void getMenu_shouldThrow_whenNotFound() {
    when(menuRepository.findById(99L)).thenReturn(Optional.empty());
 
    assertThrows(ResourceNotFoundException.class, () -> menuService.getMenu(99L));
  }
 
  // ─── deleteMenu ─────────────────────────────────────────────────────────────
 
  @Test
  void deleteMenu_shouldReturnFalse_whenNotFound() {
    when(menuRepository.existsById(99L)).thenReturn(false);
 
    boolean result = menuService.deleteMenu(99L);
 
    assertFalse(result);
    verify(menuRepository, never()).deleteById(any());
  }
 
  @Test
  void deleteMenu_shouldDeletePermanently_whenNotLinkedToRestaurant() {
    when(menuRepository.existsById(1L)).thenReturn(true);
    when(restaurantRepository.existsByMenuId(1L)).thenReturn(false);
 
    boolean result = menuService.deleteMenu(1L);
 
    assertTrue(result);
    verify(menuRepository).deleteById(1L);
  }
 
  @Test
  void deleteMenu_shouldSoftDelete_whenLinkedToRestaurant() {
    Menu m = menu();
    when(menuRepository.existsById(1L)).thenReturn(true);
    when(restaurantRepository.existsByMenuId(1L)).thenReturn(true);
    when(menuRepository.findById(1L)).thenReturn(Optional.of(m));
    when(menuRepository.save(any(Menu.class))).thenReturn(m);
 
    boolean result = menuService.deleteMenu(1L);
 
    assertTrue(result);
    assertNotNull(m.getDeletedAt()); // soft-delete timestamp was set
    verify(menuRepository, never()).deleteById(any());
  }
 
  // ─── addProduct ─────────────────────────────────────────────────────────────
 
  @Test
  void addProduct_shouldReturnProductResponse_whenMenuFound() {
    Menu m = menu();
    Product p = new Product(9.99, "Mussarela", "Pizza", m);
    when(menuRepository.findById(1L)).thenReturn(Optional.of(m));
    when(productRepository.save(any(Product.class))).thenReturn(p);
 
    ProductResponseDto result = menuService.addProduct(1L, productRequest());
 
    assertNotNull(result);
    assertEquals("Pizza", result.designation());
    assertEquals(9.99, result.price());
  }
 
  @Test
  void addProduct_shouldThrow_whenMenuNotFound() {
    when(menuRepository.findById(99L)).thenReturn(Optional.empty());
 
    assertThrows(ResourceNotFoundException.class, () -> menuService.addProduct(99L, productRequest()));
  }
 
  // ─── updateProduct ───────────────────────────────────────────────────────────
 
  @Test
  void updateProduct_shouldReturnUpdated_whenFound() {
    Menu m = menu();
    Product p = new Product(9.99, "Mussarela", "Pizza", m);
    when(productRepository.findById(1L)).thenReturn(Optional.of(p));
    when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
 
    ProductRequestDto update = new ProductRequestDto();
    update.setDesignation("Calzone");
    update.setDescription("Recheado");
    update.setPrice(12.0);
 
    ProductResponseDto result = menuService.updateProduct(1L, 1L, update);
 
    assertNotNull(result);
    assertEquals("Calzone", result.designation());
    assertEquals(12.0, result.price());
  }
 
  @Test
  void updateProduct_shouldThrow_whenNotFound() {
    when(productRepository.findById(99L)).thenReturn(Optional.empty());
 
    assertThrows(ResourceNotFoundException.class,
        () -> menuService.updateProduct(1L, 99L, productRequest()));
  }
 
  // ─── deleteProduct ───────────────────────────────────────────────────────────
 
  @Test
  void deleteProduct_shouldReturnFalse_whenProductNotFromMenu() {
    when(productRepository.existsByIdAndMenuId(99L, 1L)).thenReturn(false);
 
    boolean result = menuService.deleteProduct(1L, 99L);
 
    assertFalse(result);
    verify(productRepository, never()).deleteById(any());
  }
 
  @Test
  void deleteProduct_shouldDeletePermanently_whenMenuNotLinkedToRestaurant() {
    when(productRepository.existsByIdAndMenuId(1L, 1L)).thenReturn(true);
    when(productItemRepository.findAllByProductId(1L)).thenReturn(List.of());
 
    boolean result = menuService.deleteProduct(1L, 1L);
 
    assertTrue(result);
    verify(productRepository).deleteById(1L);
  }
 
  @Test
  void deleteProduct_shouldReturnFalse_whenHasActiveOrders() throws Exception {
    // Create a product item with an active order
    ProductItem pi = new ProductItem(new Product(9.99, "desc", "Pizza", menu()), null);
    // Set ID using reflection since this is a test
    java.lang.reflect.Field idField = pi.getClass().getDeclaredField("id");
    idField.setAccessible(true);
    idField.set(pi, 1L);
    when(productRepository.existsByIdAndMenuId(1L, 1L)).thenReturn(true);
    when(productItemRepository.findAllByProductId(1L)).thenReturn(List.of(pi));
    when(orderRepository.existsByProductItemsIdAndStatusNotIn(pi.getId(), List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))).thenReturn(true);

    boolean result = menuService.deleteProduct(1L, 1L);

    assertFalse(result);
    verify(productRepository, never()).deleteById(any());
  }

  @Test
  void deleteProduct_shouldSoftDelete_whenHasOrderHistory() {
    Menu m = menu();
    Product p = new Product(9.99, "desc", "Pizza", m);
    ProductItem pi = new ProductItem(p, null);
    when(productRepository.existsByIdAndMenuId(1L, 1L)).thenReturn(true);
    when(productItemRepository.findAllByProductId(1L)).thenReturn(List.of(pi));
    when(orderRepository.existsByProductItemsIdAndStatusNotIn(any(), any())).thenReturn(false);
    when(orderRepository.existsByProductItemsId(any())).thenReturn(true);
    when(productRepository.findById(1L)).thenReturn(Optional.of(p));
    when(productRepository.save(any(Product.class))).thenReturn(p);
    when(productItemRepository.save(any(ProductItem.class))).thenReturn(pi);

    boolean result = menuService.deleteProduct(1L, 1L);

    assertTrue(result);
    verify(productRepository, never()).deleteById(any());
    assertNotNull(pi.getDeletedAt());
  }

  @Test
  void searchBy_shouldReturnMenus_withFilters() {
    Menu m = menu();
    when(menuRepository.findAll(any(Specification.class))).thenReturn(List.of(m));

    var result = menuService.searchBy("italiano", "0", "20.0");

    assertEquals(1, result.size());
    assertEquals("Menu Italiano", result.get(0).name());
  }

  @Test
  void searchProductBy_shouldFilterByDescription() {
    Menu m = menu();
    Product p = new Product(5.0, "Chocolate", "Cake", m);
    when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(p));

    var result = menuService.searchProductBy(null, null, "chocolate");

    assertEquals(1, result.size());
    assertEquals("Cake", result.get(0).designation());
  }

  @Test
  void searchBy_shouldReturnMenus_whenNameMatches() {
    Menu m = menu();
    when(menuRepository.findAll(any(Specification.class))).thenReturn(List.of(m));

    var result = menuService.searchBy("menu", "0", "20.0");

    assertEquals(1, result.size());
    assertEquals("Menu Italiano", result.get(0).name());
  }

  // ─── searchProductBy ─────────────────────────────────────────────────────────
 
  @Test
  void searchProductBy_shouldReturnAll_whenNoFilters() {
    Menu m = menu();
    List<Product> products = List.of(new Product(5.0, "desc", "item", m));
    when(productRepository.findAll(any(Specification.class))).thenReturn(products);
 
    List<ProductResponseDto> result = menuService.searchProductBy(null, null, null);
 
    assertEquals(1, result.size());
  }
 
  @Test
  void searchProductBy_shouldFilterByDesignation() {
    Menu m = menu();
    Product p = new Product(5.0, "desc", "Pizza", m);
    when(productRepository.findAll(any(Specification.class))).thenReturn(List.of(p));
 
    List<ProductResponseDto> result = menuService.searchProductBy(null, "pizza", null);
 
    assertEquals(1, result.size());
    assertEquals("Pizza", result.get(0).designation());
  }
 
  @Test
  void searchProductBy_shouldReturnEmpty_whenNoMatch() {
    when(productRepository.findAll(any(Specification.class))).thenReturn(List.of());
 
    List<ProductResponseDto> result = menuService.searchProductBy(null, "xyz", null);
 
    assertTrue(result.isEmpty());
  }
}
