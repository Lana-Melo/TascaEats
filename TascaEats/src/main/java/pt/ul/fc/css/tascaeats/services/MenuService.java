package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequestDto;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponseDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductRequestDto;
import pt.ul.fc.css.tascaeats.dto.product.ProductResponseDto;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
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
  ///  CRUD MENU'S METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public MenuResponseDto createMenu(MenuRequestDto request) {
    Menu menu =
        new Menu(request.getName(), request.getCuisine(), request.getDescription());
    Menu saved = menuRepository.save(menu);

    return mapMenuToMenuResponse(saved);
  }

  @Transactional
  public MenuResponseDto updateMenu(Long id, MenuRequestDto request) {
    Optional<Menu> menuOpt = menuRepository.findById(id);
    if (menuOpt.isEmpty()) {
      throw new ResourceNotFoundException("Menu not found");
    }

    Menu menu = (Menu) menuOpt.get();
    menu.setName(request.getName());
    menu.setCuisine(request.getCuisine());
    menu.setDescription(request.getDescription());
    Menu saved = menuRepository.save(menu);
    return mapMenuToMenuResponse(saved);
  }

  public MenuResponseDto getMenu(Long id) {
    Optional<Menu> menuOpt = menuRepository.findById(id);
    if (menuOpt.isEmpty()) {
      throw new ResourceNotFoundException("Menu not found");
    }
    return mapMenuToMenuResponse(menuOpt.get());
  }

  public List<MenuResponseDto> searchBy(String name, String productNumber, String average) {
    List<Menu> menus =
          name == null || name.isEmpty()
            ? menuRepository.findAll()
            : menuRepository.findByNameContainingIgnoreCase(name);

    if (!productNumber.isEmpty() && productNumber != null) {
      menus.retainAll(menuRepository.findByProductNumber(Integer.parseInt(productNumber)));
    }
    if (!average.isEmpty() && average != null) {
      menus.retainAll(menuRepository.findByAveragePrice(Double.parseDouble(average)));
    }
    return menus.stream().map(this::mapMenuToMenuResponse).toList();
  }

  @Transactional
  public boolean deleteMenu(Long id) {
    if (!menuRepository.existsById(id)) {
      return false;
    } else if (!restaurantRepository.existsByMenuId(id)) {
      menuRepository.deleteById(id);
    } else {
      Menu menu = menuRepository.findById(id).get();
      menu.setDeletedAt(LocalDateTime.now());
      menuRepository.save(menu);
    }
    return true;
  }

  /////////////////////////////////////////////////////////////
  ///  CRUD PRODUCT'S METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public ProductResponseDto addProduct(Long menuId, ProductRequestDto productRequest) {
    Optional<Menu> menuOpt = menuRepository.findById(menuId);
    if (!menuOpt.isPresent()) {
      throw new ResourceNotFoundException("Menu not found");
    }

    Menu menu = menuOpt.get();
    Product product =
        new Product(
            productRequest.getPrice(),
            productRequest.getDescription(),
            productRequest.getDesignation(),
            menu);

    Product saved = productRepository.save(product);

    return mapProductToProductResponse(saved);
  }

  @Transactional
  public ProductResponseDto updateProduct(
      Long menuId, Long productId, ProductRequestDto productRequest) {
    Optional<Product> prodOpt = productRepository.findById(productId);
    if (!prodOpt.isPresent()) {
      throw new ResourceNotFoundException("Product not found");
    }

    Product product = prodOpt.get();
    product.setDescription(productRequest.getDescription());
    product.setDesignation(productRequest.getDesignation());
    product.setPrice(productRequest.getPrice());

    Product saved = productRepository.save(product);

    return mapProductToProductResponse(saved);
  }

  @Transactional
  public boolean deleteProduct(Long menuId, Long productId) {
    boolean isFromMenu = productRepository.existsByIdAndMenuId(productId, menuId);

    if (isFromMenu) {
      if (!restaurantRepository.existsByMenuId(menuId)) {
        productRepository.deleteById(productId); // definitively delete
      } else {
        return false; // don't delete product associated with orders in progress
      }
      return true;
    }
    return false;
  }

  public List<ProductResponseDto> searchProductBy(
      Long menuId,
      String designation,
      String description) {
    List<Product> products =
        (designation == null || designation.isEmpty())
            ? productRepository.findAll()
            : productRepository.findByDesignationContainingIgnoreCase(designation);

    if (description != null && !description.isEmpty()) {
      products.retainAll(productRepository.findByDescriptionContainingIgnoreCase(description));
    }
    if (menuId != null) {
      products.retainAll(productRepository.findAllByMenuId(menuId));
    }
    return products.stream().map(this::mapProductToProductResponse).toList();
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

  private ProductResponseDto mapProductToProductResponse(Product product) {
    return new ProductResponseDto(
        product.getId(),
        product.getPrice(),
        product.getDescription(),
        product.getDesignation());
  }
}
