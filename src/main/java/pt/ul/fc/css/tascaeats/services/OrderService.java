package pt.ul.fc.css.tascaeats.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.order.OrderRequest;
import pt.ul.fc.css.tascaeats.dto.order.OrderResponse;
import pt.ul.fc.css.tascaeats.dto.order.PaymentRequest;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.ProductRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Delivery;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.Payment;
import pt.ul.fc.css.tascaeats.entities.Product;
import pt.ul.fc.css.tascaeats.entities.Restaurant;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.entities.enums.PaymentType;
import pt.ul.fc.css.tascaeats.entities.enums.ProductStatus;
import pt.ul.fc.css.tascaeats.entities.enums.RestaurantStatus;

@Service
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, 
                        RestaurantRepository restaurantRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.productRepository = productRepository;
    }

    /////////////////////////////////////////////////////////////
    ///  POST ORDER METHODS
    /////////////////////////////////////////////////////////////
    
    public OrderResponse createOrder(OrderRequest request) {
        Optional<Restaurant> resOpt = restaurantRepository.findById(request.getRestaurantId());

        if (resOpt.isEmpty() || resOpt.get().getStatus() == RestaurantStatus.CLOSED) {return null;}
        Client client = userRepository.findById(request.getClientId())
                            .filter(c -> c instanceof Client)
                            .map(c -> (Client) c)
                            .orElseThrow(null);
        Restaurant restaurant = (Restaurant) resOpt.get();
        
        ArrayList<Product> products = new ArrayList<Product>();
        for (Long id : request.getProductIds()) {
            Optional<Product> prodOpt = productRepository.findById(id);
            if (prodOpt.isEmpty() || prodOpt.get().getStatus() == ProductStatus.SOLD_OUT || 
                prodOpt.get().getRestaurant() != restaurant) {return null;}

            products.add(prodOpt.get());
        }

        Order order = new Order(restaurant, client, products);
        Order saved = orderRepository.save(order);
        
        return mapOrderToOrderResponse(saved);
    }

    public OrderResponse processPayment(PaymentRequest request) {
        Optional<Order> ordOpt = orderRepository.findById(request.getOrderId());
        
        if (ordOpt.isEmpty()) {return null;}
        Order order = ordOpt.get();
        if (order.getOrderStatus() != OrderStatus.CREATED ||
            order.getTotalValue() != request.getAmount()) {return null;}
        
        Payment payment = new Payment(request.getAmount(), PaymentType.valueOf(request.getPaymentMethod()), order);
        order.setPayment(payment);
        order.setOrderStatus(OrderStatus.PAID);
        Order saved = orderRepository.save(order);

        return mapOrderToOrderResponse(saved);
    }

    /////////////////////////////////////////////////////////////
    ///  PATCH ORDER METHODS
    /////////////////////////////////////////////////////////////

    public OrderResponse updateOrderStatus(Long id, String status) {
        Optional<Order> ordOpt = orderRepository.findById(id);
        if (ordOpt.isEmpty()) {return null;}
        Order order = ordOpt.get();

        OrderStatus ordStat = OrderStatus.valueOf(status);
        switch (order.getOrderStatus()) {
            case CREATED:
                if (ordStat != OrderStatus.PAID && ordStat != OrderStatus.CANCELLED) {return null;}
                break;
            case PAID:
                if (ordStat != OrderStatus.PREPARING && ordStat != OrderStatus.CANCELLED) {return null;}
                break;
            case PREPARING:
                if (ordStat != OrderStatus.READY) {return null;}
                break;
            case READY:
                if (ordStat != OrderStatus.DELIVERED) {return null;}
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

    public OrderResponse assignDeliveryPerson(Long id, Long deliveryPersonId) {
        Optional<Order> ordOpt = orderRepository.findById(id);
        if (ordOpt.isEmpty()) {return null;}
        DeliveryPerson del = userRepository.findById(deliveryPersonId)
                            .filter(d -> d instanceof DeliveryPerson)
                            .map(d -> (DeliveryPerson) d)
                            .orElseThrow(null);

        Order order = ordOpt.get();
        if (del.getDeliveryPersonStatus() == DeliveryPersonStatus.BUSY || 
            order.getOrderStatus() != OrderStatus.READY) {return null;}
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

    public boolean cancelOrder(Long id) {
        System.out.println("1");
        if (orderRepository.existsById(id)) {
            System.out.println("2");
            Optional<Order> ordOpt = orderRepository.findById(id);
            Order order = ordOpt.get();
            if (order.getOrderStatus() != OrderStatus.CREATED && order.getOrderStatus() != OrderStatus.PAID) {
                return false;
            }
            System.out.println("3");
            order.setOrderStatus(OrderStatus.CANCELLED);
            return true;
        }
        return true;
    }

    /////////////////////////////////////////////////////////////
    ///  MAPPER METHODS
    /////////////////////////////////////////////////////////////

    private OrderResponse mapOrderToOrderResponse(Order order) {
        ArrayList<Long> productIds = new ArrayList<Long>();
        for (Product p : order.getProducts()) {
            productIds.add(p.getId());
        }
        Long delId = order.getDeliveryPerson() == null? null: order.getDeliveryPerson().getId();
        String delName = order.getDeliveryPerson() == null? null: order.getDeliveryPerson().getName();

        return new OrderResponse(
                                    order.getId(),
                                    order.getOrderStatus(),
                                    order.getTimestamp(),
                                    order.getTotalValue(),
                                    order.getClient().getId(),
                                    order.getClient().getName(),
                                    order.getRestaurant().getId(),
                                    order.getRestaurant().getDesignation(),
                                    productIds,
                                    delId,
                                    delName);
    }

}
