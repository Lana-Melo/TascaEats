package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;

@Service
public class MenuService {

  private final MenuRepository menuRepository;
  private final RestaurantRepository restaurantRepository;
  private final ProductRepository productRepository;

  public MenuService(
      MenuRepository menuRepository,
      RestaurantRepository restaurantRepository,
      ProductRepository productRepository) {
    this.menuRepository = menuRepository;
    this.restaurantRepository = restaurantRepository;
    this.productRepository = productRepository;
  }

  /////////////////////////////////////////////////////////////
  ///  POST MENU METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public MenuResponseDto createMenu(MenuRequestDto request) {
    ArrayList<Product> products = new ArrayList<>();
    for (Long id : request.getProductIds()) {
      Optional<Product> prodOpt = productRepository.findById(id);
      if (prodOpt.isEmpty()) {
        return null;
      }

      products.add(prodOpt.get());
    }
    Menu menu =
        new Menu(request.getName(), request.getCuisine(), request.getDescription(), products);
    Menu saved = menuRepository.save(menu);

    return mapMenuToMenuResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  PUT MENU METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public MenuResponseDto updateMenu(Long id, MenuRequestDto request) {
    Optional<Menu> menuOpt = menuRepository.findById(id);
    if (menuOpt.isEmpty()) {
      return null;
    }

    Menu menu = (Menu) menuOpt.get();
    menu.setName(request.getName());
    menu.setCuisine(request.getCuisine());
    menu.setDescription(request.getDescription());

    ArrayList<Product> products = new ArrayList<>();
    for (Long pid : request.getProductIds()) {
      Optional<Product> prodOpt = productRepository.findById(pid);
      if (prodOpt.isEmpty()) {
        return null;
      }

      products.add(prodOpt.get());
    }
    menu.setProducts(products);
    Menu saved = menuRepository.save(menu);
    return mapMenuToMenuResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  GET MENU METHODS
  /////////////////////////////////////////////////////////////

  public MenuResponseDto getMenu(Long id) {
    Optional<Menu> menuOpt = menuRepository.findById(id);
    return menuOpt.map(this::mapMenuToMenuResponse).orElse(null);
  }

  public List<MenuResponseDto> searchBy(String name, String productNumber, String average) {
    List<Menu> menus = name.isEmpty() || name == null
        ? menuRepository.findAll() : menuRepository.findByNameContainingIgnoreCase(name);

    if (!productNumber.isEmpty() && productNumber != null) {
      menus.retainAll(menuRepository.findByProductNumber(Integer.parseInt(productNumber)));
    }
    if (!average.isEmpty() && average != null) {
      menus.retainAll(menuRepository.findByAveragePrice(Double.parseDouble(average)));
    }
    return menus.stream()
                .map(this::mapMenuToMenuResponse)
                .toList();
  }

  /////////////////////////////////////////////////////////////
  ///  DELETE MENU METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public boolean deleteMenu(Long id) {
    if (!restaurantRepository.existsByMenuId(id)) {
      menuRepository.deleteById(id);
    } else if (!menuRepository.existsById(id)) {
      return false;
    } else {
      Menu menu = menuRepository.findById(id).get();
      menu.setDeletedAt(LocalDateTime.now());
      menuRepository.save(menu);
    }
    return true;
  }

  /////////////////////////////////////////////////////////////
  ///  MAPPER METHODS
  /////////////////////////////////////////////////////////////

  private MenuResponseDto mapMenuToMenuResponse(Menu menu) {
    List<Long> productIds = new ArrayList<Long>();
    for (Product p : menu.getProducts()) {
      productIds.add(p.getId());
    }
    return new MenuResponseDto(
        menu.getId(), menu.getName(), menu.getCuisine(), menu.getDescription(), productIds);
  }
}
