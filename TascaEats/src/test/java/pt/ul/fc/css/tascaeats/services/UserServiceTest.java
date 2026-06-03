package pt.ul.fc.css.tascaeats.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.ClientUpdateRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.DeliveryPersonRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.request.UserRequestDto;
import pt.ul.fc.css.tascaeats.dto.users.response.ClientResponseDto;
import pt.ul.fc.css.tascaeats.dto.users.response.UserResponseDto;
import pt.ul.fc.css.tascaeats.entities.Address;
import pt.ul.fc.css.tascaeats.entities.Admin;
import pt.ul.fc.css.tascaeats.entities.Client;
import pt.ul.fc.css.tascaeats.entities.DeliveryPerson;
import pt.ul.fc.css.tascaeats.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.tascaeats.repository.OrderRepository;
import pt.ul.fc.css.tascaeats.repository.RestaurantRepository;
import pt.ul.fc.css.tascaeats.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private OrderRepository orderRepository;
  @Mock private RestaurantRepository restaurantRepository;

  @InjectMocks private UserService userService;

  private Address address() {
    return new Address("Rua A", "Lisboa", "1000-001");
  }

  private Admin admin() {
    return new Admin("admin", "pass", "Admin", "912345678", "987654321");
  }

  private Client client() {
    return new Client(
        "client",
        "pass",
        "Client Name",
        "912345678",
        "123456789",
        new ArrayList<>(List.of(address())));
  }

  private DeliveryPerson deliveryPerson() {
    return new DeliveryPerson("dp", "pass", "DP Name", "912000001", "111111111");
  }

  // ─── login ──────────────────────────────────────────────────────────────────

  @Test
  void login_shouldReturnUser_whenFound() {
    Admin a = admin();
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(a));
    UserResponseDto result = userService.login("admin", "pass");
    assertNotNull(result);
    assertEquals("admin", result.username());
  }

  @Test
  void login_shouldThrow_whenNotFound() {
    when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> userService.login("ghost", "pass"));
  }

  // ─── registerNewClient ──────────────────────────────────────────────────────

  @Test
  void registerNewClient_shouldReturnClient_whenUsernameAvailable() {
    ClientRequestDto req = new ClientRequestDto();
    req.setUsername("jane");
    req.setPassword("pass");
    req.setName("Jane");
    req.setPhoneNumber("912000000");
    req.setNif("111222333");
    req.setCity("Porto");
    req.setStreet("Rua B");
    req.setPostalCode("4000-001");

    when(userRepository.findByUsername("jane")).thenReturn(Optional.empty());
    when(userRepository.save(any(Client.class))).thenAnswer(inv -> inv.getArgument(0));

    ClientResponseDto result = userService.registerNewClient(req);
    assertNotNull(result);
    assertEquals("jane", result.username());
  }

  @Test
  void registerNewClient_shouldThrow_whenUsernameExists() {
    ClientRequestDto req = new ClientRequestDto();
    req.setUsername("jane");
    when(userRepository.findByUsername("jane")).thenReturn(Optional.of(client()));
    assertThrows(IllegalArgumentException.class, () -> userService.registerNewClient(req));
  }

  // ─── registerNewAdmin ───────────────────────────────────────────────────────

  @Test
  void registerNewAdmin_shouldReturnAdmin_whenUsernameAvailable() {
    UserRequestDto req = new UserRequestDto();
    req.setUsername("newadmin");
    req.setPassword("pass");
    req.setName("New Admin");
    req.setPhoneNumber("912000000");
    req.setNif("999888777");

    when(userRepository.findByUsername("newadmin")).thenReturn(Optional.empty());
    when(userRepository.save(any(Admin.class))).thenAnswer(inv -> inv.getArgument(0));

    UserResponseDto result = userService.registerNewAdmin(req);
    assertNotNull(result);
    assertEquals("newadmin", result.username());
    assertEquals("ADMIN", result.role());
  }

  @Test
  void registerNewAdmin_shouldThrow_whenUsernameExists() {
    UserRequestDto req = new UserRequestDto();
    req.setUsername("admin");
    when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin()));
    assertThrows(IllegalArgumentException.class, () -> userService.registerNewAdmin(req));
  }

  // ─── registerNewDeliveryPerson ──────────────────────────────────────────────

  /*
  @Test
  void registerNewDeliveryPerson_shouldReturnDelivery_whenUsernameAvailable() {
    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setUsername("dperson"); req.setPassword("pass"); req.setName("DP");
    req.setPhoneNumber("912000000"); req.setNif("777666555");

    when(userRepository.findByUsername("dperson")).thenReturn(Optional.empty());
    when(userRepository.save(any(DeliveryPerson.class))).thenAnswer(inv -> inv.getArgument(0));

    DeliveryPersonResponseDto result = userService.registerNewDeliveryPerson(req);
    assertNotNull(result);
    assertEquals("dperson", result.username());
  }*/

  @Test
  void registerNewDeliveryPerson_shouldThrow_whenUsernameExists() {
    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setUsername("dp");
    when(userRepository.findByUsername("dp")).thenReturn(Optional.of(deliveryPerson()));
    assertThrows(IllegalArgumentException.class, () -> userService.registerNewDeliveryPerson(req));
  }

  // ─── getUserById ────────────────────────────────────────────────────────────

  @Test
  void getUserById_shouldReturnUser_whenFound() {
    Admin a = admin();
    when(userRepository.findById(1L)).thenReturn(Optional.of(a));
    UserResponseDto result = userService.getUserById(1L);
    assertNotNull(result);
    assertEquals("ADMIN", result.role());
  }

  @Test
  void getUserById_shouldThrow_whenNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(99L));
  }

  // ─── updateAdmin ────────────────────────────────────────────────────────────

  @Test
  void updateAdmin_shouldReturnUpdated_whenFound() {
    Admin a = admin();
    when(userRepository.findById(1L)).thenReturn(Optional.of(a));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    UserRequestDto req = new UserRequestDto();
    req.setName("Updated");
    req.setUsername("admin2");
    req.setPhoneNumber("900000000");
    req.setNif("000000001");
    req.setPassword("newpass");

    UserResponseDto result = userService.updateAdmin(1L, req);
    assertNotNull(result);
    assertEquals("admin2", result.username());
  }

  @Test
  void updateAdmin_shouldThrow_whenNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class, () -> userService.updateAdmin(99L, new UserRequestDto()));
  }

  @Test
  void updateAdmin_shouldThrow_whenUserIsNotAdmin() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(client()));
    assertThrows(
        ResourceNotFoundException.class, () -> userService.updateAdmin(1L, new UserRequestDto()));
  }

  // ─── updateClient ───────────────────────────────────────────────────────────

  @Test
  void updateClient_shouldReturnUpdated_whenFound() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ClientUpdateRequestDto req = new ClientUpdateRequestDto();
    req.setName("New Name");
    req.setUsername("newclient");
    req.setPhoneNumber("900000001");
    req.setNif("000000002");
    req.setPassword("newpass");

    ClientResponseDto result = userService.updateClient(1L, req);
    assertNotNull(result);
    assertEquals("newclient", result.username());
  }

  @Test
  void updateClient_shouldThrow_whenNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class,
        () -> userService.updateClient(99L, new ClientUpdateRequestDto()));
  }

  @Test
  void updateClient_shouldThrow_whenUserIsNotClient() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(admin()));
    assertThrows(
        ResourceNotFoundException.class,
        () -> userService.updateClient(1L, new ClientUpdateRequestDto()));
  }

  // ─── updateDeliveryPerson ───────────────────────────────────────────────────

  /*
  @Test
  void updateDeliveryPerson_shouldReturnUpdated_whenFound() {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setName("New DP");
    req.setUsername("dp2");
    req.setPhoneNumber("900000002");
    req.setNif("000000003");
    req.setPassword("newpass");

    DeliveryPersonResponseDto result = userService.updateDeliveryPerson(1L, req);
    assertNotNull(result);
    assertEquals("dp2", result.username());
  }
  */

  /*
  @Test
  void updateDeliveryPerson_shouldThrow_whenNotFound() {
    when(userRepository.findById(99L)).thenReturn(Optional.empty());
    assertThrows(
        ResourceNotFoundException.class,
        () -> userService.updateDeliveryPerson(99L, new DeliveryPersonRequestDto()));
  }*/

  @Test
  void getClientById_shouldReturnClient_whenFound() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));

    ClientResponseDto result = userService.getClientById(1L);

    assertNotNull(result);
    assertEquals("client", result.username());
  }

  @Test
  void addAddress_shouldAddNewAddress_whenNotDuplicate() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ClientResponseDto result = userService.addAddress(1L, "Rua B", "Porto", "4000-001");

    assertNotNull(result);
    assertEquals(2, result.addresses().size());
  }

  @Test
  void addAddress_shouldNotAddDuplicateAddress_whenAlreadyPresent() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ClientResponseDto result = userService.addAddress(1L, "Rua A", "Lisboa", "1000-001");

    assertNotNull(result);
    assertEquals(1, result.addresses().size());
  }

  @Test
  void removeAddress_shouldThrow_whenOnlyOneAddress() {
    Client c = client();
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));

    assertThrows(IllegalArgumentException.class, () -> userService.removeAddress(1L, 0));
  }

  @Test
  void removeAddress_shouldRemoveAddress_whenMoreThanOne() {
    Client c =
        new Client(
            "client",
            "pass",
            "Client Name",
            "912345678",
            "123456789",
            new ArrayList<>(
                List.of(
                    new Address("Rua A", "Lisboa", "1000-001"),
                    new Address("Rua B", "Porto", "4000-001"))));
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    ClientResponseDto result = userService.removeAddress(1L, 0);

    assertNotNull(result);
    assertEquals(1, result.addresses().size());
  }

  /*
  @Test
  void updateDeliveryPerson_shouldReturnUpdatedWithStatus_whenStatusProvided() {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setName("New DP");
    req.setUsername("dp2");
    req.setPhoneNumber("900000002");
    req.setNif("000000003");
    req.setPassword("newpass");
    req.setStatus("BUSY");

    DeliveryPersonResponseDto result = userService.updateDeliveryPerson(1L, req);

    assertNotNull(result);
    assertEquals("dp2", result.username());
    assertEquals("BUSY", result.status());
  }
  */

  /*
  @Test
  void updateDeliveryPerson_shouldReturnUpdated_whenStatusNotProvided() {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

    DeliveryPersonRequestDto req = new DeliveryPersonRequestDto();
    req.setName("New DP");
    req.setUsername("dp2");
    req.setPhoneNumber("900000002");
    req.setNif("000000003");
    req.setPassword("newpass");

    DeliveryPersonResponseDto result = userService.updateDeliveryPerson(1L, req);

    assertNotNull(result);
    assertEquals("dp2", result.username());
    assertEquals("AVAILABLE", result.status());
  }
  */
  @Test
  void deleteDeliveryPerson_shouldSoftDelete_whenHasCompletedOrders() throws Exception {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(any())).thenReturn(true);
    when(orderRepository.existsByDeliveryPersonIdAndStatusNotIn(any(), any())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(dp);

    boolean result = userService.deleteUser(1L);

    assertTrue(result);
    verify(userRepository, never()).deleteById(any());
    assertNotNull(dp.getDeletedAt());
  }

  /*
  @Test
  void searchBy_shouldReturnFilteredUsersByType() {
    when(userRepository.findAll(any(Specification.class))).thenReturn(List.of(client()));

    var result = userService.searchBy(null, "Client", null, null);

    assertEquals(1, result.size());
  }*/

  // ─── deleteUser ─────────────────────────────────────────────────────────────

  @Test
  void deleteUser_shouldReturnFalse_whenNotFound() throws Exception {
    when(userRepository.existsById(99L)).thenReturn(false);
    assertFalse(userService.deleteUser(99L));
  }

  @Test
  void deleteUser_shouldDeleteAdmin_whenNoRestaurants() throws Exception {
    Admin a = admin();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(a));
    when(restaurantRepository.existsByAdminId(any())).thenReturn(false);
    assertTrue(userService.deleteUser(1L));
    verify(userRepository).deleteById(any());
  }

  @Test
  void deleteUser_shouldReturnFalse_whenAdminHasRestaurants() throws Exception {
    Admin a = admin();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(a));
    when(restaurantRepository.existsByAdminId(any())).thenReturn(true);
    assertFalse(userService.deleteUser(1L));
    verify(userRepository, never()).deleteById(any());
  }

  @Test
  void deleteUser_shouldDeleteClientPermanently_whenNoOrders() throws Exception {
    Client c = client();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(orderRepository.existsByClientId(any())).thenReturn(false);
    assertTrue(userService.deleteUser(1L));
    verify(userRepository).deleteById(any());
  }

  @Test
  void deleteUser_shouldSoftDeleteClient_whenHasCompletedOrders() throws Exception {
    Client c = client();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(orderRepository.existsByClientId(any())).thenReturn(true);
    when(orderRepository.existsByClientIdAndStatusNotIn(any(), any())).thenReturn(false);
    when(userRepository.save(any())).thenReturn(c);
    assertTrue(userService.deleteUser(1L));
    verify(userRepository, never()).deleteById(any());
  }

  @Test
  void deleteUser_shouldReturnFalse_whenClientHasActiveOrders() throws Exception {
    Client c = client();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(c));
    when(orderRepository.existsByClientId(any())).thenReturn(true);
    when(orderRepository.existsByClientIdAndStatusNotIn(any(), any())).thenReturn(true);
    assertFalse(userService.deleteUser(1L));
    verify(userRepository, never()).deleteById(any());
  }

  @Test
  void deleteUser_shouldDeleteDeliveryPersonPermanently_whenNoOrders() throws Exception {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(any())).thenReturn(false);
    assertTrue(userService.deleteUser(1L));
    verify(userRepository).deleteById(any());
  }

  @Test
  void deleteUser_shouldReturnFalse_whenDeliveryPersonHasActiveOrders() throws Exception {
    DeliveryPerson dp = deliveryPerson();
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.findById(1L)).thenReturn(Optional.of(dp));
    when(orderRepository.existsByDeliveryPersonId(any())).thenReturn(true);
    when(orderRepository.existsByDeliveryPersonIdAndStatusNotIn(any(), any())).thenReturn(true);
    assertFalse(userService.deleteUser(1L));
  }
}
