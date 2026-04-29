package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.DeliveryPersonResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;

  @InjectMocks private UserService userService;

  @Mock private OrderRepository orderRepository;

  @Mock private RestaurantRepository restaurantRepository;

  @Test
  void login_shouldReturnUserResponse_whenUserExists() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));

    UserResponseDto response = userService.login("admin", "anything");

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
    ClientRequestDto request = new ClientRequestDto();
    request.setUsername("jane");
    when(userRepository.findByUsername("jane"))
        .thenReturn(Optional.of(new Admin("jane", "pass", "Jane", "912345678", "123456789")));

    assertNull(userService.registerNewClient(request));
  }

  @Test
  void registerNewClient_shouldReturnClientResponse_whenSuccessful() {
    ClientRequestDto request = new ClientRequestDto();
    request.setUsername("jane");
    request.setPassword("pass");
    request.setName("Jane Doe");
    request.setPhoneNumber("912345678");
    request.setNif("123456789");
    request.setAddresses(List.of(List.of("Lisboa", "Rua A", "1000-001")));

    when(userRepository.findByUsername("jane")).thenReturn(Optional.empty());
    when(userRepository.save(any(Client.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ClientResponseDto response = userService.registerNewClient(request);

    assertEquals("jane", response.username());
    assertEquals(List.of(List.of("Lisboa", "Rua A", "1000-001")), response.addresses());
  }

  @Test
  void updateClient_shouldReturnNull_whenUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    ClientRequestDto request = new ClientRequestDto();
    request.setUsername("client");

    assertNull(userService.updateClient(1L, request));
  }

  @Test
  void updateClient_shouldReturnNull_whenUserIsNotClient() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

    ClientRequestDto request = new ClientRequestDto();
    request.setUsername("client");

    assertNull(userService.updateClient(1L, request));
  }

  @Test
  void updateClient_shouldReturnClientResponse_whenSuccessful() {
    Client client =
        new Client(
            "client",
            "pass",
            "Client",
            "912345678",
            "123456789",
            List.of(new Address("Rua A", "Lisboa", "1000-001")));
    when(userRepository.findById(1L)).thenReturn(Optional.of(client));
    when(userRepository.save(any(Client.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    ClientRequestDto request = new ClientRequestDto();
    request.setUsername("client_updated");
    request.setPassword("pass");
    request.setName("Client Updated");
    request.setPhoneNumber("912345678");
    request.setNif("123456789");
    request.setAddresses(List.of(List.of("Porto", "Rua B", "4000-001")));

    ClientResponseDto response = userService.updateClient(1L, request);

    assertEquals("client_updated", response.username());
    assertEquals(List.of(List.of("Porto", "Rua B", "4000-001")), response.addresses());
  }

  @Test
  void registerNewAdmin_shouldReturnNull_whenUsernameAlreadyExists() {
    UserRequestDto request = new UserRequestDto();
    request.setUsername("admin");
    when(userRepository.findByUsername("admin"))
        .thenReturn(Optional.of(new Admin("admin", "pass", "Admin", "912345678", "987654321")));

    assertNull(userService.registerNewAdmin(request));
  }

  @Test
  void registerNewAdmin_shouldReturnUserResponse_whenSuccessful() {
    UserRequestDto request = new UserRequestDto();
    request.setUsername("admin");
    request.setPassword("pass");
    request.setName("Admin User");
    request.setPhoneNumber("912345678");
    request.setNif("987654321");

    when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
    when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserResponseDto response = userService.registerNewAdmin(request);

    assertEquals("admin", response.username());
    assertEquals("Admin User", response.name());
  }

  @Test
  void registerNewDeliveryPerson_shouldReturnNull_whenUsernameAlreadyExists() {
    DeliveryPersonRequestDto request = new DeliveryPersonRequestDto();
    request.setUsername("delivery");
    when(userRepository.findByUsername("delivery"))
        .thenReturn(
            Optional.of(new Admin("delivery", "pass", "Delivery", "912345678", "987654321")));

    assertNull(userService.registerNewDeliveryPerson(request));
  }

  @Test
  void registerNewDeliveryPerson_shouldReturnDeliveryPersonResponse_whenSuccessful() {
    DeliveryPersonRequestDto request = new DeliveryPersonRequestDto();
    request.setUsername("delivery");
    request.setPassword("pass");
    request.setName("Delivery User");
    request.setPhoneNumber("912345678");
    request.setNif("987654321");

    when(userRepository.findByUsername("delivery")).thenReturn(Optional.empty());
    when(userRepository.save(any(DeliveryPerson.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    DeliveryPersonResponseDto response = userService.registerNewDeliveryPerson(request);

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

    UserResponseDto response = userService.getUserById(1L);

    assertEquals("admin", response.username());
    assertEquals("Admin", response.name());
  }

  @Test
  void updateAdmin_shouldReturnNull_whenUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    UserRequestDto request = new UserRequestDto();
    request.setUsername("admin_updated");

    assertNull(userService.updateAdmin(1L, request));
  }

  @Test
  void updateAdmin_shouldReturnNull_whenUserIsNotAdmin() {
    Client client =
        new Client(
            "client",
            "pass",
            "Client",
            "912345678",
            "123456789",
            List.of(new Address("Rua A", "Lisboa", "1000-001")));
    when(userRepository.findById(1L)).thenReturn(Optional.of(client));

    UserRequestDto request = new UserRequestDto();
    request.setUsername("admin_updated");

    assertNull(userService.updateAdmin(1L, request));
  }

  @Test
  void updateAdmin_shouldReturnUserResponse_whenSuccessful() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
    when(userRepository.save(any(Admin.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserRequestDto request = new UserRequestDto();
    request.setUsername("admin_updated");
    request.setPassword("newpass");
    request.setName("Admin Updated");
    request.setPhoneNumber("912345679");
    request.setNif("111222333");

    UserResponseDto response = userService.updateAdmin(1L, request);

    assertEquals("admin_updated", response.username());
    assertEquals("Admin Updated", response.name());
  }

  @Test
  void updateDeliveryPerson_shouldReturnNull_whenUserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    DeliveryPersonRequestDto request = new DeliveryPersonRequestDto();
    request.setUsername("delivery_updated");

    assertNull(userService.updateDeliveryPerson(1L, request));
  }

  @Test
  void updateDeliveryPerson_shouldReturnNull_whenUserIsNotDeliveryPerson() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));

    DeliveryPersonRequestDto request = new DeliveryPersonRequestDto();
    request.setUsername("delivery_updated");

    assertNull(userService.updateDeliveryPerson(1L, request));
  }

  @Test
  void updateDeliveryPerson_shouldReturnDeliveryPersonResponse_whenSuccessful() {
    DeliveryPerson deliveryPerson =
        new DeliveryPerson("delivery", "pass", "Delivery", "912345678", "987654321");
    when(userRepository.findById(1L)).thenReturn(Optional.of(deliveryPerson));
    when(userRepository.save(any(DeliveryPerson.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    DeliveryPersonRequestDto request = new DeliveryPersonRequestDto();
    request.setUsername("delivery_updated");
    request.setPassword("newpass");
    request.setName("Delivery Updated");
    request.setPhoneNumber("912345679");
    request.setNif("111222333");

    DeliveryPersonResponseDto response = userService.updateDeliveryPerson(1L, request);

    assertEquals("delivery_updated", response.username());
    assertEquals("Delivery Updated", response.name());
  }

  @Test
  void deleteUser_shouldReturnFalse_whenUserDoesNotExist() {
    when(userRepository.existsById(99L)).thenReturn(false);
    assertFalse(userService.deleteUser(99L));
  }

  @Test
  void deleteAdmin_shouldHardDelete_whenNoRestaurantsAssociated() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
    when(restaurantRepository.existsByAdminId(admin.getId())).thenReturn(false);

    assertTrue(userService.deleteUser(1L));
  }

  @Test
  void deleteAdmin_shouldReturnFalse_whenRestaurantsAssociated() {
    Admin admin = new Admin("admin", "pass", "Admin", "912345678", "987654321");
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin));
    when(restaurantRepository.existsByAdminId(admin.getId())).thenReturn(true);

    assertFalse(userService.deleteUser(1L));
  }

  // --- DELETE CLIENT ---

  @Test
  void deleteClient_shouldHardDelete_whenNoOrdersAssociated() {
    Client client =
        new Client(
            "client",
            "pass",
            "Client",
            "912345678",
            "123456789",
            List.of(new Address("Rua A", "Lisboa", "1000-001")));
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(client));
    when(orderRepository.existsByClientId(client.getId())).thenReturn(false);

    assertTrue(userService.deleteUser(1L));
  }

  @Test
  void deleteClient_shouldReturnFalse_whenOrdersInProgress() {
    Client client =
        new Client(
            "client",
            "pass",
            "Client",
            "912345678",
            "123456789",
            List.of(new Address("Rua A", "Lisboa", "1000-001")));
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(client));
    when(orderRepository.existsByClientId(client.getId())).thenReturn(true);
    when(orderRepository.existsByClientIdAndStatusNotIn(eq(client.getId()), any()))
        .thenReturn(true);

    assertFalse(userService.deleteUser(1L));
  }

  @Test
  void deleteClient_shouldSoftDelete_whenAllOrdersFinished() {
    Client client =
        new Client(
            "client",
            "pass",
            "Client",
            "912345678",
            "123456789",
            List.of(new Address("Rua A", "Lisboa", "1000-001")));
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(client));
    when(orderRepository.existsByClientId(client.getId())).thenReturn(true);
    when(orderRepository.existsByClientIdAndStatusNotIn(eq(client.getId()), any()))
        .thenReturn(false);

    assertTrue(userService.deleteUser(1L));
    assertNotNull(client.getDeletedAt()); // confirma soft delete
  }

  // --- DELETE DELIVERY PERSON ---

  @Test
  void deleteDeliveryPerson_shouldHardDelete_whenNoOrdersAssociated() {
    DeliveryPerson dp =
        new DeliveryPerson("delivery", "pass", "Delivery", "912345678", "987654321");
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(dp.getId())).thenReturn(false);

    assertTrue(userService.deleteUser(1L));
  }

  @Test
  void deleteDeliveryPerson_shouldReturnFalse_whenOrdersInProgress() {
    DeliveryPerson dp =
        new DeliveryPerson("delivery", "pass", "Delivery", "912345678", "987654321");
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(dp.getId())).thenReturn(true);
    when(orderRepository.existsByDeliveryPersonIdAndStatusNotIn(eq(dp.getId()), any()))
        .thenReturn(true);

    assertFalse(userService.deleteUser(1L));
  }

  @Test
  void deleteDeliveryPerson_shouldSoftDelete_whenAllOrdersFinished() {
    DeliveryPerson dp =
        new DeliveryPerson("delivery", "pass", "Delivery", "912345678", "987654321");
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(dp.getId())).thenReturn(true);
    when(orderRepository.existsByDeliveryPersonIdAndStatusNotIn(eq(dp.getId()), any()))
        .thenReturn(false);

    assertTrue(userService.deleteUser(1L));
    assertNotNull(dp.getDeletedAt()); // confirma soft delete
  }
}
