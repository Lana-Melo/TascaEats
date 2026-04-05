package pt.ul.fc.css.tascaeats.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.tascaeats.dto.UserRequest;
import pt.ul.fc.css.tascaeats.dto.UserResponse;
import pt.ul.fc.css.tascaeats.entities.AUser;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse login(String username, String password) {
        Optional<AUser> userOpt = userRepository.findByUsername(username);
        
        if (userOpt.isEmpty()) { return null;}

        AUser user = userOpt.get();
        return mapAUserToUserResponse(user);

    }

    public UserResponse register(UserRequest request) {
        // verificar se o username já existe

        return null;
    }



    private UserResponse mapAUserToUserResponse(AUser user) {
        // mapear o AUser para UserResponse
        return null;
    }

}
