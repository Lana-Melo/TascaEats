package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.entities.APayment;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Card;
import pt.ul.fc.css.tascaeats.entities.Cash;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Delivery;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.MBWay;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final ProductRepository productRepository;

  public OrderService(
      OrderRepository orderRepository,
      UserRepository userRepository,
      RestaurantRepository restaurantRepository,
      ProductRepository productRepository) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.restaurantRepository = restaurantRepository;
    this.productRepository = productRepository;
  }

  /////////////////////////////////////////////////////////////
  ///  POST ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public OrderResponseDto createOrder(OrderRequestDto request) {
    Optional<AUser> userOpt = userRepository.findById(request.getClientId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {
      return null;
    }

    Client client = (Client) userOpt.get();

    ArrayList<Product> products = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    for (Long id : request.getProductIds()) {
      Optional<Product> prodOpt = productRepository.findById(id);
      if (prodOpt.isEmpty()
          || prodOpt.get().getStatus() == ProductStatus.SOLD_OUT
          || prodOpt.get().getRestaurant().getStatus() == RestaurantStatus.CLOSED) {
        return null;
      }

      products.add(prodOpt.get());
      if (!restaurants.contains(prodOpt.get().getRestaurant())) {
        restaurants.add(prodOpt.get().getRestaurant());
      }
    }

    if (request.getCity() == null
        || request.getStreet() == null
        || request.getPostalCode() == null) {
      return null;
    }
    Address address = new Address(request.getStreet(), request.getCity(), request.getPostalCode());

    Order order = new Order(client, products, address);

    for (Restaurant res : restaurants) {
      Optional<Restaurant> resOpt = restaurantRepository.findById(res.getId());
      if (resOpt.isEmpty()) {
        return null;
      }
      Restaurant restaurant = (Restaurant) resOpt.get();
      restaurant.addOrder(order);
      restaurantRepository.save(restaurant);
    }
    if (!client.getAddresses().contains(address)) {
      client.addAddress(address);
      userRepository.save(client);
    }

    Order saved = orderRepository.save(order);

    return mapOrderToOrderResponse(saved);
  }

  @Transactional
  public OrderResponseDto processPayment(PaymentRequestDto request) {
    Optional<Order> ordOpt = orderRepository.findById(request.getOrderId());

    if (ordOpt.isEmpty()) {
      return null;
    }
    Order order = ordOpt.get();
    if (order.getOrderStatus() != OrderStatus.CREATED) {
      return null;
    }

    APayment payment;
    switch (request.getPaymentMethod()) {
      case "MBWay":
        if (order.getTotalValue() != request.getAmount() || request.getPaymentInfo().equals(null)) {
          return null;
        }
        payment = new MBWay(request.getAmount(), order, request.getPaymentInfo());
        break;
      case "Card":
        if (order.getTotalValue() != request.getAmount() || request.getPaymentInfo().equals(null)) {
          return null;
        }
        payment = new Card(request.getAmount(), order, request.getPaymentInfo());
        break;
      case "Cash":
        if (order.getTotalValue() > request.getAmount() || !request.getPaymentInfo().equals(null)) {
          return null;
        }
        payment = new Cash(request.getAmount(), order, request.getAmount() - order.getTotalValue());
        break;
      default:
        return null;
    }

    order.setPayment(payment);
    order.setOrderStatus(OrderStatus.PAID);
    Order saved = orderRepository.save(order);

    return mapOrderToOrderResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  GET ORDER METHODS
  /////////////////////////////////////////////////////////////
  public List<OrderResponseDto> getOrdersByClient(Long clientId) {
    return orderRepository.findByClientId(clientId).stream()
        .map(this::mapOrderToOrderResponse)
        .toList();
  }

  /////////////////////////////////////////////////////////////
  ///  PATCH ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public OrderResponseDto updateOrderStatus(Long id, String status) {
    Optional<Order> ordOpt = orderRepository.findById(id);
    if (ordOpt.isEmpty()) {
      return null;
    }
    Order order = ordOpt.get();

    OrderStatus ordStat = OrderStatus.valueOf(status);
    switch (order.getOrderStatus()) {
      case CREATED:
        if (ordStat != OrderStatus.PAID && ordStat != OrderStatus.CANCELLED) {
          return null;
        }
        break;
      case PAID:
        if (ordStat != OrderStatus.PREPARING && ordStat != OrderStatus.CANCELLED) {
          return null;
        }
        break;
      case PREPARING:
        if (ordStat != OrderStatus.READY) {
          return null;
        } else {
          Optional<DeliveryPerson> delOpt =
              userRepository.findFirstByStatus(DeliveryPersonStatus.AVAILABLE);
          DeliveryPerson del = delOpt.isEmpty() ? null : delOpt.get();

          if (del != null) {
            Delivery delivery = new Delivery(LocalDateTime.now());
            order.setDelivery(delivery);
            del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
            order.setDeliveryPerson(del);
            userRepository.save(del);
          }
        }
        break;
      case READY:
        if (ordStat != OrderStatus.DELIVERED) {
          return null;
        }
        break;
      default:
        return null;
    }

    if (ordStat == OrderStatus.DELIVERED && order.getDelivery() == null) {
      return null;
    } else if (ordStat == OrderStatus.DELIVERED) {
      order.getDelivery().setDeliveryTimeStamp();
      DeliveryPerson del = order.getDeliveryPerson();
      del.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);
      userRepository.save(del);
    }

    order.setOrderStatus(ordStat);
    Order saved = orderRepository.save(order);
    return mapOrderToOrderResponse(saved);
  }

  @Transactional
  public OrderResponseDto assignDeliveryPerson(Long id, Long deliveryPersonId) {
    Optional<Order> ordOpt = orderRepository.findById(id);
    if (ordOpt.isEmpty()) {
      return null;
    }
    Optional<AUser> userOpt = userRepository.findById(deliveryPersonId);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof DeliveryPerson)) {
      return null;
    }

    DeliveryPerson del = (DeliveryPerson) userOpt.get();
    Order order = ordOpt.get();
    if (del.getDeliveryPersonStatus() == DeliveryPersonStatus.BUSY
        || order.getOrderStatus() != OrderStatus.READY) {
      return null;
    }
    Delivery delivery = new Delivery(LocalDateTime.now());
    order.setDelivery(delivery);
    del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
    order.setDeliveryPerson(del);

    userRepository.save(del);
    Order saved = orderRepository.save(order);
    return mapOrderToOrderResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  DELETE ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public boolean cancelOrder(Long id) {
    if (orderRepository.existsById(id)) {
      Optional<Order> ordOpt = orderRepository.findById(id);
      Order order = ordOpt.get();
      if (order.getOrderStatus() != OrderStatus.CREATED
          && order.getOrderStatus() != OrderStatus.PAID) {
        return false;
      }
      order.setOrderStatus(OrderStatus.CANCELLED);
      orderRepository.deleteById(id);
      return true;
    }
    return true;
  }

  /////////////////////////////////////////////////////////////
  ///  MAPPER METHODS
  /////////////////////////////////////////////////////////////

  private OrderResponseDto mapOrderToOrderResponse(Order order) {
    ArrayList<Long> productIds = new ArrayList<Long>();
    ArrayList<Long> restaurantIds = new ArrayList<Long>();
    for (Product p : order.getProducts()) {
      productIds.add(p.getId());
      if (!restaurantIds.contains(p.getRestaurant().getId())) {
        restaurantIds.add(p.getRestaurant().getId());
      }
    }
    Long delId = order.getDeliveryPerson() == null ? null : order.getDeliveryPerson().getId();
    String delName = order.getDeliveryPerson() == null ? null : order.getDeliveryPerson().getName();

    return new OrderResponseDto(
        order.getId(),
        order.getOrderStatus(),
        order.getTimestamp(),
        order.getTotalValue(),
        order.getClient().getId(),
        order.getClient().getName(),
        restaurantIds,
        productIds,
        delId,
        delName,
        order.getAddress().getCity(),
        order.getAddress().getStreet(),
        order.getAddress().getPostalCode());
  }
}
