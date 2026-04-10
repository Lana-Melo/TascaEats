package pt.ul.fc.css.tascaeats.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.enums.OrderStatus;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final RestaurantRepository restaurantRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository, RestaurantRepository restaurantRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Mock login, any password is accepted if user is valid.
     *
     */
    public UserResponse login(String username, String password) {
        Optional<AUser> userOpt = userRepository.findByUsername(username);
        return userOpt.map(this::mapAUserToUserResponse).orElse(null);
    }

    /////////////////////////////////////////////////////////////
    ///  POST USER METHODS
    /////////////////////////////////////////////////////////////
    public ClientResponse registerNewClient(ClientRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        Address address = new Address(request.getStreet(), request.getCity(), request.getPostalCode());
        Client client = new Client(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif(), address);
        Client saved = userRepository.save(client);
        return mapClientToClientResponse(saved);
    }

    public UserResponse registerNewAdmin(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        Admin admin = new Admin(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif());
        Admin saved = userRepository.save(admin);
        return mapAUserToUserResponse(saved);
    }

    public DeliveryPersonResponse registerNewDeliveryPerson(DeliveryPersonRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        DeliveryPerson deliveryPerson = new DeliveryPerson(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif());
        DeliveryPerson saved = userRepository.save(deliveryPerson);
        return mapDeliveryToDeliveryPersonResponse(saved);
    }

    /////////////////////////////////////////////////////////////
    /// GET USER METHOD
    /////////////////////////////////////////////////////////////
    public UserResponse getUserById(Long id) {
        Optional<AUser> userOpt = userRepository.findById(id);
        return userOpt.map(this::mapAUserToUserResponse).orElse(null);
    }

    /////////////////////////////////////////////////////////////
    /// PUT USER METHODS
    /////////////////////////////////////////////////////////////
    public UserResponse updateAdmin(Long id, UserRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Admin)) {return null;}

        Admin admin = (Admin) userOpt.get();
        admin.setName(request.getName());
        admin.setUsername(request.getUsername());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setNif(request.getNif());
        admin.setPassword(request.getPassword());

        Admin saved = userRepository.save(admin);
        return mapAUserToUserResponse(saved);
    }

    public ClientResponse updateClient(Long id, ClientRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof Client)) {return null;}

        Client client = (Client) userOpt.get();
        client.setName(request.getName());
        client.setUsername(request.getUsername());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setNif(request.getNif());
        client.setPassword(request.getPassword());
        Address address = new Address(request.getStreet(), request.getCity(), request.getPostalCode());
        client.setAddress(address);

        Client saved = userRepository.save(client);
        return mapClientToClientResponse(saved);
    }

    public DeliveryPersonResponse updateDeliveryPerson(Long id, DeliveryPersonRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty() || !(userOpt.get() instanceof DeliveryPerson)) {return null;}

        DeliveryPerson deliveryPerson = (DeliveryPerson) userOpt.get();
        deliveryPerson.setName(request.getName());
        deliveryPerson.setUsername(request.getUsername());
        deliveryPerson.setPhoneNumber(request.getPhoneNumber());
        deliveryPerson.setNif(request.getNif());
        deliveryPerson.setPassword(request.getPassword());

        DeliveryPerson saved = userRepository.save(deliveryPerson);
        return mapDeliveryToDeliveryPersonResponse(saved);
    }

    /////////////////////////////////////////////////////////////
    /// DELETE USER METHODS
    /////////////////////////////////////////////////////////////
  
    public boolean deleteUser(Long id) {
        if(userRepository.existsById(id)){
            AUser user = userRepository.findById(id).get();
            if(user instanceof Admin){
                return deleteAdmin(user);
            } else if (user instanceof Client){
                return deleteClient(user);
            } else if (user instanceof DeliveryPerson){
                return deleteDeliveryPerson(user);
            }
        }
        return false;
    }

    private boolean deleteDeliveryPerson(AUser user) {
        DeliveryPerson deliveryPerson = (DeliveryPerson) user;
        if(!orderRepository.existsByDeliveryPersonId(deliveryPerson.getId())){
            userRepository.deleteById(deliveryPerson.getId()); // definitively delete
        } else if(orderRepository.existsByDeliveryPersonIdAndStatusNotIn(deliveryPerson.getId(), List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))){
            return false;
        } else {
            deliveryPerson.setDeletedAt(LocalDateTime.now());
            userRepository.save(deliveryPerson);
        }
        return true;
    }

    private boolean deleteClient(AUser user) {
        Client client = (Client) user;
        if(!orderRepository.existsByClientId(client.getId())){
            userRepository.deleteById(client.getId()); // definitively delete
        } else if(orderRepository.existsByClientIdAndStatusNotIn(client.getId(), List.of(OrderStatus.CANCELLED, OrderStatus.DELIVERED))){
            return false; // don't delete client associated with orders in progress
        } else {
            client.setDeletedAt(LocalDateTime.now()); // soft delete
            userRepository.save(client);
        }
        return true;
    }

    private boolean deleteAdmin(AUser user) {
        Admin admin = (Admin) user;
        if(!restaurantRepository.existsByAdminId(admin.getId())){
            userRepository.deleteById(admin.getId());
            return true;
        }
        return false; // don't delete admin associated with restaurants
    }

    /////////////////////////////////////////////////////////////
    /// MAPPER METHODS
    /////////////////////////////////////////////////////////////

    private UserResponse mapAUserToUserResponse(AUser user) {
        return new UserResponse(user.getId(),
                                user.getUsername(), 
                                user.getName(), 
                                user.getPhoneNumber(), 
                                user.getNif());
    }

    private DeliveryPersonResponse mapDeliveryToDeliveryPersonResponse(DeliveryPerson deliveryPerson) {
        return new DeliveryPersonResponse(deliveryPerson.getId(),
                                          deliveryPerson.getUsername(), 
                                          deliveryPerson.getName(), 
                                          deliveryPerson.getPhoneNumber(), 
                                          deliveryPerson.getNif(),
                                          deliveryPerson.getDeliveryPersonStatus().toString());
    }

    private ClientResponse mapClientToClientResponse(Client client) {
        return new ClientResponse(client.getId(),
                                  client.getUsername(), 
                                  client.getName(), 
                                  client.getPhoneNumber(), 
                                  client.getNif(),
                                  client.getAddress().getCity(),
                                  client.getAddress().getStreet(),
                                  client.getAddress().getPostalCode());
    }

}
