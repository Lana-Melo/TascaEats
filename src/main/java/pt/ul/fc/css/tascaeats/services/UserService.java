package pt.ul.fc.css.tascaeats.services;

import java.util.Optional;
import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.repository.UserRepository;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    /**
     * Updates a user's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated UserResponse if the user exists, null otherwise
     */
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

    /**
     * Updates a client's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated ClientResponse if the user exists, null otherwise
     */
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

    /**
     * Updates a delivery person's record with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated DeliveryPersonResponse if the user exists, null otherwise
     */
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

    /// TODO:
    /// In the future, implement the business and softdelete rules below:
    /// - DeliveryPerson and Client
    ///     Don't delete if the user is assigned to a pending order
    ///     Do soft delete if the user is associated to past completed orders
    /// 
    /// - AdminPerson:
    ///     Not sure about the business rules here
    /// 
  
    /**
     * Deletes a user by id if they exist
     * 
     * @param id the id of the user to delete
     * @return true if the user was deleted, false otherwise
     */
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
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
