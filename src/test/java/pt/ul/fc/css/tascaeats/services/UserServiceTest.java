package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequest;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequest;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponse;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponse;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void login_shouldReturnUserResponse_whenUserExists() {
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

        UserResponse response = userService.login("admin", "anything");

        assertEquals("admin", response.username());
        assertEquals("Admin", response.name());
    }

    @Test
    void login_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertNull(userService.login("ghost", "pass"));
    }

    @Test
    void registerNewClient_shouldReturnNull_whenUsernameAlreadyExists() {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane");
        when(userRepository.findByUsername("jane")).thenReturn(Optional.of(new Admin("jane", "pass", "Jane", "912345678", "123456789")));

        assertNull(userService.registerNewClient(request));
    }

    @Test
    void registerNewClient_shouldReturnClientResponse_whenSuccessful() {
        ClientRequest request = new ClientRequest();
        request.setUsername("jane");
        request.setPassword("pass");
        request.setName("Jane Doe");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setCity("Lisboa");
        request.setStreet("Rua A");
        request.setPostalCode("1000-001");

        when(userRepository.findByUsername("jane")).thenReturn(Optional.empty());
        when(userRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientResponse response = userService.registerNewClient(request);

        assertEquals("jane", response.username());
        assertEquals("Lisboa", response.city());
    }

    @Test
    void updateClient_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        ClientRequest request = new ClientRequest();
        request.setUsername("client");

        assertNull(userService.updateClient(1L, request));
    }

    @Test
    void updateClient_shouldReturnNull_whenUserIsNotClient() {
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        ClientRequest request = new ClientRequest();
        request.setUsername("client");

        assertNull(userService.updateClient(1L, request));
    }

    @Test
    void updateClient_shouldReturnClientResponse_whenSuccessful() {
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));
        when(userRepository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ClientRequest request = new ClientRequest();
        request.setUsername("client_updated");
        request.setPassword("pass");
        request.setName("Client Updated");
        request.setPhoneNumber("912345678");
        request.setNif("123456789");
        request.setCity("Porto");
        request.setStreet("Rua B");
        request.setPostalCode("4000-001");

        ClientResponse response = userService.updateClient(1L, request);

        assertEquals("client_updated", response.username());
        assertEquals("Porto", response.city());
    }

    @Test
    void registerNewAdmin_shouldReturnNull_whenUsernameAlreadyExists() {
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(new Admin("admin", "pass", "Admin", "912345678", "987654321")));

        assertNull(userService.registerNewAdmin(request));
    }

    @Test
    void registerNewAdmin_shouldReturnUserResponse_whenSuccessful() {
        UserRequest request = new UserRequest();
        request.setUsername("admin");
        request.setPassword("pass");
        request.setName("Admin User");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.registerNewAdmin(request);

        assertEquals("admin", response.username());
        assertEquals("Admin User", response.name());
    }

    @Test
    void registerNewDeliveryPerson_shouldReturnNull_whenUsernameAlreadyExists() {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery");
        when(userRepository.findByUsername("delivery")).thenReturn(Optional.of(new Admin("delivery", "pass", "Delivery", "912345678", "987654321")));

        assertNull(userService.registerNewDeliveryPerson(request));
    }

    @Test
    void registerNewDeliveryPerson_shouldReturnDeliveryPersonResponse_whenSuccessful() {
        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery");
        request.setPassword("pass");
        request.setName("Delivery User");
        request.setPhoneNumber("912345678");
        request.setNif("987654321");

        when(userRepository.findByUsername("delivery")).thenReturn(Optional.empty());
        when(userRepository.save(any(DeliveryPerson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryPersonResponse response = userService.registerNewDeliveryPerson(request);

        assertEquals("delivery", response.username());
        assertEquals("Delivery User", response.name());
    }

    @Test
    void getUserById_shouldReturnNull_whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertNull(userService.getUserById(1L));
    }

    @Test
    void getUserById_shouldReturnUserResponse_whenUserExists() {
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        UserResponse response = userService.getUserById(1L);

        assertEquals("admin", response.username());
        assertEquals("Admin", response.name());
    }

    @Test
    void updateAdmin_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserRequest request = new UserRequest();
        request.setUsername("admin_updated");

        assertNull(userService.updateAdmin(1L, request));
    }

    @Test
    void updateAdmin_shouldReturnNull_whenUserIsNotAdmin() {
        Client client = new Client("client", "pass", "Client", "912345678", "123456789", new Address("Rua A", "Lisboa", "1000-001"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(client));

        UserRequest request = new UserRequest();
        request.setUsername("admin_updated");

        assertNull(userService.updateAdmin(1L, request));
    }

    @Test
    void updateAdmin_shouldReturnUserResponse_whenSuccessful() {
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
        when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserRequest request = new UserRequest();
        request.setUsername("admin_updated");
        request.setPassword("newpass");
        request.setName("Admin Updated");
        request.setPhoneNumber("912345679");
        request.setNif("111222333");

        UserResponse response = userService.updateAdmin(1L, request);

        assertEquals("admin_updated", response.username());
        assertEquals("Admin Updated", response.name());
    }

    @Test
    void updateDeliveryPerson_shouldReturnNull_whenUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery_updated");

        assertNull(userService.updateDeliveryPerson(1L, request));
    }

    @Test
    void updateDeliveryPerson_shouldReturnNull_whenUserIsNotDeliveryPerson() {
        Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery_updated");

        assertNull(userService.updateDeliveryPerson(1L, request));
    }

    @Test
    void updateDeliveryPerson_shouldReturnDeliveryPersonResponse_whenSuccessful() {
        DeliveryPerson deliveryPerson = new DeliveryPerson("delivery", "pass", "Delivery", "912345678", "987654321");
        when(userRepository.findById(1L)).thenReturn(Optional.of(deliveryPerson));
        when(userRepository.save(any(DeliveryPerson.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DeliveryPersonRequest request = new DeliveryPersonRequest();
        request.setUsername("delivery_updated");
        request.setPassword("newpass");
        request.setName("Delivery Updated");
        request.setPhoneNumber("912345679");
        request.setNif("111222333");

        DeliveryPersonResponse response = userService.updateDeliveryPerson(1L, request);

        assertEquals("delivery_updated", response.username());
        assertEquals("Delivery Updated", response.name());
    }

    @Test
    void deleteUser_shouldReturnTrue_whenUserExists() {
        when(userRepository.existsById(1L)).thenReturn(true);

        assertTrue(userService.deleteUser(1L));
    }

    @Test
    void deleteUser_shouldReturnFalse_whenUserDoesNotExist() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertFalse(userService.deleteUser(1L));
    }
}