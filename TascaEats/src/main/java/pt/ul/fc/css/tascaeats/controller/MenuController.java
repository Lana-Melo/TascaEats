package pt.ul.fc.css.tascaeats.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto;
import pt.ul.fc.css.tascaeats.services.MenuService;

@RestController
@RequestMapping("/api/menus")
public class MenuController {

  private final MenuService menuHandler;

  public MenuController(MenuService menuService) {
    this.menuHandler = menuService;
  }

  @PostMapping
  @ApiOperation(value = "Create menu", notes = "Creates a new menu.")
  public ResponseEntity<MenuResponseDto> createMenu(@RequestBody @Valid MenuRequestDto request) {
    MenuResponseDto menu = menuHandler.createMenu(request);
    return ResponseEntity.ok(menu);
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Update menu", notes = "Updates an existing menu.")
  public ResponseEntity<MenuResponseDto> updateMenu(
      @PathVariable Long id, @RequestBody @Valid MenuRequestDto request) {
    MenuResponseDto menu = menuHandler.updateMenu(id, request);
    return ResponseEntity.ok(menu);
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Get menu", notes = "Returns a menu for a given id.")
  public ResponseEntity<MenuResponseDto> getMenu(@PathVariable Long id) {
    MenuResponseDto menu = menuHandler.getMenu(id);
    return ResponseEntity.ok(menu);
  }

  @GetMapping("/search")
  @ApiOperation(
      value = "Search menus",
      notes = "Returns a list of menus filtered by a given category.")
  public ResponseEntity<List<MenuResponseDto>> searchMenu(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String productNumber,
      @RequestParam(required = false) String average) {
    List<MenuResponseDto> menu = menuHandler.searchBy(name, productNumber, average);
    return ResponseEntity.ok(menu);
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete menu", notes = "Removes an existing menu.")
  public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
    return menuHandler.deleteMenu(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/products")
  @ApiOperation(value = "Add product to menu", notes = "Adds a new product")
  public ResponseEntity<ProductResponseDto> addProduct(
      @PathVariable Long id, @RequestBody @Valid ProductRequestDto request) {
    ProductResponseDto product = menuHandler.addProduct(id, request);
    return ResponseEntity.ok(product);
  }

  @PutMapping("/{id}/products/{productId}")
  @ApiOperation(value = "Update product", notes = "Updates an existing product")
  public ResponseEntity<ProductResponseDto> updateProduct(
      @PathVariable Long id,
      @PathVariable Long productId,
      @RequestBody @Valid ProductRequestDto request) {
    ProductResponseDto product = menuHandler.updateProduct(id, productId, request);
    return ResponseEntity.ok(product);
  }

  @DeleteMapping("/{id}/products/{productId}")
  @ApiOperation(value = "Delete product", notes = "Deletes an existing product")
  public ResponseEntity<Void> deleteProductItem(
      @PathVariable Long id, @PathVariable Long productId) {
    return menuHandler.deleteProduct(id, productId)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @GetMapping("/products/search")
  @ApiOperation(
      value = "Search products",
      notes = "Returns a list of all products filtered a given category.")
  public ResponseEntity<List<ProductResponseDto>> searchProduct(
      @RequestParam(required = false) Long id,
      @RequestParam(required = false) String designation,
      @RequestParam(required = false) String description) {
    List<ProductResponseDto> products = menuHandler.searchProductBy(id, designation, description);
    return ResponseEntity.ok(products);
  }
}
