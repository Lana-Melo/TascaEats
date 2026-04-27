package pt.ul.fc.css.tascaeats.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;
import pt.ul.fc.css.tascaeats.dto.menu.MenuRequest;
import pt.ul.fc.css.tascaeats.dto.menu.MenuResponse;
import pt.ul.fc.css.tascaeats.entities.Menu;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.repository.MenuRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;

public class MenuService {
    
    private final MenuRepository menuRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public MenuService(MenuRepository menuRepository, RestaurantRepository restaurantRepository, ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }

    /////////////////////////////////////////////////////////////
    ///  POST MENU METHODS
    /////////////////////////////////////////////////////////////

    @Transactional
    public MenuResponse createMenu(MenuRequest request) {
        ArrayList<Product> products = new ArrayList<>();
        for (Long id : request.getProductIds()) {
            Optional<Product> prodOpt = productRepository.findById(id);
            if (prodOpt.isEmpty()) {return null;}

            products.add(prodOpt.get());
        }
        Menu menu = new Menu(request.getName(), request.getCuisine(), request.getDescription(), products);
        Menu saved = menuRepository.save(menu);

        return mapMenuToMenuResponse(saved);
    }

    /////////////////////////////////////////////////////////////
    ///  PUT MENU METHODS
    /////////////////////////////////////////////////////////////
    
    @Transactional
    public MenuResponse updateMenu(Long id, MenuRequest request) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        if (menuOpt.isEmpty()) {return null;}

        Menu menu = (Menu) menuOpt.get();
        menu.setName(request.getName());
        menu.setCuisine(request.getCuisine());
        menu.setDescription(request.getDescription());

        ArrayList<Product> products = new ArrayList<>();
        for (Long pid : request.getProductIds()) {
            Optional<Product> prodOpt = productRepository.findById(pid);
            if (prodOpt.isEmpty()) {return null;}

            products.add(prodOpt.get());
        }
        menu.setProducts(products);
        Menu saved = menuRepository.save(menu);
        return mapMenuToMenuResponse(saved);
    }

    /////////////////////////////////////////////////////////////
    ///  GET MENU METHODS
    /////////////////////////////////////////////////////////////

    public MenuResponse getMenu(Long id) {
        Optional<Menu> menuOpt = menuRepository.findById(id);
        return menuOpt.map(this::mapMenuToMenuResponse).orElse(null);
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

    private MenuResponse mapMenuToMenuResponse(Menu menu) {
        List<Long> productIds = new ArrayList<Long>();
        for (Product p : menu.getProducts()) {
            productIds.add(p.getId());
        }
        return new MenuResponse(
                                    menu.getId(),
                                    menu.getName(),
                                    menu.getCuisine(),
                                    menu.getDescription(),
                                    productIds);
    }
}
