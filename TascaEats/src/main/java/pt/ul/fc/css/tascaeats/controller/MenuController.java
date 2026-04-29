package pt.ul.fc.css.tascaeats.controller;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
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
    return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.badRequest().build();
  }

  @PutMapping("/{id}")
  @ApiOperation(value = "Update menu", notes = "Updates an existing menu.")
  public ResponseEntity<MenuResponseDto> updateMenu(
      @PathVariable Long id, @RequestBody @Valid MenuRequestDto request) {
    MenuResponseDto menu = menuHandler.updateMenu(id, request);
    return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.notFound().build();
  }

  @GetMapping("/{id}/")
  @ApiOperation(value = "Get menu", notes = "Returns a menu for a given id.")
  public ResponseEntity<MenuResponseDto> getMenu(@PathVariable Long id) {
    MenuResponseDto menu = menuHandler.getMenu(id);
    return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  @ApiOperation(value = "Delete menu", notes = "Removes an existing menu.")
  public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
    return menuHandler.deleteMenu(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }
}
