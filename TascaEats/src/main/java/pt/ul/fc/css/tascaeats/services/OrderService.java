package pt.ul.fc.css.tascaeats.services;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import pt.ul.fc.css.tascaeats.common.dto.DeliveryResponseDto;
import pt.ul.fc.css.tascaeats.common.kafka.Topics;
import pt.ul.fc.css.tascaeats.dto.order.OrderProductItemDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderRequestDto;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponseDto;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequestDto;
import pt.ul.fc.css.tascaeats.entities.APayment;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Card;
import pt.ul.fc.css.tascaeats.entities.Cash;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.MBWay;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.ProductItem;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;
import pt.ul.fc.css.tascaeats.exceptions.IncompleteInputException;
import pt.ul.fc.css.tascaeats.exceptions.RequirementsNotMetException;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.kafka.KafkaResponseStore;
import pt.ul.fc.css.tascaeats.kafka.Sender;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductItemRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class OrderService {

  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;
  private final ProductItemRepository productItemRepository;
  private final Sender sender;
  private final KafkaResponseStore kafkaResponseStore;

  public OrderService(
      OrderRepository orderRepository,
      UserRepository userRepository,
      RestaurantRepository restaurantRepository,
      ProductItemRepository productItemRepository,
      Sender sender,
      KafkaResponseStore kafkaResponseStore) {
    this.orderRepository = orderRepository;
    this.userRepository = userRepository;
    this.restaurantRepository = restaurantRepository;
    this.productItemRepository = productItemRepository;
    this.sender = sender;
    this.kafkaResponseStore = kafkaResponseStore;
  }

  /////////////////////////////////////////////////////////////
  ///  POST ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public OrderResponseDto createOrder(OrderRequestDto request) {
    if (request.getCity() == null
        || request.getStreet() == null
        || request.getPostalCode() == null) {
      throw new IncompleteInputException("All fields for address need to be filled");
    }

    Address address = new Address(request.getStreet(), request.getCity(), request.getPostalCode());

    Optional<AUser> userOpt = userRepository.findById(request.getClientId());
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {
      throw new ResourceNotFoundException("User not found");
    }

    Client client = (Client) userOpt.get();

    ArrayList<ProductItem> productItems = new ArrayList<>();
    ArrayList<Restaurant> restaurants = new ArrayList<>();
    for (Long id : request.getProductIds()) {
      Optional<ProductItem> prodOpt = productItemRepository.findById(id);
      if (prodOpt.isEmpty()
          || prodOpt.get().getStatus() == ProductStatus.SOLD_OUT
          || prodOpt.get().getRestaurant().getStatus() == RestaurantStatus.CLOSED) {
        throw new ResourceNotFoundException("Product not found or unavailable");
      }

      productItems.add(prodOpt.get());
      if (!restaurants.contains(prodOpt.get().getRestaurant())) {
        restaurants.add(prodOpt.get().getRestaurant());
      }
    }

    Order order = new Order(client, productItems, address);

    Order saved = orderRepository.save(order);

    for (Restaurant res : restaurants) {
      Optional<Restaurant> resOpt = restaurantRepository.findById(res.getId());
      if (resOpt.isEmpty()) {
        throw new ResourceNotFoundException("Restaurant not found");
      }
      Restaurant restaurant = (Restaurant) resOpt.get();
      restaurant.addOrder(saved);
      restaurantRepository.save(restaurant);
    }
    if (!client.getAddresses().contains(address)) {
      client.addAddress(address);
      userRepository.save(client);
    }
    return mapOrderToOrderResponse(saved);
  }

  @Transactional
  public OrderResponseDto processPayment(PaymentRequestDto request) {
    Optional<Order> ordOpt = orderRepository.findById(request.getOrderId());

    if (ordOpt.isEmpty()) {
      throw new ResourceNotFoundException("Order not found");
    }
    Order order = ordOpt.get();
    if (order.getOrderStatus() != OrderStatus.CREATED) {
      throw new RequirementsNotMetException("Order already has a payment processed");
    }

    APayment payment;
    switch (request.getPaymentMethod()) {
      case "MBWay":
        if (Math.abs(order.getTotalValue() - request.getAmount()) >= 0.01
            || request.getPaymentInfo() == null) {
          throw new RequirementsNotMetException(
              "Amount does not match payment/Payment info not included");
        }
        payment = new MBWay(request.getAmount(), order, request.getPaymentInfo());
        break;
      case "Card":
        if (Math.abs(order.getTotalValue() - request.getAmount()) >= 0.01
            || request.getPaymentInfo() == null) {
          throw new RequirementsNotMetException(
              "Amount does not match payment/Payment info not included");
        }
        payment = new Card(request.getAmount(), order, request.getPaymentInfo());
        break;
      case "Cash":
        if (order.getTotalValue() > request.getAmount() || request.getPaymentInfo() != null) {
          throw new RequirementsNotMetException(
              "Amount does not cover payment/Redundant info included");
        }
        payment = new Cash(request.getAmount(), order, request.getAmount() - order.getTotalValue());
        break;
      default:
        throw new IllegalArgumentException("Invalid payment method");
    }

    order.setPayment(payment);
    order.setOrderStatus(OrderStatus.PAID);
    Order saved = orderRepository.save(order);

    return mapOrderToOrderResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  ///  GET ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public List<OrderResponseDto> getOrdersByClient(Long clientId) {
    return orderRepository.findByClientId(clientId).stream()
        .map(this::mapOrderToOrderResponse)
        .toList();
  }

  @Transactional
  public List<OrderResponseDto> getAllOrders() {
    return orderRepository.findAll().stream().map(this::mapOrderToOrderResponse).toList();
  }

  /////////////////////////////////////////////////////////////
  ///  PATCH ORDER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public OrderResponseDto updateOrderStatus(Long id, String status) {
    Optional<Order> ordOpt = orderRepository.findById(id);
    if (ordOpt.isEmpty()) {
      throw new ResourceNotFoundException("Order not found");
    }
    Order order = ordOpt.get();

    OrderStatus ordStat = OrderStatus.valueOf(status);
    switch (order.getOrderStatus()) {
      case CREATED:
        if (ordStat != OrderStatus.PAID && ordStat != OrderStatus.CANCELLED) {
          throw new RequirementsNotMetException("Created order cannot update to specified status");
        }
        break;
      case PAID:
        if (ordStat != OrderStatus.PREPARING && ordStat != OrderStatus.CANCELLED) {
          throw new RequirementsNotMetException("Paid order cannot update to specified status");
        }
        break;
      case PREPARING:
        if (ordStat != OrderStatus.READY) {
          throw new RequirementsNotMetException(
              "Preparing order cannot update to specified status");
        } else {
          /*
          Optional<DeliveryPerson> delOpt =
              userRepository.findFirstByStatus(DeliveryPersonStatus.AVAILABLE);
          DeliveryPerson del = delOpt.isEmpty() ? null : delOpt.get();

          if (del != null) {
            Delivery delivery = new Delivery(LocalDateTime.now());
            order.setDelivery(delivery);
            del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
            order.setDeliveryPerson(del);
            userRepository.save(del);
          }*/
        }
        break;
      case READY:
        if (ordStat != OrderStatus.DELIVERED) {
          throw new RequirementsNotMetException("Ready order cannot update to specified status");
        }
        break;
      default:
        throw new IllegalArgumentException("Invalid or unchangeable order status");
    }

    /* if (ordStat == OrderStatus.DELIVERED && order.getDelivery() == null) {
      throw new RequirementsNotMetException(
          "Delivered order must have a delivery associated with it");
    } else if (ordStat == OrderStatus.DELIVERED) {
      order.getDelivery().setDeliveryTimeStamp();
      DeliveryPerson del = order.getDeliveryPerson();
      del.setDeliveryPersonStatus(DeliveryPersonStatus.AVAILABLE);
      userRepository.save(del);
    }*/

    order.setOrderStatus(ordStat);
    Order saved = orderRepository.save(order);
    return mapOrderToOrderResponse(saved);
  }

  @Transactional
  public OrderResponseDto assignDeliveryPerson(Long id, Long deliveryPersonId) {
    Optional<Order> ordOpt = orderRepository.findById(id);
    if (ordOpt.isEmpty()) {
      throw new ResourceNotFoundException("Order not found");
    }
    Optional<AUser> userOpt = userRepository.findById(deliveryPersonId);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof DeliveryPerson)) {
      throw new ResourceNotFoundException("User not found");
    }

    DeliveryPerson del = (DeliveryPerson) userOpt.get();
    Order order = ordOpt.get();
    if (del.getDeliveryPersonStatus() == DeliveryPersonStatus.BUSY
        || order.getOrderStatus() != OrderStatus.READY) {
      throw new RequirementsNotMetException(
          "Order must be ready/Delivery person must be available");
    }
    // Delivery delivery = new Delivery(LocalDateTime.now());
    // order.setDelivery(delivery);
    del.setDeliveryPersonStatus(DeliveryPersonStatus.BUSY);
    order.setDeliveryPerson(del);

    userRepository.save(del);
    Order saved = orderRepository.save(order);
    return mapOrderToOrderResponse(saved);
  }

  @Transactional
  public void markOrderAsDelivered(Long orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    order.setOrderStatus(OrderStatus.DELIVERED);
    orderRepository.save(order);
  }

  public List<DeliveryResponseDto> getMyDeliveries(Long deliveryPersonId) throws Exception {
    sender.sendAsJson(Topics.GET_MY_DELIVERIES, deliveryPersonId);

    List<DeliveryResponseDto> deliveries = kafkaResponseStore.waitForDeliveries(10000);

    if (deliveries == null) {
      throw new IllegalStateException("Timeout waiting for deliveries from delivery service");
    }

    return deliveries;
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
    return false;
  }

  /////////////////////////////////////////////////////////////
  ///  MAPPER METHODS
  /////////////////////////////////////////////////////////////

  private OrderResponseDto mapOrderToOrderResponse(Order order) {
    ArrayList<Long> productIds = new ArrayList<Long>();
    ArrayList<Long> restaurantIds = new ArrayList<Long>();
    ArrayList<OrderProductItemDto> products = new ArrayList<>();
    for (ProductItem p : order.getProductItems()) {
      productIds.add(p.getId());
      if (!restaurantIds.contains(p.getRestaurant().getId())) {
        restaurantIds.add(p.getRestaurant().getId());
      }
      products.add(
          new OrderProductItemDto(
              p.getId(), p.getProduct().getDesignation(), p.getProduct().getPrice()));
    }
    Long delId = order.getDeliveryPerson() == null ? null : order.getDeliveryPerson().getId();
    String delName = order.getDeliveryPerson() == null ? null : order.getDeliveryPerson().getName();
    String delNif = order.getDeliveryPerson() == null ? null : order.getDeliveryPerson().getNif();
    Double paymentAmount = order.getPayment() == null ? null : order.getPayment().getAmount();
    java.time.LocalDateTime paymentTimestamp =
        order.getPayment() == null ? null : order.getPayment().getTimestamp();
    Double paymentChange = order.getPayment() instanceof Cash cash ? cash.getPhoneNumber() : null;

    return new OrderResponseDto(
        order.getId(),
        order.getOrderStatus(),
        order.getTimestamp(),
        order.getTotalValue(),
        order.getClient().getId(),
        order.getClient().getName(),
        order.getClient().getNif(),
        restaurantIds,
        productIds,
        products,
        delId,
        delName,
        delNif,
        order.getAddress().getCity(),
        order.getAddress().getStreet(),
        order.getAddress().getPostalCode(),
        paymentAmount,
        paymentTimestamp,
        paymentChange);
  }

  @Transactional
  public List<OrderResponseDto> searchByStatus(String status) {
    return orderRepository.findAll().stream()
        .filter(
            o ->
                status == null
                    || status.isBlank()
                    || o.getOrderStatus().name().equalsIgnoreCase(status))
        .map(this::mapOrderToOrderResponse)
        .toList();
  }
}
