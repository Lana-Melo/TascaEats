package pt.ul.fc.css.tascaeats.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.UserRequest;
import pt.ul.fc.css.tascaeats.dto.UserResponse;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.repository.UserRepository;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;

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

    public UserResponse registerNewClient(UserRequest request) {
        return register(request, "CLIENT");
    }

    public UserResponse registerNewAdmin(UserRequest request) {
        return register(request, "ADMIN");
    }

    public UserResponse registerNewDeliveryPerson(UserRequest request) {
        return register(request, "DELIVERY");
    }

    public UserResponse getUserById(Long id) {
        Optional<AUser> userOpt = userRepository.findById(id);
        return userOpt.map(this::mapAUserToUserResponse).orElse(null);
    }

    /**
     * Updates a user's recod with the provided data.
     * 
     * @param id the id of the user to update
     * @param request the new user data (only name and phone number are used)
     * @return the updated UserResponse if the user exists, null otherwise
     */
    public UserResponse updateUser(Long id, UserRequest request) {
        Optional<AUser> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {return null;}

        AUser user = userOpt.get();
        user.setName(request.getName());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setNif(request.getNif());
        user.setPassword(request.getPassword());

        userRepository.save(user);
        return mapAUserToUserResponse(user);
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

    private UserResponse register(UserRequest request, String role) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return null; 
        }

        AUser newUser = AUser.create(role, request.getUsername(), request.getPassword(), request.getName(), request.getPhoneNumber(), request.getNif());
        userRepository.save(newUser);
        return mapAUserToUserResponse(newUser);
    }

    private UserResponse mapAUserToUserResponse(AUser user) {
        String role;
        if (user instanceof Client) role = "CLIENT";
        else if (user instanceof Admin) role = "ADMIN";
        else if (user instanceof DeliveryPerson) role = "DELIVERY";
        else throw new IllegalArgumentException("Unknown user type");

        return new UserResponse(user.getId(),
                                user.getUsername(), 
                                user.getName(), 
                                user.getPhoneNumber(), 
                                user.getNif(), 
                                role);
    }

}
