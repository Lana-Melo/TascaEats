package pt.ul.fc.css.tascaeats.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import jakarta.validation.Valid;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequest;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponse;
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
    public ResponseEntity<MenuResponse> createMenu(@RequestBody @Valid MenuRequest request) {
        MenuResponse menu = menuHandler.createMenu(request);
        return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Update menu", notes = "Updates an existing menu.")
    public ResponseEntity<MenuResponse> updateMenu(@PathVariable Long id, @RequestBody @Valid MenuRequest request) {
        MenuResponse menu = menuHandler.updateMenu(id, request);
        return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/")
    @ApiOperation(value = "Get menu", notes = "Returns a menu for a given id.")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long id) {  
        MenuResponse menu = menuHandler.getMenu(id);
        return menu != null ? ResponseEntity.ok(menu) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete menu", notes = "Removes an existing menu.")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        return menuHandler.deleteMenu(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
