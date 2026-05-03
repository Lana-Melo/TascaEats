package pt.ul.fc.css.tascaeats.services;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
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
    Menu menu = new Menu(request.getName(), request.getCuisine(), request.getDescription());
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

  @Transactional
  public List<MenuResponseDto> searchBy(String name, String productNumber, String average) {
    Specification<Menu> spec = getSearchSpec(name, productNumber, average);
    List<Menu> menus = menuRepository.findAll(spec);
    return menus.stream().map(this::mapMenuToMenuResponse).toList();
  }

  private Specification<Menu> getSearchSpec(String name, String productNumber, String average) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (name != null && !name.isEmpty()) {
        predicates.add(cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"));
      }

      if (productNumber != null && !productNumber.isEmpty()) {
        predicates.add(
            cb.greaterThanOrEqualTo(
                cb.size(root.get("products")), Integer.parseInt(productNumber)));
      }

      if (average != null && !average.isEmpty()) {
        double avgValue = Double.parseDouble(average);
        Join<Menu, Product> productJoin = root.join("products", JoinType.INNER);
        query.groupBy(root);
        query.having(cb.le(cb.avg(productJoin.get("price")), avgValue));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
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
      Long menuId, String designation, String description) {

    Specification<Product> spec =
        (root, query, cb) -> {
          List<Predicate> predicates = new ArrayList<>();

          if (menuId != null) {
            predicates.add(cb.equal(root.get("menu").get("id"), menuId));
          }
          if (designation != null && !designation.isEmpty()) {
            predicates.add(
                cb.like(cb.upper(root.get("designation")), "%" + designation.toUpperCase() + "%"));
          }
          if (description != null && !description.isEmpty()) {
            predicates.add(
                cb.like(cb.upper(root.get("description")), "%" + description.toUpperCase() + "%"));
          }

          return cb.and(predicates.toArray(new Predicate[0]));
        };

    return productRepository.findAll(spec).stream().map(this::mapProductToProductResponse).toList();
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
        product.getId(), product.getPrice(), product.getDescription(), product.getDesignation());
  }
}
