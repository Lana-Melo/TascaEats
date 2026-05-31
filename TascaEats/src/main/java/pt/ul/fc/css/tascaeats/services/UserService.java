package pt.ul.fc.css.tascaeats.services;

import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientUpdateRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.AddressDto;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.Order;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.kafka.Sender;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final OrderRepository orderRepository;
  private final RestaurantRepository restaurantRepository;
  private final Sender sender;

  public UserService(
      UserRepository userRepository,
      OrderRepository orderRepository,
      RestaurantRepository restaurantRepository,
      Sender sender) {
    this.userRepository = userRepository;
    this.orderRepository = orderRepository;
    this.restaurantRepository = restaurantRepository;
    this.sender = sender;
  }

  /** Mock login, any password is accepted if user is valid. */
  @Transactional(readOnly = true)
  public UserResponseDto login(String username, String password) {
    Optional<AUser> userOpt = userRepository.findByUsername(username);
    return userOpt
        .map(this::mapAUserToUserResponse)
        .orElseThrow(() -> new ResourceNotFoundException("User not found."));
  }

  /////////////////////////////////////////////////////////////
  ///  POST USER METHODS
  /////////////////////////////////////////////////////////////
  public ClientResponseDto registerNewClient(ClientRequestDto request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }
    List<Address> addresses =
        List.of(new Address(request.getStreet(), request.getCity(), request.getPostalCode()));
    Client client =
        new Client(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif(),
            addresses);
    Client saved = userRepository.save(client);
    return mapClientToClientResponse(saved);
  }

  public UserResponseDto registerNewAdmin(UserRequestDto request) {
    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }
    Admin admin =
        new Admin(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif());
    Admin saved = userRepository.save(admin);
    return mapAUserToUserResponse(saved);
  }

  public DeliveryPersonResponseDto registerNewDeliveryPerson(DeliveryPersonRequestDto request) {
    /*
    sender.sendAsJson("register-delivery-person", request);
    como eu posso fazer com que este servico fique a espera do ms?

    */

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new IllegalArgumentException("Username already exists.");
    }
    DeliveryPerson deliveryPerson =
        new DeliveryPerson(
            request.getUsername(),
            request.getPassword(),
            request.getName(),
            request.getPhoneNumber(),
            request.getNif());
    DeliveryPerson saved = userRepository.save(deliveryPerson);
    return mapDeliveryToDeliveryPersonResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  /// GET USER METHOD
  /////////////////////////////////////////////////////////////
  public UserResponseDto getUserById(Long id) {
    Optional<AUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty()) {
      throw new ResourceNotFoundException("User not found");
    }
    return mapAUserToUserResponse(userOpt.get());
  }

  public ClientResponseDto getClientById(Long id) {
    AUser user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    return mapClientToClientResponse((Client) user);
  }

  public List<UserResponseDto> searchBy(
      String name, String userType, String orderNumber, String deliveryNumber) {
    Specification<AUser> spec = getSearchSpec(name, userType, orderNumber, deliveryNumber);
    List<AUser> users = userRepository.findAll(spec);
    return users.stream().map(this::mapAUserToUserResponse).toList();
  }

  private Specification<AUser> getSearchSpec(
      String name, String userType, String orderNumber, String deliveryNumber) {
    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (name != null && !name.isEmpty()) {
        predicates.add(cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%"));
      }

      if (userType != null && !userType.isEmpty()) {
        switch (userType) {
          case "Client":
            predicates.add(cb.equal(root.type(), cb.literal(Client.class)));
            break;
          case "Delivery Person":
            predicates.add(cb.equal(root.type(), cb.literal(DeliveryPerson.class)));
            break;
          case "Admin":
            predicates.add(cb.equal(root.type(), cb.literal(Admin.class)));
            break;
        }
      }

      if (orderNumber != null && !orderNumber.isEmpty()) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Order> orderRoot = subquery.from(Order.class);
        subquery.select(cb.count(orderRoot)).where(cb.equal(orderRoot.get("client"), root));
        predicates.add(cb.greaterThanOrEqualTo(subquery, Long.parseLong(orderNumber)));
      }

      if (deliveryNumber != null && !deliveryNumber.isEmpty()) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Order> orderRoot = subquery.from(Order.class);
        subquery
            .select(cb.count(orderRoot))
            .where(
                cb.and(
                    cb.equal(orderRoot.get("deliveryPerson"), root),
                    cb.equal(orderRoot.get("status"), OrderStatus.DELIVERED)));
        predicates.add(cb.greaterThanOrEqualTo(subquery, Long.parseLong(deliveryNumber)));
      }

      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  /////////////////////////////////////////////////////////////
  /// PUT USER METHODS
  /////////////////////////////////////////////////////////////
  @Transactional
  public UserResponseDto updateAdmin(Long id, UserRequestDto request) {
    Optional<AUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Admin)) {
      throw new ResourceNotFoundException("User not found");
    }

    Admin admin = (Admin) userOpt.get();
    admin.setName(request.getName());
    admin.setUsername(request.getUsername());
    admin.setPhoneNumber(request.getPhoneNumber());
    admin.setNif(request.getNif());
    admin.setPassword(request.getPassword());

    Admin saved = userRepository.save(admin);
    return mapAUserToUserResponse(saved);
  }

  @Transactional
  public ClientResponseDto updateClient(Long id, ClientUpdateRequestDto request) {
    Optional<AUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {
      throw new ResourceNotFoundException("User not found");
    }

    Client client = (Client) userOpt.get();
    client.setName(request.getName());
    client.setUsername(request.getUsername());
    client.setPhoneNumber(request.getPhoneNumber());
    client.setNif(request.getNif());
    client.setPassword(request.getPassword());

    Client saved = userRepository.save(client);
    return mapClientToClientResponse(saved);
  }

  @Transactional
  public ClientResponseDto addAddress(Long id, String street, String city, String postalCode) {
    Client client =
        (Client)
            userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    Address newAddress = new Address(street, city, postalCode);
    if (!client.getAddresses().contains(newAddress)) {
      client.addAddress(newAddress);
    }
    return mapClientToClientResponse(userRepository.save(client));
  }

  @Transactional
  public ClientResponseDto removeAddress(Long id, int index) {
    Client client =
        (Client)
            userRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    if (client.getAddresses().size() <= 1)
      throw new IllegalArgumentException("Must have at least one address");
    client.getAddresses().remove(index);
    return mapClientToClientResponse(userRepository.save(client));
  }

  @Transactional
  public DeliveryPersonResponseDto updateDeliveryPerson(Long id, DeliveryPersonRequestDto request) {
    Optional<AUser> userOpt = userRepository.findById(id);
    if (userOpt.isEmpty() || !(userOpt.get() instanceof DeliveryPerson)) {
      throw new ResourceNotFoundException("User not found");
    }

    DeliveryPerson deliveryPerson = (DeliveryPerson) userOpt.get();
    deliveryPerson.setName(request.getName());
    deliveryPerson.setUsername(request.getUsername());
    deliveryPerson.setPhoneNumber(request.getPhoneNumber());
    deliveryPerson.setNif(request.getNif());
    deliveryPerson.setPassword(request.getPassword());
    if (request.getStatus() != null && !request.getStatus().isEmpty()) {
      deliveryPerson.setDeliveryPersonStatus(DeliveryPersonStatus.valueOf(request.getStatus()));
    }

    DeliveryPerson saved = userRepository.save(deliveryPerson);
    return mapDeliveryToDeliveryPersonResponse(saved);
  }

  /////////////////////////////////////////////////////////////
  /// DELETE USER METHODS
  /////////////////////////////////////////////////////////////

  @Transactional
  public boolean deleteUser(Long id) {
    if (userRepository.existsById(id)) {
      AUser user = userRepository.findById(id).get();
      if (user instanceof Admin) {
        return deleteAdmin(user);
      } else if (user instanceof Client) {
        return deleteClient(user);
      } else if (user instanceof DeliveryPerson) {
        return deleteDeliveryPerson(user);
      }
    }
    return false;
  }

  private boolean deleteDeliveryPerson(AUser user) {
    DeliveryPerson deliveryPerson = (DeliveryPerson) user;
    if (!orderRepository.existsByDeliveryPersonId(deliveryPerson.getId())) {
      userRepository.deleteById(deliveryPerson.getId()); // definitively delete
    } else if (orderRepository.existsByDeliveryPersonIdAndStatusNotIn(
        deliveryPerson.getId(), List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))) {
      return false;
    } else {
      deliveryPerson.setDeletedAt(LocalDateTime.now());
      userRepository.save(deliveryPerson);
    }
    return true;
  }

  private boolean deleteClient(AUser user) {
    Client client = (Client) user;
    if (!orderRepository.existsByClientId(client.getId())) {
      userRepository.deleteById(client.getId()); // definitively delete
    } else if (orderRepository.existsByClientIdAndStatusNotIn(
        client.getId(), List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))) {
      return false; // don't delete client associated with orders in progress
    } else {
      client.setDeletedAt(LocalDateTime.now()); // soft delete
      userRepository.save(client);
    }
    return true;
  }

  private boolean deleteAdmin(AUser user) {
    Admin admin = (Admin) user;
    if (!restaurantRepository.existsByAdminId(admin.getId())) {
      userRepository.deleteById(admin.getId());
      return true;
    }
    return false; // don't delete admin associated with restaurants
  }

  /////////////////////////////////////////////////////////////
  /// MAPPER METHODS
  /////////////////////////////////////////////////////////////

  private UserResponseDto mapAUserToUserResponse(AUser user) {
    String role = null;
    if (user instanceof DeliveryPerson) {
      role = "DELIVERY_PERSON";
    } else if (user instanceof Admin) {
      role = "ADMIN";
    } else if (user instanceof Client) {
      role = "CLIENT";
    }

    return new UserResponseDto(
        user.getId(),
        role,
        user.getUsername(),
        user.getName(),
        user.getPhoneNumber(),
        user.getNif());
  }

  private DeliveryPersonResponseDto mapDeliveryToDeliveryPersonResponse(
      DeliveryPerson deliveryPerson) {
    return new DeliveryPersonResponseDto(
        deliveryPerson.getId(),
        deliveryPerson.getUsername(),
        deliveryPerson.getName(),
        deliveryPerson.getPhoneNumber(),
        deliveryPerson.getNif(),
        deliveryPerson.getDeliveryPersonStatus().toString());
  }

  private ClientResponseDto mapClientToClientResponse(Client client) {
    List<AddressDto> addresses =
        client.getAddresses().stream()
            .map(a -> new AddressDto(a.getStreet(), a.getCity(), a.getPostalCode()))
            .toList();
    return new ClientResponseDto(
        client.getId(),
        client.getUsername(),
        client.getName(),
        client.getPhoneNumber(),
        client.getNif(),
        addresses);
  }
}
