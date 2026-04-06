package pt.ul.fc.css.tascaeats.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.users.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.UserResponse;
import pt.ul.fc.css.tascaeats.dto.users.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.repository.UserRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.entities.enums.DeliveryPersonStatus;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserService(UserRepository userRepository, OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    /**
     * Mock login, any password is accepted if user is valid.
     * 
     * @param username the username of the user trying to log in
     * @param password the password of the user trying to log in (ignored in this mock
     * implementation)
     * @return a UserResponse if the user exists, null otherwise
     */
    public UserResponse login(String username, String password) {
        Optional<AUser> userOpt = userRepository.findByUsername(username);
        return userOpt.map(this::mapAUserToUserResponse).orElse(null);
    }

    public ClientResponse registerNewClient(ClientRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        Address address = new Address(request.getCity(), request.getStreet(), request.getPostalCode());
        Client client = new Client(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif(), address);
        userRepository.save(client);
        return mapClientToClientResponse(client, address);
    }

    public UserResponse registerNewAdmin(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        Admin admin = new Admin(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif());
        userRepository.save(admin);
        return mapAUserToUserResponse(admin);
    }

    public DeliveryPersonResponse registerNewDeliveryPerson(UserRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }
        DeliveryPerson deliveryPerson = new DeliveryPerson(request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif());
        userRepository.save(deliveryPerson);
        return mapDeliveryToDeliveryPersonResponse(deliveryPerson);
    }

    public UserResponse getUserById(Long id) {
        Optional<AUser> userOpt = userRepository.findById(id);
        return userOpt.map(this::mapAUserToUserResponse).orElse(null);
    }

    /**
     * Updates a user's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated UserResponse if the user exists, null otherwise
     */
    public UserResponse updateAdmin(Long id, UserRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {return null;}

        Admin admin = (Admin) userOpt.get();
        admin.setName(request.getName());
        admin.setUsername(request.getUsername());
        admin.setPhoneNumber(request.getPhoneNumber());
        admin.setNif(request.getNif());
        admin.setPassword(request.getPassword());

        userRepository.save(admin);
        return mapAUserToUserResponse(admin);
    }

    /**
     * Updates a client's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated ClientResponse if the user exists, null otherwise
     */
    public ClientResponse updateClient(Long id, ClientRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {return null;}

        Client client = (Client) userOpt.get();
        client.setName(request.getName());
        client.setUsername(request.getUsername());
        client.setPhoneNumber(request.getPhoneNumber());
        client.setNif(request.getNif());
        client.setPassword(request.getPassword());
        Address address = new Address(request.getCity(), request.getStreet(), request.getPostalCode());
        client.setAddress(address);

        userRepository.save(client);
        return mapClientToClientResponse(client, address);
    }

    /**
     * Updates a delivery person's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated DeliveryPersonResponse if the user exists, null otherwise
     */
    public DeliveryPersonResponse updateDeliveryPerson(Long id, DeliveryPersonRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {return null;}

        DeliveryPerson deliveryPerson = (DeliveryPerson) userOpt.get();
        deliveryPerson.setName(request.getName());
        deliveryPerson.setUsername(request.getUsername());
        deliveryPerson.setPhoneNumber(request.getPhoneNumber());
        deliveryPerson.setNif(request.getNif());
        deliveryPerson.setPassword(request.getPassword());
        deliveryPerson.setDeliveryPersonStatus((DeliveryPersonStatus) request.getStatus());

        userRepository.save(deliveryPerson);
        return mapDeliveryToDeliveryPersonResponse(deliveryPerson);
    }

    /**
     * Deletes a user by id if they exist and have no associated orders.
     * 
     * @param id the id of the user to delete
     * @return true if the user was deleted, false if the user does not exist or
     * has associated orders
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id) && orderRepository.findByClientId(id).isEmpty()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

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

    private ClientResponse mapClientToClientResponse(Client client, Address address) {
        return new ClientResponse(client.getId(),
                                  client.getUsername(), 
                                  client.getName(), 
                                  client.getPhoneNumber(), 
                                  client.getNif(),
                                  address.getCity(),
                                  address.getStreet(),
                                  address.getPostalCode());
    }

}
